package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.daos.mappers.BasicTypeMapper;
import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.metamodel.BasicType;
import java.io.IOException;
import java.sql.*;

import static cl.minsal.semantikos.kernel.util.StringUtils.underScoreToCamelCaseJSON;

/**
 * @author Andrés Farías
 */
@Stateless
public class BasicTypeDAOImpl implements BasicTypeDAO {
    private static final Logger logger = LoggerFactory.getLogger(BasicTypeDAOImpl.class);

    @Override
    public BasicTypeValue getBasicTypeValueByID(long idBasicValue) {

        BasicTypeValue basicTypeValue;
        //String jsonResult;
        ConnectionBD connect = new ConnectionBD();
        String sqlQuery = "{call semantikos.get_basic_type_by_id(?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sqlQuery)) {

            /* Se invoca la consulta para recuperar el basic type */
            call.setLong(1, idBasicValue);
            call.execute();

            /* Cada Fila del ResultSet trae una relación */
            ResultSet rs = call.getResultSet();

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


    public BasicTypeValue createBasicTypeFromJSON(String jsonResult) {
        BasicTypeDTO basicTypeDTO;
        ObjectMapper mapper = new ObjectMapper();
        try {
            basicTypeDTO = mapper.readValue(underScoreToCamelCaseJSON(jsonResult), BasicTypeDTO.class);
        } catch (IOException e) {
            String errorMsg = "Error al parsear un JSON hacia un BasicTypeDTO";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        /* Se evaluan los tipos básicos */

        if (basicTypeDTO.getFloatValue() != null) {
            BasicTypeValue bt=new BasicTypeValue<Float>(basicTypeDTO.getFloatValue());
            bt.setId(basicTypeDTO.getId());
            return bt;
        } else if (basicTypeDTO.getIntValue() != null) {
            BasicTypeValue bt=new BasicTypeValue<Integer>(basicTypeDTO.getIntValue());
            bt.setId(basicTypeDTO.getId());
            return bt;
        } else if (basicTypeDTO.getBooleanValue() != null) {
            BasicTypeValue bt=new BasicTypeValue<Boolean>(basicTypeDTO.getBooleanValue());
            bt.setId(basicTypeDTO.getId());
            return bt;
        } else if (basicTypeDTO.getStringValue() != null) {
            BasicTypeValue bt=new BasicTypeValue<String>(basicTypeDTO.getStringValue());
            bt.setId(basicTypeDTO.getId());
            return bt;
        } else if (basicTypeDTO.getDateValue() != null) {

            BasicTypeValue bt=new BasicTypeValue<Timestamp>(basicTypeDTO.getDateValue());
            bt.setId(basicTypeDTO.getId());
            return bt;
        } else {
            String message = "Existe un caso no contemplado";
            logger.error(message);
            throw new EJBException(message);
        }

    }




}
class BasicTypeDTO {
    private Long id;
    private Float floatValue;
    private Timestamp dateValue;
    private String stringValue;
    private Boolean booleanValue;
    private Integer intValue;

    public BasicTypeDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(Float floatValue) {
        this.floatValue = floatValue;
    }

    public Timestamp getDateValue() {
        return dateValue;
    }

    public void setDateValue(Timestamp dateValue) {
        this.dateValue = dateValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }


    @Override
    public String toString() {
        return "BasicTypeDTO{" +
                "id=" + id +
                ", floatValue=" + floatValue +
                ", dateValue=" + dateValue +
                ", stringValue='" + stringValue + '\'' +
                ", booleanValue=" + booleanValue +
                ", intValue=" + intValue +
                "}";
    }
}
