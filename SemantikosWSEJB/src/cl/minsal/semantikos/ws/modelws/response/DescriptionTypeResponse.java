package cl.minsal.semantikos.ws.modelws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-10-13.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "tipoDescripcion", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "TipoDescripcion", namespace = "http://service.ws.semantikos.minsal.cl/")
public class DescriptionTypeResponse implements Serializable {

    @XmlElement(name="nombre")
    private String name;
    @XmlElement(name="descripcion")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
