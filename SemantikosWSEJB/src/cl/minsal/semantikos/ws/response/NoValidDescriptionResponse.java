package cl.minsal.semantikos.ws.response;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.Description;
import cl.minsal.semantikos.model.NoValidDescription;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrés Farías on 12/13/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "descripcionNoValida", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "DescripcionNoValida", namespace = "http://service.ws.semantikos.minsal.cl/")
public class NoValidDescriptionResponse implements Serializable {

    @XmlElement(name="conceptID")
    private String conceptId;
    @XmlElement(name="descriptionID")
    private String descriptionId;
    @XmlElement(name="descriptionEncontrada")
    private String term;
    @XmlElement(name="tipoDescripcion")
    private String descriptionType;
    @XmlElement(name="razonNoValido")
    private String noValidityCause;
    @XmlElement(name="validez")
    private Boolean valid;
    @XmlElementWrapper(name="descripcionesSugeridas")
    @XmlElement(name="descripcionSugerida")
    private List<PerfectMatchDescriptionResponse> suggestedDescriptions;
    //@XmlElement(name="cantidadRegistros")
    //private Integer numberOfEntries;

    public NoValidDescriptionResponse() {}

    public NoValidDescriptionResponse(@NotNull NoValidDescription noValidDescription) {
        this.valid = false;

        this.descriptionId = noValidDescription.getNoValidDescription().getDescriptionId();
        this.conceptId = noValidDescription.getNoValidDescription().getConceptSMTK().getConceptID();
        this.term = noValidDescription.getNoValidDescription().getTerm();
        this.descriptionType = noValidDescription.getNoValidDescription().getDescriptionType().getName();

        if ( noValidDescription.getObservationNoValid() != null ) {
            this.noValidityCause = noValidDescription.getObservationNoValid().getDescription();
        }
        if ( noValidDescription.getSuggestedConcepts() != null ) {
            Integer size = noValidDescription.getSuggestedConcepts().size();
            this.suggestedDescriptions = new ArrayList<>(size);
            for (ConceptSMTK suggestedConcept : noValidDescription.getSuggestedConcepts()) {
                this.suggestedDescriptions.add(new PerfectMatchDescriptionResponse(suggestedConcept.getDescriptionFavorite()));
            }
            //this.numberOfEntries = size;
        } else {
            //this.numberOfEntries = 0;
        }
    }

    public String getNoValidityCause() {
        return noValidityCause;
    }

    public void setNoValidityCause(String noValidityCause) {
        this.noValidityCause = noValidityCause;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public List<PerfectMatchDescriptionResponse> getSuggestedDescriptions() {
        return suggestedDescriptions;
    }

    public void setSuggestedDescriptions(List<PerfectMatchDescriptionResponse> suggestedDescriptions) {
        this.suggestedDescriptions = suggestedDescriptions;
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

    /*
    public Integer getNumberOfEntries() {
        return numberOfEntries;
    }

    public void setNumberOfEntries(Integer numberOfEntries) {
        this.numberOfEntries = numberOfEntries;
    }
    */
}
