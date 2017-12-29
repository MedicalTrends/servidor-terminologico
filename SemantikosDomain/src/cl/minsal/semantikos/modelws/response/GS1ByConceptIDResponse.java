package cl.minsal.semantikos.modelws.response;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author Andrés Farías on 12/13/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "GS1ByConceptID", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "GS1ByConceptID", namespace = "http://service.ws.semantikos.minsal.cl/")
public class GS1ByConceptIDResponse implements Serializable {

    @XmlElement(name="codeGS1")
    private int codeGS1;
    @XmlElement(name="descriptionPreferida")
    private String preferredTerm;
    @XmlElement(name="descriptionIDPreferida")
    private String preferredTermId;
    @XmlElement(name="nombreCategoria")
    private String categoryName;

    public GS1ByConceptIDResponse() { }

    public GS1ByConceptIDResponse(@NotNull ConceptSMTK conceptSMTK) {
        RelationshipDefinition relationshipDefinition = conceptSMTK.getCategory().findRelationshipDefinitionsByName("Número GS1 GTIN").get(0);
        List<Relationship> relationshipGS1 = conceptSMTK.getRelationshipsByRelationDefinition(relationshipDefinition);
        if(!relationshipGS1.isEmpty()) {
            BasicTypeValue codeGS1 = (BasicTypeValue) relationshipGS1.get(0).getTarget();
            this.codeGS1 = (int) codeGS1.getValue();
        }
        this.preferredTermId = conceptSMTK.getDescriptionFavorite().getDescriptionId();
        this.categoryName = conceptSMTK.getCategory().getName();
        this.preferredTerm = conceptSMTK.getDescriptionFavorite().getTerm();
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

    public int getCodeGS1() {
        return codeGS1;
    }

    public void setCodeGS1(int codeGS1) {
        this.codeGS1 = codeGS1;
    }

    public String getPreferredTermId() {
        return preferredTermId;
    }

    public void setPreferredTermId(String preferredTermId) {
        this.preferredTermId = preferredTermId;
    }


}
