package cl.minsal.semantikos.model.helpertables;

import cl.minsal.semantikos.model.relationships.TargetDefinition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BluePrints Developer on 14-12-2016.
 */

public class HelperTable implements TargetDefinition, Serializable {
    private long id;
    private String name;
    private String description;
    private List<HelperTableColumn> columns = new ArrayList<>();
    private List<HelperTableRow> rows = new ArrayList<>();

    public long getId() {
        return id;
    }

    @Override
    public boolean isPersistent() {
        return true;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public List<HelperTableColumn> getColumns() {
        return columns;
    }

    public List<HelperTableColumn> getShowableColumns() {

        List<HelperTableColumn> showableColumns = new ArrayList<>();

        for (HelperTableColumn column: getColumns()) {
            if(column.isShowable())
                showableColumns.add(column);
        }
        return showableColumns;
    }

    public List<HelperTableColumn> getShowableColumnsButFK() {

        List<HelperTableColumn> showableColumns = new ArrayList<>();

        for (HelperTableColumn column: getColumns()) {
            if(column.isShowable() && !column.isForeignKey()) {
                if(column.getName().toUpperCase().contains("VISIBLE")) {
                    continue;
                }
                showableColumns.add(column);
            }             
        }
        return showableColumns;
    }

    public List<HelperTableColumn> getColumnsButSTK() {

        List<HelperTableColumn> columns = new ArrayList<>();

        for (HelperTableColumn column: getColumns()) {
            if(!column.getName().equals("CONCEPTO_STK") && !column.getName().equals("CONCEPTO STK")) {
                columns.add(column);
            }
        }

        return columns;
    }

    public void setColumns(List<HelperTableColumn> columns) {
        this.columns = columns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HelperTable that = (HelperTable) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public boolean isBasicType() {
        return false;
    }

    @Override
    public boolean isSMTKType() {
        return false;
    }

    @Override
    public boolean isHelperTable() {
        return true;
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
        return "ID "+name+" ¦ DESC "+name;
    }

    public List<HelperTableRow> getRows() {
        return rows;
    }

    public void setRows(List<HelperTableRow> rows) {
        this.rows = rows;
    }
}
