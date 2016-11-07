package cl.minsal.semantikos.model;

import cl.minsal.semantikos.model.audit.AuditableEntity;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.TargetDefinition;

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

    /** Vigencia de la categoría */
    private boolean isValid;

    /** Color de la categoría */
    private String color;

    private List<RelationshipDefinition> relationshipDefinitions;

    public Category() {
        super();
    }

    public Category(long idCategory, String name, String nameAbbreviated, boolean restriction, boolean isValid, String color, TagSMTK tagSMTK){
        super(idCategory);

        this.name = name;
        this.nameAbbreviated = nameAbbreviated;
        this.restriction = restriction;
        this.isValid = isValid;
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

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        this.isValid = valid;
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
}
