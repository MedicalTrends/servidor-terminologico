package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.Question;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.model.users.UserFactory;

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

    public void update(User originalUser, User updatedUser, User user);

    public List<Question> getAllQuestions();

    public long createUser(User user, String baseURL, User _user);

    public void activateAccount(User user, User _user);

    public boolean checkActivationCode(String key);

    public boolean checkAnswers(User user);

    public void resetAccount(User user, String baseURL, User _user);

    public void deleteUser(User user, User _user);

    @Deprecated
    public List<Profile> getAllProfiles();

    public Profile getProfileById(long id);

    public void unlockUser(User user, User _user);

    public void lockUser(String email, User user);

    public UserFactory getUserFactory();

}
