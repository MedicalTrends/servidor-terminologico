package cl.minsal.semantikos.modelws.request;

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
    private long descriptionID;

    public long getDescriptionID() {
        return descriptionID;
    }

    public void setDescriptionID(long descriptionID) {
        this.descriptionID = descriptionID;
    }
}
