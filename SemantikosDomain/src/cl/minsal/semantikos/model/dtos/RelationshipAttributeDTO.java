package cl.minsal.semantikos.model.dtos;

import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttributeDefinition;
import cl.minsal.semantikos.model.relationships.Target;

import java.io.Serializable;

/**
 * Created by root on 08-07-16.
 */
// TODO: Normalizar esta clase
public class RelationshipAttributeDTO implements Serializable {

    private Long id;
    private long relationAttributeDefinitionId;
    private long relationshipId;
    private TargetDTO targetDTO;

    public RelationshipAttributeDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getRelationAttributeDefinitionId() {
        return relationAttributeDefinitionId;
    }

    public void setRelationAttributeDefinitionId(long relationAttributeDefinitionId) {
        this.relationAttributeDefinitionId = relationAttributeDefinitionId;
    }

    public long getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(long relationshipId) {
        this.relationshipId = relationshipId;
    }

    public TargetDTO getTargetDTO() {
        return targetDTO;
    }

    public void setTargetDTO(TargetDTO targetDTO) {
        this.targetDTO = targetDTO;
    }

}
