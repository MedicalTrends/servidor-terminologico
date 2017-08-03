package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.daos.mappers.UserMapper;
import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.kernel.factories.DataSourceFactory;
import cl.minsal.semantikos.kernel.util.StringUtils;
import cl.minsal.semantikos.model.users.Answer;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.User;
import oracle.jdbc.OracleTypes;
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
public class AuthDAOImpl implements AuthDAO {

    static final Logger logger = LoggerFactory.getLogger(AuthDAOImpl.class);

    @EJB
    private InstitutionDAO institutionDAO;

    @EJB
    private QuestionDAO questionDAO;

    @EJB
    private UserMapper userMapper;

    @Override
    public User getUserById(long id) {

        //ConnectionBD connect = new ConnectionBD();
        User user = null;

        String sql = "begin ? := stk.stk_pck_user.get_user_by_id(?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, id);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                user = userMapper.makeUserFromResult(rs);
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

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, documentNumber);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                user = userMapper.makeUserFromResult(rs);
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

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, key);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                user = userMapper.makeUserFromResult(rs);
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

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, username);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                user = userMapper.makeUserFromResult(rs);
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

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, email);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                user = userMapper.makeUserFromResult(rs);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return user;
    }


    @Override
    public List<Profile> getUserProfiles(Long userId) {

        List<Profile> profiles = new ArrayList<Profile>();

        //ConnectionBD connect = new ConnectionBD();
        Profile profile = null;

        String sql = "begin ? := stk.stk_pck_user.get_profiles_by_user_id(?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, userId);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                profile = userMapper.makeProfileFromResult(rs);
                profiles.add(profile);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar perfiles de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return profiles;

    }

    @Override
    public List<User> getAllUsers() {

        ArrayList<User> users = new ArrayList<>();

        //ConnectionBD connect = new ConnectionBD();
        User user = null;

        String sql = "begin ? := stk.stk_pck_user.get_all_users; end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                user = userMapper.makeUserFromResult(rs);
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

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
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

        /**
         * Agregar los perfiles
         */
        for (Profile p : user.getProfiles()) {
            addProfileToUser(user, p);
        }

    }

    @Override
    public void updateUser(User user) {

        String sql = "begin ? := stk.stk_pck_user.update_user(?,?,?,?,?,?,?,?,?,?,?,?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
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

        sql = "begin ? := stk.stk_pck_user.delete_user_profiles(?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.INTEGER);
            call.setLong(2, user.getId());

            call.execute();

        } catch (SQLException e) {
            String errorMsg = "Error al eliminar perfiles de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        for (Profile p : user.getProfiles()) {
            addProfileToUser(user, p);
        }

        sql = "begin ? := stk.stk_pck_user.delete_user_answers(?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.INTEGER);
            call.setLong(2, user.getId());

            call.execute();

        } catch (SQLException e) {
            String errorMsg = "Error al eliminar perfiles de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        for (Answer a : user.getAnswers()) {
            addAnswerToUser(user, a);
        }
    }

    private void addProfileToUser(User user, Profile p) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_user.add_user_profile(?,?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.INTEGER);
            call.setLong(2, user.getId());
            call.setLong(3,  p.getId());

            call.execute();

        } catch (SQLException e) {
            String errorMsg = "Error al agregar perfila a usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

    }

    private void addAnswerToUser(User user, Answer a) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_user.add_answer(?,?,?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
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
    public List<Profile> getAllProfiles() {

        List<Profile> profiles = new ArrayList<Profile>();

        //ConnectionBD connect = new ConnectionBD();
        Profile profile = null;

        String sql = "begin ? := stk.stk_pck_user.get_all_profiles; end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                profile = userMapper.makeProfileFromResult(rs);
                profiles.add(profile);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar perfiles de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return profiles;
    }

    @Override
    public void updateUserPasswords(User user) {

        //ConnectionBD connect = new ConnectionBD();
        boolean updated = false;

        String sql = "begin ? := stk.stk_pck_user.update_user_passwords(?,?,?,?,?,?,?,?,?,?,?,?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
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
            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                /* Se recupera el status de la transacción */
                updated = rs.getBoolean(1);
            } else {
                String errorMsg = "El concepto no fue creado por una razón desconocida. Alertar al area de desarrollo" +
                        " sobre esto";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }


        } catch (SQLException e) {
            String errorMsg = "Error al actualizar usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        if (updated) {
            logger.info("Información de usuario (USER_ID=" + user.getId() + ") actualizada exitosamente.");
        } else {
            String errorMsg = "Información de usuario (USER_ID=" + user.getId() + ") no fue actualizada.";
            logger.error(errorMsg);
            throw new EJBException(errorMsg);
        }

    }


    /* marca la ultima fecha de ingreso del usuario */
    @Override
    public void markLogin(String email) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_user.mark_login(?,?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
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

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
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

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
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

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
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

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
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
    public Profile getProfile(long id) {

        //ConnectionBD connect = new ConnectionBD();
        Profile profile = null;

        String sql = "begin ? := stk.stk_pck_user.get_profile_by_id(?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2,id);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                profile = userMapper.makeProfileFromResult(rs);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar perfiles de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }
        return profile;
    }

    @Override
    public void unlockUser(String username) {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_user.unlock_user(?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
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

}
