package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.relationships.TargetDefinition;
import cl.minsal.semantikos.kernel.factories.TargetDefinitionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Andrés Farías
 */
@Stateless
public class TargetDefinitionDAOImpl implements TargetDefinitionDAO {

    /** El logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(TargetDefinitionDAOImpl.class);

    @EJB
    TargetDefinitionFactory targetDefinitionFactory;

    @Override
    public TargetDefinition getTargetDefinitionById(long idTargetDefinition) {

        ConnectionBD connect = new ConnectionBD();
        String sqlQuery = "{call semantikos.get_target_definition_by_id(?)}";

        TargetDefinition targetDefinition = null;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sqlQuery)) {

            /* Se invoca la consulta para recuperar las relaciones */
            call.setLong(1, idTargetDefinition);
            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                String jsonResult = rs.getString(1);
                targetDefinition = targetDefinitionFactory.createFromJSON(jsonResult);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al invocar get_target_definition_by_id(" + idTargetDefinition + ")";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return targetDefinition;
    }
}
