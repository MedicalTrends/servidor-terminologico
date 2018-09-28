package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.exceptions.PasswordChangeException;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.User;

import javax.naming.AuthenticationException;
import javax.xml.ws.handler.MessageContext;
import java.security.Principal;

/**
 * @author Andrés Farías
 */
public interface AuthenticationManager {

    public static final int MAX_FAILED_ANSWER_ATTEMPTS = 2;

    public Principal login();

    public boolean authenticate(String email, String password) throws AuthenticationException;

    public User authenticateWS(String email, String password) throws Exception;

    public Institution validateInstitution(String idInstitution) throws Exception ;

    public User getUserDetails(String email);

    public void setUserPassword(String username, String password) throws PasswordChangeException;

    public void createUserPassword(User user, String username, String password) throws PasswordChangeException;

    public void createUserVerificationCode(User user, String username, String password) throws PasswordChangeException;

    public boolean checkPassword(User user, String username, String password);
}
