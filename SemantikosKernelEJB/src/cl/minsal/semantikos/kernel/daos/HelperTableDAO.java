package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.helpertables.*;
import cl.minsal.semantikos.model.relationships.TargetDefinition;

import java.util.List;

/**
 * Created by BluePrints Developer on 09-01-2017.
 */
public interface HelperTableDAO {
    List<HelperTable> getAllTables();

    HelperTableColumn updateColumn(HelperTableColumn column);

    List<HelperTableDataType> getAllDataTypes();

    HelperTableColumn createColumn(HelperTableColumn column);

    List<HelperTableRow> getTableRows(long tableId);

    HelperTableRow createRow(HelperTableRow newRow);

    HelperTableData createData(HelperTableData data);

    HelperTableRow getRowById(long id);

    HelperTableRow updateRow(HelperTableRow row);

    HelperTable getHelperTableByID(long tableId);

    List<HelperTableRow> searchRecords(HelperTable helperTable, String pattern);

    List<HelperTableRow> getValidTableRows(long id);
}
