package cl.minsal.semantikos.ws.modelws.response;

import cl.minsal.semantikos.model.descriptions.Description;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-12-13.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "descripcionSimplificada", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "DescripcionSimplificada", namespace = "http://service.ws.semantikos.minsal.cl/")
public class SimplifiedDescriptionResponse implements Serializable {

    @XmlElement(name="conceptID")
    private String conceptId;
    @XmlElement(name="descriptionID")
    private String descriptionId;
    @XmlElement(name="descriptionEncontrada")
    private String term;
    @XmlElement(name="tipoDescripcion")
    private String descriptionType;

    public SimplifiedDescriptionResponse() {}

    public SimplifiedDescriptionResponse(@NotNull Description description) {
        this.descriptionId = description.getDescriptionId();
        this.term = description.getTerm();
        this.conceptId = description.getConceptSMTK().getConceptID();
        this.descriptionType = description.getDescriptionType().getName();
    }

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
