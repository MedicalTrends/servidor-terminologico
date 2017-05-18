package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.users.Answer;
import cl.minsal.semantikos.model.users.Question;
import cl.minsal.semantikos.model.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Francisco Mendez on 01-07-16.
 */

@Stateless
public class QuestionDAOImpl implements QuestionDAO {

    static final Logger logger = LoggerFactory.getLogger(QuestionDAOImpl.class);

    @EJB
    private AuthDAO authDao;

    @Override
    public List<Question> getAllQuestions() {
        ConnectionBD connect = new ConnectionBD();
        String GET_ALL_QUESTIONS = "{call semantikos.get_all_questions()}";
        List<Question> institutions= new ArrayList<>();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(GET_ALL_QUESTIONS)) {
            call.execute();
            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                institutions.add(createQuestionFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al al obtener los RefSets ", e);
        }

        return institutions;
    }

    @Override
    public List<Answer> getAnswersByUser(User user) {

        ConnectionBD connect = new ConnectionBD();
        String GET_ANSWERS_BY_USERS = "{call semantikos.get_answers_by_user(?)}";
        List<Answer> answers= new ArrayList<>();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(GET_ANSWERS_BY_USERS)) {
            call.setLong(1, user.getIdUser());
            call.execute();
            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                answers.add(createAnswerFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al obtener los Answers ", e);
        }

        return answers;
    }

    @Override
    public Question getQuestionById(long id) {
        ConnectionBD connect = new ConnectionBD();
        String GET_QUESTION_BY_ID = "{call semantikos.get_question_by_id(?)}";
        Question question = null;

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(GET_QUESTION_BY_ID)) {
            call.setLong(1, id);
            call.execute();
            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                question = createQuestionFromResultSet(rs);
            }
        } catch (SQLException e) {
            logger.error("Error al al obtener los RefSets ", e);
        }

        return question;
    }

    @Override
    public void deleteUserAnswers(User user) {
        ConnectionBD connect = new ConnectionBD();
        String DELETE_USER_ANSWERS = "{call semantikos.delete_user_answers(?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(DELETE_USER_ANSWERS)) {
            call.setLong(1, user.getIdUser());
            call.execute();
            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                if(!rs.getBoolean(1)) {
                    String errorMsg = "Las Answers no fueron eliminadas. Alertar al area de desarrollo sobre esto";
                    logger.error(errorMsg);
                    throw new EJBException(errorMsg);
                }
            }
        } catch (SQLException e) {
            logger.error("Error al eliminar los Answers ", e);
        }
    }

    private Question createQuestionFromResultSet(ResultSet resultSet) {
        Question question = new Question();
        try {
            question.setId(resultSet.getLong("id"));
            question.setQuestion(resultSet.getString("question"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return question;
    }

    private Answer createAnswerFromResultSet(ResultSet resultSet) {

        Answer answer = new Answer();

        try {
            answer.setId(resultSet.getLong("id"));
            answer.setIdUser(resultSet.getLong("id_user"));
            answer.setAnswer(resultSet.getString("answer"));

            Question question = getQuestionById(resultSet.getLong("id_question"));

            answer.setQuestion(question);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return answer;
    }

}
