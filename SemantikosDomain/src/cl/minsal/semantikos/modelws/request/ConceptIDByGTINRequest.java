package cl.minsal.semantikos.modelws.request;

import cl.minsal.semantikos.modelws.fault.IllegalInputFault;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-11-22.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "peticionConceptIDPorGTIN", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionConceptIDPorGTIN", namespace = "http://service.ws.semantikos.minsal.cl/")
public class ConceptIDByGTINRequest extends Request implements Serializable {

    @XmlElement(required = true, name = "GTIN")
    private int GTIN;

    public int getGTIN() {
        return GTIN;
    }

    public void setGTIN(int GTIN) {
        this.GTIN = GTIN;
    }

    public void validate() throws IllegalInputFault {
        super.validate();
        if (getGTIN() == 0 || "".equals(String.valueOf(getGTIN()))) {
            throw new IllegalInputFault("Debe indicar número GTIN");
        }
    };
}
