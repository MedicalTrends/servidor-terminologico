package cl.minsal.semantikos.modelws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Esta clase representa una petición de servicio que recibe como argumento una categoría.
 *
 * @author Alonso Cornejo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "peticionConceptoPorConceptID", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionConceptoPorConceptID", namespace = "http://service.ws.semantikos.minsal.cl/")
public class ConceptByConceptIDRequest extends Request implements Serializable {

    @XmlElement(required = true, name = "conceptID")
    private String conceptID;

    public String getConceptID() {
        return conceptID;
    }

    public void setConceptID(String conceptID) {
        this.conceptID = conceptID;
    }
}
