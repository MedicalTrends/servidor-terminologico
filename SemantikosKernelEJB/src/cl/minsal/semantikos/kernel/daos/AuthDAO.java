package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.Profile;
import cl.minsal.semantikos.model.User;

import java.util.List;

/**
 * Created by BluePrints Developer on 02-11-2016.
 */
public interface AuthDAO {

    User getUserById(long id);

    User getUserByUsername(String username);

    User getUserByEmail(String username);

    User getUserByRut(String rut);

    User getUserByVerificationCode(String key);

    List<Profile> getUserProfiles(Long userId);

    List<User> getAllUsers();

    void createUser(User user);

    void updateUser(User user);

    List<Profile> getAllProfiles();

    void updateUserPasswords(User user);

    /* marca la ultima fecha de ingreso del usuario */
    void markLogin(String username);

    void markLoginFail(String username);

    void lockUser(String username);

    Profile getProfile(long id);

    void unlockUser(String username);

    public class UserExistsException extends Exception {
    }
}

