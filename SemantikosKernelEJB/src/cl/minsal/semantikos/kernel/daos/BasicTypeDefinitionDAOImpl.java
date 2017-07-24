package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.daos.mappers.BasicTypeMapper;
import cl.minsal.semantikos.kernel.factories.BasicTypeDefinitionFactory;
import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.basictypes.BasicTypeDefinition;

import oracle.jdbc.OracleTypes;
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

        String sql = "begin ? := stk.stk_pck_basic_type.get_basic_type_definition_by_id(?); end;";

        BasicTypeDefinition basicTypeDefinition;

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se invoca la consulta para recuperar las relaciones */
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, idBasicTypeDefinition);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                //basicTypeDefinition = basicTypeDefinitionFactory.createSimpleBasicTypeDefinitionFromJSON(jsonResult);
                basicTypeDefinition = BasicTypeMapper.createBasicTypeDefinitionFromResultSet(rs);
            } else {
                String errorMsg = "Un error imposible ocurrio al pasar el resultSet a BasicTypeDefinition para id ="+idBasicTypeDefinition;
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "No se pudo mapear el resultSet a BasicTypeDefinition para id = "+idBasicTypeDefinition;
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

        return basicTypeDefinition;
    }
}
