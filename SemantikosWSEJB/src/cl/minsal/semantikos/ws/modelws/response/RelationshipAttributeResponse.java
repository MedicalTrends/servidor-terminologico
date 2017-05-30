package cl.minsal.semantikos.ws.modelws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-10-14.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "atributoRelacion", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "AtributoRelacion", namespace = "http://service.ws.semantikos.minsal.cl/")
public class RelationshipAttributeResponse implements Serializable {

    @XmlElement(name="objetivo")
    private TargetResponse target;
    @XmlElement(name="definicionAtributoRelacion")
    private RelationshipAttributeDefinitionResponse relationshipAttributeDefinition;
    @XmlElement(name="relacion")
    private RelationshipResponse relationship;
    @XmlElement(name="tipo")
    private String type;
    @XmlElement(name="nombre")
    private String name;
    @XmlElement(name="valor")
    private String value;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
