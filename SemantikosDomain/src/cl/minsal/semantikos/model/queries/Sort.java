package cl.minsal.semantikos.model.queries;

import java.io.Serializable;

/**
 * Created by root on 25-10-16.
 */
public class Sort implements Serializable {

    Boolean sort;
    boolean asc;

    public Sort(Boolean sort, boolean asc) {
        this.sort = sort;
        this.asc = asc;
    }

    public Boolean getSort() {
        return sort;
    }

    public void setSort(Boolean sort) {
        this.sort = sort;
    }

    public boolean isAsc() {
        return asc;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }
}
