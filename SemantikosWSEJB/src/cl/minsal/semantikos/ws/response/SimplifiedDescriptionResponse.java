package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-12-13.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "descripcionPendiente", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "DescripcionPendiente", namespace = "http://service.ws.semantikos.minsal.cl/")
public class SimplifiedDescriptionResponse implements Serializable {

    @XmlElement(name="idConcepto")
    private String conceptId;
    @XmlElement(name="idDescripcion")
    private String descriptionId;
    @XmlElement(name="termino")
    private String term;
    @XmlElement(name="tipoDescripcion")
    private String descriptionType;

    public String getConceptId() {
        return conceptId;
    }

    public void setConceptId(String conceptId) {
        this.conceptId = conceptId;
    }

    public String getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(String descriptionId) {
        this.descriptionId = descriptionId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDescriptionType() {
        return descriptionType;
    }

    public void setDescriptionType(String descriptionType) {
        this.descriptionType = descriptionType;
    }
}
