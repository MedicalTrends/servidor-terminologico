package cl.minsal.semantikos.modelws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-11-22.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "peticionConceptoRefSet", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionConceptoRefSet", namespace = "http://service.ws.semantikos.minsal.cl/")
public class ConceptToRefSetRequest extends Request implements Serializable {

    @XmlElement(required = true, name = "terminoPropuesto")
    private String term;

    @XmlElement(required = true, name = "nombreRefSet")
    private String refSetName;

    @XmlElement(required = false, name = "conceptID")
    private String conceptID;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getRefSetName() {
        return refSetName;
    }

    public void setRefSetName(String refSetName) {
        this.refSetName = refSetName;
    }

    public String getConceptID() {
        return conceptID;
    }

    public void setConceptID(String conceptID) {
        this.conceptID = conceptID;
    }
}
