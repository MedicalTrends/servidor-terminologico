package cl.minsal.semantikos.model.relationships;

import cl.minsal.semantikos.model.PersistentEntity;

import javax.persistence.Basic;
import javax.persistence.Column;

/**
 * Created by root on 08-07-16.
 */
// TODO: Normalizar esta clase
public class RelationshipAttribute {

    private Long idRelationshipAttribute;
    private RelationshipAttributeDefinition relationAttributeDefinition;
    private Relationship Relationship;
    private Target target;

    public RelationshipAttribute() {
    }

    public RelationshipAttribute(RelationshipAttributeDefinition relationAttributeDefinition, Relationship relationship, Target target) {
        this.relationAttributeDefinition = relationAttributeDefinition;
        Relationship = relationship;
        //this.target = target;
        if(target != null)
            this.setTarget(target.copy());
    }

    public RelationshipAttribute(Long idRelationshipAttribute, RelationshipAttributeDefinition relationAttributeDefinition, Relationship relationship, Target target) {
        this(relationAttributeDefinition,relationship, target);
        this.idRelationshipAttribute = idRelationshipAttribute;
    }

    public Long getIdRelationshipAttribute() {
        return idRelationshipAttribute;
    }

    public void setIdRelationshipAttribute(Long idRelationshipAttribute) {
        this.idRelationshipAttribute = idRelationshipAttribute;
    }

    public RelationshipAttributeDefinition getRelationAttributeDefinition() {
        return relationAttributeDefinition;
    }

    public void setRelationAttributeDefinition(RelationshipAttributeDefinition relationAttributeDefinition) {
        this.relationAttributeDefinition = relationAttributeDefinition;
    }

    public cl.minsal.semantikos.model.relationships.Relationship getRelationship() {
        return Relationship;
    }

    public void setRelationship(cl.minsal.semantikos.model.relationships.Relationship relationship) {
        Relationship = relationship;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RelationshipAttribute that = (RelationshipAttribute) o;

        /*
        if (idRelationshipAttribute != null ? !idRelationshipAttribute.equals(that.idRelationshipAttribute) : that.idRelationshipAttribute != null)
            return false;
        */

        return this.getRelationAttributeDefinition().equals(that.getRelationAttributeDefinition()) && this.getTarget().equals(that.getTarget());

        //return true;
    }

    @Override
    public int hashCode() {
        int result = idRelationshipAttribute != null ? idRelationshipAttribute.hashCode() : 0;
        return result;
    }
}
