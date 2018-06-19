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
@XmlRootElement(name = "respuestaConceptIDPorGTIN", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RespuestaConceptIDPorGTIN", namespace = "http://service.ws.semantikos.minsal.cl/")
public class ConceptIDByGTINResponse implements Serializable {

    @XmlElement(name="codeGTIN")
    private int codeGTIN;
    @XmlElement(name="conceptID")
    private String conceptId;
    @XmlElement(name="descriptionPreferida")
    private String preferredTerm;
    @XmlElement(name="descriptionIDPreferida")
    private String preferredTermId;
    @XmlElement(name="nombreCategoria")
    private String categoryName;

    public ConceptIDByGTINResponse() { }

    public ConceptIDByGTINResponse(@NotNull ConceptSMTK conceptSMTK) {
        RelationshipDefinition relationshipDefinition = conceptSMTK.getCategory().findRelationshipDefinitionsByName(TargetDefinition.GTINGS1).get(0);
        List<Relationship> relationshipGS1 = conceptSMTK.getRelationshipsByRelationDefinition(relationshipDefinition);
        if(!relationshipGS1.isEmpty()) {
            BasicTypeValue codeGS1 = (BasicTypeValue) relationshipGS1.get(0).getTarget();
            this.codeGTIN = Integer.parseInt(codeGS1.getValue().toString());
        }
        this.conceptId = conceptSMTK.getConceptID();
        this.categoryName = conceptSMTK.getCategory().getName();
        this.preferredTerm = conceptSMTK.getDescriptionFavorite().getTerm();
        this.preferredTermId = conceptSMTK.getDescriptionFavorite().getDescriptionId();
    }

    public String getConceptId() {
        return conceptId;
    }

    public void setConceptId(String conceptId) {
        this.conceptId = conceptId;
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

    public int getCodeGTIN() {
        return codeGTIN;
    }

    public void setCodeGTIN(int codeGTIN) {
        this.codeGTIN = codeGTIN;
    }
}
