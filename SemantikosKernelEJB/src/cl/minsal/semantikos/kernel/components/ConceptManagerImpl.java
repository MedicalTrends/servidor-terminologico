package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.businessrules.*;
import cl.minsal.semantikos.kernel.daos.ConceptDAO;
import cl.minsal.semantikos.kernel.daos.DescriptionDAO;
import cl.minsal.semantikos.kernel.daos.RelationshipDAO;
import cl.minsal.semantikos.kernel.daos.ws.ConceptWSDAO;
import cl.minsal.semantikos.kernel.util.ConceptUtils;
import cl.minsal.semantikos.kernel.util.IDGenerator;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.businessrules.*;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.crossmaps.IndirectCrossmap;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetType;
import cl.minsal.semantikos.model.tags.Tag;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.modelweb.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.krb5.internal.crypto.Des;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.*;


import static cl.minsal.semantikos.model.DAO.NON_PERSISTED_ID;
import static cl.minsal.semantikos.model.PersistentEntity.getIdArray;

/**
 * @author Andrés Farías
 */
@Stateless
public class ConceptManagerImpl implements ConceptManager {

    /**
     * El logger de la clase
     */
    private static final Logger logger = LoggerFactory.getLogger(ConceptManagerImpl.class);

    @EJB
    private ConceptDAO conceptDAO;

    @EJB
    private ConceptWSDAO conceptWSDAO;

    @EJB
    private AuditManager auditManager;

    @EJB
    private DescriptionDAO descriptionDAO;

    @EJB
    private RefSetManager refSetManager;

    @EJB
    private RelationshipDAO relationshipDAO;

    @EJB
    private TagManager tagManager;

    @EJB
    private DescriptionManager descriptionManager;

    @EJB
    private RelationshipManagerImpl relationshipManager;

    @EJB
    private CrossmapsManagerImpl crossmapsManager;

    @EJB
    private ConceptTransferBR conceptTransferBR;

    @EJB
    private RelationshipBindingBR relationshipBindingBR;

    @EJB
    private ConceptSearchBR conceptSearchBR;

    @Override
    public ConceptSMTK getConceptByCONCEPT_ID(String conceptId) {

        /* Se recupera el concepto base (sus atributos) sin sus relaciones ni descripciones */
        ConceptSMTK concept = this.conceptDAO.getConceptByCONCEPT_ID(conceptId);

        /* Se cargan las descripciones del concepto */
        // TODO: Factorizar esto para que siempre se cree el concepto de la misma manera cuando se crea.
        //concept.setDescriptions(descriptionDAO.getDescriptionsByConcept(concept));

        return concept;
    }

    @Override
    public ConceptSMTK getConceptByID(long id) {

        /* Se recupera el concepto base (sus atributos) sin sus relaciones ni descripciones */
        ConceptSMTK conceptSMTK = this.conceptDAO.getConceptByID(id);

        /* Se cargan las descripciones del concepto */
        //conceptSMTK.setDescriptions(descriptionDAO.getDescriptionsByConcept(conceptSMTK));

        return conceptSMTK;
    }

    @Override
    public long persist(@NotNull ConceptSMTK conceptSMTK, User user) throws Exception {
        logger.debug("El concepto " + conceptSMTK + " será persistido.");

        /* Pre-condición técnica: el concepto no debe estar persistido */
        validatesIsNotPersistent(conceptSMTK);

        /* Se validan las invariantes */
        new ConceptInvariantsBR().invariants(conceptSMTK);

        /* Pre-condiciones: Reglas de negocio para la persistencia */
        new ConceptCreationBR().apply(conceptSMTK, user);

        /* En este momento se está listo para persistir el concepto (sus atributos básicos) */
        conceptDAO.persistConceptAttributes(conceptSMTK, user);
        conceptSMTK.setConceptID(IDGenerator.generator(String.valueOf(conceptSMTK.getId()),IDGenerator.TYPE_CONCEPT));
        conceptDAO.update(conceptSMTK);

        /* Y se persisten sus descripciones */
        for (Description description : conceptSMTK.getDescriptions()) {
            descriptionManager.bindDescriptionToConcept(conceptSMTK, description, false, user);
        }

        /* Y sus relaciones */
        for (Relationship relationship : conceptSMTK.getRelationships()) {
            relationshipManager.createRelationship(relationship);
            /* Se realizan las acciones asociadas a la asociación */
            relationshipBindingBR.postActions(relationship, user);
        }

        for (Tag tag : conceptSMTK.getTags()) {
            /* Y sus tags. los tags deberian estar persistidos, sin embargo se soporta el caso en que no lo esten */
            if(!tag.isPersistent()) {
                List<Tag> tags = tagManager.findTagByNamePattern(tag.getName());
                if (tags.isEmpty()) {
                    tag.setId(tagManager.persist(tag));
                }
                else {
                    tag = tags.get(0);
                }
            }
            tagManager.assignTag(conceptSMTK, tag);
        }

        /* Se deja registro en la auditoría sólo para conceptos modelados */

        auditManager.recordNewConcept(conceptSMTK, user);

        logger.debug("El concepto " + conceptSMTK + " fue persistido.");

        return conceptSMTK.getId();
    }

    @Override
    public void updateFields(@NotNull ConceptSMTK originalConcept, @NotNull ConceptSMTK updatedConcept, User user) {

        /* Se actualiza con el DAO */
        conceptDAO.update(updatedConcept);

        if (updatedConcept.isModeled()) {
            auditManager.recordUpdateConcept(updatedConcept, user);
        }
    }

    @Override
    public void update(@NotNull ConceptSMTK originalConcept, @NotNull ConceptSMTK updatedConcept, User user) throws Exception {

        /* Se validan las invariantes */
        new ConceptInvariantsBR().invariants(updatedConcept);

        /* Pre-condiciones: Reglas de negocio para la persistencia */
        new ConceptCreationBR().apply(updatedConcept, user);

        /**
         * Se asume que ambas imagenes deben estar persistidas
         */
        updatedConcept.setId(originalConcept.getId());

        updatedConcept.setConceptID(IDGenerator.generator(String.valueOf(updatedConcept.getId()),IDGenerator.TYPE_CONCEPT));

        /* Primero de actualizan los campos propios del concepto */
        if(!originalConcept.equals(updatedConcept)) {
            updateFields(originalConcept, updatedConcept, user);
        }

        /* Luego para cada descripción se realiza la acción correspondiente */
        for (Description description : ConceptUtils.getNewDesciptions(originalConcept.getDescriptions(), updatedConcept.getDescriptions())) {
            descriptionManager.bindDescriptionToConcept(updatedConcept, description, true, user);
        }

        for (Description description : ConceptUtils.getRemovedDescriptions(originalConcept.getDescriptions(), updatedConcept.getDescriptions())) {
            descriptionManager.deleteDescription(description, user);
        }

        for (Pair<Description, Description> descriptionPair : ConceptUtils.getModifiedDescriptions(originalConcept.getDescriptions(), updatedConcept.getDescriptions())) {
            descriptionManager.updateDescription(updatedConcept, descriptionPair.getFirst(), descriptionPair.getSecond(), user);
        }

        /* Luego para cada relación se realiza la acción correspondiente */
        for (Relationship relationship : ConceptUtils.getNewRelationships(originalConcept.getRelationships(), updatedConcept.getRelationships())) {
            relationshipManager.bindRelationshipToConcept(updatedConcept, relationship, user);
        }

        for (Relationship relationship : ConceptUtils.getRemovedRelationships(originalConcept.getRelationships(), updatedConcept.getRelationships())) {
            relationshipManager.removeRelationship(updatedConcept, relationship, user);
        }

        for (Pair<Relationship, Relationship> relationshipPair : ConceptUtils.getModifiedRelationships(originalConcept.getRelationships(), updatedConcept.getRelationships())) {
            relationshipManager.updateRelationship(updatedConcept, relationshipPair.getFirst(), relationshipPair.getSecond(), user);
        }

    }

    @Override
    public void publish(@NotNull ConceptSMTK conceptSMTK, User user) {
        conceptSMTK.setPublished(true);
        conceptDAO.update(conceptSMTK);

        if (conceptSMTK.isModeled()) {
            auditManager.recordConceptPublished(conceptSMTK, user);
        }
    }

    @Override
    public void delete(@NotNull ConceptSMTK conceptSMTK, User user) {

        /* Se validan las pre-condiciones para eliminar un concepto */
        ConceptDeletionBR conceptDeletionBR = new ConceptDeletionBR();
        conceptDeletionBR.preconditions(conceptSMTK, user);

        /* Se invalida el concepto */
        invalidate(conceptSMTK, user);

        /* Se elimina físicamente si es borrador */
        if (!conceptSMTK.isModeled()) {
            conceptDAO.delete(conceptSMTK);
        }
    }

    @Override
    public void invalidate(@NotNull ConceptSMTK conceptSMTK, @NotNull User user) {

        logger.info("Se dejará no vigente el concepto: " + conceptSMTK);

        /* Se validan las pre-condiciones para eliminar un concepto */
        new ConceptEditionBusinessRuleContainer().preconditionsConceptInvalidation(conceptSMTK, user);

        /* Se invalida el concepto */
        conceptSMTK.setPublished(false);
        conceptSMTK.setValidUntil(new Timestamp(System.currentTimeMillis()));
        conceptDAO.update(conceptSMTK);

        /* Se registra en el historial */
        if (conceptSMTK.isModeled()) {
            auditManager.recordConceptInvalidation(conceptSMTK, user);
        }
        logger.info("Se ha dejado no vigente el concepto: " + conceptSMTK);
    }

    @Override
    public void changeCategory(@NotNull ConceptSMTK conceptSMTK, @NotNull Category targetCategory, User user) {
        /* TODO: Validar reglas de negocio */

        /* TODO: Cambiar la categoría y actualizar el cambio */
        Category originalCategory = conceptSMTK.getCategory();

        /* Complex Logic here */

        /* Se registra en el historial si el concepto está modelado */
        if (conceptSMTK.isModeled()) {
            auditManager.recordConceptCategoryChange(conceptSMTK, originalCategory, user);
        }
    }

    @Override
    public void bindRelationshipToConcept(@NotNull ConceptSMTK conceptSMTK, @NotNull Relationship relationship, @NotNull User user) throws Exception {
        relationshipManager.bindRelationshipToConcept(conceptSMTK, relationship, user);
    }

    @Override
    public void changeTagSMTK(@NotNull ConceptSMTK conceptSMTK, @NotNull TagSMTK tagSMTK, User user) {
        /* Se realizan las validaciones básicas */
        new ConceptEditionBusinessRuleContainer().preconditionsConceptEditionTag(conceptSMTK);

        /* Se realiza la actualización */
        conceptDAO.update(conceptSMTK);
    }

    /**
     * Este método es responsable de validar que el concepto no se encuentre persistido.
     *
     * @param conceptSMTK El concepto sobre el cual se realiza la validación de persistencia.
     *
     * @throws javax.ejb.EJBException Se arroja si el concepto tiene un ID de persistencia.
     */
    private void validatesIsNotPersistent(ConceptSMTK conceptSMTK) throws EJBException {
        long id = conceptSMTK.getId();
        if (id != NON_PERSISTED_ID) {
            throw new EJBException("El concepto ya se encuentra persistido. ID=" + id);
        }
    }

    @Override
    public String generateConceptId(long id) {
        return IDGenerator.generator(String.valueOf(id),IDGenerator.TYPE_CONCEPT);
    }

    @Override
    public List<Description> getDescriptionsBy(ConceptSMTK concept) {
        return descriptionDAO.getDescriptionsByConcept(concept);
    }

    @Override
    public ConceptSMTK getConceptByDescriptionID(String descriptionId) {
        /* Se hace delegación sobre el DescriptionManager */
        return descriptionManager.getDescriptionByDescriptionID(descriptionId).getConceptSMTK();
    }

    @Override
    public List<Relationship> loadRelationships(ConceptSMTK concept) throws Exception {
        List<Relationship> relationships = relationshipDAO.getRelationshipsBySourceConcept(concept);
        /* Se agregan las relaciones al componente */
        concept.setRelationships(relationships);
        /* Se agregan los crossmaps indirectos al componente */
        List<IndirectCrossmap> relationshipsCrossMapIndirect = crossmapsManager.getIndirectCrossmaps(concept);
        relationships.addAll(relationshipsCrossMapIndirect);

        /* Se retorna la lista de relaciones agregadas al concepto */
        return relationships;
    }

    @Override
    public List<Relationship> getRelationships(ConceptSMTK concept) {
        return relationshipDAO.getRelationshipsBySourceConcept(concept);
    }

    @Override
    public ConceptSMTK getNoValidConcept() {
        return conceptDAO.getNoValidConcept();
    }

    @Override
    public ConceptSMTK transferConcept(ConceptSMTK conceptSMTK, Category category, User user) throws Exception {

        /* Validacion de pre-condiciones */
        conceptTransferBR.validatePreConditions(conceptSMTK);

        Category originalCategory = conceptSMTK.getCategory();

        /* Acciones de negocio */
        conceptSMTK.setCategory(category);
        conceptDAO.update(conceptSMTK);

        auditManager.recordConceptCategoryChange(conceptSMTK, originalCategory, user);

        logger.info("Se ha trasladado un concepto de categoría.");
        return conceptSMTK;
    }

    @Override
    public ConceptSMTK getPendingConcept() {
        return conceptDAO.getPendingConcept();
    }

    @Override
    public List<ConceptSMTK> perfectMatch(String pattern, List<Category> categories, List<RefSet> refsets, Boolean isModeled) {
        return conceptDAO.findPerfectMatch(pattern, PersistentEntity.getIdArray(categories), PersistentEntity.getIdArray(refsets), isModeled);
    }

    @Override
    public List<ConceptSMTK> truncateMatch(String pattern, List<Category> categories, List<RefSet> refsets, Boolean isModeled) {
        return conceptDAO.findTruncateMatch(pattern, PersistentEntity.getIdArray(categories), PersistentEntity.getIdArray(refsets), isModeled);
    }

    @Override
    public List<ConceptSMTK> getRelatedConcepts(ConceptSMTK conceptSMTK) {
        return conceptDAO.getRelatedConcepts(conceptSMTK);
    }
    @Override
    public List<ConceptSMTK> getRelatedConcepts(ConceptSMTK conceptSMTK, Category... categories) {

        /* Se recuperan los conceptos relacionados: 1o se intenta con los conceptos padres */
        //List<ConceptSMTK> relatedConcepts =  conceptWSDAO.getRelatedConcepts(conceptSMTK);
        List<ConceptSMTK> relatedConcepts =  conceptDAO.getRelatedConcepts(conceptSMTK);

        /* Si no hay categorías por las que filtrar, se retorna la lista original */
        if (categories == null || categories.length == 0) {
            return relatedConcepts;
        }

        /* Se filtra para retornar sólo aquellos que pertenecen a alguna de las categorías dadas */
        ArrayList<ConceptSMTK> filteredRelatedConcepts = new ArrayList<>();
        for (ConceptSMTK relatedConcept : relatedConcepts) {

            Category conceptCategory = relatedConcept.getCategory();
            List<Category> categoryFilters = Arrays.asList(categories);

            /* Se agrega el concepto si su categoría está dentro de las categorías para filtrar y si está modelado */
            if (categoryFilters.contains(conceptCategory) && relatedConcept.isModeled()) {
                filteredRelatedConcepts.add(relatedConcept);
            }
        }

        /* Si no se obtuvieron conceptos relacionados se intenta con los conceptos hijos */
        if(filteredRelatedConcepts.isEmpty()) {
            for (Relationship relationship : relationshipManager.getRelationshipsBySourceConcept(conceptSMTK)) {
                if(relationship.getRelationshipDefinition().getTargetDefinition().isSMTKType()) {
                    ConceptSMTK relatedConcept = (ConceptSMTK) relationship.getTarget();
                    List<Category> categoryFilters = Arrays.asList(categories);

                    /* Se agrega el concepto si su categoría está dentro de las categorías para filtrar y si está modelado */
                    if (categoryFilters.contains(relatedConcept.getCategory()) && relatedConcept.isModeled()) {
                        filteredRelatedConcepts.add(relatedConcept);
                    }
                }
            }
        }

        return filteredRelatedConcepts;
    }

    @Override
    public List<ConceptSMTK> findConcepts(String pattern, List<Category> categories, List<RefSet> refsets, Boolean modeled) {

        String patternStandard = conceptSearchBR.standardizationPattern(pattern);
        List<ConceptSMTK> results;

        results = conceptDAO.findPerfectMatch(patternStandard, PersistentEntity.getIdArray(categories),
                                              PersistentEntity.getIdArray(refsets), modeled);

        if (results.isEmpty()) {
            results = conceptDAO.findTruncateMatch(patternStandard, PersistentEntity.getIdArray(categories),
                                            PersistentEntity.getIdArray(refsets), modeled);
        }

        new ConceptSearchBR().applyPostActions(results);

        return results;
    }

    @Override
    public int countConcepts(String pattern, List<Category> categories, List<RefSet> refsets, Boolean modeled) {
        String patternStandard = conceptSearchBR.standardizationPattern(pattern);

        int count = countPerfectMatch(patternStandard, categories, refsets, modeled);

        if (count != 0) {
            return count;
        } else {
            return countTruncateMatch(patternStandard, categories, refsets, modeled);
        }
    }

    public int countPerfectMatch(String pattern, List<Category> categories, List<RefSet> refsets, Boolean modeled) {
        return conceptDAO.countPerfectMatch(pattern, PersistentEntity.getIdArray(categories), PersistentEntity.getIdArray(refsets), modeled);
    }

    public int countTruncateMatch(String pattern,List<Category> categories, List<RefSet> refsets, Boolean modeled) {
        return conceptDAO.countTruncateMatch(pattern,  PersistentEntity.getIdArray(categories), PersistentEntity.getIdArray(refsets), modeled);
    }

    @Override
    public List<ConceptSMTK> findConceptsPaginated(Category category, int pageSize, int pageNumber, Boolean modeled) {
        return this.conceptDAO.getConceptsPaginated(category.getId(), pageSize, pageNumber, modeled);
        //return this.conceptWSDAO.getConceptsPaginated(category.getId(), pageSize, pageNumber, modeled);
    }

    @Override
    public List<ConceptSMTK> findConceptsWithTarget(Relationship relationship) {
        return conceptDAO.findConceptsWithTarget(relationship);
    }

}
