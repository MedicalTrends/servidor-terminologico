package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.User;

import java.util.List;

/**
 * Created by BluePrints Developer on 02-11-2016.
 */
public interface AuthDAO {

    User getUserById(long id);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    User getUserByDocumentNumber(String documentNumber);

    User getUserByVerificationCode(String key);

    List<Profile> getUserProfiles(Long userId);

    List<User> getAllUsers();

    void createUser(User user);

    void updateUser(User user);

    List<Profile> getAllProfiles();

    void updateUserPasswords(User user);

    /* marca la ultima fecha de ingreso del usuario */
    void markLogin(String email);

    void markLoginFail(String email);

    void markAnswer(String email);

    void markAnswerFail(String email);

    void lockUser(String email);

    Profile getProfile(long id);

    void unlockUser(String email);

    public class UserExistsException extends Exception {
    }
}

