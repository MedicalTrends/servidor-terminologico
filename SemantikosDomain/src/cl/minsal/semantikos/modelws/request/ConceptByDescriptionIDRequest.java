package cl.minsal.semantikos.modelws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Esta clase representa una petición de servicio que recibe como argumento una categoría.
 *
 * @author Alonso Cornejo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "peticionConceptoPorDescriptionID", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionConceptoPorDescriptionID", namespace = "http://service.ws.semantikos.minsal.cl/")
public class ConceptByDescriptionIDRequest extends Request implements Serializable {

    @XmlElement(required = true, name = "descriptionID")
    private String descriptionID;

    public String getDescriptionID() {
        return descriptionID;
    }

    public void setDescriptionID(String descriptionID) {
        this.descriptionID = descriptionID;
    }
}
