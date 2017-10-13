package cl.minsal.semantikos.model.queries;

import cl.minsal.semantikos.model.relationships.RelationshipDefinition;

import java.io.Serializable;

/**
 * Created by root on 25-10-16.
 */
public class QueryColumn implements Serializable {

    String columnName;
    Sort sort;
    RelationshipDefinition relationshipDefinition;
    boolean secondOrder;
    boolean showable;

    public QueryColumn(String columnName, Sort sort) {
        this.columnName = columnName;
        this.sort = sort;
    }

    public QueryColumn(String columnName, Sort sort, RelationshipDefinition relationshipDefinition, boolean showable) {
        this(columnName, sort);
        this.relationshipDefinition = relationshipDefinition;
        this.showable = showable;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public RelationshipDefinition getRelationshipDefinition() {
        return relationshipDefinition;
    }

    public void setRelationshipDefinition(RelationshipDefinition relationshipDefinition) {
        this.relationshipDefinition = relationshipDefinition;
    }

    public boolean isSecondOrder() {
        return secondOrder;
    }

    public void setSecondOrder(boolean secondOrder) {
        this.secondOrder = secondOrder;
    }

    public boolean isShowable() {
        return showable;
    }

    public void setShowable(boolean showable) {
        this.showable = showable;
    }
}
