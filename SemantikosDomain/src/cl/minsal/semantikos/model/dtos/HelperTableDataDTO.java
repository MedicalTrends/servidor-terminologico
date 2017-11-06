package cl.minsal.semantikos.model.dtos;

import cl.minsal.semantikos.model.helpertables.HelperTableColumn;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import static cl.minsal.semantikos.model.DAO.NON_PERSISTED_ID;
import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;

/**
 * Created by BluePrints Developer on 14-12-2016.
 */

public class HelperTableDataDTO implements Serializable {

    private long id;
    private Long intValue;
    private Float floatValue;
    private String stringValue;
    private Date dateValue;
    private Boolean booleanValue;
    private Long foreignKeyValue;
    private long rowId;
    private long columnId;

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

}
