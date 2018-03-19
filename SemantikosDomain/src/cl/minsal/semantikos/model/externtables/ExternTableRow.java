package cl.minsal.semantikos.model.externtables;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetType;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;

/**
 * Created by BluePrints Developer on 14-12-2016.
 */

public class ExternTableRow extends PersistentEntity implements Target, Serializable {


    private List<ExternTableRelationship> relationships = new ArrayList<>();
    private List<ExternTableData> cells = new ArrayList<>();
    private ExternTable table;

    public ExternTable getTable() {
        return table;
    }

    public void setTable(ExternTable table) {
        this.table = table;
    }

    public List<ExternTableRelationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<ExternTableRelationship> relationships) {
        this.relationships = relationships;
    }

    @Override
    public TargetType getTargetType() {
        return TargetType.CrossMap;
    }

    @Override
    public String getRepresentation() {
        return String.valueOf(getId());
    }

    @Override
    public Target copy() {
        ExternTableRow copy = new ExternTableRow();

        copy.setId(getId());
        copy.setTable(table);
        copy.setCells(cells);
        copy.setRelationships(relationships);

        return copy;

    }

    public List<ExternTableData> getCells() {
        return cells;
    }

    public void setCells(List<ExternTableData> cells) {
        this.cells = cells;
    }

    public String getColumnValue(ExternTableColumn column){

        if (this.getCells()==null) {
            return null;
        }

        for (ExternTableData cell: getCells()) {

            if(cell.getColumn().equals(column)) {

                if (column.getDataType().equals(ExternTableDataType.STRING_TYPE)) //String
                    return cell.getStringValue();

                if (column.getDataType().equals(ExternTableDataType.BOOLEAN_TYPE)) //Boolean
                    return ""+cell.getBooleanValue();

                if (column.getDataType().equals(ExternTableDataType.INTEGER_TYPE)) //int
                    return ""+cell.getIntValue();

                if (column.getDataType().equals(ExternTableDataType.FLOAT_TYPE)) { //Float
                    if (cell.getFloatValue() == null) {
                        return EMPTY_STRING;
                    } else {
                        return "" + cell.getFloatValue();
                    }
                }
                if (column.getDataType().equals(ExternTableDataType.DATE_TYPE)) { //Date
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
