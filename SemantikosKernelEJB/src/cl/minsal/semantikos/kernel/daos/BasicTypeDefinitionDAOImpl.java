package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.factories.BasicTypeDefinitionFactory;
import cl.minsal.semantikos.kernel.factories.DataSourceFactory;
import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.kernel.util.DaoTools;
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

    public Interval getBasicTypeInterval(long idBasicTypeDefinition) {

        String sql = "begin ? := stk.stk_pck_basic_type.get_basic_type_interval(?); end;";

        Interval interval = null;

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se invoca la consulta para recuperar las relaciones */
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, idBasicTypeDefinition);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                interval = createBasicTypeIntervalFromResultSet(rs);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "No se pudo mapear el resultSet a Interval para id = "+idBasicTypeDefinition;
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        } catch (ParseException e) {
            String errorMsg = "Error al parsear el tipo fecha para intervalo = "+idBasicTypeDefinition;
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

        return interval;
    }

    public List getBasicTypeDomain(long idBasicTypeDefinition) {

        String sql = "begin ? := stk.stk_pck_basic_type.get_basic_type_domain(?); end;";

        List domain = new ArrayList();

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se invoca la consulta para recuperar las relaciones */
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, idBasicTypeDefinition);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                domain.add(createBasicTypeDomainFromResultSet(rs));
            }

            rs.close();

        } catch (SQLException e) {
            String errorMsg = "No se pudo mapear el resultSet a Interval para id = "+idBasicTypeDefinition;
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        } catch (ParseException e) {
            String errorMsg = "Error al parsear el tipo fecha para dominio = "+idBasicTypeDefinition;
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

        return domain;
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

            BasicTypeDefinition basicTypeDefinition = new BasicTypeDefinition(idBasicType, nameBasicType, descriptionBasicType, basicTypeType);

            Interval interval = getBasicTypeInterval(idBasicType);

            if(interval != null) {
                basicTypeDefinition.setInterval(interval);
            }

            List domain = getBasicTypeDomain(idBasicType);

            if(domain != null) {
                basicTypeDefinition.setDomain(domain);
            }

            return  basicTypeDefinition;


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Crea un BasicTypeDefinition en su forma básica: sin dominio ni intervalos
     *
     * @param rs El JSON a partir del cual se crea el dominio.
     *
     * @return Un BasicTypeDefinition básico, sin dominio ni intervalos.
     */
    public Interval createBasicTypeIntervalFromResultSet(ResultSet rs) throws ParseException {

        try {
            BasicTypeType basicTypeType = null;
            basicTypeType = BasicTypeType.valueOf(rs.getLong("id_type"));

            CloseInterval interval = new CloseInterval();

            switch (basicTypeType) {

                case STRING_TYPE:
                    interval.setLowerBoundary(rs.getString("lower_bound_string_value"));
                    interval.setUpperBoundary(rs.getString("upper_bound_string_value"));
                    return interval;

                case BOOLEAN_TYPE:
                    interval.setLowerBoundary(rs.getBoolean("lower_bound_boolean_value"));
                    interval.setUpperBoundary(rs.getBoolean("upper_bound_boolean_value"));
                    return interval;

                case INTEGER_TYPE:
                    Integer lowerBound = DaoTools.getInteger(rs,"lower_bound_int_value");
                    Integer upperBound = DaoTools.getInteger(rs,"upper_bound_int_value");
                    interval.setLowerBoundary(lowerBound==null?Integer.MIN_VALUE:lowerBound);
                    interval.setUpperBoundary(upperBound==null?Integer.MAX_VALUE:upperBound);
                    return interval;

                case FLOAT_TYPE:
                    Float lowerBound2 = DaoTools.getFloat(rs,"lower_bound_float_value");
                    Float upperBound2 = DaoTools.getFloat(rs,"upper_bound_float_value");
                    interval.setLowerBoundary(lowerBound2==null?Float.MIN_VALUE:lowerBound2);
                    interval.setUpperBoundary(upperBound2==null?Float.MAX_VALUE:upperBound2);
                    return interval;

                case DATE_TYPE:
                    interval.setLowerBoundary(rs.getTimestamp("lower_bound_date_value"));
                    interval.setUpperBoundary(rs.getTimestamp("upper_bound_date_value"));
                    return interval;

                default:
                    throw new IllegalArgumentException("TODO");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Crea un BasicTypeDefinition en su forma básica: sin dominio ni intervalos
     *
     * @param rs El JSON a partir del cual se crea el dominio.
     *
     * @return Un BasicTypeDefinition básico, sin dominio ni intervalos.
     */
    public Comparable createBasicTypeDomainFromResultSet(ResultSet rs) throws ParseException {

        try {
            BasicTypeType basicTypeType = null;
            basicTypeType = BasicTypeType.valueOf(rs.getLong("id_type"));

            switch (basicTypeType) {

                case STRING_TYPE:
                    return rs.getString("string_value");

                case BOOLEAN_TYPE:
                    return rs.getBoolean("boolean_value");

                case INTEGER_TYPE:
                    return rs.getInt("int_value");

                case FLOAT_TYPE:
                    return rs.getFloat("float_value");

                case DATE_TYPE:
                    return rs.getTimestamp("date_value");

                default:
                    throw new IllegalArgumentException("TODO");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
