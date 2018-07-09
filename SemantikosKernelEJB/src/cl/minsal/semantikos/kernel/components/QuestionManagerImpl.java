package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.ProfileDAO;
import cl.minsal.semantikos.kernel.daos.QuestionDAO;
import cl.minsal.semantikos.model.users.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by des01c7 on 16-12-16.
 */
@Stateless
public class QuestionManagerImpl implements QuestionManager {

    @EJB
    private QuestionDAO questionDAO;

    @EJB
    private AuditManager auditManager;


    @Override
    public List<Answer> getAnswersByUser(User user) {
        return questionDAO.getAnswersByUser(user);
    }

    @Override
    public Question getQuestionById(long id) {
        return questionDAO.getQuestionById(id);
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionDAO.getAllQuestions();
    }

    @Override
    public Answer bindAnswerToUser(User user, Answer answer, User _user) {

        questionDAO.bindAnswerToUser(user, answer);

        /* Registrar en el Historial si es preferida (Historial BR) */
        auditManager.recordUserActivation(user, _user);

        /* Se retorna el establecimiento persistido */
        return answer;
    }

}
