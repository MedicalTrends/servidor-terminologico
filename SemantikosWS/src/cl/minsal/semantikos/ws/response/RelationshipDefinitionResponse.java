package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Development on 2016-10-14.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "definicionRelacion")
public class RelationshipDefinitionResponse implements Serializable {

    @XmlElement(name="name")
    private String name;
    @XmlElement(name="description")
    private String description;
    @XmlElement(name="multiplicidad")
    private MultiplicityResponse multiplicity;
    @XmlElement(name="definicionObjetivo")
    private TargetDefinitionResponse targetDefinition;
    @XmlElement(name="definicionRelacionExcluida")
    private RelationshipDefinitionResponse excludes;
    @XmlElementWrapper(name = "definicionesAtributoRelacion")
    @XmlElement(name="definicionAtributoRelacion")
    private List<RelationshipAttributeDefinitionResponse> relationshipAttributeDefinitionResponses;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultiplicityResponse getMultiplicity() {
        return multiplicity;
    }

    public void setMultiplicity(MultiplicityResponse multiplicity) {
        this.multiplicity = multiplicity;
    }

    public TargetDefinitionResponse getTargetDefinition() {
        return targetDefinition;
    }

    public void setTargetDefinition(TargetDefinitionResponse targetDefinition) {
        this.targetDefinition = targetDefinition;
    }

    public RelationshipDefinitionResponse getExcludes() {
        return excludes;
    }

    public void setExcludes(RelationshipDefinitionResponse excludes) {
        this.excludes = excludes;
    }

    public List<RelationshipAttributeDefinitionResponse> getRelationshipAttributeDefinitionResponses() {
        return relationshipAttributeDefinitionResponses;
    }

    public void setRelationshipAttributeDefinitionResponses(List<RelationshipAttributeDefinitionResponse> relationshipAttributeDefinitionResponses) {
        this.relationshipAttributeDefinitionResponses = relationshipAttributeDefinitionResponses;
    }
}
