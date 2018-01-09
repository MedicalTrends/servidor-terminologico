package cl.minsal.semantikos.modelws.response;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.TargetDefinition;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author Andrés Farías on 12/13/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "respuestaGTINPorConceptID", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RespuestaGTINPorConceptID", namespace = "http://service.ws.semantikos.minsal.cl/")
public class GTINByConceptIDResponse implements Serializable {

    @XmlElement(name="conceptID")
    private String conceptID;
    @XmlElement(name="descriptionPreferida")
    private String preferredTerm;
    @XmlElement(name="descriptionIDPreferida")
    private String preferredTermId;
    @XmlElement(name="nombreCategoria")
    private String categoryName;
    @XmlElement(name="codeGTIN")
    private int codeGTIN;

    public GTINByConceptIDResponse() { }

    public GTINByConceptIDResponse(@NotNull ConceptSMTK conceptSMTK) {
        RelationshipDefinition relationshipDefinition = conceptSMTK.getCategory().findRelationshipDefinitionsByName(TargetDefinition.GTINGS1).get(0);
        List<Relationship> relationshipGS1 = conceptSMTK.getRelationshipsByRelationDefinition(relationshipDefinition);
        if(!relationshipGS1.isEmpty()) {
            BasicTypeValue codeGS1 = (BasicTypeValue) relationshipGS1.get(0).getTarget();
            this.codeGTIN = (int) codeGS1.getValue();
        }
        this.conceptID = conceptSMTK.getConceptID();
        this.preferredTermId = conceptSMTK.getDescriptionFavorite().getDescriptionId();
        this.categoryName = conceptSMTK.getCategory().getName();
        this.preferredTerm = conceptSMTK.getDescriptionFavorite().getTerm();
    }

    public String getConceptID() {
        return conceptID;
    }

    public void setConceptID(String conceptID) {
        this.conceptID = conceptID;
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

    public int getCodeGTIN() {
        return codeGTIN;
    }

    public void setCodeGTIN(int codeGTIN) {
        this.codeGTIN = codeGTIN;
    }

    public String getPreferredTermId() {
        return preferredTermId;
    }

    public void setPreferredTermId(String preferredTermId) {
        this.preferredTermId = preferredTermId;
    }

}
