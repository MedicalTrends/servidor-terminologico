package cl.minsal.semantikos.kernel.auth;

import cl.minsal.semantikos.kernel.components.InstitutionManager;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.User;
import org.apache.commons.codec.binary.Base64;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.InvocationContext;
import javax.jws.WebParam;
import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.handler.MessageContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;

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

    @EJB
    InstitutionManager institutionManager;


    @PermitAll
    public boolean authenticate(String email, String password, HttpServletRequest request) throws AuthenticationException {
        return getAuthenticationMethod().authenticate(email, password, request);
    }

    @PermitAll
    public void authenticate(MessageContext mctx) throws Exception {

        Map http_headers = (Map) mctx.get(MessageContext.HTTP_REQUEST_HEADERS);

        ArrayList list = (ArrayList) http_headers.get("Authorization");

        if (list == null || list.size() == 0) {
            throw new Exception("Error de autenticación: Este WS necesita Autenticación!");
        }

        String userpass = (String) list.get(0);
        userpass = userpass.substring(5);
        byte[] buf = new byte[0];

        buf = Base64.decodeBase64(userpass.getBytes());

        String credentials = new String(buf);

        String username = null;
        String password = null;
        int p = credentials.indexOf(":");

        if (p > -1) {
            username = credentials.substring(0, p);
            password = credentials.substring(p+1);
        }
        else {
            throw new Exception("Hubo un error al decodificar la autenticación");
        }
        // This should be changed to a DB / Ldap authentication check
        try {
            getAuthenticationMethod().authenticate(username, password);
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
