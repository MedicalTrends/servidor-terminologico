package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Development on 2016-10-14.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "relacion")
public class RelationshipResponse implements Serializable {

    @XmlElement(name = "definicion")
    private RelationshipDefinitionResponse relationshipDefinition;
    @XmlElement(name = "objetivo")
    private TargetResponse target;
    @XmlElement(name = "concepto")
    private ConceptResponse concept;
    @XmlElementWrapper(name = "atributos")
    @XmlElement(name = "atributo")
    private List<RelationshipAttributeResponse> relationshipAttribute;

    public TargetResponse getTarget() {
        return target;
    }

    public void setTarget(TargetResponse target) {
        this.target = target;
    }

    public RelationshipDefinitionResponse getRelationshipDefinition() {
        return relationshipDefinition;
    }

    public void setRelationshipDefinition(RelationshipDefinitionResponse relationshipDefinition) {
        this.relationshipDefinition = relationshipDefinition;
    }

    public ConceptResponse getConcept() {
        return concept;
    }

    public void setConcept(ConceptResponse concept) {
        this.concept = concept;
    }

    public List<RelationshipAttributeResponse> getRelationshipAttribute() {
        return relationshipAttribute;
    }

    public void setRelationshipAttribute(List<RelationshipAttributeResponse> relationshipAttribute) {
        this.relationshipAttribute = relationshipAttribute;
    }
}
