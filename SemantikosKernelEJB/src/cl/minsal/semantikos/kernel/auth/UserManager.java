package cl.minsal.semantikos.kernel.auth;

import cl.minsal.semantikos.kernel.daos.AuthDAO;

import cl.minsal.semantikos.kernel.daos.QuestionDAO;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.Question;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.model.businessrules.UserCreationBRInterface;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by BluePrints Developer on 14-07-2016.
 */

@Stateless
public class UserManager {


    @EJB
    AuthDAO authDAO;

    @EJB
    QuestionDAO questionDAO;

    @EJB
    AuthenticationManager authenticationManager;

    @EJB
    UserCreationBRInterface userCreationBR;


    public List<User> getAllUsers() {

        return authDAO.getAllUsers();

    }

    public User getUser(long idUser) {
        return authDAO.getUserById(idUser);
    }

    public User getUserByRut(String rut) { return authDAO.getUserByRut(rut); }

    public User getUserByUsername(String username) { return authDAO.getUserByUsername(username); }

    public User getUserByEmail(String username) { return authDAO.getUserByEmail(username); }

    public User getUserByVerificationCode(String key) { return authDAO.getUserByVerificationCode(key); }

    public void updateUser(User user) {

        authDAO.updateUser(user);
    }

    public List<Question> getAllQuestions() {
        return questionDAO.getAllQuestions();
    }

    public void createUser(User user, HttpServletRequest request) throws BusinessRuleException {

        /* Se validan las pre-condiciones para crear un usuario */
        //UserCreationBR userCreationBR = new UserCreationBR();
        //userCreationBR.preconditions(user);
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
        //UserCreationBR userCreationBR = new UserCreationBR();
        //userCreationBR.preconditions(user);
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
        //UserCreationBR userCreationBR = new UserCreationBR();
        //userCreationBR.preconditions(user);
        User user = userCreationBR.br307verificationCodeExists(key);

        if(user!=null) {
            return true;
        }
        else {
            return false;
        }
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
