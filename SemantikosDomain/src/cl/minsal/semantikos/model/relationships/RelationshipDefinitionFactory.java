package cl.minsal.semantikos.model.relationships;

import cl.minsal.semantikos.model.tags.TagSMTK;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andrés Farías
 */
public class RelationshipDefinitionFactory implements Serializable {

    private static final RelationshipDefinitionFactory instance = new RelationshipDefinitionFactory();

    /** La lista de tagSMTK */

    private List<RelationshipDefinition> relationshipDefinitions;

    public Map<String, RelationshipDefinition> getRelationshipDefinitionByName() {
        return relationshipDefinitionByName;
    }

    public void setRelationshipDefinitionByName(Map<String, RelationshipDefinition> relationshipDefinitionByName) {
        this.relationshipDefinitionByName = relationshipDefinitionByName;
    }

    public List<RelationshipDefinition> getRelationshipDefinitions() {

        return relationshipDefinitions;
    }

    /** Mapa de tagSMTK por su nombre. */
    private Map<String, RelationshipDefinition> relationshipDefinitionByName;

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private RelationshipDefinitionFactory() {
        this.relationshipDefinitions = new ArrayList<>();
        this.relationshipDefinitionByName = new HashMap<>();
    }

    public static RelationshipDefinitionFactory getInstance() {
        return instance;
    }

    /**
     * Este método es responsable de retornar el tipo de descripción llamado FSN.
     *
     * @return Retorna una instancia de FSN.
     */
    public RelationshipDefinition findRelationshipDefinitionByName(String name) {

        if (relationshipDefinitionByName.containsKey(name)) {
            return this.relationshipDefinitionByName.get(name);
        }

        return null;
    }

    /**
     * Este método es responsable de asignar un nuevo conjunto de tagsSMTJ. Al hacerlo, es necesario actualizar
     * los mapas.
     */
    public void setRelationshipDefinitions(List<RelationshipDefinition> relationshipDefinitions) {

        /* Se actualiza la lista */
        this.relationshipDefinitions = relationshipDefinitions;

        /* Se actualiza el mapa por nombres */
        this.relationshipDefinitionByName.clear();

        for (RelationshipDefinition relationshipDefinition : relationshipDefinitions) {
            this.relationshipDefinitionByName.put(relationshipDefinition.getName(), relationshipDefinition);
        }
    }

    /**
     * Este método es responsable de asignar un nuevo conjunto de tagsSMTJ. Al hacerlo, es necesario actualizar
     * los mapas.
     */
    public void addRelationshipDefinitions(List<RelationshipDefinition> relationshipDefinitions) {

        /* Se actualiza la lista */
        this.relationshipDefinitions.addAll(relationshipDefinitions);

        for (RelationshipDefinition relationshipDefinition : relationshipDefinitions) {
            this.relationshipDefinitionByName.put(relationshipDefinition.getName(), relationshipDefinition);
        }
    }

}
