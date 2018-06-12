package cl.minsal.semantikos.modelws.request;

import cl.minsal.semantikos.modelws.fault.IllegalInputFault;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-11-22.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "peticionGTINPorConceptID", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionGTINPorConceptID", namespace = "http://service.ws.semantikos.minsal.cl/")
public class GTINByConceptIDRequest extends Request implements Serializable {

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

    public void validate() throws IllegalInputFault {
        super.validate();
        if ((getConceptID() == null || "".equals(getConceptID())) && (getDescriptionID() == null || "".equals(getDescriptionID()))) {
            throw new IllegalInputFault("Debe indicar por lo menos un idConcepto o idDescripcion");
        }
    };
}
