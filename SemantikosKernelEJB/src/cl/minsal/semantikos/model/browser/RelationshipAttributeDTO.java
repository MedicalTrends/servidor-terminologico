package cl.minsal.semantikos.model.browser;

import cl.minsal.semantikos.model.relationships.RelationshipAttribute;

import java.io.Serializable;

/**
 * Created by root on 21-03-17.
 */
public class RelationshipAttributeDTO implements Serializable {

    /**
     * Filtros estáticos
     */
    private long id;

    private String name;

    private TargetDTO target;

    public RelationshipAttributeDTO() {
    }

    public RelationshipAttributeDTO(RelationshipAttribute relationshipAttribute) {
        this.id = relationshipAttribute.getIdRelationshipAttribute();
        this.name = relationshipAttribute.getRelationAttributeDefinition().getName();
        this.target = new TargetDTO(relationshipAttribute.getTarget());
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
}
