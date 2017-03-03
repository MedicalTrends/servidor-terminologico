package cl.minsal.semantikos.kernel.auth;


import cl.minsal.semantikos.model.User;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Francisco Méndez on 19-05-2016.
 */
public abstract class AuthenticationMethod {

    public abstract boolean authenticate(String username, String password, HttpServletRequest request) throws AuthenticationException;
    public abstract boolean authenticate(String username, String password) throws AuthenticationException;
    public abstract void setUserPassword(String username, String password) throws PasswordChangeException;
    public abstract String createUserPassword(String username, String password);

    public abstract User getUser(String username);


}
