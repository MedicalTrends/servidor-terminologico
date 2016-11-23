package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-10-11.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "tagSMTK", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "TagSMTK", namespace = "http://service.ws.semantikos.minsal.cl/")
public class TagSMTKResponse implements Serializable {

    @XmlElement(name="nombre")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
