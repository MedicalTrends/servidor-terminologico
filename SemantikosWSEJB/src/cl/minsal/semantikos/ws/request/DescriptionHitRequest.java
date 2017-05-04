package cl.minsal.semantikos.ws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-11-22.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "peticionHitDescripcion", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionHitDescripcion", namespace = "http://service.ws.semantikos.minsal.cl/")
public class DescriptionHitRequest implements Serializable {

    /** Identificador de negocio de una institución asociada al usuario que realiza la solicitud de creación */
    @XmlElement(required = true, name = "idEstablecimiento")
    private String idInstitution;

    @XmlElement(required = true, name = "descriptionID")
    private String descriptionID;

    public String getDescriptionID() {
        return descriptionID;
    }

    public void setDescriptionID(String descriptionID) {
        this.descriptionID = descriptionID;
    }

    public String getIdInstitution() {
        return idInstitution;
    }

    public void setIdInstitution(String idInstitution) {
        this.idInstitution = idInstitution;
    }

}
