package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.AuditDAO;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.audit.*;
import cl.minsal.semantikos.kernel.businessrules.HistoryRecordBL;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.crossmaps.Crossmap;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.Profile;
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
    public void recordUserCreation(User user, User _user) {

        /* Se crea el registro de historial, para poder validar Reglas de Negocio */
        UserAuditAction userAuditAction = new UserAuditAction(user, USER_CREATION, now(), _user, user);

        auditDAO.recordAuditAction(userAuditAction);
    }

    @Override
    public void recordUserUpgrade(User user, User _user) {

        /* Se crea el registro de historial, para poder validar Reglas de Negocio */
        UserAuditAction userAuditAction = new UserAuditAction(user, USER_ATTRIBUTE_CHANGE, now(), _user, user);

        auditDAO.recordAuditAction(userAuditAction);
    }

    @Override
    public void recordUserProfileBinding(User user, Profile profile, User _user) {

        /* Se crea el registro de historial, para poder validar Reglas de Negocio */
        UserAuditAction userAuditAction = new UserAuditAction(user, USER_PROFILE_BINDING, now(), _user, profile);

        auditDAO.recordAuditAction(userAuditAction);
    }

    @Override
    public void recordUserProfileUnbinding(User user, Profile profile, User _user) {

        /* Se crea el registro de historial, para poder validar Reglas de Negocio */
        UserAuditAction userAuditAction = new UserAuditAction(user, USER_PROFILE_UNBINDING, now(), _user, profile);

        auditDAO.recordAuditAction(userAuditAction);
    }

    @Override
    public void recordUserInstitutionBinding(User user, Institution institution, User _user) {

        /* Se crea el registro de historial, para poder validar Reglas de Negocio */
        UserAuditAction userAuditAction = new UserAuditAction(user, USER_INSTITUTION_BINDING, now(), _user, institution);

        auditDAO.recordAuditAction(userAuditAction);
    }

    @Override
    public void recordUserInstitutionUnbinding(User user, Institution institution, User _user) {

        /* Se crea el registro de historial, para poder validar Reglas de Negocio */
        UserAuditAction userAuditAction = new UserAuditAction(user, USER_INSTITUTION_UNBINDING, now(), _user, institution);

        auditDAO.recordAuditAction(userAuditAction);
    }

    @Override
    public void recordUserActivation(User user, User _user) {
        /* Se crea el registro de historial, para poder validar Reglas de Negocio */
        UserAuditAction userAuditAction = new UserAuditAction(user, USER_ACTIVATION, now(), _user, user);

        auditDAO.recordAuditAction(userAuditAction);
    }

    @Override
    public void recordUserPasswordChange(User user, User _user) {
        /* Se crea el registro de historial, para poder validar Reglas de Negocio */
        UserAuditAction userAuditAction = new UserAuditAction(user, USER_PASSWORD_CHANGE, now(), _user, user);

        auditDAO.recordAuditAction(userAuditAction);
    }

    @Override
    public void recordUserPasswordRecover(User user, User _user) {
        /* Se crea el registro de historial, para poder validar Reglas de Negocio */
        UserAuditAction userAuditAction = new UserAuditAction(user, USER_PASSWORD_RECOVER, now(), _user, user);

        auditDAO.recordAuditAction(userAuditAction);
    }

    @Override
    public void recordUserLocking(User user, User _user) {
        /* Se crea el registro de historial, para poder validar Reglas de Negocio */
        UserAuditAction userAuditAction = new UserAuditAction(user, USER_LOCKING, now(), _user, user);

        auditDAO.recordAuditAction(userAuditAction);
    }

    @Override
    public void recordUserUnlocking(User user, User _user) {
        /* Se crea el registro de historial, para poder validar Reglas de Negocio */
        UserAuditAction userAuditAction = new UserAuditAction(user, USER_UNLOCKING, now(), _user, user);

        auditDAO.recordAuditAction(userAuditAction);
    }

    @Override
    public void recordUserDelete(User user, User _user) {
        /* Se crea el registro de historial, para poder validar Reglas de Negocio */
        UserAuditAction userAuditAction = new UserAuditAction(user, USER_DELETE, now(), _user, user);

        auditDAO.recordAuditAction(userAuditAction);
    }

    @Override
    public void recordUserAccountReset(User user, User _user) {
        /* Se crea el registro de historial, para poder validar Reglas de Negocio */
        UserAuditAction userAuditAction = new UserAuditAction(user, USER_ACCOUNT_RESET, now(), _user, user);

        auditDAO.recordAuditAction(userAuditAction);
    }

    @Override
    public void recordInstitutionCreation(Institution institution, User user) {

        /* Se crea el registro de historial, para poder validar Reglas de Negocio */
        InstitutionAuditAction institutionAuditAction = new InstitutionAuditAction(institution, INSTITUTION_CREATION, now(), user, institution);

        auditDAO.recordAuditAction(institutionAuditAction);
    }

    @Override
    public void recordInstitutionDelete(Institution institution, User user) {
        /* Se crea el registro de historial, para poder validar Reglas de Negocio */
        InstitutionAuditAction institutionAuditAction = new InstitutionAuditAction(institution, INSTITUTION_DELETE, now(), user, institution);

        auditDAO.recordAuditAction(institutionAuditAction);
    }

    @Override
    public void recordInstitutiuonUpgrade(Institution institution, User user) {

        /* Se crea el registro de historial, para poder validar Reglas de Negocio */
        InstitutionAuditAction institutionAuditAction = new InstitutionAuditAction(institution, INSTITUTION_ATTRIBUTE_CHANGE, now(), user, institution);

        auditDAO.recordAuditAction(institutionAuditAction);
    }

    @Override
    public List<AuditActionType> getAllAuditActionTypes() {
        return Arrays.asList(AuditActionType.values());
    }

    @Override
    public List<ConceptAuditAction> getConceptAuditActions(ConceptSMTK conceptSMTK, boolean changes) {
        return auditDAO.getConceptAuditActions(conceptSMTK, changes);
    }

    @Override
    public List<UserAuditAction> getUserAuditActions(User user) {
        return auditDAO.getUserAuditActions(user);
    }

    @Override
    public List<InstitutionAuditAction> getInstitutionAuditActions(Institution institution) {
        return auditDAO.getInstitutionAuditActions(institution);
    }

    @Override
    public ConceptAuditAction getConceptCreationAuditAction(ConceptSMTK conceptSMTK, boolean changes) {
        for (ConceptAuditAction conceptAuditAction : auditDAO.getConceptAuditActions(conceptSMTK, changes)) {
            if(conceptAuditAction.getAuditActionType().equals(AuditActionType.CONCEPT_CREATION)) {
                return conceptAuditAction;
            }
        }
        return null;
    }

    @Override
    public ConceptAuditAction getConceptPublicationAuditAction(ConceptSMTK conceptSMTK, boolean changes) {
        for (ConceptAuditAction conceptAuditAction : auditDAO.getConceptAuditActions(conceptSMTK, changes)) {
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
