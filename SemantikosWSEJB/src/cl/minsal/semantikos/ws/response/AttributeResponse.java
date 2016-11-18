package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Development on 2016-10-20.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "atributo")
public class AttributeResponse implements Serializable {

    @XmlElement(name="nombre")
    private String name;
    @XmlElement(name="tipo")
    private String type;
    @XmlElement(name="valor")
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
