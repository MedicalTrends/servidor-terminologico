package cl.minsal.semantikos.model.queries;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * Created by root on 29-09-16.
 */
public class QueryParameter implements Serializable {

    Type type;
    Object value;
    boolean array;

    public QueryParameter(Type type, Object value, boolean array) {
        this.type = type;
        this.value = value;
        this.array = array;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isArray() {
        return array;
    }

    public void setArray(boolean array) {
        this.array = array;
    }
}
