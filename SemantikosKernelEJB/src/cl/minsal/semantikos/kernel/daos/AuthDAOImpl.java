package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.kernel.util.StringUtils;
import cl.minsal.semantikos.model.Profile;
import cl.minsal.semantikos.model.User;
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

    @Override
    public User getUserById(long id) {

        ConnectionBD connect = new ConnectionBD();
        User user = null;

        String sql = "{call semantikos.get_user_by_id(?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, id);
            call.execute();

            ResultSet rs = call.getResultSet();
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
    public User getUserByRut(String rut) {

        ConnectionBD connect = new ConnectionBD();
        User user = null;

        String sql = "{call semantikos.get_user_by_rut(?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setString(1, rut);
            call.execute();

            ResultSet rs = call.getResultSet();
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

        ConnectionBD connect = new ConnectionBD();
        User user = null;

        String sql = "{call semantikos.get_user_by_verification_code(?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setString(1, key);
            call.execute();

            ResultSet rs = call.getResultSet();
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


        ConnectionBD connect = new ConnectionBD();
        User user = null;

        String sql = "{call semantikos.get_user_by_username(?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setString(1, username);
            call.execute();

            ResultSet rs = call.getResultSet();
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

        ConnectionBD connect = new ConnectionBD();
        User user = null;

        String sql = "{call semantikos.get_user_by_email(?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setString(1, email);
            call.execute();

            ResultSet rs = call.getResultSet();
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
    public List<Profile> getUserProfiles(Long userId) {

        List<Profile> profiles = new ArrayList<Profile>();

        ConnectionBD connect = new ConnectionBD();
        Profile profile = null;

        String sql = "{call semantikos.get_profiles_by_user_id(?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, userId);
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                profile = makeProfileFromResult(rs);
                profiles.add(profile);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar perfiles de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return profiles;

    }

    /**
     * Este método es responsable de crear un Profile a partir de una fila de un resultset.
     *
     * @param rs  resultset parado en la fila a procesar
     *
     * @return El Profile creado a partir de la fila.
     */
    private Profile makeProfileFromResult(ResultSet rs) throws SQLException {

        long idProfile = ( rs.getBigDecimal(1) ).longValue();
        String name = rs.getString(2);
        String description = rs.getString(3);

        return new Profile(idProfile, name, description);
    }


    private User makeUserFromResult(ResultSet rs) throws SQLException {

        User u = new User();

        u.setIdUser(rs.getBigDecimal(1).longValue());
        u.setUsername(rs.getString(2));
        u.setPasswordHash(rs.getString(3));
        u.setPasswordSalt(rs.getString(4));
        u.setName(rs.getString(5));
        u.setLastName(rs.getString(6));
        u.setSecondLastName(rs.getString(7));
        u.setEmail(rs.getString(8));

        u.setLocked(rs.getBoolean(9));
        u.setFailedLoginAttempts(rs.getInt(10));

        u.setLastLogin(rs.getTimestamp(11));
        u.setLastPasswordChange(rs.getTimestamp(12));

        u.setLastPasswordHash1(rs.getString(13));
        u.setLastPasswordHash2(rs.getString(14));
        u.setLastPasswordHash3(rs.getString(15));
        u.setLastPasswordHash4(rs.getString(16));

        u.setLastPasswordSalt1(rs.getString(17));
        u.setLastPasswordSalt2(rs.getString(18));
        u.setLastPasswordSalt3(rs.getString(19));
        u.setLastPasswordSalt4(rs.getString(20));

        u.setRut(rs.getString(21));
        u.setVerificationCode(rs.getString(22));

        u.setProfiles(getUserProfiles(u.getIdUser()));

        u.setInstitutions(institutionDAO.getInstitutionBy(u));

        return u;
    }


    @Override
    public List<User> getAllUsers() {

        ArrayList<User> users = new ArrayList<>();


        ConnectionBD connect = new ConnectionBD();
        User user = null;

        String sql = "{call semantikos.get_all_users()}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.execute();


            ResultSet rs = call.getResultSet();
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


        ConnectionBD connect = new ConnectionBD();

        String sql = "{call semantikos.create_user(?,?,?,?,?,?,?,?,?,?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setString(1, user.getUsername().trim());
            call.setString(2, user.getName().trim());
            call.setString(3, user.getLastName().trim());
            call.setString(4, user.getSecondLastName().trim());
            call.setString(5, user.getEmail().trim());
            call.setBoolean(6, false);
            call.setInt(7, 0);
            call.setString(8, StringUtils.parseRut(user.getRut().trim()));
            call.setString(9, user.getPasswordHash());
            call.setString(10, user.getVerificationCode());

            call.execute();

            ResultSet rs = call.getResultSet();

            if (rs.next()) {
                user.setIdUser(rs.getLong(1));
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

        String sql = "{call semantikos.update_user(?,?,?,?,?,?,?,?)}";
        try (Connection connection = (new ConnectionBD()).getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setString(1, user.getName());
            call.setString(2, user.getLastName());
            call.setString(3, user.getSecondLastName());
            call.setString(4, user.getEmail());
            call.setString(5, user.getRut());
            call.setBoolean(6, user.isLocked());
            if(user.getVerificationCode()==null)
                call.setNull(7, Types.VARCHAR);
            else
                call.setString(7, user.getVerificationCode());
            call.setLong(8, user.getIdUser());

            call.execute();
        } catch (SQLException e) {
            String errorMsg = "Error al actualizar usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        sql = "{call semantikos.delete_user_profiles(?)}";
        try (Connection connection = (new ConnectionBD()).getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, user.getIdUser());
            call.execute();
        } catch (SQLException e) {
            String errorMsg = "Error al eliminar perfiles de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        for (Profile p : user.getProfiles()) {
            addProfileToUser(user, p);
        }
    }

    private void addProfileToUser(User user, Profile p) {

        ConnectionBD connect = new ConnectionBD();

        String sql = "{call semantikos.add_user_profile(?,?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {


            call.setLong(1, user.getIdUser());
            call.setLong(2,  p.getId());

            call.execute();


        } catch (SQLException e) {
            String errorMsg = "Error al agregar perfila a usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

    }

    @Override
    public List<Profile> getAllProfiles() {

        List<Profile> profiles = new ArrayList<Profile>();

        ConnectionBD connect = new ConnectionBD();
        Profile profile = null;

        String sql = "{call semantikos.get_all_profiles()}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                profile = makeProfileFromResult(rs);
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

        ConnectionBD connect = new ConnectionBD();
        boolean updated = false;

        String sql = "{call semantikos.update_user_passwords(?,?,?,?,?,?,?,?,?,?,?,?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setTimestamp(1, new Timestamp(user.getLastPasswordChange().getTime()));
            call.setString(2, user.getPasswordHash());
            call.setString(3, user.getLastPasswordHash1());
            call.setString(4, user.getLastPasswordHash2());
            call.setString(5, user.getLastPasswordHash3());
            call.setString(6, user.getLastPasswordHash4());
            call.setString(7, user.getPasswordSalt());
            call.setString(8, user.getLastPasswordSalt1());
            call.setString(9, user.getLastPasswordSalt2());
            call.setString(10, user.getLastPasswordSalt3());
            call.setString(11, user.getLastPasswordSalt4());
            call.setLong(12, user.getIdUser());


            call.execute();

            ResultSet rs = call.getResultSet();

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
            logger.info("Información de usuario (USER_ID=" + user.getIdUser() + ") actualizada exitosamente.");
        } else {
            String errorMsg = "Información de usuario (USER_ID=" + user.getIdUser() + ") no fue actualizada.";
            logger.error(errorMsg);
            throw new EJBException(errorMsg);
        }

    }


    /* marca la ultima fecha de ingreso del usuario */
    @Override
    public void markLogin(String username) {

        ConnectionBD connect = new ConnectionBD();

        String sql = "{call semantikos.mark_login(?,?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setDate(1, new java.sql.Date(new Date().getTime()));
            call.setString(2, username);
            call.execute();


        } catch (SQLException e) {
            String errorMsg = "Error al actualizar usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }


    }

    @Override
    public void markLoginFail(String username) {
        ConnectionBD connect = new ConnectionBD();

        String sql = "{call semantikos.mark_login_fail(?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

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
        ConnectionBD connect = new ConnectionBD();

        String sql = "{call semantikos.lock_user(?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setString(1, username);
            call.execute();


        } catch (SQLException e) {
            String errorMsg = "Error al actualizar usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }
    }

    @Override
    public Profile getProfile(long id) {


        ConnectionBD connect = new ConnectionBD();
        Profile profile = null;

        String sql = "{call semantikos.get_profile_by_id(?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1,id);
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                profile = makeProfileFromResult(rs);
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
        ConnectionBD connect = new ConnectionBD();

        String sql = "{call semantikos.unlock_user(?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setString(1, username);
            call.execute();


        } catch (SQLException e) {
            String errorMsg = "Error al actualizar usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }
    }



}
