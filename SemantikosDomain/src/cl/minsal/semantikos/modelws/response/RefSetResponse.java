package cl.minsal.semantikos.modelws.response;

import cl.minsal.semantikos.model.refsets.RefSet;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Development on 2016-10-13.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "refSet", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RefSet", namespace = "http://service.ws.semantikos.minsal.cl/")
public class RefSetResponse implements Serializable {

    @XmlElement(name="nombreRefSet")
    private String name;

    @XmlElement(name="valido")
    private Boolean isValid;

    @XmlElement(name="validoHasta")
    private Date validityUntil;

    @XmlElement(name="fechaCreacionRefSet")
    private Date creationDate;

    @XmlElement(name="duenoRefSet")
    private String institution;

    @XmlElementWrapper(name = "conceptos")
    @XmlElement(name="concepto")
    private List<ConceptResponse> concepts;

    @XmlElement(name = "cantidadRegistros")
    private Integer quantity;

    public RefSetResponse() {
        concepts = new ArrayList<>();
    }

    public RefSetResponse(RefSet refSet) {
        this.name = refSet.getName();
        this.isValid = refSet.isValid();
        this.validityUntil = refSet.getValidityUntil();
        this.creationDate = refSet.getCreationDate();
        this.institution = refSet.getInstitution().getName();
        //this.concepts = new ArrayList<>();
        this.concepts = null;
        this.quantity = null;
        /*
        for (ConceptSMTK conceptSMTK : refSet.getConcepts()) {
            this.concepts.add(new ConceptResponse(conceptSMTK));
        }
        */
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

    public Date getValidityUntil() {
        return validityUntil;
    }

    public void setValidityUntil(Date validityUntil) {
        this.validityUntil = validityUntil;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<ConceptResponse> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<ConceptResponse> concepts) {
        this.concepts = concepts;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
