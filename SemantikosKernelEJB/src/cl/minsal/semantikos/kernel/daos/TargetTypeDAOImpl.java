package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.kernel.util.DaoTools;
import cl.minsal.semantikos.model.basictypes.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by andres on 7/22/16.
 */
@Stateless
public class TargetTypeDAOImpl implements TargetTypeDAO {


    @Override
    public BasicTypeDefinition findByID(long idBasicType) {

        BasicTypeDefinition basicTypeDefinition = new BasicTypeDefinition();

        ConnectionBD connect = new ConnectionBD();
        String GET_BASIC_TYPE_BY_ID = "{call semantikos.get_basic_type_definition_by_id(?)}";
        String GET_BASIC_TYPE_INTERVAL_BY_ID = "{call semantikos.get_basic_type_interval_by_id(?)}";
        String GET_BASIC_DOMAIN_BY_ID = "{call semantikos.get_basic_domain_definition_by_id(?)}";

        /*
        String GET_BASIC_TYPE_BY_ID = "begin ? := stk.stk_pck_basic_type_definition.get_basic_type_definition_by_id(?); end;";
        String GET_BASIC_TYPE_INTERVAL_BY_ID = "begin ? := stk.stk_pck_basic_type_definition.get_basic_type_definition_by_id(?); end;";
        */

        try (Connection connection = connect.getConnection();

             CallableStatement call_basic_type = connection.prepareCall(GET_BASIC_TYPE_BY_ID);
             CallableStatement call_basic_type_interval = connection.prepareCall(GET_BASIC_TYPE_INTERVAL_BY_ID);
             CallableStatement call_basic_type_domain = connection.prepareCall(GET_BASIC_DOMAIN_BY_ID)) {

             /* Se invoca la consulta para recuperar el basic type definition */

            call_basic_type.setLong(1, idBasicType);
            call_basic_type.execute();

            ResultSet rs = call_basic_type.getResultSet();

            while (rs.next()) {

                long id = rs.getLong("id");
                String name = rs.getString("name");
                String description = rs.getString("description");

                basicTypeDefinition = new BasicTypeDefinition(id, name, description);

                /* Se invoca la consulta para recuperar el basic type interval */
                call_basic_type_interval.setLong(1, idBasicType);
                call_basic_type_interval.execute();

                ResultSet rs2 = call_basic_type_interval.getResultSet();

                while (rs2.next()) {

                    Float lowerBoundaryFloatValue = DaoTools.getFloat(rs2,"lower_bound_float_value");
                    String lowerBoundaryStringValue = DaoTools.getString(rs2,"lower_bound_string_value");
                    Long lowerBoundaryIntValue = DaoTools.getLong(rs2,"lower_bound_int_value");
                    Date lowerBoundaryDateValue = DaoTools.getDate(rs2,"lower_bound_date_value");

                    Float upperBoundaryFloatValue = DaoTools.getFloat(rs2,"upper_bound_float_value");
                    String upperBoundaryStringValue = DaoTools.getString(rs2,"upper_bound_string_value");
                    Long upperBoundaryIntValue = DaoTools.getLong(rs2,"upper_bound_int_value");
                    Date upperBoundaryDateValue = DaoTools.getDate(rs2,"upper_bound_date_value");

                    if(lowerBoundaryFloatValue!=null || upperBoundaryFloatValue!=null)
                        basicTypeDefinition.setInterval(new OpenInterval(lowerBoundaryFloatValue, upperBoundaryFloatValue));
                    if(lowerBoundaryFloatValue!=null && upperBoundaryFloatValue!=null)
                        basicTypeDefinition.setInterval(new CloseInterval(lowerBoundaryFloatValue, upperBoundaryFloatValue));

                    if(lowerBoundaryStringValue!=null || upperBoundaryStringValue!=null)
                        basicTypeDefinition.setInterval(new OpenInterval(lowerBoundaryStringValue, upperBoundaryStringValue));
                    if(lowerBoundaryStringValue!=null && upperBoundaryStringValue!=null)
                        basicTypeDefinition.setInterval(new CloseInterval(lowerBoundaryStringValue, upperBoundaryStringValue));

                    if(lowerBoundaryIntValue!=null || upperBoundaryIntValue!=null)
                        basicTypeDefinition.setInterval(new OpenInterval(lowerBoundaryIntValue, upperBoundaryIntValue));
                    if(lowerBoundaryIntValue!=null && upperBoundaryIntValue!=null)
                        basicTypeDefinition.setInterval(new CloseInterval(lowerBoundaryIntValue, upperBoundaryIntValue));

                    if(lowerBoundaryDateValue!=null || upperBoundaryDateValue!=null)
                        basicTypeDefinition.setInterval(new OpenInterval(lowerBoundaryDateValue, upperBoundaryDateValue));
                    if(lowerBoundaryDateValue!=null && upperBoundaryDateValue!=null)
                        basicTypeDefinition.setInterval(new CloseInterval(lowerBoundaryDateValue, upperBoundaryDateValue));
                }

                /* Se invoca la consulta para recuperar el basic type domain */
                call_basic_type_domain.setLong(1, idBasicType);
                call_basic_type_domain.execute();

                ResultSet rs3 = call_basic_type_domain.getResultSet();

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

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return basicTypeDefinition;

    }

}
