package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.factories.DataSourceFactory;
import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.User;
import oracle.jdbc.OracleTypes;
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

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_institution.get_institution_by_id(?); end;";

        Institution institution= new Institution();
        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, id);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

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
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_institution.get_institution_by_user(?); end;";

        List<Institution> institutions= new ArrayList<>();
        try (Connection connection = DataSourceFactory.getInstance().getConnection();
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

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
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

        String sql = "begin ? := stk.stk_pck_institution.get_all_institution; end;";

        List<Institution> institutions= new ArrayList<>();
        try (Connection connection = DataSourceFactory.getInstance().getConnection();
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
