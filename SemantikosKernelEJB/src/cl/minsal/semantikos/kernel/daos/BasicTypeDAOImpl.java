package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.factories.DataSourceFactory;
import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.kernel.util.DaoTools;
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

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_basic_type.get_basic_type_by_id(?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se invoca la consulta para recuperar el basic type */
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, idBasicValue);
            call.execute();

            /* Cada Fila del ResultSet trae una relación */
            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                basicTypeValue = createBasicTypeFromResultSet(rs);
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

    public BasicTypeValue createBasicTypeFromResultSet(ResultSet rs) {

        BasicTypeValue bt = null;

        /* Se evaluan los tipos básicos */
        try {
            long id = rs.getLong("id");

            if (DaoTools.getFloat(rs, "float_value") != null) {
                bt = new BasicTypeValue<Float>(DaoTools.getFloat(rs, "float_value"));
                bt.setId(id);
            } else if (DaoTools.getInteger(rs, "int_value") != null) {
                bt = new BasicTypeValue<Integer>(DaoTools.getInteger(rs, "int_value"));
                bt.setId(id);
            } else if (DaoTools.getBoolean(rs, "boolean_value") != null) {
                bt = new BasicTypeValue<Boolean>(DaoTools.getBoolean(rs, "boolean_value"));
                bt.setId(id);
            } else if (DaoTools.getString(rs, "string_value") != null) {
                bt = new BasicTypeValue<String>(DaoTools.getString(rs, "string_value"));
                bt.setId(id);
            } else if (DaoTools.getDate(rs, "date_value") != null) {
                bt = new BasicTypeValue<Timestamp>(DaoTools.getTimestamp(rs, "date_value"));
                bt.setId(id);
            } else {
                String message = "Existe un caso no contemplado";
                logger.error(message);
                throw new EJBException(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bt;
    }

}

