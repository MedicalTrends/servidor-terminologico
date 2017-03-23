package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.audit.ConceptAuditAction;
import cl.minsal.semantikos.model.audit.RefSetAuditAction;
import cl.minsal.semantikos.model.users.Question;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Andrés Farías on 8/23/16.
 */
@Local
public interface QuestionDAO {


    public List<Question> getAllQuestions();

    public List<Question> getQuestionsByUser();

}
