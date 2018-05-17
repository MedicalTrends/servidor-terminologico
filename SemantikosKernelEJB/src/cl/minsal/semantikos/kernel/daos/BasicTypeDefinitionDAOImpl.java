package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.factories.BasicTypeDefinitionFactory;
import cl.minsal.semantikos.kernel.factories.DataSourceFactory;
import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.basictypes.BasicTypeDefinition;

import cl.minsal.semantikos.model.basictypes.CloseInterval;
import cl.minsal.semantikos.model.basictypes.Interval;
import cl.minsal.semantikos.model.relationships.BasicTypeType;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * @author Andrés Farías & Gustavo Punucura
 */
@Stateless
public class BasicTypeDefinitionDAOImpl implements BasicTypeDefinitionDAO {

    /** El logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(BasicTypeDefinitionDAOImpl.class);

    @EJB
    private BasicTypeDefinitionFactory basicTypeDefinitionFactory;

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    @Override
    public BasicTypeDefinition getBasicTypeDefinitionById(long idBasicTypeDefinition) {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_basic_type.get_basic_type_definition_by_id(?); end;";

        BasicTypeDefinition basicTypeDefinition;

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se invoca la consulta para recuperar las relaciones */
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, idBasicTypeDefinition);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                //basicTypeDefinition = basicTypeDefinitionFactory.createSimpleBasicTypeDefinitionFromJSON(jsonResult);
                basicTypeDefinition = createBasicTypeDefinitionFromResultSet(rs);
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

    /**
     * Crea un BasicTypeDefinition en su forma básica: sin dominio ni intervalos
     *
     * @param rs El JSON a partir del cual se crea el dominio.
     *
     * @return Un BasicTypeDefinition básico, sin dominio ni intervalos.
     */
    public BasicTypeDefinition createBasicTypeDefinitionFromResultSet(ResultSet rs) {

        try {
            long idBasicType = rs.getLong("id");
            String nameBasicType = rs.getString("name");
            String descriptionBasicType = rs.getString("description");
            BasicTypeType basicTypeType = null;
            basicTypeType = BasicTypeType.valueOf(rs.getLong("id_type"));

            Array domain = rs.getArray("domain");
            Array interval = rs.getArray("interval");

            switch (basicTypeType) {

                case STRING_TYPE:
                    BasicTypeDefinition<String> stringBasicTypeDefinition;
                    stringBasicTypeDefinition = new BasicTypeDefinition<>(idBasicType, nameBasicType, descriptionBasicType, basicTypeType);

                    if (domain != null) {
                        stringBasicTypeDefinition.setDomain(asList((String[])domain.getArray()));
                    } else {
                        stringBasicTypeDefinition.setDomain(new ArrayList<String>());
                    }
                    return stringBasicTypeDefinition;

                case BOOLEAN_TYPE:
                    return new BasicTypeDefinition<Boolean>(idBasicType, nameBasicType, descriptionBasicType, basicTypeType);

                case INTEGER_TYPE:
                    BasicTypeDefinition<Integer> integerBasicTypeDefinition = new BasicTypeDefinition<>(idBasicType, nameBasicType, descriptionBasicType, basicTypeType);
                    integerBasicTypeDefinition.setInterval(buildInterval(interval));

                    return integerBasicTypeDefinition;

                case FLOAT_TYPE:
                    return new BasicTypeDefinition<Float>(idBasicType, nameBasicType, descriptionBasicType, basicTypeType);

                case DATE_TYPE:
                    return new BasicTypeDefinition<Timestamp>(idBasicType, nameBasicType, descriptionBasicType, basicTypeType);

                default:
                    throw new IllegalArgumentException("TODO");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    CloseInterval buildInterval(Array array) throws SQLException {

        List<String> intervalElements = asList((String[]) array.getArray());

        if(intervalElements.isEmpty()) {
            return null;
        }

        CloseInterval interval = new CloseInterval();

        if(intervalElements.get(0) != null) {
            interval.setLowerBoundary(Integer.parseInt(intervalElements.get(0)));
        }

        if(intervalElements.get(1) != null) {
            interval.setUpperBoundary(Integer.parseInt(intervalElements.get(1)));
        }

        return interval;
    }
}
