package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.factories.DataSourceFactory;
import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.kernel.util.StringUtils;
import cl.minsal.semantikos.model.audit.RefSetAuditAction;
import cl.minsal.semantikos.model.users.Answer;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.Question;
import cl.minsal.semantikos.model.users.User;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Francisco Mendez on 01-07-16.
 */

@Stateless
public class QuestionDAOImpl implements QuestionDAO {

    static final Logger logger = LoggerFactory.getLogger(QuestionDAOImpl.class);

    @EJB
    private AuthDAO authDao;

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    @Override
    public List<Question> getAllQuestions() {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_question.get_all_questions; end;";

        List<Question> institutions= new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

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

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_question.get_answers_by_user(?); end;";

        List<Answer> answers= new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, user.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

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
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_question.get_question_by_id(?); end;";

        Question question = null;

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, id);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

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
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_question.delete_user_answers(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, user.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

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

    public void bindAnswerToUser(User user, Answer answer) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_user.add_answer(?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.INTEGER);
            call.setLong(2, user.getId());
            call.setLong(3,  answer.getQuestion().getId());
            call.setString(4, answer.getAnswer());

            call.execute();

        } catch (SQLException e) {
            String errorMsg = "Error al agregar respuesta a usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
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
