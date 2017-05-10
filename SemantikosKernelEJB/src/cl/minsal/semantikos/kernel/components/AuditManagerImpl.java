package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.AuditDAO;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.audit.AuditActionType;
import cl.minsal.semantikos.model.audit.ConceptAuditAction;
import cl.minsal.semantikos.model.audit.RefSetAuditAction;
import cl.minsal.semantikos.model.businessrules.HistoryRecordBL;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.crossmaps.Crossmap;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static cl.minsal.semantikos.model.descriptions.DescriptionType.PREFERIDA;
import static cl.minsal.semantikos.model.audit.AuditActionType.*;

/**
 * @author Andrés Farías
 */
@Stateless
public class AuditManagerImpl implements AuditManager {

    @EJB
    private AuditDAO auditDAO;

    @Override
    public void recordNewConcept(ConceptSMTK conceptSMTK, User user) {

        /* Se crea el registro de historial, para poder validar Reglas de Negocio */
        ConceptAuditAction conceptAuditAction = new ConceptAuditAction(conceptSMTK, CONCEPT_CREATION, now(), user, conceptSMTK);


        auditDAO.recordAuditAction(conceptAuditAction);
    }

    @Override
    public void recordUpdateConcept(ConceptSMTK conceptSMTK, User user) {

        /* Se crea el registro de historial, para poder validar Reglas de Negocio */
        ConceptAuditAction conceptAuditAction = new ConceptAuditAction(conceptSMTK, CONCEPT_ATTRIBUTE_CHANGE, now(), user, conceptSMTK);

        /* Se validan las reglas de negocio para realizar el registro */
        new HistoryRecordBL().validate(conceptAuditAction);

        auditDAO.recordAuditAction(conceptAuditAction);
    }

    @Override
    public void recordDescriptionMovement(ConceptSMTK sourceConcept, ConceptSMTK targetConcept, Description description, User user) {

        /* Se crea el registro de historial, para poder validar Reglas de Negocio */
        ConceptAuditAction conceptAuditMovement = new ConceptAuditAction(sourceConcept, CONCEPT_DESCRIPTION_MOVEMENT, now(), user, description);
        ConceptAuditAction conceptAuditAddingDescription = new ConceptAuditAction(targetConcept, CONCEPT_DESCRIPTION_RECEPTION, now(), user, description);

        /* Se validan las reglas de negocio para realizar el registro */
        if (sourceConcept.isModeled()) {
            new HistoryRecordBL().validate(conceptAuditMovement);
            auditDAO.recordAuditAction(conceptAuditMovement);
        }

        if(targetConcept.isModeled()){
            new HistoryRecordBL().validate(conceptAuditAddingDescription);
            auditDAO.recordAuditAction(conceptAuditAddingDescription);
        }

    }

    @Override
    public void recordConceptPublished(ConceptSMTK conceptSMTK, User user) {

        ConceptAuditAction auditAction = new ConceptAuditAction(conceptSMTK, CONCEPT_PUBLICATION, now(), user, conceptSMTK);

        /* Se validan las reglas de negocio para realizar el registro */
        new HistoryRecordBL().validate(auditAction);
        auditDAO.recordAuditAction(auditAction);
    }

    @Override
    public void recordFavouriteDescriptionUpdate(ConceptSMTK conceptSMTK, Description originalDescription, User user) {

        /* Condición sobre la cual se debe registrar el cambio */
        if (!conceptSMTK.isModeled() || !originalDescription.getDescriptionType().equals(PREFERIDA)) {
            return;
        }

        /* Se validan las reglas de negocio para realizar el registro */
        ConceptAuditAction auditAction = new ConceptAuditAction(conceptSMTK, CONCEPT_FAVOURITE_DESCRIPTION_CHANGE, now(), user, originalDescription);
        new HistoryRecordBL().validate(auditAction);

        auditDAO.recordAuditAction(auditAction);
    }

    @Override
    public void recordConceptCategoryChange(ConceptSMTK conceptSMTK, Category originalCategory, User user) {

        /* Se validan las reglas de negocio para realizar el registro */
        ConceptAuditAction auditAction = new ConceptAuditAction(conceptSMTK, CONCEPT_CATEGORY_CHANGE, now(), user, originalCategory);
        new HistoryRecordBL().validate(auditAction);

        auditDAO.recordAuditAction(auditAction);
    }

    @Override
    public void recordAttributeChange(ConceptSMTK conceptSMTK, Relationship originalRelationship, User user) {
        /* Se validan las reglas de negocio para realizar el registro */
        //ConceptAuditAction auditAction = new ConceptAuditAction(conceptSMTK, CONCEPT_ATTRIBUTE_CHANGE, now(), user, originalRelationship);
        ConceptAuditAction auditAction = new ConceptAuditAction(conceptSMTK, CONCEPT_RELATIONSHIP_REMOVAL, now(), user, originalRelationship);
        new HistoryRecordBL().validate(auditAction);

        auditDAO.recordAuditAction(auditAction);
    }

    @Override
    public void recordRelationshipCreation(Relationship relationship, User user) {

        /* Se validan las reglas de negocio para realizar el registro */
        ConceptAuditAction auditAction = new ConceptAuditAction(relationship.getSourceConcept(), CONCEPT_RELATIONSHIP_CREATION, now(), user, relationship);
        new HistoryRecordBL().validate(auditAction);

        auditDAO.recordAuditAction(auditAction);
    }

    @Override
    public void recordRelationshipRemoval(Relationship relationship, User user) {
        /* Se validan las reglas de negocio para realizar el registro */
        ConceptAuditAction auditAction = new ConceptAuditAction(relationship.getSourceConcept(), CONCEPT_RELATIONSHIP_REMOVAL, now(), user, relationship);
        new HistoryRecordBL().validate(auditAction);

        auditDAO.recordAuditAction(auditAction);
    }

    @Override
    public void recordCrossMapCreation(Crossmap crossmap, User user) {
        /* Se validan las reglas de negocio para realizar el registro */
        ConceptAuditAction auditAction = new ConceptAuditAction(crossmap.getSourceConcept(), CONCEPT_RELATIONSHIP_CROSSMAP_CREATION, now(), user, crossmap);
        new HistoryRecordBL().validate(auditAction);

        auditDAO.recordAuditAction(auditAction);
    }

    @Override
    public void recordCrossMapRemoval(Crossmap crossmap, User user) {
        /* Se validan las reglas de negocio para realizar el registro */
        ConceptAuditAction auditAction = new ConceptAuditAction(crossmap.getSourceConcept(), CONCEPT_RELATIONSHIP_CROSSMAP_REMOVAL, now(), user, crossmap);
        new HistoryRecordBL().validate(auditAction);

        auditDAO.recordAuditAction(auditAction);
    }

    @Override
    public void recordDescriptionCreation(Description description, User user) {

        /* Se validan las reglas de negocio para realizar el registro */
        ConceptAuditAction auditAction = new ConceptAuditAction(description.getConceptSMTK(), CONCEPT_DESCRIPTION_CREATION, now(), user, description);
        new HistoryRecordBL().validate(auditAction);

        auditDAO.recordAuditAction(auditAction);

    }

    @Override
    public void recordDescriptionDeletion(ConceptSMTK conceptSMTK, Description description, User user) {
              /* Se validan las reglas de negocio para realizar el registro */
        ConceptAuditAction auditAction = new ConceptAuditAction(conceptSMTK, CONCEPT_DESCRIPTION_DELETION, now(), user, description);
        new HistoryRecordBL().validate(auditAction);

        auditDAO.recordAuditAction(auditAction);
    }

    @Override
    public void recordConceptInvalidation(ConceptSMTK conceptSMTK, User user) {
        /* Se crea el registro de historial */
        ConceptAuditAction conceptAuditAction = new ConceptAuditAction(conceptSMTK, CONCEPT_INVALIDATION, now(), user, conceptSMTK);

        /* Se validan las reglas de negocio para realizar el registro */
        new HistoryRecordBL().validate(conceptAuditAction);
    }

    @Override
    public void recordRefSetCreation(RefSet refSet, User user) {
        /* Se crea el registro de historial */
        RefSetAuditAction refSetAuditAction = new RefSetAuditAction(refSet, REFSET_CREATION, now(), user);

        /* Se validan las reglas de negocio para realizar el registro */
        new HistoryRecordBL().validate(refSetAuditAction);

        auditDAO.recordAuditAction(refSetAuditAction);
    }

    @Override
    public void recordRefSetUpdate(RefSet refSet, User user) {
        /* Se crea el registro de historial */
        RefSetAuditAction refSetAuditAction = new RefSetAuditAction(refSet, REFSET_UPDATE, now(), user);

        /* Se validan las reglas de negocio para realizar el registro */
        new HistoryRecordBL().validate(refSetAuditAction);

        auditDAO.recordAuditAction(refSetAuditAction);
    }

    @Override
    public void recordRefSetBinding(RefSet refSet, ConceptSMTK conceptSMTK, User user) {

        /* Se crea el registro de historial */
        RefSetAuditAction refSetAuditAction = new RefSetAuditAction(refSet, REFSET_UPDATE, now(), user, conceptSMTK);

        /* Se validan las reglas de negocio para realizar el registro */
        new HistoryRecordBL().validate(refSetAuditAction);

        auditDAO.recordAuditAction(refSetAuditAction);
    }

    @Override
    public void recordRefSetUnbinding(RefSet refSet, ConceptSMTK conceptSMTK, User user) {
        /* Se crea el registro de historial */
        RefSetAuditAction refSetAuditAction = new RefSetAuditAction(refSet, REFSET_UPDATE, now(), user, conceptSMTK);

        /* Se validan las reglas de negocio para realizar el registro */
        new HistoryRecordBL().validate(refSetAuditAction);

        auditDAO.recordAuditAction(refSetAuditAction);
    }

    @Override
    public void recordRefSetInvalidate(RefSet refSet, User user) {
        /* Se crea el registro de historial */
        RefSetAuditAction refSetAuditAction = new RefSetAuditAction(refSet, REFSET_UPDATE, now(), user);

        /* Se validan las reglas de negocio para realizar el registro */
        new HistoryRecordBL().validate(refSetAuditAction);

        auditDAO.recordAuditAction(refSetAuditAction);
    }

    @Override
    public List<AuditActionType> getAllAuditActionTypes() {
        return Arrays.asList(AuditActionType.values());
    }

    @Override
    public List<ConceptAuditAction> getConceptAuditActions(ConceptSMTK conceptSMTK, boolean changes) {
        return auditDAO.getConceptAuditActions(conceptSMTK.getId(), changes);
    }

    @Override
    public ConceptAuditAction getConceptCreationAuditAction(ConceptSMTK conceptSMTK, boolean changes) {
        for (ConceptAuditAction conceptAuditAction : auditDAO.getConceptAuditActions(conceptSMTK.getId(), changes)) {
            if(conceptAuditAction.getAuditActionType().equals(AuditActionType.CONCEPT_CREATION)) {
                return conceptAuditAction;
            }
        }
        return null;
    }

    @Override
    public ConceptAuditAction getConceptPublicationAuditAction(ConceptSMTK conceptSMTK, boolean changes) {
        for (ConceptAuditAction conceptAuditAction : auditDAO.getConceptAuditActions(conceptSMTK.getId(), changes)) {
            if(conceptAuditAction.getAuditActionType().equals(AuditActionType.CONCEPT_PUBLICATION)) {
                return conceptAuditAction;
            }
        }
        return null;
    }

    private Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }
}
