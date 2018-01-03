package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.User;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by des01c7 on 15-12-16.
 */
@Stateless
public class ProfileDAOImpl implements ProfileDAO {


    private static final Logger logger = LoggerFactory.getLogger(ProfileDAOImpl.class);

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    @Override
    public List<Profile> getProfilesBy(User user) {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_profile.get_profiles_by_user(?); end;";

        List<Profile> profiles = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, user.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                profiles.add(createProfileFromResult(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al al obtener los RefSets ", e);
        }

        return profiles;
    }

    @Override
    public Profile getProfileById(long id) {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_profile.get_profile_by_id(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, id);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                return createProfileFromResult(rs);
            }

        } catch (SQLException e) {
            logger.error("Error al al obtener el perfil ", e);
        }

        return null;
    }

    @Override
    public List<Profile> getAllProfiles() {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_profile.get_all_profiles; end;";

        List<Profile> profiles = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                profiles.add(createProfileFromResult(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al al obtener los RefSets ", e);
        }

        return profiles;
    }

    public void bindProfileToUser(User user, Profile profile) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_profile.bind_profile_to_user(?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.INTEGER);
            call.setLong(2, user.getId());
            call.setLong(3,  profile.getId());

            call.execute();

        } catch (SQLException e) {
            String errorMsg = "Error al enlazar perfil a usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

    }

    public void unbindProfileFromUser(User user, Profile profile) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_profile.unbind_profile_from_user(?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.INTEGER);
            call.setLong(2, user.getId());
            call.setLong(3,  profile.getId());

            call.execute();

        } catch (SQLException e) {
            String errorMsg = "Error al desenlazar perfil de usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

    }

    /**
     * Este método es responsable de crear un Profile a partir de una fila de un resultset.
     *
     * @param rs  resultset parado en la fila a procesar
     *
     * @return El Profile creado a partir de la fila.
     */
    public Profile createProfileFromResult(ResultSet rs) throws SQLException {

        long idProfile = ( rs.getBigDecimal(1) ).longValue();
        String name = rs.getString(2);
        String description = rs.getString(3);

        return new Profile(idProfile, name, description);
    }
}
