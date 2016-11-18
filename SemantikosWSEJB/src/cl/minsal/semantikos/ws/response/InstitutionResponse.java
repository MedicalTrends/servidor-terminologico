package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Development on 2016-10-13.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "institucion")
public class InstitutionResponse implements Serializable {

    @XmlElement(name="nombre")
    private String name;
    @XmlElementWrapper(name = "administradores")
    @XmlElement(name="usuario")
    private List<UserResponse> administrators;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserResponse> getAdministrators() {
        return administrators;
    }

    public void setAdministrators(List<UserResponse> administrators) {
        this.administrators = administrators;
    }
}
