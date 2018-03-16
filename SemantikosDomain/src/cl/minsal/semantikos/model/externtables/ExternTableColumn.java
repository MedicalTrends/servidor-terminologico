package cl.minsal.semantikos.model.externtables;

import cl.minsal.semantikos.model.PersistentEntity;

import java.io.Serializable;

/**
 * Created by BluePrints Developer on 14-12-2016.
 */
public class ExternTableColumn extends PersistentEntity implements Serializable {

    private String name;
    private ExternTable externTable;
    private ExternTableDataType dataType;

    public ExternTableDataType getDataType() {
        return dataType;
    }

    public void setDataType(ExternTableDataType dataType) {
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExternTable getExternTable() {
        return externTable;
    }

    public void setExternTable(ExternTable externTable) {
        this.externTable = externTable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExternTableColumn that = (ExternTableColumn) o;

        if (getId() != that.getId()) return false;
        if (name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId()>>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

}
