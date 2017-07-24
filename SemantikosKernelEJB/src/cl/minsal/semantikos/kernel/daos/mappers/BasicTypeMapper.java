package cl.minsal.semantikos.kernel.daos.mappers;

import cl.minsal.semantikos.kernel.util.DaoTools;
import cl.minsal.semantikos.model.basictypes.BasicTypeDefinition;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.relationships.BasicTypeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJBException;
import java.io.IOException;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import static cl.minsal.semantikos.kernel.util.StringUtils.underScoreToCamelCaseJSON;
import static java.util.Arrays.asList;

/**
 * Created by root on 28-06-17.
 */
public class BasicTypeMapper implements ObjectMapper {

    /**
     * El logger para esta clase
     */
    private static final Logger logger = LoggerFactory.getLogger(BasicTypeMapper.class);

    @Override
    public Object createObjectFromResultSet(ResultSet resultSet) {
        return null;
    }

    public static BasicTypeValue createBasicTypeFromResultSet(ResultSet rs) {

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

    /**
     * Crea un BasicTypeDefinition en su forma básica: sin dominio ni intervalos
     *
     * @param rs El JSON a partir del cual se crea el dominio.
     *
     * @return Un BasicTypeDefinition básico, sin dominio ni intervalos.
     */
    public static BasicTypeDefinition createBasicTypeDefinitionFromResultSet(ResultSet rs) {

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
                    return new BasicTypeDefinition<Integer>(idBasicType, nameBasicType, descriptionBasicType, basicTypeType);
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
}
