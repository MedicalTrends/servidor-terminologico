package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.ConceptDAO;
import cl.minsal.semantikos.kernel.daos.RelationshipAttributeDAO;
import cl.minsal.semantikos.kernel.daos.RelationshipDAO;
import cl.minsal.semantikos.kernel.daos.TargetDAO;
import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.User;
import cl.minsal.semantikos.model.businessrules.*;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

import static java.lang.System.currentTimeMillis;

/**
 * @author Andrés Farías.
 */
@Stateless
public class RelationshipManagerImpl implements RelationshipManager {

    /** El gestor de relaciones con la base de datos */
    @EJB
    private RelationshipDAO relationshipDAO;

    @EJB
    private ConceptDAO conceptDAO;

    @EJB
    private TargetDAO targetDAO;

    @EJB
    private AuditManager auditManager;

    @EJB
    private RelationshipAttributeDAO relationshipAttributeDAO;

    @EJB
    private RelationshipBindingBRInterface relationshipBindingBR;

    @Override
    public Relationship bindRelationshipToConcept(ConceptSMTK concept, Relationship relationship, User user) {

        /* Primero se validan las reglas de negocio asociadas a la eliminación de un concepto */
        relationshipBindingBR.verifyPreConditions(concept, relationship, user);

        /* Se hace la relación a nivel del modelo */
        if (!concept.getRelationships().contains(relationship)) {
            concept.addRelationship(relationship);
        }

        /* Se asegura la persistencia de la entidad */
        assurePersistence(concept, relationship, user);

          /* Se registra en el historial si el concepto fuente de la relación está modelado */
        if (relationship.getSourceConcept().isModeled()) {
            auditManager.recordRelationshipCreation(relationship, user);
        }

        /* Se realizan las acciones asociadas a la asociación */
        new RelationshipBindingBR().postActions(relationship, conceptDAO);

        /* Se retorna persistida */
        return relationship;
    }

    @Override
    public Relationship createRelationship(Relationship relationship) {
        relationship = relationshipDAO.persist(relationship);
        for (RelationshipAttribute relationshipAttribute : relationship.getRelationshipAttributes()) {
            relationshipAttribute.setRelationship(relationship);
            relationshipAttributeDAO.createRelationshipAttribute(relationshipAttribute);
        }
        return relationship;
    }

    /**
     * Este método es responsable de asegurar que una relación sea persistente. Si no lo es, lo persiste.
     *
     * @param concept      El concepto asociado a la relación.
     * @param relationship La relación.
     * @param user         El usuario que realiza la operación.
     */
    private void assurePersistence(ConceptSMTK concept, Relationship relationship, User user) {
        if (!relationship.isPersistent()) {

            /* Se validan las reglas de negocio */
            new ConceptCreationBR().apply(concept, user);

            relationshipDAO.persist(relationship);

            /* Se persisten los atributos */
            for (RelationshipAttribute relationshipAttribute : relationship.getRelationshipAttributes()) {
                relationshipAttribute.setRelationship(relationship);
                relationshipAttributeDAO.createRelationshipAttribute(relationshipAttribute);
            }
        }
    }

    @Override
    public RelationshipDefinition createRelationshipDefinition(RelationshipDefinition relationshipDefinition) {
        return relationshipDAO.persist(relationshipDefinition);
    }

    @Override
    public Relationship removeRelationship(Relationship relationship, User user) {

        /* Primero se validan las reglas de negocio asociadas a la eliminación de un concepto */
        new RelationshipRemovalBR().applyRules(relationship, user);

        /* Luego se elimina la relación */
        relationship.setValidityUntil(new Timestamp(currentTimeMillis()));
        relationshipDAO.invalidate(relationship);

        /* Se registra en el historial la eliminación (si el concepto asociado está modelado) */
        if (relationship.getSourceConcept().isModeled()) {
            auditManager.recordRelationshipRemoval(relationship, user);
        }

        return relationship;
    }

    @Override
    public Relationship[] findRelationsByOriginConcept(long id) {
        // TODO: Terminar esto
        return new Relationship[0];
    }

    @Override
    public List<Relationship> findRelationsByTargetSCTConcept(ConceptSCT conceptSCT) {
        return relationshipDAO.getRelationshipsToCSTConcept(conceptSCT);
    }

    @Override
    public List<ConceptSMTK> getTargetConceptsByCategory(ConceptSMTK conceptSMTK, Category category) {

        /* Se espera, por defecto, que el concepto no traiga sus relaciones cargadas, pero se verificará */
        if (conceptSMTK.getRelationships().isEmpty()) {
            List<Relationship> relationshipsBySourceConcept = this.getRelationshipsBySourceConcept(conceptSMTK);
            conceptSMTK.setRelationships(relationshipsBySourceConcept);
        }

        /* Se retornan los conceptos que son el destino de relaciones SMTK */
        return conceptSMTK.getRelatedSMTKConceptsBy(category);
    }

    @Override
    public void updateRelationship(@NotNull ConceptSMTK conceptSMTK, @NotNull Relationship originalRelationship, @NotNull Relationship editedRelationship, @NotNull User user) {

        /* Se aplican las reglas de negocio */
        new RelationshipEditionBR().applyRules(originalRelationship, editedRelationship);

        /* Si el concepto es borrador, sólo se actualiza la relación BR-DES-006 */
        if (!conceptSMTK.isModeled()) {

            /* Se copian los cambios al concepto original */
            mergeRelationship(originalRelationship, editedRelationship);

            /* Si el concepto editado está persistido se elimina */
            /*
            if (editedRelationship.isPersistent()){
                relationshipDAO.delete(editedRelationship);
            }
            */

            /* Se actualiza la relación original */
            relationshipDAO.update(originalRelationship);

            targetDAO.update(editedRelationship);

            /* Se actualizan los atributos */

            for (RelationshipAttribute attribute : editedRelationship.getRelationshipAttributes()) {
                // TODO: Normalizar esto cuando se normalice la clase <code>RelationshipAttribute</code>
                /**
                 * Si el atributo no esta persistido, persistirlo
                 */
                if (attribute.getIdRelationshipAttribute() == null) {
                    relationshipAttributeDAO.createRelationshipAttribute(attribute);
                }
                /**
                 * Si el atributo ya esta persistido, actualizarlo
                 */
                else {
                    relationshipAttributeDAO.update(attribute);
                    targetDAO.update(attribute);
                }

            }

        }

        /* Si el concepto está modelado, se versiona y actualiza */
        if (conceptSMTK.isModeled()) {

            /* Primero se versiona el original */
            this.invalidate(originalRelationship);
            relationshipDAO.persist(editedRelationship);

            /* Se actualiza el objeto de negocio */
            List<Relationship> relationships = conceptSMTK.getRelationships();
            relationships.remove(originalRelationship);
            if (!relationships.contains(editedRelationship)) {
                relationships.add(editedRelationship);
            }
        }

        /* Registrar en el Historial si es preferida (Historial BR) */
        if (editedRelationship.isAttribute() && editedRelationship.getSourceConcept().isModeled()) {
            auditManager.recordAttributeChange(conceptSMTK, originalRelationship, user);
        }
    }

    /**
     * Este método copia los atributos de una relación editada hacia la original.
     *
     * @param originalRelationship La relación original.
     * @param editedRelationship   La relación editada.
     */
    private void mergeRelationship(Relationship originalRelationship, Relationship editedRelationship) {
        originalRelationship.setValidityUntil(editedRelationship.getValidityUntil());
        originalRelationship.setRelationshipDefinition(editedRelationship.getRelationshipDefinition());
        originalRelationship.setSourceConcept(editedRelationship.getSourceConcept());
        originalRelationship.setTarget(editedRelationship.getTarget());
    }

    @Override
    // TODO: Terminar esto
    public int updateRelationAttribute(int idRelationship, int idRelationshipAttribute) {
        return 0;
    }

    @Override
    public Relationship invalidate(Relationship relationship) {
        relationship.setValidityUntil(new Timestamp(currentTimeMillis()));
        relationshipDAO.invalidate(relationship);

        return relationship;
    }

    @Override
    public List<Relationship> getRelationshipsLike(RelationshipDefinition relationshipDefinition, Target target) {
        return relationshipDAO.getRelationshipsLike(relationshipDefinition, target);
    }

    @Override
    public List<Relationship> getRelationshipsBySourceConcept(ConceptSMTK concept) {
        return relationshipDAO.getRelationshipsBySourceConcept(concept.getId());
    }
}
