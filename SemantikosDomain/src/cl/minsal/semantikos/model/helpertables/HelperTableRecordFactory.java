package cl.minsal.semantikos.model.helpertables;


import cl.minsal.semantikos.kernel.components.HelperTablesManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Andrés Farías
 */
@Singleton
public class HelperTableRecordFactory {

    @EJB
    HelperTablesManager helperTableManager;

    private ObjectMapper mapper = new ObjectMapper();

    public static final long U_VOLUMEN_ID = 12L;
    public static final long COLUMN_U_VOL = 12L;

    public static final long U_POTENCIA_ID = 9L;
    public static final long COLUMN_U_POTENCIA = 13L;

    public static final long U_UNIDAD_CANTIDAD_ID = 15L;
    public static final long COLUMN_U_UNIDAD_CANTIDAD = 15L;

    public static final long U_PACK_MULTI_ID = 16L;
    public static final long COLUMN_U_PACK_MULTI = 14L;

    public static final long U_VOLUMEN_TOT_ID = 17L;
    public static final long COLUMN_U_VOLUMEN_TOT = 12L;

    public static final long U_PP_ID = 11L;
    //public static final long COLUMN_U_PP = ?L;

    public static final long U_ASIST_ID = 61L;
    public static final long COLUMN_U_ASIST = 11L;

    public HelperTableRecordFactory() {
    }


    /**
     * Este método es responsable de crear un HelperTable Row a partir de un objeto JSON.
     *
     * @param jsonExpression El objeto JSON a partir del cual se crea el objeto.
     *
     * @return Un objeto fresco de tipo <code>HelperTableRow</code> creado a partir del objeto JSON.
     *
     * @throws IOException Arrojada si hay un problema.
     */
    public HelperTableRow createHelperTAbleRowFromJSON(String jsonExpression) throws IOException {
        HelperTableRow row = mapper.readValue(jsonExpression, HelperTableRow.class);

        return row;
    }

    /**
     * Este método es responsable de crear un HelperTable Record a partir de un objeto JSON.
     *
     * @param jsonExpression El objeto JSON a partir del cual se crea el objeto. El formato JSON será:
     *                       <code>{"TableName":"helper_table_atc","records":[{"id":1,"codigo_atc":"atc1"}</code>
     *
     * @return Un objeto fresco de tipo <code>HelperTableRecord</code> creado a partir del objeto JSON.
     *
     * @throws IOException Arrojada si hay un problema.
     */
    public List<HelperTableRow> createHelperTableRowsFromJSON(String jsonExpression) throws IOException {

        HelperTableRow[] jSONecords = mapper.readValue(jsonExpression, HelperTableRow[].class);

        List<HelperTableRow> records = new ArrayList<>();

        for (HelperTableRow row: jSONecords ) {

            if(row.getCells()==null)
                row.setCells(new ArrayList<HelperTableData>());

            records.add(row);
        }

        return records;
    }

    public List<HelperTable> createHelperTablesFromJSON(String jsonExpression) throws IOException {

        HelperTable[] jsonHelperTables = mapper.readValue(jsonExpression, HelperTable[].class);
        List<HelperTable> helperTableList = new ArrayList<>();

        for (HelperTable table : jsonHelperTables) {

            if(table.getColumns()==null)
                table.setColumns(new ArrayList<HelperTableColumn>());

            helperTableList.add( table);
        }

        return helperTableList;
    }

    public HelperTableColumn createHelperTableColumnFromJSON(String jsonExpression) throws IOException {

        HelperTableColumn jsonHelperTableColumn = mapper.readValue(jsonExpression, HelperTableColumn.class);

        return jsonHelperTableColumn;
    }

    public List<HelperTableColumn> createHelperTableColumnsFromJSON(String jsonExpression) throws IOException {

        HelperTableColumn[] jsonHelperTableColumns = mapper.readValue(jsonExpression, HelperTableColumn[].class);

        return Arrays.asList(jsonHelperTableColumns);
    }



    public List<HelperTableDataType> createHelperTablesDataTypesFromJSON(String jsonExpression) throws IOException {
        HelperTableDataType[] jsonHelperTablesDataTypes = mapper.readValue(jsonExpression, HelperTableDataType[].class);
        List<HelperTableDataType> list = new ArrayList<>();

        for (HelperTableDataType type : jsonHelperTablesDataTypes) {
            list.add( type);
        }

        return list;
    }

    /**
     * Este método transforma una expresión JSON a un HelperTable.
     *
     * @param jsonExpression La expresión JSON con el Helper Table
     *
     * @return Una instancia del Helper Table.
     *
     * @throws IOException
     */
    public HelperTable createHelperTableFromJSON(String jsonExpression) throws IOException {
        HelperTable helperTable = mapper.readValue(jsonExpression, HelperTable.class);

        if(helperTable.getColumns()==null)
            helperTable.setColumns(new ArrayList<HelperTableColumn>());

        return helperTable;
    }


}

