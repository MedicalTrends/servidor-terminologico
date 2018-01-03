package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.users.Answer;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.Question;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.Remote;
import java.util.List;

/**
 * Created by des01c7 on 16-12-16.
 */
@Remote
public interface QuestionManager {

    /**
     * Método encargado de obtener las instituciones a las que se encuentra asociado un usuario
     * @param user
     * @return Lista de instituciones
     */
    public List<Answer> getAnswersByUser(User user);

    public Question getQuestionById(long id);


    /**
     * Método encargado de obtener una lista con todas las instituciones
     * @return Lista de instituciones
     */
    public List<Question> getAllQuestions();
}

