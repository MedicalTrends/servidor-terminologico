package cl.minsal.semantikos.ws.modelws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-11-23.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "peticionConceptosPorRefSet", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionConceptosPorRefSet", namespace = "http://service.ws.semantikos.minsal.cl/")
public class ConceptsByRefsetRequest extends Request implements Serializable {

    @XmlElement(required = true, name = "nombreRefSet")
    private String refSetName;

    public String getRefSetName() {
        return refSetName;
    }

    public void setRefSetName(String refSetName) {
        this.refSetName = refSetName;
    }

}
