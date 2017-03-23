package cl.minsal.semantikos.kernel.auth;


import cl.minsal.semantikos.model.users.User;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Francisco Méndez on 19-05-2016.
 */
public abstract class AuthenticationMethod {

    public abstract boolean authenticate(String email, String password, HttpServletRequest request) throws AuthenticationException;
    public abstract boolean authenticate(String email, String password) throws AuthenticationException;
    public abstract void setUserPassword(String email, String password) throws PasswordChangeException;
    public abstract String createUserPassword(String email, String password);

    public abstract User getUser(String email);


}
