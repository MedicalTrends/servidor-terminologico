package cl.minsal.semantikos.model.externtables;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.crossmaps.CrossmapSet;
import cl.minsal.semantikos.model.relationships.TargetDefinition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BluePrints Developer on 14-12-2016.
 */

public class ExternTable extends PersistentEntity implements TargetDefinition, Serializable {

    private String name;
    private CrossmapSet crossmapSet;
    private List<ExternTableColumn> columns = new ArrayList<>();
    private List<ExternTableRow> rows = new ArrayList<>();
    private List<ExternTableReference> references = new ArrayList<>();

    public ExternTable(String name, CrossmapSet crossmapSet) {
        this.name = name;
        this.crossmapSet = crossmapSet;
    }

    public ExternTable(long id, String name, CrossmapSet crossmapSet) {
        super(id);
        this.name = name;
        this.crossmapSet = crossmapSet;
    }

    @Override
    public boolean isPersistent() {
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CrossmapSet getCrossmapSet() {
        return crossmapSet;
    }

    public void setCrossmapSet(CrossmapSet crossmapSet) {
        this.crossmapSet = crossmapSet;
    }

    public List<ExternTableColumn> getColumns() {
        return columns;
    }

    public List<ExternTableReference> getReferences() {
        return references;
    }

    public void setReferences(List<ExternTableReference> references) {
        this.references = references;
    }

    public void setColumns(List<ExternTableColumn> columns) {
        this.columns = columns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExternTable that = (ExternTable) o;

        if (getId() != that.getId()) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
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
        return false;
    }

    @Override
    public boolean isSnomedCTType() {
        return false;
    }

    @Override
    public boolean isCrossMapType() {
        return true;
    }

    @Override
    public String getRepresentation() {
        return "ID "+name+" ¦ DESC "+name;
    }

    public List<ExternTableRow> getRows() {
        return rows;
    }

    public void setRows(List<ExternTableRow> rows) {
        this.rows = rows;
    }
}
