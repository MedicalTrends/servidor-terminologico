package cl.minsal.semantikos.modelws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-11-22.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "peticionGS1PorConceptID", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionGS1PorConceptID", namespace = "http://service.ws.semantikos.minsal.cl/")
public class GS1ByConceptIDRequest extends Request implements Serializable {

    @XmlElement(required = true, name = "conceptID")
    private String conceptID;

    @XmlElement(required = true, name = "descriptionID")
    private String descriptionID;

    public String getConceptID() {
        return conceptID;
    }

    public void setConceptID(String conceptID) {
        this.conceptID = conceptID;
    }

    public String getDescriptionID() {
        return descriptionID;
    }

    public void setDescriptionID(String descriptionID) {
        this.descriptionID = descriptionID;
    }
}
