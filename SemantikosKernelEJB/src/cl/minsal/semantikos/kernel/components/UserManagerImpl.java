package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.AuthDAO;

import cl.minsal.semantikos.kernel.daos.QuestionDAO;
import cl.minsal.semantikos.model.exceptions.PasswordChangeException;
import cl.minsal.semantikos.model.users.Answer;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.Question;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.kernel.businessrules.UserCreationBR;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static cl.minsal.semantikos.kernel.components.AuthenticationManager.MAX_FAILED_ANSWER_ATTEMPTS;

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
    UserCreationBR userCreationBR;


    public List<User> getAllUsers() {

        return authDAO.getAllUsers();

    }

    public User getUser(long idUser) {
        return authDAO.getUserById(idUser);
    }

    public User getUserByDocumentNumber(String documentNumber) { return authDAO.getUserByDocumentNumber(documentNumber); }

    public User getUserByUsername(String username) { return authDAO.getUserByUsername(username); }

    public User getUserByEmail(String username) {
        return authDAO.getUserByEmail(username);
    }

    public User getUserByVerificationCode(String key) { return authDAO.getUserByVerificationCode(key); }

    public void updateUser(User user) {

        authDAO.updateUser(user);
    }

    public List<Question> getAllQuestions() {
        return questionDAO.getAllQuestions();
    }

    public void createUser(User user, HttpServletRequest request) throws BusinessRuleException {

        /* Se validan las pre-condiciones para crear un usuario */
        try {
            userCreationBR.verifyPreConditions(user);
            userCreationBR.preActions(user);
            authDAO.createUser(user);
            //user = authDAO.getUserById(user.getIdUser());
            userCreationBR.postActions(user, request);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void activateAccount(User user) {

        /* Se validan las pre-condiciones para crear un usuario */
        try {
            authenticationManager.createUserPassword(user,user.getEmail(),user.getPassword());
        } catch (PasswordChangeException e) {
            e.printStackTrace();
        }
        user.setLocked(false);
        user.setVerificationCode(null);
        authDAO.updateUser(user);

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

        if (user.getFailedAnswerAttempts() >= MAX_FAILED_ANSWER_ATTEMPTS) {
            user.setLocked(true);
            authDAO.lockUser(user.getEmail());
        }

    }

    public void resetAccount(User user, HttpServletRequest request) {
        userCreationBR.preActions(user);
        userCreationBR.postActions(user, request);
        questionDAO.deleteUserAnswers(user);
        user.setFailedAnswerAttempts(0);
    }

    public void deleteUser(User user) {
        user.setValid(false);
        authDAO.updateUser(user);
    }

    public List<Profile> getAllProfiles() {

        return authDAO.getAllProfiles();
    }

    public Profile getProfileById(long id){
        return authDAO.getProfile(id);
    }

    public void unlockUser(String email) {
        authDAO.unlockUser(email);
    }

    public void lockUser(String email) {
        authDAO.lockUser(email);
    }

}
