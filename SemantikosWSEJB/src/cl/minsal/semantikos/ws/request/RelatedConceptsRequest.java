package cl.minsal.semantikos.ws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-11-23.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "peticionConceptosRelacionados", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionConceptosRelacionados", namespace = "http://service.ws.semantikos.minsal.cl/")
public class RelatedConceptsRequest extends Request implements Serializable {

    @XmlElement(required = false, name = "idConcepto")
    private String conceptId;
    @XmlElement(required = false, name = "idDescripcion")
    private String descriptionId;

    public String getConceptId() {
        return conceptId;
    }

    public void setConceptId(String conceptId) {
        this.conceptId = conceptId;
    }

    public String getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(String descriptionId) {
        this.descriptionId = descriptionId;
    }

}
