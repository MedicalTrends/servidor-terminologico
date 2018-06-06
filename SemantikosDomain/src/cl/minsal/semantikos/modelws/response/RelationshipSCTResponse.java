package cl.minsal.semantikos.modelws.response;

import cl.minsal.semantikos.model.snomedct.DescriptionSCT;
import cl.minsal.semantikos.model.snomedct.RelationshipSCT;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Development on 2016-10-14.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "relacionSCT", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RelacionSCT", namespace = "http://service.ws.semantikos.minsal.cl/")
public class RelationshipSCTResponse implements Serializable {

    @XmlElement(name="tipo")
    private String type;

    @XmlElement(name="conceptID")
    private long conceptID;

    @XmlElement(name="FSN")

    private String FSN;

    @XmlElement(name="grupo")
    private long group;

    public RelationshipSCTResponse() {
    }

    public RelationshipSCTResponse(RelationshipSCT relationshipSCT) {

        this.conceptID = relationshipSCT.getDestinationConcept().getId();
        DescriptionSCT descriptionSCT = relationshipSCT.getTypeConcept().getDescriptionFavouriteSynonymous();
        this.type = descriptionSCT!=null?descriptionSCT.getTerm():relationshipSCT.getTypeConcept().getDescriptionFSN().getTerm();
        this.FSN = relationshipSCT.getDestinationConcept().getDescriptionFSN().getTerm();
        this.group = relationshipSCT.getRelationshipGroup();

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getConceptID() {
        return conceptID;
    }

    public void setConceptID(long conceptID) {
        this.conceptID = conceptID;
    }

    public String getFSN() {
        return FSN;
    }

    public void setFSN(String FSN) {
        this.FSN = FSN;
    }

    public long getGroup() {
        return group;
    }

    public void setGroup(long group) {
        this.group = group;
    }

}
