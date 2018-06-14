package cl.minsal.semantikos.modelws.request;

import cl.minsal.semantikos.modelws.fault.IllegalInputFault;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Esta clase representa una petición de servicio que recibe como argumento una categoría.
 *
 * @author Alonso Cornejo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "peticionConceptoSCTPorDescriptionID", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionConceptoSCTPorDescriptionID", namespace = "http://service.ws.semantikos.minsal.cl/")
public class ConceptSCTByDescriptionIDRequest extends Request implements Serializable {

    @XmlElement(required = true, name = "descriptionID")
    private String descriptionID;

    public String getDescriptionID() {
        return descriptionID;
    }

    public void setDescriptionID(String descriptionID) {
        this.descriptionID = descriptionID;
    }

    public void validate() throws IllegalInputFault {
        super.validate();
        if(getDescriptionID() == null || getDescriptionID().isEmpty()) {
            throw new IllegalInputFault("Debe ingresar un valor para el parámetro DescriptionID");
        }
        try {
            Long.parseLong(getDescriptionID());
        }
        catch (NumberFormatException e) {
            throw new IllegalInputFault("Debe ingresar un valor numérico para el parámetro DescriptionID");
        }
    }
}
