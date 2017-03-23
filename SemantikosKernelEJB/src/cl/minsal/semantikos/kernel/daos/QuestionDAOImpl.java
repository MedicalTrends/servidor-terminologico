package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.kernel.util.StringUtils;
import cl.minsal.semantikos.model.audit.RefSetAuditAction;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.Question;
import cl.minsal.semantikos.model.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
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
    public List<Question> getQuestionsByUser() {
        return null;
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

}
