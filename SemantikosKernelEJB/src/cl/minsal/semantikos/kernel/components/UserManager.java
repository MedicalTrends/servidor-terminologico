package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.audit.AuditActionType;
import cl.minsal.semantikos.model.audit.ConceptAuditAction;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.crossmaps.Crossmap;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.exceptions.PasswordChangeException;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.users.*;

import javax.ejb.Remote;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Andrés Farías
 */
@Remote
public interface UserManager {

    List<User> getAllUsers();

    public User getUser(long idUser);

    public User getUserByDocumentNumber(String documentNumber);

    public User getUserByUsername(String username);

    public User getUserByEmail(String username);

    public User getUserByVerificationCode(String key);

    public void updateUser(User user);

    public void updateFields(User originalUser, User updatedUser, User user);

    public void update(User originalUser, User updatedUser, User user);

    public List<Question> getAllQuestions();

    public long createUser(User user, String baseURL, User _user);

    public void activateAccount(User user, User _user);

    public boolean checkActivationCode(String key);

    public boolean checkAnswers(User user);

    public void resetAccount(User user, String baseURL);

    public void deleteUser(User user);

    public void unlockUser(String email);

    public void lockUser(String email);

    public UserFactory getUserFactory();

}
