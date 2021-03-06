package cl.minsal.semantikos.modelws.request;

import cl.minsal.semantikos.modelws.fault.IllegalInputFault;

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
public class RefSetsByDescriptionIdRequest extends Request implements Serializable {

    private List<String> descriptionId;

    private Boolean includeInstitutions;

    @XmlElement(required = false, defaultValue = "true", name = "incluyeEstablecimientos")
    public Boolean getIncludeInstitutions() {
        return includeInstitutions;
    }
    public void setIncludeInstitutions(Boolean includeInstitutions) {
        this.includeInstitutions = includeInstitutions;
    }

    @XmlElement(required = true, name = "descriptionID")
    public List<String> getDescriptionId() {
        return descriptionId;
    }
    public void setDescriptionId(List<String> descriptionId) {
        this.descriptionId = descriptionId;
    }

    public void validate() throws IllegalInputFault {
        super.validate();
        if (descriptionId == null || descriptionId.isEmpty()) {
            throw new IllegalInputFault("Debe ingresar por lo menos un idDescripcion");
        }

    }

}
