package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.auth.AuthenticationMethod;
import cl.minsal.semantikos.kernel.auth.DummyAuthenticationBean;
import cl.minsal.semantikos.kernel.auth.JbossSecurityDomainAuthenticationBean;
import cl.minsal.semantikos.model.exceptions.PasswordChangeException;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.User;
import org.apache.commons.codec.binary.Base64;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.handler.MessageContext;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Francisco Méndez on 19-05-2016.
 */
@Stateless
//@SecurityDomain("semantikos")
public class AuthenticationManagerImpl implements AuthenticationManager {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationManagerImpl.class);

    @EJB(name = "DummyAuthenticationEJB")
    DummyAuthenticationBean dummyAuthenticationBean;

    @EJB(name = "JbossSecurityDomainAuthenticationEJB")
    JbossSecurityDomainAuthenticationBean jbossSecurityDomainAuthenticationBean;

    @EJB
    InstitutionManager institutionManager;

    @PermitAll
    public boolean authenticate(String email, String password, HttpServletRequest request) throws AuthenticationException {
        return getAuthenticationMethod().authenticate(email, password, request);
    }

    @PermitAll
    @Override
    public boolean authenticate(String email, String password) throws AuthenticationException {
        return getAuthenticationMethod().authenticateWeb(email, password);
    }

    @PermitAll
    public void authenticateWS(String username, String password) throws Exception {

        // This should be changed to a DB / Ldap authentication check
        try {
            getAuthenticationMethod().authenticateWS(username, password);
        }
        catch (AuthenticationException e) {
            throw new Exception(e.getMessage());
        }
    }

    @PermitAll
    public void validateInstitution(String idInstitution) throws Exception {

        if(idInstitution.isEmpty()) {
            throw new Exception("No se ha especificado idEstablemciento como parámetro de esta operación");
        }

        Institution institution = null;

        try {
            institution = institutionManager.getInstitutionById(Long.parseLong(idInstitution));
        }
        catch (Exception e) {
            throw new Exception("El parámetro idEstablecimiento debe ser un valor numérico");
        }

        if(institution == null) {
            throw new Exception("No existe un establecimiento con el idEstablecimiento proporcionado");
        }
    }

    @PermitAll
    public User getUserDetails(String email) {
        return getAuthenticationMethod().getUser(email);
    }

    private AuthenticationMethod getAuthenticationMethod() {
        return jbossSecurityDomainAuthenticationBean;
    }


    //@RolesAllowed("Administrador")
    @PermitAll()
    public void setUserPassword(String username, String password) throws PasswordChangeException {
        getAuthenticationMethod().setUserPassword(username, password);
    }

    @PermitAll()
    //@RolesAllowed("Administrador")
    public void createUserPassword(User user, String username, String password) throws PasswordChangeException {
        user.setPasswordHash(getAuthenticationMethod().createUserPassword(username, password));
    }

    @PermitAll()
    //@RolesAllowed("Administrador")
    public void createUserVerificationCode(User user, String username, String password) throws PasswordChangeException {
        user.setVerificationCode(getAuthenticationMethod().createUserPassword(username, username+"."+password));
    }

    @PermitAll()
    public boolean checkPassword(User user, String username, String password) {
        return getAuthenticationMethod().createUserPassword(username, password).equals(user.getPasswordHash());
    }

}
