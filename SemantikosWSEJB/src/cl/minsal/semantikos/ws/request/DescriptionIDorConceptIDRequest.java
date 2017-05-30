package cl.minsal.semantikos.ws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * @author Andrés Farías on 2016-11-23.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "peticionObtenerCrossmaps", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionObtenerCrossmaps", namespace = "http://service.ws.semantikos.minsal.cl/")
public class DescriptionIDorConceptIDRequest extends Request implements Serializable {

    @XmlElement(required = true, name = "descriptionID")
    private String descriptionId;

    @XmlElement(required = true, name = "conceptID")
    private String conceptId;

    public String getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(String descriptionId) {
        this.descriptionId = descriptionId;
    }

    public String getConceptId() {
        return conceptId;
    }

    public void setConceptId(String conceptId) {
        this.conceptId = conceptId;
    }
}
