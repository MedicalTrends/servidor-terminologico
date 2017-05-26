package cl.minsal.semantikos.model.queries;

import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 21-03-17.
 */
public class RelationshipDTO implements Serializable {

    /**
     * Filtros estáticos
     */
    private long id;

    private String name;

    private TargetDTO target;

    private List<RelationshipAttributeDTO> relationshipAttributes = new ArrayList<>();

    public RelationshipDTO() {
    }

    public RelationshipDTO(Relationship relationship) {
        this.id = relationship.getId();
        this.name = relationship.getRelationshipDefinition().getName();
        this.target = new TargetDTO(relationship.getTarget());
        for (RelationshipAttribute relationshipAttribute : relationship.getRelationshipAttributes()) {
            this.relationshipAttributes.add(new RelationshipAttributeDTO(relationshipAttribute));
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TargetDTO getTarget() {
        return target;
    }

    public void setTarget(TargetDTO target) {
        this.target = target;
    }

    public List<RelationshipAttributeDTO> getRelationshipAttributes() {
        return relationshipAttributes;
    }

    public void setRelationshipAttributes(List<RelationshipAttributeDTO> relationshipAttributes) {
        this.relationshipAttributes = relationshipAttributes;
    }

    public List<RelationshipAttributeDTO> getAttributesDTOByName(String name) {
        List<RelationshipAttributeDTO> someAttributes = new ArrayList<>();
        for (RelationshipAttributeDTO attribute : relationshipAttributes) {
            if (attribute.getName().equals(name)) {
                someAttributes.add(attribute);
            }
        }
        return someAttributes;
    }
}
