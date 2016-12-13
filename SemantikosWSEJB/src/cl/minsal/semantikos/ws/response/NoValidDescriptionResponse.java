package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author Andrés Farías on 12/13/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "descripcionPerfectMatch", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "DescripcionPerfectMatch", namespace = "http://service.ws.semantikos.minsal.cl/")
public class NoValidDescriptionResponse implements Serializable {

    @XmlElement(name="razonNoValido")
    private String noValidityCause;
    @XmlElement(name="validez")
    private Boolean valid;
    @XmlElementWrapper(name="idConceptosSugeridos")
    @XmlElement(name="idConceptoSugerido")
    private List<String> suggestedConceptsId;
    @XmlElementWrapper(name="descripcionesSugeridas")
    @XmlElement(name="descripcionSugerida")
    private List<PerfectMatchDescriptionResponse> suggestedDescriptions;
    @XmlElement(name="cantidadRegistros")
    private Integer numberOfEntries;

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

    public List<String> getSuggestedConceptsId() {
        return suggestedConceptsId;
    }

    public void setSuggestedConceptsId(List<String> suggestedConceptsId) {
        this.suggestedConceptsId = suggestedConceptsId;
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
