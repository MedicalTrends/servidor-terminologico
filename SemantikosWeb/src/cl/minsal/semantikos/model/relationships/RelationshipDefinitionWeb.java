package cl.minsal.semantikos.model.relationships;

import cl.minsal.semantikos.model.Multiplicity;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

/**
 * @author Andrés Farías on 10/5/16.
 */
public class RelationshipDefinitionWeb extends RelationshipDefinition implements Comparable<RelationshipDefinitionWeb> {

    /** El identificador del composite que se quiere usar en las vistas */
    private long compositeID;

    /** Establece el orden o posición */
    private int order;

    /** Establece el estilo para el estado de error */
    private boolean isMultiplicitySatisfied = true;

    /** Lista de Relationship Attribute Web **/
    private List<RelationshipAttributeDefinitionWeb> relationshipAttributeDefinitionWebs;

    /** Establece el valor por defecto para esta definición */
    private Target defaultValue;

    public RelationshipDefinitionWeb(long id, String name, String description, TargetDefinition targetDefinition, Multiplicity multiplicity, long compositeID, int order) {
        super(id, name, description, targetDefinition, multiplicity);
        this.compositeID = compositeID;
        this.order = order;
    }

    public long getCompositeID() {
        return compositeID;
    }

    public int getOrder() {
        return order;
    }


    public Target getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Target defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean hasDefaultValue(){
        return getDefaultValue()!=null;
    }

    public boolean isMultiplicitySatisfied() {
        return isMultiplicitySatisfied;
    }

    public void setMultiplicitySatisfied(boolean multiplicitySatisfied) {
        isMultiplicitySatisfied = multiplicitySatisfied;
    }

    public List<RelationshipAttributeDefinitionWeb> getRelationshipAttributeDefinitionWebs() {
        Collections.sort(relationshipAttributeDefinitionWebs);
        return relationshipAttributeDefinitionWebs;
    }

    public void setRelationshipAttributeDefinitionWebs(List<RelationshipAttributeDefinitionWeb> relationshipAttributeDefinitionWebs) {
        this.relationshipAttributeDefinitionWebs = relationshipAttributeDefinitionWebs;
    }

    @Override
    public int compareTo(@NotNull RelationshipDefinitionWeb relationshipDefinitionWeb) {
        return this.order - relationshipDefinitionWeb.order;
    }

}
