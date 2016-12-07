package cl.minsal.semantikos.kernel.auth;

import cl.minsal.semantikos.model.User;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Francisco Méndez on 19-05-2016.
 */
@Stateless(name = "AuthenticationManagerEJB")
@SecurityDomain("semantikos")
public class AuthenticationManager {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationManager.class);

    @EJB(name = "DummyAuthenticationEJB")
    DummyAuthenticationBean dummyAuthenticationBean;


    @EJB(name = "JbossSecurityDomainAuthenticationEJB")
    JbossSecurityDomainAuthenticationBean jbossSecurityDomainAuthenticationBean;


    @PermitAll
    public boolean authenticate(String username, String password, HttpServletRequest request) throws AuthenticationException {
        return getAuthenticationMethod().authenticate(username, password, request);
    }

    @PermitAll
    public User getUserDetails(String username) {
        return getAuthenticationMethod().getUser(username);
    }

    private AuthenticationMethod getAuthenticationMethod() {
        return jbossSecurityDomainAuthenticationBean;
    }


    @RolesAllowed("Administrador")
    public void setUserPassword(String username, String password) throws PasswordChangeException {
        getAuthenticationMethod().setUserPassword(username, password);
    }
}
