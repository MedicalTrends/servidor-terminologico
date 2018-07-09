package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.AuthDAO;

import cl.minsal.semantikos.kernel.daos.QuestionDAO;
import cl.minsal.semantikos.kernel.util.ConceptUtils;
import cl.minsal.semantikos.kernel.util.UserUtils;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.audit.UserAuditAction;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.exceptions.PasswordChangeException;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.users.*;
import cl.minsal.semantikos.kernel.businessrules.UserCreationBR;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.modelweb.Pair;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

import static cl.minsal.semantikos.kernel.components.AuthenticationManager.MAX_FAILED_ANSWER_ATTEMPTS;
import static cl.minsal.semantikos.model.audit.AuditActionType.USER_ATTRIBUTE_CHANGE;

/**
 * Created by BluePrints Developer on 14-07-2016.
 */

@Stateless
public class UserManagerImpl implements UserManager {

    @EJB
    AuthDAO authDAO;

    @EJB
    QuestionDAO questionDAO;

    @EJB
    AuthenticationManager authenticationManager;

    @EJB
    InstitutionManager institutionManager;

    @EJB
    ProfileManager profileManager;

    @EJB
    QuestionManager questionManager;

    @EJB
    UserCreationBR userCreationBR;

    @EJB
    AuditManager auditManager;


    public List<User> getAllUsers() {

        return authDAO.getAllUsers();

    }

    public User getUser(long idUser) {
        return authDAO.getUserById(idUser);
    }

    public User getUserByDocumentNumber(String documentNumber) { return authDAO.getUserByDocumentNumber(documentNumber); }

    public User getUserByUsername(String username) { return authDAO.getUserByUsername(username); }

    public User getUserByEmail(String username) {
        //return UserFactory.getInstance().findUserByEmail(email);
        return authDAO.getUserByEmail(username);
    }

    public User getUserByVerificationCode(String key) { return authDAO.getUserByVerificationCode(key); }

    public void updateUser(User user) {
        /* Se persisten los atributos basicos del usuario*/
        authDAO.updateUser(user);

        /* Luego se persisten sus respuestas */
        /*
        for (Answer answer : user.getAnswers()) {
            institutionManager.bindInstitutionToUser(user, answer, user);
        }
        */

    }

    @Override
    public void updateFields(@NotNull User originalUser, @NotNull User updatedUser, User user) {

        /* Se actualiza con el DAO */
        authDAO.updateUser(updatedUser);

        auditManager.recordUserUpgrade(updatedUser, user);
    }

    @Override
    public void update(@NotNull User originalUser, @NotNull User updatedUser, User user) {

        boolean change = false;

        /* Primero de actualizan los campos propios del concepto */
        if(!originalUser.equals(updatedUser)) {
            updateFields(originalUser, updatedUser, user);
            change = true;
        }

        /* Luego para cada perfil se realiza la acción correspondiente */
        for (Profile profile : UserUtils.getNewProfiles(originalUser.getProfiles(), updatedUser.getProfiles())) {
            profileManager.bindProfileToUser(updatedUser, profile, user);
            change = true;
        }

        for (Profile profile : UserUtils.getRemovedProfiles(originalUser.getProfiles(), updatedUser.getProfiles())) {
            profileManager.unbindProfileFromUser(updatedUser, profile, user);
            change = true;
        }

        /* Luego para cada establecimiento se realiza la acción correspondiente */
        for (Institution institution : UserUtils.getNewInstitutions(originalUser.getInstitutions(), updatedUser.getInstitutions())) {
            institutionManager.bindInstitutionToUser(updatedUser, institution, user);
            change = true;
        }

        for (Institution institution : UserUtils.getRemovedInstitutions(originalUser.getInstitutions(), updatedUser.getInstitutions())) {
            institutionManager.unbindInstitutionFromUser(updatedUser, institution, user);
            change = true;
        }

        /* Luego para cada respuesta se realiza la acción correspondiente */
        for (Answer answer : UserUtils.getNewAnswers(originalUser.getAnswers(), updatedUser.getAnswers())) {
            questionManager.bindAnswerToUser(updatedUser, answer, user);
            change = true;
        }

        if(!change) {
            throw new EJBException("No es posible actualizar un usuario con una instancia idéntica!!");
        }
    }

    public List<Question> getAllQuestions() {
        return questionDAO.getAllQuestions();
    }

    public long createUser(User user, String baseURL, User _user) throws BusinessRuleException {

        /* Se validan las pre-condiciones para crear un usuario */
        try {
            userCreationBR.verifyPreConditions(user);
            user = userCreationBR.preActions(user);
            /* Se persisten los atributos basicos del usuario*/
            authDAO.createUser(user);
            /* Se deja registro en la auditoría */
            auditManager.recordUserCreation(user, _user);
            /* Luego se persisten sus perfiles */
            for (Profile profile : user.getProfiles()) {
                profileManager.bindProfileToUser(user, profile, _user);
            }
            /* Luego se persisten sus establecimientos */
            for (Institution institution : user.getInstitutions()) {
                institutionManager.bindInstitutionToUser(user, institution, _user);
            }
            //user = authDAO.getUserById(user.getIdUser());
            userCreationBR.postActions(user, baseURL);
            return user.getId();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void activateAccount(User originalUser, User updatedUser, User user) {

        /* Se validan las pre-condiciones para crear un usuario */
        try {
            updatedUser.setPasswordHash(authenticationManager.createUserPassword(updatedUser, updatedUser.getEmail(), updatedUser.getPassword()));
        } catch (PasswordChangeException e) {
            e.printStackTrace();
        }
        user.setLocked(false);
        user.setVerificationCode(null);
        update(originalUser, updatedUser, user);
        /* Se deja registro en la auditoría */
        auditManager.recordUserActivation(updatedUser, user);
        /**
         * Se actualiza la cache de usuarios
         */
        UserFactory.getInstance().refresh(user);

    }

    public boolean checkActivationCode(String key) {

        /* Se validan las pre-condiciones para crear un usuario */
        User user = userCreationBR.br307verificationCodeExists(key);

        if(user!=null) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean checkAnswers(User user) {

        User persistedUser = getUserByEmail(user.getEmail());

        for (Answer answer : persistedUser.getAnswers()) {
            if(!user.getAnswers().contains(answer)) {
                failAnswer(user);
                return false;
            }
        }

        authDAO.markAnswer(user.getEmail());
        return true;
    }

    private void failAnswer(User user) {

        authDAO.markAnswerFail(user.getEmail());
        user.setFailedAnswerAttempts(user.getFailedAnswerAttempts()+1);
        //user = authDAO.getUserByEmail(user.getEmail());

        if (user.getFailedAnswerAttempts() > MAX_FAILED_ANSWER_ATTEMPTS) {
            user.setLocked(true);
            authDAO.lockUser(user.getEmail());
        }

    }

    public void resetAccount(User user, String baseURL, User _user) {
        user = userCreationBR.preActions(user);
        user = userCreationBR.postActions(user, baseURL);
        questionDAO.deleteUserAnswers(user);
        user.setFailedAnswerAttempts(0);
        /* Se crea el registro de historial, para poder validar Reglas de Negocio */
        auditManager.recordUserAccountReset(user, _user);
    }

    public void deleteUser(User user, User _user) {
        user.setValid(false);
        authDAO.updateUser(user);
        /* Se crea el registro de historial, para poder validar Reglas de Negocio */
        auditManager.recordUserDelete(user, _user);
    }

    public void unlockUser(User user, User _user) {
        authDAO.unlockUser(user.getEmail());
        /**
         * Se actualiza la cache de usuarios
         */
        UserFactory.getInstance().refresh(user);
        /* Se crea el registro de historial, para poder validar Reglas de Negocio */
        auditManager.recordUserUnlocking(user, _user);
    }

    public void lockUser(String email) {
        authDAO.lockUser(email);
    }

    public UserFactory getUserFactory() {
        return UserFactory.getInstance();
    }

}
