package cl.minsal.semantikos.model.categories;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.audit.AuditableEntity;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.TargetDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * Una categoría puede ser el sujeto de una acción de auditoría.
 */

public class Category extends PersistentEntity implements TargetDefinition, AuditableEntity {

    /** Nombre de la categoría */
    private String name;

    /** Nombre abreviado de la categoría */
    private String nameAbbreviated;

    /** Puede ser editado sólo por modeladores? */
    private boolean restriction;

    /** El tag Semantikos asociado */
    private TagSMTK tagSemantikos;

    /** Color de la categoría */
    private String color;

    private List<RelationshipDefinition> relationshipDefinitions;

    public Category() {
        super();
    }

    public Category(long idCategory, String name, String nameAbbreviated, boolean restriction, String color, TagSMTK tagSMTK){
        super(idCategory);

        this.name = name;
        this.nameAbbreviated = nameAbbreviated;
        this.restriction = restriction;

        this.color = color;
        this.tagSemantikos = tagSMTK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameAbbreviated() {
        return nameAbbreviated;
    }

    public void setNameAbbreviated(String nameAbbreviated) {
        this.nameAbbreviated = nameAbbreviated;
    }

    public boolean isRestriction() {
        return restriction;
    }

    public void setRestriction(boolean restriction) {
        this.restriction = restriction;
    }

    public TagSMTK getTagSemantikos() {
        return tagSemantikos;
    }

    public void setTagSemantikos(TagSMTK tagSemantikos) {
        this.tagSemantikos = tagSemantikos;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<RelationshipDefinition> getRelationshipDefinitions() {
        return relationshipDefinitions;
    }

    public void setRelationshipDefinitions(List<RelationshipDefinition> relationshipDefinitions) {
        this.relationshipDefinitions = relationshipDefinitions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (name != null ? !name.equals(category.name) : category.name != null) return false;

        return true;
    }

    public List<RelationshipDefinition> findRelationshipDefinitionsByName(String name) {
        List<RelationshipDefinition> someRelationshipDefinitions = new ArrayList<>();

        for (RelationshipDefinition relationshipDefinition : getRelationshipDefinitions()) {
            if(relationshipDefinition.getName().equals(name)) {
                someRelationshipDefinitions.add(relationshipDefinition);
            }
        }

        return someRelationshipDefinitions;
    }

    public List<RelationshipDefinition> findRelationshipDefinitionsById(long id) {
        List<RelationshipDefinition> someRelationshipDefinitions = new ArrayList<>();

        for (RelationshipDefinition relationshipDefinition : getRelationshipDefinitions()) {
            if(relationshipDefinition.getId() == id) {
                someRelationshipDefinitions.add(relationshipDefinition);
            }
        }

        return someRelationshipDefinitions;
    }

    public boolean hasAttributeSpecial() {

        for (RelationshipDefinition relationshipDefinition : relationshipDefinitions) {
            if(relationshipDefinition.getName().contains("Especial")) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public boolean isHasRelationshipDefinitions() {
        return !relationshipDefinitions.isEmpty();
    }

    @Override
    public boolean isBasicType() {
        return false;
    }

    @Override
    public boolean isSMTKType() {
        return true;
    }

    @Override
    public boolean isHelperTable() {
        return false;
    }

    @Override
    public boolean isSnomedCTType() {
        return false;
    }

    @Override
    public boolean isCrossMapType() {
        return false;
    }

    @Override
    public String getRepresentation() {
        return "ConceptID "+name+" ¦ Preferido "+name;
    }

}
