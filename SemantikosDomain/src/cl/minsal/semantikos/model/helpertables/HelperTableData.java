package cl.minsal.semantikos.model.helpertables;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import static cl.minsal.semantikos.model.DAO.NON_PERSISTED_ID;
import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;

/**
 * Created by BluePrints Developer on 14-12-2016.
 */

public class HelperTableData implements Serializable {

    private long id;
    private Long intValue;
    private Float floatValue;
    private String stringValue;
    private Date dateValue;
    private Boolean booleanValue;
    private Long foreignKeyValue;
    private long rowId;
    private long columnId;
    private HelperTableRow row;
    private HelperTableColumn column;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty("int_value")
    public Long getIntValue() {
        return intValue;
    }

    @JsonProperty("int_value")
    public void setIntValue(Long intValue) {
        this.intValue = intValue;
    }

    @JsonProperty("float_value")
    public Float getFloatValue() {
        return floatValue;
    }

    @JsonProperty("float_value")
    public void setFloatValue(Float floatValue) {
        this.floatValue = floatValue;
    }

    @JsonProperty("string_value")
    public String getStringValue() {
        return stringValue;
    }

    @JsonProperty("string_value")
    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    @JsonProperty("date_value")
    public Date getDateValue() {
        return dateValue;
    }

    @JsonProperty("date_value")
    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    @JsonProperty("boolean_value")
    public Boolean getBooleanValue() {
        return booleanValue;
    }

    @JsonProperty("boolean_value")
    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    @JsonProperty("foreign_key_value")
    public Long getForeignKeyValue() {
        return foreignKeyValue;
    }

    @JsonProperty("foreign_key_value")
    public void setForeignKeyValue(Long foreignKeyValue) {
        this.foreignKeyValue = foreignKeyValue;
    }

    public HelperTableRow getRow() {
        return row;
    }

    public void setRow(HelperTableRow row) {
        this.row = row;
    }

    @JsonProperty("row_id")
    public long getRowId() {
        return rowId;
    }

    @JsonProperty("row_id")
    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    @JsonProperty("column_id")
    public long getColumnId() {
        return columnId;
    }

    @JsonProperty("column_id")
    public void setColumnId(long columnId) {
        this.columnId = columnId;
    }


    public HelperTableColumn getColumn() {
        return column;
    }

    public void setColumn(HelperTableColumn column) {
        this.column = column;
    }

    /*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HelperTableData that = (HelperTableData) o;

        if (id != that.id) return false;
        if (intValue != that.intValue) return false;
        if (booleanValue != that.booleanValue) return false;
        if (foreignKeyValue != that.foreignKeyValue) return false;
        if (floatValue != null ? !floatValue.equals(that.floatValue) : that.floatValue != null) return false;
        if (stringValue != null ? !stringValue.equals(that.stringValue) : that.stringValue != null) return false;
        if (dateValue != null ? !dateValue.equals(that.dateValue) : that.dateValue != null) return false;

        return true;
    }
    */

    @Override
    public boolean equals(Object other) {

        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        HelperTableData that = (HelperTableData) other;

        /* Si ambas están persistidas y no tienen el mismo ID, entonces son distintas */
        if (this.id != NON_PERSISTED_ID && that.id != NON_PERSISTED_ID && this.getId() != that.getId()) return false;

        /* Si alguna de ellas no está persistida, comparamos su representación */
        return this.toString().equals(that.toString());

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (intValue ^ (intValue >>> 32));
        result = 31 * result + (floatValue != null ? floatValue.hashCode() : 0);
        result = 31 * result + (stringValue != null ? stringValue.hashCode() : 0);
        result = 31 * result + (dateValue != null ? dateValue.hashCode() : 0);
        result = 31 * result + (booleanValue ? 1 : 0);
        result = 31 * result + (int) (foreignKeyValue ^ (foreignKeyValue >>> 32));
        return result;
    }

    @Override
    public String toString() {

        if(intValue != null) {
            return String.valueOf(intValue);
        }
        if(booleanValue != null) {
            return String.valueOf(booleanValue);
        }
        else if(floatValue != null) {
            return String.valueOf(floatValue);
        }
        else if(stringValue != null) {
            return stringValue;
        }
        else if(dateValue != null) {
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            return outputFormat.format(dateValue);
        }
        else if(foreignKeyValue != null) {
            return String.valueOf(foreignKeyValue);
        }
        else {
            return EMPTY_STRING;
            //return String.valueOf(id);
        }

    }

}
