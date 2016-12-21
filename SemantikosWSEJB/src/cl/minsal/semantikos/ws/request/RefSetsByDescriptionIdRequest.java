package cl.minsal.semantikos.ws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Development on 2016-11-23.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "peticionRefSetsPorIdDescripcion", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionRefSetsPorIdDescripcion", namespace = "http://service.ws.semantikos.minsal.cl/")
public class RefSetsByDescriptionIdRequest implements Serializable {

    @XmlElement(required = true, name = "idDescripcion")
    private List<String> descriptionId;
    @XmlElement(required = false, defaultValue = "true", name = "incluyeEstablecimientos")
    private Boolean includeInstitutions;

    public List<String> getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(List<String> descriptionId) {
        this.descriptionId = descriptionId;
    }

    public Boolean getIncludeInstitutions() {
        return includeInstitutions;
    }

    public void setIncludeInstitutions(Boolean includeInstitutions) {
        this.includeInstitutions = includeInstitutions;
    }
}
