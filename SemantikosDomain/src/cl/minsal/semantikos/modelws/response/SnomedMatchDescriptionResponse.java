package cl.minsal.semantikos.modelws.response;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.snomedct.DescriptionSCT;
import cl.minsal.semantikos.model.snomedct.DescriptionSCTType;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * @author Andrés Farías on 12/13/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "descripcionSnomedEncontrada", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "DescripcionSnomedEncontrada", namespace = "http://service.ws.semantikos.minsal.cl/")
public class SnomedMatchDescriptionResponse implements Serializable {

    @XmlElement(name="conceptID")
    private long conceptId;
    @XmlElement(name="descriptionID")
    private long descriptionId;
    @XmlElement(name="descriptionEncontrada")
    private String term;
    @XmlElement(name="tipoDescripcion")
    private String descriptionType;
    @XmlElement(name="preferida")
    private boolean preferredTerm;
    @XmlElement(name="descriptionFSN")
    private String termFSN;
    @XmlElement(name="descriptionIDFSN")
    private long termFSNId;

    public SnomedMatchDescriptionResponse() { }

    public SnomedMatchDescriptionResponse(@NotNull DescriptionSCT description, ConceptSCT conceptSCT) {
        this.descriptionId = description.getId();
        this.term = description.getTerm();
        this.descriptionType = description.getDescriptionType().getName();
        this.preferredTerm = description.isFavourite();
        this.conceptId = conceptSCT.getId();
        this.termFSN = conceptSCT.getDescriptionFSN().getTerm();
        this.termFSNId = conceptSCT.getDescriptionFSN().getId();
    }

    public long getConceptId() {
        return conceptId;
    }

    public void setConceptId(long conceptId) {
        this.conceptId = conceptId;
    }

    public long getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(long descriptionId) {
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

    public boolean isPreferredTerm() {
        return preferredTerm;
    }

    public void setPreferredTerm(boolean preferredTerm) {
        this.preferredTerm = preferredTerm;
    }

    public String getTermFSN() {
        return termFSN;
    }

    public void setTermFSN(String termFSN) {
        this.termFSN = termFSN;
    }

    public long getTermFSNId() {
        return termFSNId;
    }

    public void setTermFSNId(long termFSNId) {
        this.termFSNId = termFSNId;
    }
}
