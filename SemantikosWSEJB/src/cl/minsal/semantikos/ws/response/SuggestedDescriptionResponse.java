package cl.minsal.semantikos.ws.response;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.descriptions.Description;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * @author Andrés Farías on 12/13/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "descripcionSugerida", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "DescripcionSugerida", namespace = "http://service.ws.semantikos.minsal.cl/")
public class SuggestedDescriptionResponse implements Serializable {

    @XmlElement(name="conceptID")
    private String conceptId;
    @XmlElement(name="descriptionIDEncontrada")
    private String descriptionId;
    @XmlElement(name="nombreCategoria")
    private String categoryName;
    @XmlElement(name="terminoEncontrado")
    private String term;
    @XmlElement(name="tipoDescripcion")
    private String descriptionType;
    @XmlElement(name="descriptionPreferida")
    private String preferredTerm;
    @XmlElement(name="descriptionIDPreferida")
    private String preferredTermId;
    @XmlElement(name="validez")
    private boolean valid;

    public SuggestedDescriptionResponse() { }

    public SuggestedDescriptionResponse(@NotNull Description description) {
        this.descriptionId = description.getDescriptionId();
        this.term = description.getTerm();
        this.descriptionType = description.getDescriptionType().getName();
        ConceptSMTK conceptSMTK = description.getConceptSMTK();
        this.conceptId = conceptSMTK.getConceptID();
        this.categoryName = conceptSMTK.getCategory().getName();
        this.preferredTerm = conceptSMTK.getDescriptionFavorite().getTerm();
        this.preferredTermId = conceptSMTK.getDescriptionFavorite().getDescriptionId();
        this.valid = description.isValid();
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPreferredTerm() {
        return preferredTerm;
    }

    public void setPreferredTerm(String preferredTerm) {
        this.preferredTerm = preferredTerm;
    }

    public String getPreferredTermId() {
        return preferredTermId;
    }

    public void setPreferredTermId(String preferredTermId) {
        this.preferredTermId = preferredTermId;
    }
}
