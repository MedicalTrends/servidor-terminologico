package cl.minsal.semantikos.modelws.response;

import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.snomedct.DescriptionSCT;
import cl.minsal.semantikos.model.snomedct.DescriptionSCTType;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Development on 2016-10-13.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "descripcionSCT", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "DescripcionSCT", namespace = "http://service.ws.semantikos.minsal.cl/")
public class DescriptionSCTResponse implements Serializable, Comparable<DescriptionSCTResponse> {

    @XmlElement(name="descriptionID")
    private long descriptionID;

    @XmlElement(name="tipoDescripcion")
    private String type;

    @XmlElement(name="description")
    private String term;

    @XmlElement(name="esSensibleAMayusculas")
    private Boolean caseSensitive;

    @XmlElement(name="esPreferida")
    private boolean preferredTerm;


    public DescriptionSCTResponse() {
    }

    public DescriptionSCTResponse(DescriptionSCT description) {
        this.descriptionID = description.getId();
        this.type = description.getDescriptionType().getName();
        this.term = description.getTerm();
        this.caseSensitive = description.getCaseSignificanceId() == DescriptionSCT.CASE_SENSITIVE;
        this.preferredTerm = description.isFavourite();
    }


    public long getDescriptionID() {
        return descriptionID;
    }

    public void setDescriptionID(long descriptionID) {
        this.descriptionID = descriptionID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(Boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public Boolean getCaseSensitive() {
        return caseSensitive;
    }

    public boolean isPreferredTerm() {
        return preferredTerm;
    }

    public void setPreferredTerm(boolean preferredTerm) {
        this.preferredTerm = preferredTerm;
    }

    @Override
    public int compareTo(DescriptionSCTResponse o) {
        if ( this.getType() != null ) {
            if ( "preferida".equalsIgnoreCase(this.getType()) ) {
                return -1;
            } else if ( "FSN".equalsIgnoreCase(this.getType()) ) {
                if ( "preferida".equalsIgnoreCase(o.getType()) ) {
                    return 1;
                } else {
                    return -1;
                }
            }
        }
        return 0;
    }
}
