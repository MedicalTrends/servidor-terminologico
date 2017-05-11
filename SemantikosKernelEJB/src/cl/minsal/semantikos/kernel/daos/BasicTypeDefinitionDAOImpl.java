package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.basictypes.BasicTypeDefinition;
import cl.minsal.semantikos.kernel.factories.BasicTypeDefinitionFactory;
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
 * @author Andrés Farías & Gustavo Punucura
 */
@Stateless
public class BasicTypeDefinitionDAOImpl implements BasicTypeDefinitionDAO {

    /** El logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(BasicTypeDefinitionDAOImpl.class);

    @EJB
    private BasicTypeDefinitionFactory basicTypeDefinitionFactory;

    @Override
    public BasicTypeDefinition getBasicTypeDefinitionById(long idBasicTypeDefinition) {
        ConnectionBD connect = new ConnectionBD();
        String sqlQuery = "{call semantikos.get_basic_type_type_definition_by_id(?)}";

        BasicTypeDefinition basicTypeDefinition;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sqlQuery)) {

            /* Se invoca la consulta para recuperar las relaciones */
            call.setLong(1, idBasicTypeDefinition);
            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                String jsonResult = rs.getString(1);
                basicTypeDefinition = basicTypeDefinitionFactory.createSimpleBasicTypeDefinitionFromJSON(jsonResult);
            } else {
                String errorMsg = "Un error imposible ocurrio al pasar JSON a BasicTypeDefinition";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "No se pudo parsear el JSON a BasicTypeDefinition.";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

        return basicTypeDefinition;
    }
}
