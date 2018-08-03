package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.ProfileDAO;
import cl.minsal.semantikos.kernel.daos.QuestionDAO;
import cl.minsal.semantikos.model.users.*;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by des01c7 on 16-12-16.
 */
@Stateless
@SecurityDomain("SemantikosDomain")
@DeclareRoles({Roles.ADMINISTRATOR_ROLE, Roles.DESIGNER_ROLE, Roles.MODELER_ROLE, Roles.WS_CONSUMER_ROLE, Roles.REFSET_ADMIN_ROLE, Roles.QUERY_ROLE})
public class QuestionManagerImpl implements QuestionManager {

    @EJB
    private QuestionDAO questionDAO;

    @EJB
    private AuditManager auditManager;

    @Override
    @PermitAll
    public List<Answer> getAnswersByUser(User user) {
        return questionDAO.getAnswersByUser(user);
    }

    @Override
    @PermitAll
    public Question getQuestionById(long id) {
        return questionDAO.getQuestionById(id);
    }

    @Override
    @PermitAll
    public List<Question> getAllQuestions() {
        return questionDAO.getAllQuestions();
    }

    @Override
    @PermitAll
    public Answer bindAnswerToUser(User user, Answer answer, User _user) {

        questionDAO.bindAnswerToUser(user, answer);

        /* Registrar en el Historial si es preferida (Historial BR) */
        auditManager.recordUserActivation(user, _user);

        /* Se retorna el establecimiento persistido */
        return answer;
    }

}
