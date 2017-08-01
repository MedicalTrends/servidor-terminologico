package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.auth.AuthenticationMethod;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.audit.AuditActionType;
import cl.minsal.semantikos.model.audit.ConceptAuditAction;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.crossmaps.Crossmap;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.exceptions.PasswordChangeException;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.User;
import org.apache.commons.codec.binary.Base64;

import javax.annotation.security.PermitAll;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.handler.MessageContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Andrés Farías
 */
@Remote
public interface AuthenticationManager {

    public static final int MAX_FAILED_ANSWER_ATTEMPTS = 2;

    public boolean authenticate(String email, String password, HttpServletRequest request) throws AuthenticationException;

    public boolean authenticate(String email, String password) throws AuthenticationException;

    public void authenticateWS(String email, String password) throws Exception;

    public void validateInstitution(String idInstitution) throws Exception ;

    public User getUserDetails(String email);

    public void setUserPassword(String username, String password) throws PasswordChangeException;

    public String createUserPassword(User user, String username, String password) throws PasswordChangeException;

    public void createUserVerificationCode(User user, String username, String password) throws PasswordChangeException;

    public boolean checkPassword(User user, String username, String password);
}
