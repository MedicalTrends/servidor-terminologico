package cl.minsal.semantikos.model.helpertables;

import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;

/**
 * Created by BluePrints Developer on 14-12-2016.
 */

public class HelperTableRow implements Target, Serializable {

    private long id;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss", timezone="America/Buenos_Aires")
    private Timestamp creationDate;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss", timezone="America/Buenos_Aires")
    private Timestamp lastEditDate;
    private boolean valid;
    private Timestamp validityUntil;
    private String description;
    private String creationUsername;
    private String lastEditUsername;
    private List<HelperTableData> cells = new ArrayList<>();
    private long helperTableId;


    public HelperTableRow(){
        super();
    }

    /*
    crea fila placeholder a partir de la tabla
     */
    public HelperTableRow(HelperTable helperTable) {
        this.id=-1;
        this.helperTableId=helperTable.getId();
    }

    /*
    crea fila placeholder a partir de la tabla y data adicional
     */
    public HelperTableRow(HelperTable helperTable, List<HelperTableData> cells) {
        this.id=-1;
        this.helperTableId=helperTable.getId();
        this.cells = cells;
    }


    public long getId() {
        return id;
    }

    @Override
    public TargetType getTargetType() {
        return TargetType.HelperTable;
    }



    @Override
    public String getRepresentation() {
        return description;
    }

    @Override
    public Target copy() {
        HelperTableRow copy = new HelperTableRow();

        copy.setId(id);
        copy.setDescription(description);
        copy.setLastEditUsername(lastEditUsername);
        copy.setCells(cells);
        copy.setHelperTableId(helperTableId);
        copy.setValid(valid);
        copy.setCreationDate(creationDate);
        copy.setLastEditDate(lastEditDate);
        copy.setValid (valid);
        copy.setValidityUntil (validityUntil);
        copy.setCreationUsername (creationUsername);
        copy.setHelperTableId (helperTableId);

        return copy;

    }

    public HelperTableData getCellByColumnName(String columnName) {
        for (HelperTableData cell : cells) {
            if(cell.getColumn().getDescription().toLowerCase().equals(columnName.toLowerCase())) {
                return cell;
            }
        }
        return null;
    }

    public String getDateCreationFormat() {

        if(getCreationDate() != null) {
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return outputFormat.format(getCreationDate());
        }

        return "";
    }


    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty("creation_date")
    public Timestamp getCreationDate() {
        return creationDate;
    }

    @JsonProperty("creation_date")
    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    @JsonProperty("last_edit_date")
    public Timestamp getLastEditDate() {
        return lastEditDate;
    }

    @JsonProperty("last_edit_date")
    public void setLastEditDate(Timestamp lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    @JsonProperty("valid")
    public boolean isValid() {
        return valid;
    }

    @JsonProperty("valid")
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @JsonProperty("validity_until")
    public Date getValidityUntil() {
        return validityUntil;
    }


    @JsonProperty("validity_until")
    public void setValidityUntil(Timestamp validityUntil) {
        this.validityUntil = validityUntil;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public List<HelperTableData> getCells() {
        return cells;
    }

    public void setCells(List<HelperTableData> cells) {
        this.cells = cells;
    }


    @JsonProperty("creation_user")
    public String getCreationUsername() {
        return creationUsername;
    }


    @JsonProperty("creation_user")
    public void setCreationUsername(String creationUsername) {
        this.creationUsername = creationUsername;
    }

    @JsonProperty("helper_table_id")
    public long getHelperTableId() {

        return helperTableId;
    }

    @JsonProperty("helper_table_id")
    public void setHelperTableId(long helperTableId) {
        this.helperTableId = helperTableId;
    }

    @JsonProperty("last_edit_user")
    public String getLastEditUsername() {
        return lastEditUsername;
    }

    @JsonProperty("last_edit_user")
    public void setLastEditUsername(String lastEditUsername) {
        this.lastEditUsername = lastEditUsername;
    }

    /*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HelperTableRow that = (HelperTableRow) o;

        if (id != that.id) return false;
        if (valid != that.valid) return false;
        if (creationDate != null ? !creationDate.equals(that.creationDate) : that.creationDate != null) return false;
        if (lastEditDate != null ? !lastEditDate.equals(that.lastEditDate) : that.lastEditDate != null) return false;
        if (validityUntil != null ? !validityUntil.equals(that.validityUntil) : that.validityUntil != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }
    */

    @Override
    public boolean equals(Object other) {

        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        HelperTableRow that = (HelperTableRow) other;

        /* Si ambas están persistidas y no tienen el mismo ID, entonces son distintas */
        if (this.isPersistent() && that.isPersistent() && this.getId() != that.getId()) return false;

        /* Si alguna de ellas no está persistida, comparamos 1. tabla auxiliar, 2. descripción del row, y 3. campos del row */

        /* 1. Se compara la tabla auxiliar  */
        if (this.getHelperTableId() != that.getHelperTableId()) return false;

        /* 2. Se compara la descripción */
        if(!this.getDescription().equals(that.getDescription())) return false;

        /* 2. Si no tienen los mismos campos */
        for (HelperTableData cell : this.getCells()) {
            if (!that.getCells().contains(cell)) {
                return false;
            }
        }

        return true;

    }

    @Override
    public String toString() {
        return description;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (lastEditDate != null ? lastEditDate.hashCode() : 0);
        result = 31 * result + (valid ? 1 : 0);
        result = 31 * result + (validityUntil != null ? validityUntil.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    public boolean isPersistent() {
        return id != -1;
    }


    public String getColumnValue(HelperTableColumn column){

        if (this.getCells()==null)
            return null;


        for (HelperTableData cell: getCells()) {

            if(cell.getColumnId()==column.getId()){

                if(column.isForeignKey())
                    return cell.getForeignKeyValue()+"";

                if (column.getHelperTableDataTypeId()==1)
                    return cell.getStringValue();

                if (column.getHelperTableDataTypeId()==2)
                    return ""+cell.getBooleanValue();

                if (column.getHelperTableDataTypeId()==3)
                    return ""+cell.getIntValue();

                if (column.getHelperTableDataTypeId()==4)
                    return ""+cell.getFloatValue();

                if (column.getHelperTableDataTypeId()==5) {
                    if(cell.getDateValue()==null) {
                        return EMPTY_STRING;
                    }
                    else {
                        return new SimpleDateFormat("dd/MM/yyyy").format(cell.getDateValue());
                    }
                }

            }


        }

        return null;
    }
}
