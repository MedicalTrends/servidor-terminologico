package cl.minsal.semantikos.modelws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

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
    private long conceptID;

    public long getConceptID() {
        return conceptID;
    }

    public void setConceptID(long conceptID) {
        this.conceptID = conceptID;
    }
}
