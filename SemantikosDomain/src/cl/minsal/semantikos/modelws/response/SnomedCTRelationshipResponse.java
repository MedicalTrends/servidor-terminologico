package cl.minsal.semantikos.modelws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-12-28.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "relacionSnomedCT", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RelacionSnomedCT", namespace = "http://service.ws.semantikos.minsal.cl/")
public class SnomedCTRelationshipResponse implements Serializable {

    @XmlElement(name = "tipoRelacion")
    private String type;
    @XmlElement(name = "idSnomedCT")
    private String id;
    @XmlElement(name = "descripcionSNM")
    private String description;
    @XmlElement(name = "grupo")
    private String group;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
