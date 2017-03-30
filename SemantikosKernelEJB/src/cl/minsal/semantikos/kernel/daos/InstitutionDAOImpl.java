package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by des01c7 on 15-12-16.
 */
@Stateless
public class InstitutionDAOImpl implements InstitutionDAO {


    private static final Logger logger = LoggerFactory.getLogger(InstitutionDAOImpl.class);


    @Override
    public Institution getInstitutionBy(long id) {

        ConnectionBD connect = new ConnectionBD();
        String GET_INSTITUTION_BY_ID = "{call semantikos.get_institution_by_id(?)}";
        Institution institution= new Institution();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(GET_INSTITUTION_BY_ID)) {
            call.setLong(1, id);
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                institution = createInstitutionFromResultSet(rs);
            }
        } catch (SQLException e) {
            logger.error("Error al al obtener los RefSets ", e);
        }

        return institution;
    }

    @Override
    public List<Institution> getInstitutionBy(User user) {
        ConnectionBD connect = new ConnectionBD();
        String GET_INSTITUTION_BY_USER = "{call semantikos.get_institution_by_user(?)}";
        List<Institution> institutions= new ArrayList<>();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(GET_INSTITUTION_BY_USER)) {
            call.setLong(1, user.getIdUser());
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                institutions.add(createInstitutionFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al al obtener los RefSets ", e);
        }

        return institutions;
    }

    @Override
    public List<Institution> getAllInstitution() {
        ConnectionBD connect = new ConnectionBD();
        String GET_ALL_INSTITUTION = "{call semantikos.get_all_institution()}";
        List<Institution> institutions= new ArrayList<>();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(GET_ALL_INSTITUTION)) {
            call.execute();
            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                institutions.add(createInstitutionFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al al obtener los RefSets ", e);
        }

        return institutions;
    }

    private Institution createInstitutionFromResultSet(ResultSet resultSet) {
        Institution institution = new Institution();
        try {
            institution.setId(resultSet.getLong("id"));
            institution.setName(resultSet.getString("name"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return institution;
    }
}
