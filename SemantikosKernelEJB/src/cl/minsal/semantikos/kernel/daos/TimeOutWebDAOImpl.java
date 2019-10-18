package cl.minsal.semantikos.kernel.daos;

import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.sql.DataSource;
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

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    @Override
    public int getTimeOut() {
        //ConnectionBD connect = new ConnectionBD();
        int time = 0;

        String sql = "begin ? := stk.stk_pck_system.get_time_out; end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                time = rs.getInt("time_out");
            }

            rs.close();

        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }
        return time;
    }
}
