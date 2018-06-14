package cl.minsal.semantikos.modelws.request;

import cl.minsal.semantikos.modelws.fault.IllegalInputFault;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.text.ParseException;

/**
 * Esta clase representa una petición de servicio que recibe como argumento una categoría.
 *
 * @author Alonso Cornejo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "peticionConceptoSCTPorConceptID", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionConceptoSCTPorConceptID", namespace = "http://service.ws.semantikos.minsal.cl/")
public class ConceptSCTByConceptIDRequest extends Request implements Serializable {

    @XmlElement(required = true, name = "conceptID")
    private String conceptID;

    public String getConceptID() {
        return conceptID;
    }

    public void setConceptID(String conceptID) {
        this.conceptID = conceptID;
    }

    public void validate() throws IllegalInputFault {
        super.validate();
        if(getConceptID() == null || getConceptID().isEmpty()) {
            throw new IllegalInputFault("Debe ingresar un valor para el parámetro ConceptID");
        }
        try {
            Long.parseLong(getConceptID());
        }
        catch (NumberFormatException e) {
            throw new IllegalInputFault("Debe ingresar un valor numérico para el parámetro ConceptID");
        }
    }
}
