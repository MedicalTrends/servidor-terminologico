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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        } catch (ParseException e) {
            String errorMsg = "Error al parsear el tipo fecha para intervalo = "+idBasicTypeDefinition;
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
    public BasicTypeDefinition createBasicTypeDefinitionFromResultSet(ResultSet rs) throws ParseException {

        try {
            long idBasicType = rs.getLong("id");
            String nameBasicType = rs.getString("name");
            String descriptionBasicType = rs.getString("description");
            BasicTypeType basicTypeType = null;
            basicTypeType = BasicTypeType.valueOf(rs.getLong("id_type"));

            Array domain = rs.getArray("domain");
            Array interval = rs.getArray("interval");

            Interval interval1 = buildInterval(interval, basicTypeType);

            List<String> domain1 = new ArrayList<>();

            if (domain != null) {
                domain1 = asList((String[])domain.getArray());
            }

            switch (basicTypeType) {

                case STRING_TYPE:
                    BasicTypeDefinition<String> stringBasicTypeDefinition;
                    stringBasicTypeDefinition = new BasicTypeDefinition<>(idBasicType, nameBasicType, descriptionBasicType, basicTypeType);

                    stringBasicTypeDefinition.setDomain(domain1);
                    stringBasicTypeDefinition.setInterval(interval1);

                    return stringBasicTypeDefinition;

                case BOOLEAN_TYPE:
                    return new BasicTypeDefinition<Boolean>(idBasicType, nameBasicType, descriptionBasicType, basicTypeType);

                case INTEGER_TYPE:
                    BasicTypeDefinition<Integer> integerBasicTypeDefinition = new BasicTypeDefinition<>(idBasicType, nameBasicType, descriptionBasicType, basicTypeType);
                    integerBasicTypeDefinition.setInterval(interval1);

                    return integerBasicTypeDefinition;

                case FLOAT_TYPE:
                    BasicTypeDefinition<Float> floatBasicTypeDefinition = new BasicTypeDefinition<>(idBasicType, nameBasicType, descriptionBasicType, basicTypeType);

                    floatBasicTypeDefinition.setInterval(interval1);

                    return floatBasicTypeDefinition;

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

    CloseInterval buildInterval(Array array, BasicTypeType basicTypeType) throws SQLException, ParseException {

        List<String> intervalElements = asList((String[]) array.getArray());

        if(intervalElements.isEmpty()) {
            return null;
        }

        CloseInterval interval = new CloseInterval();

        if(intervalElements.get(0) != null) {
            switch (basicTypeType.getTypeName()) {
                case "string":
                    interval.setLowerBoundary(intervalElements.get(0));
                    break;
                case "boolean":
                    interval.setLowerBoundary(Boolean.parseBoolean(intervalElements.get(0)));
                    break;
                case "int":
                    interval.setLowerBoundary(Integer.parseInt(intervalElements.get(0)));
                    break;
                case "float":
                    interval.setLowerBoundary(Float.parseFloat(intervalElements.get(0)));
                    break;
                case "date":
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    interval.setLowerBoundary(Timestamp.valueOf(format.format(intervalElements.get(0))));
                    break;
            }
        }

        if(intervalElements.get(1) != null) {
            switch (basicTypeType.getTypeName()) {
                case "string":
                    interval.setUpperBoundary(intervalElements.get(1));
                    break;
                case "boolean":
                    interval.setUpperBoundary(Boolean.parseBoolean(intervalElements.get(1)));
                    break;
                case "int":
                    interval.setUpperBoundary(Integer.parseInt(intervalElements.get(1)));
                    break;
                case "float":
                    interval.setUpperBoundary(Float.parseFloat(intervalElements.get(1)));
                    break;
                case "date":
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    java.util.Date parsedDate = dateFormat.parse((intervalElements.get(1)));
                    //return new java.sql.Timestamp(parsedDate.getTime());
                    interval.setUpperBoundary(new java.sql.Timestamp(parsedDate.getTime()));
                    break;
            }
        }

        return interval;
    }
}
