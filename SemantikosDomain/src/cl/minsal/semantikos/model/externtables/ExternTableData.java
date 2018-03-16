package cl.minsal.semantikos.model.externtables;

import cl.minsal.semantikos.model.PersistentEntity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;

/**
 * Created by BluePrints Developer on 14-12-2016.
 */

public class ExternTableData extends PersistentEntity implements Serializable {

    private Long intValue;
    private Float floatValue;
    private String stringValue;
    private Date dateValue;
    private Boolean booleanValue;

    private ExternTableRow row;
    private ExternTableColumn column;

    public ExternTableRow getRow() {
        return row;
    }

    public void setRow(ExternTableRow row) {
        this.row = row;
    }

    public ExternTableColumn getColumn() {
        return column;
    }

    public void setColumn(ExternTableColumn column) {
        this.column = column;
    }

    //@JsonProperty("int_value")
    public Long getIntValue() {
        return intValue;
    }

    //@JsonProperty("int_value")
    public void setIntValue(Long intValue) {
        this.intValue = intValue;
    }

    //@JsonProperty("float_value")
    public Float getFloatValue() {
        return floatValue;
    }

    //@JsonProperty("float_value")
    public void setFloatValue(Float floatValue) {
        this.floatValue = floatValue;
    }

    //@JsonProperty("string_value")
    public String getStringValue() {
        return stringValue;
    }

    //@JsonProperty("string_value")
    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    //@JsonProperty("date_value")
    public Date getDateValue() {
        return dateValue;
    }

    //@JsonProperty("date_value")
    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    //@JsonProperty("boolean_value")
    public Boolean getBooleanValue() {
        return booleanValue;
    }

    //@JsonProperty("boolean_value")
    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    @Override
    public boolean equals(Object other) {

        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        ExternTableData that = (ExternTableData) other;

        /* Si ambas están persistidas y no tienen el mismo ID, entonces son distintas */
        if (this.getId() != NON_PERSISTED_ID && that.getId() != NON_PERSISTED_ID && this.getId() != that.getId()) return false;

        /* Si alguna de ellas no está persistida, comparamos su representación */
        return this.toString().equals(that.toString());

    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (int) (intValue ^ (intValue >>> 32));
        result = 31 * result + (floatValue != null ? floatValue.hashCode() : 0);
        result = 31 * result + (stringValue != null ? stringValue.hashCode() : 0);
        result = 31 * result + (dateValue != null ? dateValue.hashCode() : 0);
        result = 31 * result + (booleanValue ? 1 : 0);
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
        else {
            return EMPTY_STRING;
            //return String.valueOf(id);
        }

    }

}
