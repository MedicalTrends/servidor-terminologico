package cl.minsal.semantikos.modelweb;

import cl.minsal.semantikos.model.relationships.Multiplicity;
import cl.minsal.semantikos.model.relationships.RelationshipAttributeDefinition;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetDefinition;

import java.io.Serializable;

/**
 * Created by des01c7 on 04-01-17.
 */
public class RelationshipAttributeDefinitionWeb extends RelationshipAttributeDefinition implements Comparable<RelationshipAttributeDefinitionWeb>, Serializable {

    /** El identificador del composite que se quiere usar en las vistas */
    private long compositeID;

    /** Establece el orden o posición */
    private int order;

    /** Establece el valor por defecto para esta definición */
    private Target defaultValue;

    private RelationshipAttributeDefinition relationshipAttributeDefinition;

    public RelationshipAttributeDefinitionWeb(long id, TargetDefinition target, String name, Multiplicity multiplicity, long compositeID, int order, RelationshipAttributeDefinition relationshipAttributeDefinition) {
        super(id, target,name, multiplicity);
        this.order =order;
        this.compositeID=compositeID;
        this.relationshipAttributeDefinition= relationshipAttributeDefinition;
    }

    public long getCompositeID() {
        return compositeID;
    }

    public void setCompositeID(long compositeID) {
        this.compositeID = compositeID;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public RelationshipAttributeDefinition getRelationshipAttributeDefinition() {
        return relationshipAttributeDefinition;
    }

    public void setRelationshipAttributeDefinition(RelationshipAttributeDefinition relationshipAttributeDefinition) {
        this.relationshipAttributeDefinition = relationshipAttributeDefinition;
    }

    public Target getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Target defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public int compareTo(RelationshipAttributeDefinitionWeb o) {
        return this.getOrder() - o.getOrder();
    }
}
