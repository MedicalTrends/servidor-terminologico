package cl.minsal.semantikos.model.queries;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.queries.IQuery;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;

/**
 * Created by root on 27-04-17.
 */
public class Query implements IQuery, Serializable {

    private String query;

    private Boolean truncateMatch = false;

    private Boolean valid = true;

    /**
     * Order
     */
    private int order;
    private String asc;

    /**
     * Pagination
     */
    private int pageSize;
    private int pageNumber;

    /**
     * Columnas dinámicas
     */
    private List<QueryColumn> columns = new ArrayList<>();

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Boolean getTruncateMatch() {
        return truncateMatch;
    }

    public void setTruncateMatch(Boolean truncateMatch) {
        this.truncateMatch = truncateMatch;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getAsc() {
        return asc;
    }

    public void setAsc(String asc) {
        this.asc = asc;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<QueryColumn> getColumns() {
        return columns;
    }

    public List<QueryColumn> getShowableColumns() {
        List<QueryColumn> showableColumns = new ArrayList<>();
        for (QueryColumn column : columns) {
            if(column.isShowable()) {
                showableColumns.add(column);
            }
        }
        return showableColumns;
    }

    public List<QueryColumn> getShowableColumnsByConcept(ConceptSMTK conceptSMTK) {
        List<QueryColumn> showableColumns = new ArrayList<>();
        for (QueryColumn column : columns) {
            if(column.isShowable() && !conceptSMTK.getRelationshipsByRelationDefinition(column.getRelationshipDefinition()).isEmpty()) {
                showableColumns.add(column);
            }
        }
        return showableColumns;
    }

    public void setColumns(List<QueryColumn> columns) {
        this.columns = columns;
    }

    @Override
    public List<QueryParameter> getQueryParameters() {
        return null;
    }

    public List<Long> getDefinitionIds() {

        List<Long> ids = new ArrayList<>();

        for (QueryColumn column : columns) {
            ids.add(column.getRelationshipDefinition().getId());
        }

        return ids;
    }

    public List<Long> getSecondOrderDefinitionIds() {

        List<Long> ids = new ArrayList<>();

        for (QueryColumn column : columns) {
            if(column.isSecondOrder()) {
                ids.add(column.getRelationshipDefinition().getId());
            }
        }

        return ids;
    }

    public void removeColumn(RelationshipDefinition relationshipDefinition) {
        for (QueryColumn column : columns) {
            if(column.getRelationshipDefinition().equals(relationshipDefinition)) {
                columns.remove(column);
                break;
            }
        }
    }

    public void resetQuery() {
        setOrder(0);
        setAsc("asc");
        setQuery(EMPTY_STRING);
        setTruncateMatch(false);
        setPageSize(15);
    }
}
