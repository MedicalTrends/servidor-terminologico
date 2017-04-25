package cl.minsal.semantikos.model.helpertables;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by BluePrints Developer on 14-12-2016.
 */
public class HelperTableColumn implements Serializable {

    private long id;
    private String name;
    private long helperTableId;
    private long helperTableDataTypeId;
    private long foreignKeyHelperTableId;
    private HelperTable helperTable;
    private HelperTable foreignKeyHelperTable;
    private HelperTableDataType helperTableDataType;
    private boolean foreignKey;
    private String description;
    private boolean searchable;
    private boolean editable;
    private boolean sortable;
    private boolean showable;
    private boolean required;


    public long getId() {
        return id;
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

    @JsonProperty("helper_table_id")
    public long getHelperTableId() {
        return helperTableId;
    }

    @JsonProperty("helper_table_id")
    public void setHelperTableId(long helperTableId) {
        this.helperTableId = helperTableId;
    }

    public HelperTable getHelperTable() {
        return helperTable;
    }

    public void setHelperTable(HelperTable helperTable) {
        this.helperTable = helperTable;
    }

    public HelperTable getForeignKeyHelperTable() {
        return foreignKeyHelperTable;
    }

    public void setForeignKeyHelperTable(HelperTable foreignKeyHelperTable) {
        this.foreignKeyHelperTable = foreignKeyHelperTable;
    }

    @JsonProperty("helper_table_data_type_id")
    public long getHelperTableDataTypeId() {
        return helperTableDataTypeId;
    }

    @JsonProperty("helper_table_data_type_id")
    public void setHelperTableDataTypeId(long helperTableDataTypeId) {
        this.helperTableDataTypeId = helperTableDataTypeId;
    }

    @JsonProperty("helper_table_data_type")
    public HelperTableDataType getHelperTableDataType() {
        return helperTableDataType;
    }

    @JsonProperty("helper_table_data_type")
    public void setHelperTableDataType(HelperTableDataType helperTableDataType) {
        this.helperTableDataType = helperTableDataType;
    }

    @JsonProperty("foreign_key_table_id")
    public long getForeignKeyHelperTableId() {
        return foreignKeyHelperTableId;
    }

    @JsonProperty("foreign_key_table_id")
    public void setForeignKeyHelperTableId(long foreignKeyTableId) {
        this.foreignKeyHelperTableId = foreignKeyTableId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HelperTableColumn that = (HelperTableColumn) o;

        if (id != that.id) return false;
        if (helperTableId != that.helperTableId) return false;
        if (helperTableDataTypeId != that.helperTableDataTypeId) return false;
        if (foreignKeyHelperTableId != that.foreignKeyHelperTableId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (int) (helperTableId ^ (helperTableId >>> 32));
        result = 31 * result + (int) (helperTableDataTypeId ^ (helperTableDataTypeId >>> 32));
        result = 31 * result + (int) (foreignKeyHelperTableId ^ (foreignKeyHelperTableId >>> 32));
        return result;
    }

    @JsonProperty("foreign_key")
    public boolean isForeignKey() {
        return foreignKey;
    }

    @JsonProperty("foreign_key")
    public void setForeignKey(boolean foreignKey) {
        this.foreignKey = foreignKey;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("searchable")
    public boolean isSearchable() {
        return searchable;
    }

    @JsonProperty("searchable")
    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

    @JsonProperty("editable")
    public boolean isEditable() {
        return editable;
    }

    @JsonProperty("editable")
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @JsonProperty("sortable")
    public boolean isSortable() {
        return sortable;
    }

    @JsonProperty("sortable")
    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    @JsonProperty("showable")
    public boolean isShowable() {
        return showable;
    }

    @JsonProperty("showable")
    public void setShowable(boolean showable) {
        this.showable = showable;
    }

    @JsonProperty("required")
    public boolean isRequired() {
        return required;
    }

    @JsonProperty("required")
    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public String toString() {
        return "HelperTableColumn{" +
                "description='" + description + '\'' +
                '}';
    }
}
