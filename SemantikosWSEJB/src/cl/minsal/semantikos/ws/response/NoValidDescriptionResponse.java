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

    @XmlElement(name="razonNoValido")
    private String noValidityCause;
    @XmlElement(name="validez")
    private Boolean valid;
    @XmlElementWrapper(name="descripcionesSugeridas")
    @XmlElement(name="descripcionSugerida")
    private List<PerfectMatchDescriptionResponse> suggestedDescriptions;
    @XmlElement(name="cantidadRegistros")
    private Integer numberOfEntries;

    public NoValidDescriptionResponse() {}

    public NoValidDescriptionResponse(@NotNull NoValidDescription noValidDescription) {
        this.valid = false;
        if ( noValidDescription.getObservationNoValid() != null ) {
            this.noValidityCause = noValidDescription.getObservationNoValid().getDescription();
        }
        if ( noValidDescription.getSuggestedConcepts() != null ) {
            Integer size = noValidDescription.getSuggestedConcepts().size();
            this.suggestedDescriptions = new ArrayList<>(size);
            for (ConceptSMTK suggestedConcept : noValidDescription.getSuggestedConcepts()) {
                this.suggestedDescriptions.add(new PerfectMatchDescriptionResponse(suggestedConcept.getDescriptionFavorite()));
            }
            this.numberOfEntries = size;
        } else {
            this.numberOfEntries = 0;
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

    public Integer getNumberOfEntries() {
        return numberOfEntries;
    }

    public void setNumberOfEntries(Integer numberOfEntries) {
        this.numberOfEntries = numberOfEntries;
    }
}
