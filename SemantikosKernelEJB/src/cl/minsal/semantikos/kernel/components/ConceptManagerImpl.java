package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.ConceptDAO;
import cl.minsal.semantikos.kernel.daos.DescriptionDAO;
import cl.minsal.semantikos.kernel.daos.RelationshipDAO;
import cl.minsal.semantikos.kernel.util.IDGenerator;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.businessrules.*;
import cl.minsal.semantikos.model.crossmaps.IndirectCrossmap;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.*;

import static cl.minsal.semantikos.kernel.daos.DAO.NON_PERSISTED_ID;
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
    private RelationshipManager relationshipManager;

    @EJB
    private CrossmapsManager crossmapsManager;

    @EJB
    private ConceptTransferBR conceptTransferBR;

    @EJB
    private RelationshipBindingBRInterface relationshipBindingBR;

    @Override
    public ConceptSMTK getConceptByCONCEPT_ID(String conceptId) {

        /* Se recupera el concepto base (sus atributos) sin sus relaciones ni descripciones */
        ConceptSMTK concept = this.conceptDAO.getConceptByCONCEPT_ID(conceptId);

        /* Se cargan las descripciones del concepto */
        // TODO: Factorizar esto para que siempre se cree el concepto de la misma manera cuando se crea.
        concept.setDescriptions(descriptionDAO.getDescriptionsByConcept(concept));

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
    public List<ConceptSMTK> findConceptsBy(Category category) {

        /* Se validan los parámetros */
        if (category == null) {
            logger.error("Se solicitan los conceptos de categoría nula.");
            return Collections.emptyList();
        }

        /* Se delega al DAO directamente */
        return conceptDAO.findConceptsBy(category);
    }



    @Override
    public List<ConceptSMTK> findConcepts(Category aCategory, List<String> refSetNames, RelationshipDefinition basicTypeAttribute, String value) {

        /* Primero se debe validar que el RelationshipDefinition es tipo Tipo Básico */
        boolean basicType = basicTypeAttribute.getTargetDefinition().isBasicType();
        if (!basicType) {
            throw new IllegalArgumentException("Se consideró un tipo básico de un target que no lo es: " + basicTypeAttribute);
        }

        /* Luego se recuperan los refsets */
        ArrayList<RefSet> refsets = new ArrayList<>();
        for (String refSetName : refSetNames) {
            RefSet refsetByName = refSetManager.getRefsetByName(refSetName);
            refsets.add(refsetByName);
        }

        /* Por razones de eficiencia, es mejor realizar la búsqueda directamente en la base de datos */
        if (refsets.isEmpty()) {
            return conceptDAO.findConceptsWithStringBasicType(aCategory, basicTypeAttribute, value);
        } else {
            return conceptDAO.findConceptsWithStringBasicType(aCategory, refsets, basicTypeAttribute, value);
        }
    }

    @Override
    public List<ConceptSMTK> findModeledConceptBy(Category category, int pageSize, int pageNumber) {
        return this.conceptDAO.getModeledConceptBy(category.getId(), pageSize, pageNumber);
    }

    @Override
    public List<ConceptSMTK> findModeledConceptPaginated(Category category, int pageSize, int pageNumber) {
        return this.conceptDAO.getModeledConceptPaginated(category.getId(), pageSize, pageNumber);
    }

    @Override
    public int countModeledConceptBy(Category category) {
        return this.conceptDAO.countModeledConceptBy(category.getId());
    }

    @Override
    public List<ConceptSMTK> findModeledConceptsBy(RefSet refSet, int page, int pageSize) {
        return this.conceptDAO.findModeledConceptsBy(refSet, page, pageSize);
    }

    @Override
    public Integer countModeledConceptsBy(RefSet refSet) {
        return this.conceptDAO.countModeledConceptsBy(refSet);
    }

    @Override
    public Integer countConceptBy(String pattern, Long[] categories, Long[] refsets) {
        boolean isModeled = true;
        categories = (categories == null) ? new Long[0] : categories;
        refsets = (refsets == null) ? new Long[0] : refsets;

        pattern = standardizationPattern(pattern);
        String[] arrayPattern = patternToArray(pattern);

        return this.conceptDAO.countConceptBy(arrayPattern, categories, refsets, isModeled);
    }

    @Override
    public List<ConceptSMTK> findConceptsBy(String patternOrConceptID, Long[] categories, int pageNumber, int pageSize) {

        boolean isModeled = true;
        categories = (categories == null) ? new Long[0] : categories;

        //Búsqueda por categoría
        if (categories.length > 0 && patternOrConceptID.trim().length() == 0) {
            return conceptDAO.findConceptsBy(categories, isModeled, pageSize, pageNumber);
        }

        //Búsqueda páginas
        if (categories.length == 0 && patternOrConceptID.trim().length() == 0) {
            return conceptDAO.getConceptsBy(isModeled, pageSize, pageNumber);
        }
        return findConceptBy(patternOrConceptID, categories, pageNumber, pageSize, isModeled);
    }

    @Override
    public List<ConceptSMTK> findConceptTruncatePerfect(String pattern, Long[] categories, Long[] refsets, int pageNumber, int pageSize) {
        boolean isModeled = true;
        //TODO: Actualizar esto de los estados que ya no va.

        categories = (categories == null) ? new Long[0] : categories;
        refsets = (refsets == null) ? new Long[0] : refsets;

        pattern = standardizationPattern(pattern);
        String[] arrayPattern = patternToArray(pattern);

        return conceptDAO.findConceptsBy(arrayPattern, categories, refsets, isModeled, pageSize, pageNumber);
    }

    @Override
    public List<ConceptSMTK> findConceptTruncatePerfect(String termPattern, List<Category> categories, List<RefSet> refSets) {

        /* En esta implementación se utiliza el método
         * findConceptTruncatePerfect(
         *      String pattern,
         *      Long[] categories,
         *      Long[] refsets,
         *      int pageNumber,
         *      int pageSize)
         *
         * Se recuperan de a 1000 conceptos
         */
        List<ConceptSMTK> concepts = new ArrayList<>();
        int page = 1;


        boolean thereAreMore;
        do {
            //List<ConceptSMTK> conceptTruncatePerfect = truncateMatch(termPattern, getIdArray(categories), pageNumber, pageSize, isModeled);
            List<ConceptSMTK> conceptTruncatePerfect = findConceptTruncatePerfect(termPattern, getIdArray(categories), getIdArray(refSets), page++, 1000);
            concepts.addAll(conceptTruncatePerfect);

            /* Si la busqueda retorno 1000, quizás hay más */
            thereAreMore = conceptTruncatePerfect.size() == 1000;
        } while (thereAreMore);

        return concepts;
    }

    @Override
    public int countConceptBy(String pattern, Long[] categories) {
        boolean isModeled = true;
        pattern = standardizationPattern(pattern);
        //Cuenta por categoría
        if (categories.length > 0) {
            return conceptDAO.countConceptBy((String[]) null, categories, isModeled);
        }
        if (categories.length == 0 && pattern.trim().length() == 0) {
            return conceptDAO.countConceptBy((String[]) null, categories, isModeled);
        }
        return countConceptBy(pattern, categories, isModeled);

    }

    @Override
    public List<ConceptSMTK> findConceptsBy(String pattern) {

        /* Se realiza la búsqueda estándard */
        List<ConceptSMTK> conceptSMTKList = findConceptsBy(pattern, new Long[0], 0, countConceptBy(pattern, new Long[0]));
        if (conceptSMTKList.size() != 0) {
            return conceptSMTKList;
        }

        /* Si la búsqueda estándard no dio resultados, se intenta con una búsqueda truncada */
        else {
            pattern = truncatePattern(pattern);
            return findConceptsBy(pattern, new Long[0], 0, countConceptBy(pattern, new Long[0]));
        }

    }

    @Override
    public List<ConceptSMTK> getConceptBy(RefSet refSet) {
        return conceptDAO.findConceptsBy(refSet);

    }

    @Override
    public void persist(@NotNull ConceptSMTK conceptSMTK, User user) {
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

        /* Y sus tags */
        for (Tag tag : conceptSMTK.getTags()) {
            if (tag.isPersistent()) {
                tagManager.persist(tag);
                tagManager.assignTag(conceptSMTK, tag);
            }
        }

        /* Se deja registro en la auditoría sólo para conceptos modelados */

        auditManager.recordNewConcept(conceptSMTK, user);

        logger.debug("El concepto " + conceptSMTK + " fue persistido.");
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
    public void bindRelationshipToConcept(@NotNull ConceptSMTK conceptSMTK, @NotNull Relationship relationship, @NotNull User user) {
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
    public List<Relationship> loadRelationships(ConceptSMTK concept) {
        List<Relationship> relationships = relationshipDAO.getRelationshipsBySourceConcept(concept.getId());
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
        return relationshipDAO.getRelationshipsBySourceConcept(concept.getId());
    }

    @Override
    public List<ConceptSMTK> getConceptDraft() {
        return conceptDAO.getConceptDraft();
    }

    @Override
    public ConceptSMTK getNoValidConcept() {
        return conceptDAO.getNoValidConcept();
    }

    @Override
    public ConceptSMTK transferConcept(ConceptSMTK conceptSMTK, Category category, User user) {

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
    public List<ConceptSMTK> getRelatedConcepts(ConceptSMTK conceptSMTK) {
        return conceptDAO.getRelatedConcepts(conceptSMTK);
    }
    @Override
    public List<ConceptSMTK> getRelatedConcepts(ConceptSMTK conceptSMTK, Category... categories) {

        /* Se recuperan los conceptos relacionados: 1o se intenta con los conceptos padres */
        List<ConceptSMTK> relatedConcepts =  getRelatedConcepts(conceptSMTK);

        /* Si no hay categorías por las que filtrar, se retorna la lista original */
        if (categories == null || categories.length == 0) {
            return relatedConcepts;
        }

        /* Se filtra para retornar sólo aquellos que pertenecen a alguna de las categorías dadas */
        ArrayList<ConceptSMTK> filteredRelatedConcepts = new ArrayList<>();
        for (ConceptSMTK relatedConcept : relatedConcepts) {

            Category conceptCategory = relatedConcept.getCategory();
            List<Category> categoryFilters = Arrays.asList(categories);

            /* Se agrega el concepto si su categoría está dentro de las categorías para filtrar */
            if (categoryFilters.contains(conceptCategory)) {
                filteredRelatedConcepts.add(relatedConcept);
            }
        }

        /* Si no se obtuvieron conceptos relacionados se intenta con los conceptos hijos */
        if(filteredRelatedConcepts.isEmpty()) {
            for (Relationship relationship : getRelationships(conceptSMTK)) {
                if(relationship.getRelationshipDefinition().getTargetDefinition().isSMTKType()) {
                    ConceptSMTK relatedConcept = (ConceptSMTK) relationship.getTarget();
                    List<Category> categoryFilters = Arrays.asList(categories);

                    /* Se agrega el concepto si su categoría está dentro de las categorías para filtrar */
                    if (categoryFilters.contains(relatedConcept.getCategory())) {
                        filteredRelatedConcepts.add(relatedConcept);
                    }
                }
            }
        }

        return filteredRelatedConcepts;
    }


    @Override
    public List<ConceptSMTK> findConceptBy(String pattern, Long[] categories, int pageNumber, int pageSize, boolean isModeled) {

        String patternStandard = standardizationPattern(pattern);

        if (patternStandard.length() > 2) {
            List<ConceptSMTK> resultToFind = perfectMatch(patternStandard, categories, pageNumber, pageSize, isModeled);

            if (!resultToFind.isEmpty()) {
                return resultToFind;
            } else {

                resultToFind = truncateMatch(patternStandard, categories, pageNumber, pageSize, isModeled);
                if (resultToFind.isEmpty()) {
                    return Collections.emptyList();
                } else {
                    return resultToFind;
                }
            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<ConceptSMTK> perfectMatch(String pattern, Long[] categories, int pageNumber, int pageSize, Boolean isModeled) {

        /**
         * Existe al menos una categoría y el patron de búsqueda
         */
        if ((categories.length != 0 && pattern.length() != 0)) {
            return conceptDAO.findPerfectMatchConceptBy(pattern, categories, isModeled, pageSize, pageNumber);
        }

        /**
         * No existen categorías pero si un patrón de búsqueda
         */
        if ((categories.length == 0 && pattern.length() != 0)) {
            return conceptDAO.findPerfectMatchConceptBy(pattern, isModeled, pageSize, pageNumber);
        }


        return Collections.emptyList();
    }

    @Override
    public List<ConceptSMTK> truncateMatch(String pattern, Long[] categories, int pageNumber, int pageSize, Boolean isModeled) {

        String patternTruncate = truncatePattern(pattern);
        List <ConceptSMTK> conceptSMTKs= new ArrayList<>();
        /**
         * Existe al menos una categoría y el patron de búsqueda
         */
        if ((categories.length != 0 && pattern.length() != 0)) {
            conceptSMTKs= conceptDAO.findTruncateMatchConceptBy(patternTruncate, categories, isModeled, pageSize, pageNumber);
        }else{
            if ((categories.length == 0 && pattern.length() != 0)) {
                conceptSMTKs= conceptDAO.findTruncateMatchConceptBy(patternTruncate, isModeled, pageSize, pageNumber);
            }
        }


        if(conceptSMTKs.isEmpty()){
            return Collections.emptyList();
        }
        Collections.sort(conceptSMTKs,new ConceptSMTKComparator());

        return conceptSMTKs;
    }

    class ConceptSMTKComparator implements Comparator<ConceptSMTK> {

        @Override
        public int compare(ConceptSMTK conceptSMTK1, ConceptSMTK conceptSMTK2) {

            return conceptSMTK1.getDescriptionFavorite().getTerm().length() -  conceptSMTK2.getDescriptionFavorite().getTerm().length();
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }


    @Override
    public int countConceptBy(String pattern, Long[] categories, Boolean isModeled) {
        String patternStandard = standardizationPattern(pattern);

        int count = countPerfectMatch(patternStandard, categories, isModeled);

        if (count != 0) {
            return count;
        } else {
            count = countTruncateMatch(patternStandard, categories, isModeled);
            if (count == 0) {
                return 0;
            } else {
                return count;
            }
        }
    }

    @Override
    public int countPerfectMatch(String pattern, Long[] categories, Boolean isModeled) {
        /**
         * Existe al menos una categoría y el patron de búsqueda
         */
        if ((categories.length != 0 && pattern.length() != 0)) {
            return conceptDAO.countPerfectMatchConceptBy(pattern, categories, isModeled);
        }

        /**
         * No existen categorías pero si un patrón de búsqueda
         */
        if ((categories.length == 0 && pattern.length() != 0)) {
            return conceptDAO.countPerfectMatchConceptBy(pattern, new Long[0], isModeled);
        }

        return 0;
    }

    @Override
    public int countTruncateMatch(String pattern, Long[] categories, Boolean isModeled) {
        String patternTruncate = truncatePattern(pattern);

        /**
         * Existe al menos una categoría y el patron de búsqueda
         */
        if ((categories.length != 0 && pattern.length() != 0)) {
            return conceptDAO.countTruncateMatchConceptBy(patternTruncate, categories, isModeled);
        }

        /**
         * No existen categorías pero si un patrón de búsqueda
         */
        if ((categories.length == 0 && pattern.length() != 0)) {
            return conceptDAO.countTruncateMatchConceptBy(patternTruncate, new Long[0], isModeled);
        }

        return 0;
    }

    /**
     * Método de normalización del patrón de búsqueda, lleva las palabras a minúsculas,
     * le quita los signos de puntuación y ortográficos
     *
     * @param pattern patrón de texto a normalizar
     * @return patrón normalizado
     */
    //TODO: Falta quitar los StopWords (no se encuentran definidos)
    public String standardizationPattern(String pattern) {

        if (pattern != null) {
            pattern = Normalizer.normalize(pattern, Normalizer.Form.NFD);
            pattern = pattern.toLowerCase();
            pattern = pattern.replaceAll("[^\\p{ASCII}]", "");
            pattern = pattern.replaceAll("\\p{Punct}+", "");
        } else {
            pattern = "";
        }
        return pattern;
    }

    /**
     * Método encargado de truncar a un largo de 3 las palabras del String ingresado
     *
     * @param pattern arreglo de palabras
     * @return arreglo de String con las palabras truncadas
     */
    private String truncatePattern(String pattern) {
        if(pattern.length()==0)return pattern;
        pattern = standardizationPattern(pattern);
        String[] arrayToPattern = patternToArray(pattern);
        String patternTruncate="";
        for (int i = 0; i < arrayToPattern.length; i++) {
            if ( arrayToPattern[i].length()<=2) {
                patternTruncate = patternTruncate + arrayToPattern[i].substring(0, arrayToPattern[i].length()) + " ";
            } else {
                patternTruncate = patternTruncate + arrayToPattern[i].substring(0, 3) + " ";
            }
        }
        if(patternTruncate.charAt(patternTruncate.length()-1)==' '){
            patternTruncate=patternTruncate.substring(0,patternTruncate.length()-1);
        }
        return patternTruncate;
    }

    /**
     * Método encargado de convertir un string en una lista de string.
     *
     * @param pattern patrón de texto
     * @return retorna una lista de String
     */

    private String[] patternToArray(String pattern) {
        if (pattern != null) {
            StringTokenizer st;
            String token;
            st = new StringTokenizer(pattern, " ");
            ArrayList<String> listPattern = new ArrayList<>();
            int count = 0;

            while (st.hasMoreTokens()) {
                token = st.nextToken();
                if (token.length() >= 2) {
                    listPattern.add(token.trim());
                }
                if (count == 0 && listPattern.size() == 0) {
                    listPattern.add(token.trim());
                }
                count++;
            }
            return listPattern.toArray(new String[listPattern.size()]);
        }
        return new String[0];
    }


}
