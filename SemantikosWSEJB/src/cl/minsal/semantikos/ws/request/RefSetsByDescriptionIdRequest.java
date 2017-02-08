package cl.minsal.semantikos.ws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Development on 2016-11-23.
 *
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "peticionRefSetsPorIdDescripcion", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionRefSetsPorIdDescripcion", namespace = "http://service.ws.semantikos.minsal.cl/")
public class RefSetsByDescriptionIdRequest implements Serializable {

    private String descriptionId;

    private Boolean includeInstitutions;

    private String idStablishment;

    @XmlElement(required = false, defaultValue = "true", name = "incluyeEstablecimientos")
    public Boolean getIncludeInstitutions() {
        return includeInstitutions;
    }
    public void setIncludeInstitutions(Boolean includeInstitutions) {
        this.includeInstitutions = includeInstitutions;
    }

    @XmlElement(required = true, name = "idDescripcion")
    public String getDescriptionId() {
        return descriptionId;
    }
    public void setDescriptionId(String descriptionId) {
        this.descriptionId = descriptionId;
    }

    @XmlElement(required = true, name = "idStablishment")
    public String getIdStablishment() {
        return idStablishment;
    }
    public void setIdStablishment(String idStablishment) {
        this.idStablishment = idStablishment;
    }
}
