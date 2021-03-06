package cl.minsal.semantikos.modelweb;

import cl.minsal.semantikos.model.relationships.Multiplicity;
import cl.minsal.semantikos.model.relationships.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Andrés Farías on 10/5/16.
 */
public class RelationshipDefinitionWeb extends RelationshipDefinition implements Comparable<RelationshipDefinitionWeb>, Serializable {

    /** El identificador del composite que se quiere usar en las vistas */
    private long compositeID;

    /** Establece el orden o posición */
    private int order;

    /** Establece si esta definición participa en la autogeneración del término preferido */
    private boolean autogenerate;

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

    public boolean isAutogenerate() {
        return autogenerate;
    }

    public void setAutogenerate(boolean autogenerate) {
        this.autogenerate = autogenerate;
    }

    public Target getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Target defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean hasDefaultValue(){
        if(getDefaultValue()!=null) {
            return true;
        }
        /*
        for (RelationshipAttributeDefinitionWeb relationshipAttributeDefinitionWeb : relationshipAttributeDefinitionWebs) {
            if(relationshipAttributeDefinitionWeb.getDefaultValue()!=null) {
                return true;
            }
        }
        */
        return false;
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

    public List<RelationshipAttributeDefinitionWeb> getRelationshipAttributeDefinitionWebsByName(String name) {

        List<RelationshipAttributeDefinitionWeb> someAttributes = new ArrayList<>();

        for (RelationshipAttributeDefinitionWeb relationshipAttributeDefinitionWeb : relationshipAttributeDefinitionWebs) {
            if(relationshipAttributeDefinitionWeb.getName().toLowerCase().equals(name.toLowerCase())) {
                someAttributes.add(relationshipAttributeDefinitionWeb);
            }
        }
        Collections.sort(someAttributes);
        return someAttributes;
    }

    public RelationshipAttributeDefinition getRelationshipAttributeDefinitionByName(String name) {

        for (RelationshipAttributeDefinitionWeb relationshipAttributeDefinitionWeb : relationshipAttributeDefinitionWebs) {
            if(relationshipAttributeDefinitionWeb.getName().toLowerCase().equals(name.toLowerCase())) {
                return relationshipAttributeDefinitionWeb.getRelationshipAttributeDefinition();
            }
        }
        return null;
    }

    public void setRelationshipAttributeDefinitionWebs(List<RelationshipAttributeDefinitionWeb> relationshipAttributeDefinitionWebs) {
        this.relationshipAttributeDefinitionWebs = relationshipAttributeDefinitionWebs;
    }

    @Override
    public int compareTo(@NotNull RelationshipDefinitionWeb relationshipDefinitionWeb) {
        return this.order - relationshipDefinitionWeb.order;
    }

}
