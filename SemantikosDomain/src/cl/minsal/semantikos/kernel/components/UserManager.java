package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.Question;
import cl.minsal.semantikos.model.users.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Andrés Farías
 */
public interface UserManager {

    List<User> getAllUsers();

    public User getUser(long idUser);

    public User getUserByDocumentNumber(String documentNumber);

    public User getUserByUsername(String username);

    public User getUserByEmail(String username);

    public User getUserByVerificationCode(String key);

    public void updateUser(User user);

    public List<Question> getAllQuestions();

    public void createUser(User user, HttpServletRequest request);

    public void activateAccount(User user);

    public boolean checkActivationCode(String key);

    public boolean checkAnswers(User user);

    public void resetAccount(User user, HttpServletRequest request);

    public void deleteUser(User user);

    public List<Profile> getAllProfiles();

    public Profile getProfileById(long id);

    public void unlockUser(String email);

    public void lockUser(String email);

}