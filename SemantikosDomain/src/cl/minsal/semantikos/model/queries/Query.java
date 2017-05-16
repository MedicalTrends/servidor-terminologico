package cl.minsal.semantikos.model.queries;

import java.io.Serializable;
import java.util.List;

/**
 * Created by root on 27-04-17.
 */
public class Query implements IQuery, Serializable {

    private String query;

    private Boolean truncateMatch = false;

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

    @Override
    public List<QueryParameter> getQueryParameters() {
        return null;
    }
}
