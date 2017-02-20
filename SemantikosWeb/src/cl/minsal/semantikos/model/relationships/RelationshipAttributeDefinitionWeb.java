package cl.minsal.semantikos.model.relationships;

import cl.minsal.semantikos.model.Multiplicity;
import cl.minsal.semantikos.model.RelationshipWeb;

/**
 * Created by des01c7 on 04-01-17.
 */
public class RelationshipAttributeDefinitionWeb extends RelationshipAttributeDefinition implements Comparable<RelationshipAttributeDefinitionWeb> {

    /** El identificador del composite que se quiere usar en las vistas */
    private long compositeID;

    /** Establece el orden o posición */
    private int order;

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

    @Override
    public int compareTo(RelationshipAttributeDefinitionWeb o) {
        return this.getOrder() - o.getOrder();
    }
}
