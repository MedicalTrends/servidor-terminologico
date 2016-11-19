package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-10-14.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "tipoTipoBasico")
@XmlType(name = "TipoTipoBasico")
public class BasicTypeTypeResponse implements Serializable {

    @XmlElement(name="nombreTipo")
    private String typeName;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
