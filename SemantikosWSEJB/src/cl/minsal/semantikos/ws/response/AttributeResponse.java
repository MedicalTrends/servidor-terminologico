package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-10-20.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "atributo", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "Atributo", namespace = "http://service.ws.semantikos.minsal.cl/")
public class AttributeResponse implements Serializable {

    @XmlElement(name="tipo")
    private String type;
    @XmlElement(name="nombre")
    private String name;
    @XmlElement(name="valor")
    private String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
