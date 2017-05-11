package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by des01c7 on 09-01-17.
 */
@Stateless
public class TimeOutWebDAOImpl implements TimeOutWebDAO {

    /** El logger de esta clase */
    private static final Logger logger = LoggerFactory.getLogger(TimeOutWebDAOImpl.class);

    @Override
    public int getTimeOut() {
        ConnectionBD connect = new ConnectionBD();
        int time = 0;

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.get_time_out()}")) {
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                time = rs.getInt("time_out_parameter");
            }

            rs.close();

        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }
        return time;
    }
}
