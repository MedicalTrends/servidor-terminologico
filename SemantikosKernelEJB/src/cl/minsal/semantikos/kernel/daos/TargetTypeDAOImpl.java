package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.kernel.util.DaoTools;
import cl.minsal.semantikos.model.basictypes.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Date;


/**
 * Created by andres on 7/22/16.
 */
@Stateless
public class TargetTypeDAOImpl implements TargetTypeDAO {

    static final Logger logger = LoggerFactory.getLogger(TargetTypeDAOImpl.class);

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    @Override
    public BasicTypeDefinition findByID(long idBasicType) {

        BasicTypeDefinition basicTypeDefinition = new BasicTypeDefinition();

        ConnectionBD connect = new ConnectionBD();
        String GET_BASIC_TYPE_BY_ID = "{call semantikos.get_basic_type_definition_by_id(?)}";
        String GET_BASIC_TYPE_INTERVAL_BY_ID = "{call semantikos.get_basic_type_interval_by_id(?)}";
        String GET_BASIC_DOMAIN_BY_ID = "{call semantikos.get_basic_domain_definition_by_id(?)}";

        try (Connection connection = connect.getConnection();

             CallableStatement call_basic_type = connection.prepareCall(GET_BASIC_TYPE_BY_ID);
             CallableStatement call_basic_type_interval = connection.prepareCall(GET_BASIC_TYPE_INTERVAL_BY_ID);
             CallableStatement call_basic_type_domain = connection.prepareCall(GET_BASIC_DOMAIN_BY_ID);
        ) {

             /* Se invoca la consulta para recuperar el basic type definition */

            call_basic_type.setLong(1, idBasicType);
            call_basic_type.execute();
            //@mromero: se refactoriza codigo para que no haya try dentro de otro try
            basicTypeDefinition = tryGetResultSet(call_basic_type,
                    call_basic_type_interval,
                    call_basic_type_domain,
                    basicTypeDefinition,
            idBasicType);

        } catch (SQLException e) {
            //@mromero he quitado el e.printstacktrace() y agregado logger
            logger.error("Ha ocurrido el siguiente error: " + e.getMessage(), e);
        }
        return basicTypeDefinition;

    }

    private BasicTypeDefinition tryGetResultSet(CallableStatement call_basic_type,
                                                CallableStatement call_basic_type_interval,
                                                CallableStatement call_basic_type_domain,
                                                BasicTypeDefinition basicTypeDefinition,
                                                long idBasicType) throws SQLException{

        try(ResultSet rs = call_basic_type.getResultSet()){
            while (rs.next()) {

                long id = rs.getLong("id");
                String name = rs.getString("name");
                String description = rs.getString("description");

                //@mromero: se refactoriza codigo para que metodo no sea tan grande
                basicTypeDefinition = new BasicTypeDefinition(id, name, description);

                /* Se invoca la consulta para recuperar el basic type interval */
                call_basic_type_interval.setLong(1, idBasicType);
                call_basic_type_interval.execute();

                ResultSet rs2 = call_basic_type_interval.getResultSet();

                basicTypeDefinition = this.setInterval(rs2, basicTypeDefinition);

                /* Se invoca la consulta para recuperar el basic type domain */
                call_basic_type_domain.setLong(1, idBasicType);
                call_basic_type_domain.execute();

                ResultSet rs3 = call_basic_type_domain.getResultSet();

                //@mromero: se refactoriza codigo para que metodo no sea tan grande
                basicTypeDefinition = this.addToDomain(rs3, basicTypeDefinition);

            }
        }catch(SQLException e){
            throw e;
        }
        return basicTypeDefinition;
    }


    private BasicTypeDefinition setInterval(ResultSet rs2, BasicTypeDefinition basicTypeDefinition) throws SQLException{
        if(rs2 == null)
            return basicTypeDefinition;
        while (rs2.next()) {
            basicTypeDefinition = setIntervalFloat(rs2, basicTypeDefinition);
            basicTypeDefinition = setIntervalString(rs2, basicTypeDefinition);
            basicTypeDefinition = setIntervalInt(rs2, basicTypeDefinition);
            basicTypeDefinition = setIntervalDate(rs2, basicTypeDefinition);
        }
        return basicTypeDefinition;
    }

    private BasicTypeDefinition setIntervalFloat(ResultSet rs2, BasicTypeDefinition basicTypeDefinition) throws SQLException{
        Float lowerBoundaryFloatValue = DaoTools.getFloat(rs2,"lower_bound_float_value");
        Float upperBoundaryFloatValue = DaoTools.getFloat(rs2,"upper_bound_float_value");
        if(lowerBoundaryFloatValue!=null || upperBoundaryFloatValue!=null)
            basicTypeDefinition.setInterval(new OpenInterval(lowerBoundaryFloatValue, upperBoundaryFloatValue));
        if(lowerBoundaryFloatValue!=null && upperBoundaryFloatValue!=null)
            basicTypeDefinition.setInterval(new CloseInterval(lowerBoundaryFloatValue, upperBoundaryFloatValue));
        return basicTypeDefinition;
    }

    private BasicTypeDefinition setIntervalString(ResultSet rs2, BasicTypeDefinition basicTypeDefinition) throws SQLException {
        String lowerBoundaryStringValue = DaoTools.getString(rs2,"lower_bound_string_value");
        String upperBoundaryStringValue = DaoTools.getString(rs2,"upper_bound_string_value");
        if(lowerBoundaryStringValue!=null || upperBoundaryStringValue!=null)
            basicTypeDefinition.setInterval(new OpenInterval(lowerBoundaryStringValue, upperBoundaryStringValue));
        if(lowerBoundaryStringValue!=null && upperBoundaryStringValue!=null)
            basicTypeDefinition.setInterval(new CloseInterval(lowerBoundaryStringValue, upperBoundaryStringValue));
        return basicTypeDefinition;
    }

    private BasicTypeDefinition setIntervalInt(ResultSet rs2, BasicTypeDefinition basicTypeDefinition) throws SQLException {

        Long lowerBoundaryIntValue = DaoTools.getLong(rs2,"lower_bound_int_value");
        Long upperBoundaryIntValue = DaoTools.getLong(rs2,"upper_bound_int_value");
        if(lowerBoundaryIntValue!=null || upperBoundaryIntValue!=null)
            basicTypeDefinition.setInterval(new OpenInterval(lowerBoundaryIntValue, upperBoundaryIntValue));
        if(lowerBoundaryIntValue!=null && upperBoundaryIntValue!=null)
            basicTypeDefinition.setInterval(new CloseInterval(lowerBoundaryIntValue, upperBoundaryIntValue));
        return basicTypeDefinition;
    }

    private BasicTypeDefinition setIntervalDate(ResultSet rs2, BasicTypeDefinition basicTypeDefinition) throws SQLException {
        Date lowerBoundaryDateValue = DaoTools.getDate(rs2,"lower_bound_date_value");
        Date upperBoundaryDateValue = DaoTools.getDate(rs2,"upper_bound_date_value");
        if(lowerBoundaryDateValue!=null || upperBoundaryDateValue!=null)
            basicTypeDefinition.setInterval(new OpenInterval(lowerBoundaryDateValue, upperBoundaryDateValue));
        if(lowerBoundaryDateValue!=null && upperBoundaryDateValue!=null)
            basicTypeDefinition.setInterval(new CloseInterval(lowerBoundaryDateValue, upperBoundaryDateValue));
        return basicTypeDefinition;
    }

    private BasicTypeDefinition addToDomain(ResultSet rs3, BasicTypeDefinition basicTypeDefinition) throws SQLException{
        if(rs3 == null)
            return basicTypeDefinition;

        while (rs3.next()) {

            Float floatValue = DaoTools.getFloat(rs3,"float_value");
            String stringValue = DaoTools.getString(rs3,"string_value");
            Long intValue = DaoTools.getLong(rs3,"int_value");
            Boolean booleanValue = DaoTools.getBoolean(rs3,"boolean_value");
            Date dateValue = DaoTools.getDate(rs3,"date_value");

            if (floatValue!=null)
                basicTypeDefinition.addToDomain(floatValue);
            if (dateValue!=null)
                basicTypeDefinition.addToDomain(dateValue);
            if (stringValue!=null)
                basicTypeDefinition.addToDomain(stringValue);
            if (booleanValue!=null)
                basicTypeDefinition.addToDomain(booleanValue);
            if (intValue != null)
                basicTypeDefinition.addToDomain(intValue);
        }

        return basicTypeDefinition;
    }

}
