package cl.minsal.semantikos.model;

import cl.minsal.semantikos.model.relationships.TargetDefinition;

/**
 * Created by root on 08-07-16.
 */
public class CategoryRelationship {
    private long idAtributteCategory;
    private long idCategoria;
    private long idAtributteCategoryDefinition;

    private Category category;

    private TargetDefinition targetDefinition;

    public long getIdAtributteCategory() {
        return idAtributteCategory;
    }

    public void setIdAtributteCategory(long idAtributteCategory) {
        this.idAtributteCategory = idAtributteCategory;
    }

    public long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public long getIdAtributteCategoryDefinition() {
        return idAtributteCategoryDefinition;
    }

    public void setIdAtributteCategoryDefinition(long idAtributteCategoryDefinition) {
        this.idAtributteCategoryDefinition = idAtributteCategoryDefinition;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public TargetDefinition getTargetDefinition() {
        return targetDefinition;
    }

    public void setTargetDefinition(TargetDefinition targetDefinition) {
        this.targetDefinition = targetDefinition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryRelationship that = (CategoryRelationship) o;

        if (idAtributteCategory != that.idAtributteCategory) return false;
        if (idCategoria != that.idCategoria) return false;
        if (idAtributteCategoryDefinition != that.idAtributteCategoryDefinition) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (idAtributteCategory ^ (idAtributteCategory >>> 32));
        result = 31 * result + (int) (idCategoria ^ (idCategoria >>> 32));
        result = 31 * result + (int) (idAtributteCategoryDefinition ^ (idAtributteCategoryDefinition >>> 32));
        return result;
    }
}
