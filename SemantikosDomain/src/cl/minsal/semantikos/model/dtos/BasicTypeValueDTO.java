package cl.minsal.semantikos.model.dtos;

import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 */
public class BasicTypeValueDTO implements TargetDTO, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(BasicTypeValueDTO.class);

    /** Identificador único de la base de datos */
    private long id;

    private String value;

    public BasicTypeValueDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value)
    {

        if(value!=null){
            logger.debug("seteando valor de target valor={}", value);
            this.value = value;
        }

    }


}
