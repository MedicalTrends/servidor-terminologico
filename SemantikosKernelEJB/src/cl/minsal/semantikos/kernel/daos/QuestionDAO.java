package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.audit.ConceptAuditAction;
import cl.minsal.semantikos.model.audit.RefSetAuditAction;
import cl.minsal.semantikos.model.users.Answer;
import cl.minsal.semantikos.model.users.Question;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Andrés Farías on 8/23/16.
 */
@Local
public interface QuestionDAO {


    public List<Question> getAllQuestions();

    public List<Answer> getAnswersByUser(User user);

    public Question getQuestionById(long id);

    public void deleteUserAnswers(User user);


}
