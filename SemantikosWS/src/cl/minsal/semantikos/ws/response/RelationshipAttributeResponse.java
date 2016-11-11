package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Development on 2016-10-14.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "relacionAtributo")
public class RelationshipAttributeResponse implements Serializable {

    @XmlElement(name="objetivo")
    private TargetResponse target;
    @XmlElement(name="definicionAtributoRelacion")
    private RelationshipAttributeDefinitionResponse relationshipAttributeDefinition;
    @XmlElement(name="relacion")
    private RelationshipResponse relationship;

    public TargetResponse getTarget() {
        return target;
    }

    public void setTarget(TargetResponse target) {
        this.target = target;
    }

    public RelationshipAttributeDefinitionResponse getRelationshipAttributeDefinition() {
        return relationshipAttributeDefinition;
    }

    public void setRelationshipAttributeDefinition(RelationshipAttributeDefinitionResponse relationshipAttributeDefinition) {
        this.relationshipAttributeDefinition = relationshipAttributeDefinition;
    }

    public RelationshipResponse getRelationship() {
        return relationship;
    }

    public void setRelationship(RelationshipResponse relationship) {
        this.relationship = relationship;
    }
}
