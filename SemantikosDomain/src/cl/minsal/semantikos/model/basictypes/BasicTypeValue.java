package cl.minsal.semantikos.model.basictypes;

import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetType;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 */
public class BasicTypeValue<T extends Comparable> implements Target, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(BasicTypeValue.class);

    /** Identificador único de la base de datos */
    private long id;

    private T value;

    public BasicTypeValue(T value) {
        this.value = value;
        this.id = -1;
    }

    public BasicTypeValue(long id, T value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public TargetType getTargetType() {
        return TargetType.BasicType;
    }

    @Override
    public String getRepresentation() {
        return toString();
    }

    public void setId(long id) {
        this.id = id;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value)

    {

        if(value!=null){
            logger.debug("seteando valor de target valor={}", value);
            this.value = value;
        }

    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof BasicTypeValue) && ( String.valueOf(value) != null )
                ? String.valueOf(value).equals(String.valueOf(((BasicTypeValue) other).value))
                : (other == this);
    }

    @Override
    public int hashCode() {
        return (String.valueOf(value) != null)
                ? (this.getClass().hashCode() + String.valueOf(value).hashCode())
                : super.hashCode();
    }

    @Override
    public String toString() {
        //return String.format("ExampleEntity[%d, %s]", idDescriptionType, glosa);
        if(this.getValue() != null) {
            if(this.getValue() instanceof Boolean) {
                if((Boolean)this.getValue()) {
                    return "1";
                }
                else {
                    return "0";
                }
            }
            return getValue().toString();
        }
        else {
            return "null";
        }
    }

    public boolean isBoolean() {
        return this.value.getClass().equals(Boolean.class);
    }

    public boolean isDate() {
        return this.value.getClass().equals(Timestamp.class);
    }

    public boolean isFloat() {
        return this.value.getClass().equals(Float.class) || this.value.getClass().equals(Double.class);
    }

    public boolean isInteger() {
        return this.value.getClass().equals(Integer.class);
    }

    public boolean isString() {
        return this.value.getClass().equals(String.class);
    }

    @Override
    public BasicTypeValue copy() {
        return new BasicTypeValue<>(this.id, this.value);
    }
}
