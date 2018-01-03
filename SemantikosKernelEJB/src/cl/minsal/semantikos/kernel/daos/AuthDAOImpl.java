package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.factories.DataSourceFactory;
import cl.minsal.semantikos.kernel.util.StringUtils;
import cl.minsal.semantikos.model.users.Answer;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.User;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Francisco Mendez on 01-07-16.
 */

@Stateless
public class AuthDAOImpl implements AuthDAO {

    static final Logger logger = LoggerFactory.getLogger(AuthDAOImpl.class);

    @EJB
    private ProfileDAO profileDAO;

    @EJB
    private InstitutionDAO institutionDAO;

    @EJB
    private QuestionDAO questionDAO;

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    @Override
    public User getUserById(long id) {

        //ConnectionBD connect = new ConnectionBD();
        User user = null;

        String sql = "begin ? := stk.stk_pck_user.get_user_by_id(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, id);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                user = makeUserFromResult(rs);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return user;


    }

    @Override
    public User getUserByDocumentNumber(String documentNumber) {

        //ConnectionBD connect = new ConnectionBD();
        User user = null;

        String sql = "begin ? := stk.stk_pck_user.get_user_by_document_number(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, documentNumber);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                user = makeUserFromResult(rs);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return user;
    }

    @Override
    public User getUserByVerificationCode(String key) {

        //ConnectionBD connect = new ConnectionBD();
        User user = null;

        String sql = "begin ? := stk.stk_pck_user.get_user_by_verification_code(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, key);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                user = makeUserFromResult(rs);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return user;
    }

    @Override
    public User getUserByUsername(String username) {

        //ConnectionBD connect = new ConnectionBD();
        User user = null;

        String sql = "begin ? := stk.stk_pck_user.get_user_by_username(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, username);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                user = makeUserFromResult(rs);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return user;
    }

    @Override
    public User getUserByEmail(String email) {

        //ConnectionBD connect = new ConnectionBD();
        User user = null;

        String sql = "begin ? := stk.stk_pck_user.get_user_by_email(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            //connection.setReadOnly(true);

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, email);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                user = makeUserFromResult(rs);
            }

            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return user;
    }

    @Override
    public List<User> getAllUsers() {

        ArrayList<User> users = new ArrayList<>();

        //ConnectionBD connect = new ConnectionBD();
        User user = null;

        String sql = "begin ? := stk.stk_pck_user.get_all_users; end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                user = makeUserFromResult(rs);
                users.add(user);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return users;

    }

    @Override
    public void createUser(User user) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_user.create_user(?,?,?,?,?,?,?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.NUMERIC);
            call.setString(2, user.getName().trim());
            call.setString(3, user.getLastName().trim());
            call.setString(4, user.getSecondLastName().trim());
            call.setString(5, user.getEmail().trim());
            call.setBoolean(6, false);
            call.setInt(7, 0);
            call.setBoolean(8, user.isDocumentRut());
            call.setString(9, user.isDocumentRut()?StringUtils.parseRut(user.getDocumentNumber().trim()):user.getDocumentNumber());
            call.setString(10, user.getPasswordHash());
            call.setString(11, user.getVerificationCode());

            call.execute();

            //ResultSet rs = call.getResultSet();

            if (call.getLong(1) > 0) {
                user.setId(call.getLong(1));
            } else {
                String errorMsg = "El usuario no fué creado. Esta es una situación imposible. Contactar a Desarrollo";
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al crear el usuario en la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

    }

    @Override
    public void updateUser(User user) {

        String sql = "begin ? := stk.stk_pck_user.update_user(?,?,?,?,?,?,?,?,?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.INTEGER);
            call.setString(2, user.getName());
            call.setString(3, user.getLastName());
            call.setString(4, user.getSecondLastName());
            call.setString(5, user.getEmail());
            call.setString(6, user.getDocumentNumber());
            call.setBoolean(7, user.isLocked());
            call.setString(8, user.getPasswordHash());

            if(user.getVerificationCode()==null)
                call.setNull(9, Types.VARCHAR);
            else
                call.setString(9, user.getVerificationCode());

            call.setBoolean(10, user.isValid());
            call.setInt(11, user.getFailedLoginAttempts());
            call.setInt(12, user.getFailedAnswerAttempts());

            call.setLong(13, user.getId());

            call.execute();

        } catch (SQLException e) {
            String errorMsg = "Error al actualizar usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

    }

    private void addAnswerToUser(User user, Answer a) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_user.add_answer(?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.INTEGER);
            call.setLong(2, user.getId());
            call.setLong(3,  a.getQuestion().getId());
            call.setString(4, a.getAnswer());

            call.execute();

        } catch (SQLException e) {
            String errorMsg = "Error al agregar respuesta a usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

    }

    @Override
    public void updateUserPasswords(User user) {

        //ConnectionBD connect = new ConnectionBD();
        boolean updated = false;

        String sql = "begin ? := stk.stk_pck_user.update_user_passwords(?,?,?,?,?,?,?,?,?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.INTEGER);
            call.setTimestamp(2, new Timestamp(user.getLastPasswordChange().getTime()));
            call.setString(3, user.getPasswordHash());
            call.setString(4, user.getLastPasswordHash1());
            call.setString(5, user.getLastPasswordHash2());
            call.setString(6, user.getLastPasswordHash3());
            call.setString(7, user.getLastPasswordHash4());
            call.setString(8, user.getPasswordSalt());
            call.setString(9, user.getLastPasswordSalt1());
            call.setString(10, user.getLastPasswordSalt2());
            call.setString(11, user.getLastPasswordSalt3());
            call.setString(12, user.getLastPasswordSalt4());
            call.setLong(13, user.getId());

            call.execute();

            //ResultSet rs = call.getResultSet();

            if (call.getLong(1) == 0) {
                String errorMsg = "Información de usuario (USER_ID=" + user.getId() + ") no fue actualizada.";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            else {
                logger.info("Información de usuario (USER_ID=" + user.getId() + ") actualizada exitosamente.");
            }

        } catch (SQLException e) {
            String errorMsg = "Error al actualizar usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

    }


    /* marca la ultima fecha de ingreso del usuario */
    @Override
    public void markLogin(String email) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_user.mark_login(?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.INTEGER);
            call.setDate(2, new java.sql.Date(new Date().getTime()));
            call.setString(3, email);
            call.execute();

        } catch (SQLException e) {
            String errorMsg = "Error al actualizar usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

    }

    @Override
    public void markLoginFail(String username) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_user.mark_login_fail(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.INTEGER);
            call.setString(2, username);
            call.execute();

        } catch (SQLException e) {
            String errorMsg = "Error al actualizar usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }
    }

    /* marca la ultima fecha de ingreso del usuario */
    @Override
    public void markAnswer(String email) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_user.mark_answer(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.INTEGER);
            call.setString(2, email);
            call.execute();

        } catch (SQLException e) {
            String errorMsg = "Error al actualizar usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

    }

    @Override
    public void markAnswerFail(String username) {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_user.mark_answer_fail(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.INTEGER);
            call.setString(1, username);
            call.execute();

        } catch (SQLException e) {
            String errorMsg = "Error al actualizar usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }
    }

    @Override
    public void lockUser(String username) {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_user.lock_user(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.INTEGER);
            call.setString(2, username);
            call.execute();

        } catch (SQLException e) {
            String errorMsg = "Error al actualizar usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }
    }

    @Override
    public void unlockUser(String username) {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_user.unlock_user(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, username);
            call.execute();

        } catch (SQLException e) {
            String errorMsg = "Error al actualizar usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }
    }

    public User makeUserFromResult(ResultSet rs) throws SQLException {

        User u = new User();

        u.setId(rs.getBigDecimal(1).longValue());
        u.setUsername(rs.getString(2));
        u.setPasswordHash(rs.getString(3));
        u.setPasswordSalt(rs.getString(4));
        u.setName(rs.getString(5));
        u.setLastName(rs.getString(6));
        u.setSecondLastName(rs.getString(7));
        u.setEmail(rs.getString(8));

        u.setLocked(rs.getBoolean(9));
        u.setFailedLoginAttempts(rs.getInt(10));
        u.setFailedAnswerAttempts(rs.getInt(11));

        u.setLastLogin(rs.getTimestamp(12));
        u.setLastPasswordChange(rs.getTimestamp(13));

        u.setLastPasswordHash1(rs.getString(14));
        u.setLastPasswordHash2(rs.getString(15));
        u.setLastPasswordHash3(rs.getString(16));
        u.setLastPasswordHash4(rs.getString(17));

        u.setLastPasswordSalt1(rs.getString(18));
        u.setLastPasswordSalt2(rs.getString(19));
        u.setLastPasswordSalt3(rs.getString(20));
        u.setLastPasswordSalt4(rs.getString(21));

        u.setDocumentNumber(rs.getString(22));
        u.setVerificationCode(rs.getString(23));
        u.setValid(rs.getBoolean(24));
        u.setDocumentRut(rs.getBoolean(25));

        u.setProfiles(profileDAO.getProfilesBy(u));

        u.setInstitutions(institutionDAO.getInstitutionBy(u));

        u.setAnswers(questionDAO.getAnswersByUser(u));

        return u;
    }

}
