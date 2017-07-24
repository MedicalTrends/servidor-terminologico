package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.daos.mappers.BasicTypeMapper;
import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import java.io.IOException;
import java.sql.*;

import static cl.minsal.semantikos.util.StringUtils.underScoreToCamelCaseJSON;

/**
 * @author Andrés Farías
 */
@Stateless
public class BasicTypeDAOImpl implements BasicTypeDAO {
    private static final Logger logger = LoggerFactory.getLogger(BasicTypeDAOImpl.class);

    @Override
    public BasicTypeValue getBasicTypeValueByID(long idBasicValue) {

        BasicTypeValue basicTypeValue;

        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_basic_type.get_basic_type_by_id(?); end;";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se invoca la consulta para recuperar el basic type */
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, idBasicValue);
            call.execute();

            /* Cada Fila del ResultSet trae una relación */
            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                basicTypeValue = BasicTypeMapper.createBasicTypeFromResultSet(rs);
                //jsonResult = rs.getString(1);

            } else {
                String errorMsg = "Un error imposible acaba de ocurrir";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }

            rs.close();
        } catch (SQLException e) {
            String errorMsg = "Erro al invocar get_basic_type_by_id(" + idBasicValue + ")";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }
        //return createBasicTypeFromJSON(jsonResult);
        return basicTypeValue;
    }

}

