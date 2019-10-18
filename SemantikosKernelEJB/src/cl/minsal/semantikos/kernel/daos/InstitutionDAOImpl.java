package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.DaoTools;
import cl.minsal.semantikos.model.users.Institution;
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
public class InstitutionDAOImpl implements InstitutionDAO {


    private static final Logger logger = LoggerFactory.getLogger(InstitutionDAOImpl.class);

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    @Override
    public List<Institution> getInstitutionBy(User user) {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_institution.get_institutions_by_user(?); end;";

        List<Institution> institutions= new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, user.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                institutions.add(createInstitutionFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al al obtener los RefSets ", e);
        }

        return institutions;
    }

    @Override
    public Institution getInstitutionById(long id) {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_institution.get_institution_by_id(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, id);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                return createInstitutionFromResultSet(rs);
            }
        } catch (SQLException e) {
            logger.error("Error al al obtener los RefSets ", e);
        }

        return null;
    }

    @Override
    public Institution getInstitutionByCode(long id) {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_institution.get_institution_by_code(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, id);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                return createInstitutionFromResultSet(rs);
            }
        } catch (SQLException e) {
            logger.error("Error al al obtener los RefSets ", e);
        }

        return null;
    }

    @Override
    public List<Institution> getAllInstitution() {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_institution.get_all_institutions; end;";

        List<Institution> institutions= new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                institutions.add(createInstitutionFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al al obtener los RefSets ", e);
        }

        return institutions;
    }

    public void createInstitution(Institution institution) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_institution.create_institution(?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.NUMERIC);
            call.setString(2, institution.getName());
            call.setLong(3, institution.getCode());

            call.execute();

            //ResultSet rs = call.getResultSet();

            if (call.getLong(1) > 0) {
                institution.setId(call.getLong(1));
            } else {
                String errorMsg = "El establecimiento no fué creado. Esta es una situación imposible. Contactar a Desarrollo";
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al crear el establecimiento en la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

    }

    @Override
    public void updateInstitution(Institution institution) {

        String sql = "begin ? := stk.stk_pck_institution.update_institution(?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.INTEGER);
            call.setString(2, institution.getName());
            call.setTimestamp(3, institution.getValidityUntil());
            call.setLong(4, institution.getId());

            call.execute();

        } catch (SQLException e) {
            String errorMsg = "Error al actualizar establecimiento de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

    }

    public void bindInstitutionToUser(User user, Institution institution) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_institution.bind_institution_to_user(?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.INTEGER);
            call.setLong(2, user.getId());
            call.setLong(3,  institution.getId());

            call.execute();

        } catch (SQLException e) {
            String errorMsg = "Error al enlazar establecimiento a usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

    }

    public void unbindInstitutionFromUser(User user, Institution institution) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_institution.unbind_institution_from_user(?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.INTEGER);
            call.setLong(2, user.getId());
            call.setLong(3,  institution.getId());

            call.execute();

        } catch (SQLException e) {
            String errorMsg = "Error al desenlazar establecimiento de usuario de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

    }

    private Institution createInstitutionFromResultSet(ResultSet resultSet) {
        Institution institution = new Institution();
        try {
            institution.setId(resultSet.getLong("id"));
            institution.setName(resultSet.getString("name"));
            institution.setCode(DaoTools.getLong(resultSet, "code"));
            institution.setValidityUntil(resultSet.getTimestamp("validity_until"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return institution;
    }
}
