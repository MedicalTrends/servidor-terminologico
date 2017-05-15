package cl.minsal.semantikos.modelws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-10-11.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "tag", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "Tag", namespace = "http://service.ws.semantikos.minsal.cl/")
public class TagResponse implements Serializable {

    @XmlElement(name="nombre")
    private String name;
    private String colorBackground;
    private String colorLetter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
