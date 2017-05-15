package cl.minsal.semantikos.modelws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-11-23.
 *
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "peticionRefSets", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionRefSets", namespace = "http://service.ws.semantikos.minsal.cl/")
public class RefSetsRequest extends Request implements Serializable {

    private Boolean includeInstitutions;

    @XmlElement(required = false, defaultValue = "true", name = "incluyeEstablecimientos")
    public Boolean getIncludeInstitutions() {
        return includeInstitutions;
    }
    public void setIncludeInstitutions(Boolean includeInstitutions) {
        this.includeInstitutions = includeInstitutions;
    }

}
