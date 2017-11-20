package cl.minsal.semantikos.kernel.daos.ws;

import cl.minsal.semantikos.kernel.daos.BasicTypeDAO;
import cl.minsal.semantikos.kernel.util.DaoTools;
import cl.minsal.semantikos.model.basictypes.BasicTypeDefinition;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.dtos.BasicTypeValueDTO;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Comparator;

/**
 * @author Andrés Farías
 */
@Stateless
public class BasicTypeWSDAOImpl implements BasicTypeWSDAO {
    private static final Logger logger = LoggerFactory.getLogger(BasicTypeWSDAOImpl.class);

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    public BasicTypeValue createBasicTypeFromDTO(BasicTypeValueDTO basicTypeValueDTO, BasicTypeDefinition basicTypeDefinition) {

        Comparable value = null;

        switch(basicTypeDefinition.getType().getTypeName()) {
            case "string":
                value = String.valueOf(basicTypeValueDTO.getValue());
                break;
            case "float":
                value = Float.valueOf(basicTypeValueDTO.getValue().replace(",","."));
                break;
            case "int":
                value = Integer.valueOf(basicTypeValueDTO.getValue());
                break;
            case "date":
                value = Timestamp.valueOf(basicTypeValueDTO.getValue());
                break;
            case "boolean":
                value = Boolean.valueOf(basicTypeValueDTO.getValue());
                break;
        }

        BasicTypeValue bt = new BasicTypeValue(basicTypeValueDTO.getId(), value);

        return bt;
    }

}

