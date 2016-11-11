package cl.minsal.semantikos.model.helpertables;

import cl.minsal.semantikos.kernel.components.HelperTableManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.io.IOException;
import java.util.*;

/**
 * @author Andrés Farías
 */
@Singleton
public class HelperTableRecordFactory {

    @EJB
    HelperTableManager helperTableManager;

    private ObjectMapper mapper = new ObjectMapper();

    public HelperTableRecordFactory() {
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
    public HelperTableRecord createRecordFromJSON(String jsonExpression) throws IOException {
        JSONHelperTableRecord jsonHelperTableRecord = mapper.readValue(jsonExpression, JSONHelperTableRecord.class);

        HelperTable helperTable = helperTableManager.findHelperTableByID(jsonHelperTableRecord.getTableId());
        HelperTableRecord helperTableRecord = new HelperTableRecord(helperTable, jsonHelperTableRecord.getFields());
        /**
         * Se setea el id desde el fields para ser utilizado por el custom converter
         */
        Map<String, String> fields = helperTableRecord.getFields();
        if ( fields != null && fields.get("id") != null ) {
            helperTableRecord.setId(new Long(fields.get("id")));
        }
        //return new HelperTableRecord(helperTable, jsonHelperTableRecord.getFields());
        return helperTableRecord;
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
    public List<HelperTableRecord> createHelperRecordsFromJSON(String jsonExpression) throws IOException {

        JSONHelperTableRecords jsonHelperTableRecord = mapper.readValue(jsonExpression, JSONHelperTableRecords.class);
        HelperTable helperTable = helperTableManager.findHelperTableByID(jsonHelperTableRecord.getTableId());

        List<HelperTableRecord> records = new ArrayList<>();
        for (Map<String, String> fields : jsonHelperTableRecord.getRecords()) {
            HelperTableRecord helperTableRecord = new HelperTableRecord(helperTable, fields);
            /**
             * Se setea el id desde el fields para ser utilizado por el custom converter
             */
            helperTableRecord.setId(new Long(helperTableRecord.getFields().get("id")));
            records.add(helperTableRecord);
        }

        return records;
    }

    public List<HelperTable> createHelperTablesFromJSON(String jsonExpression) throws IOException {
        HelperTable[] jsonHelperTables = mapper.readValue(jsonExpression, HelperTable[].class);

        return Arrays.asList(jsonHelperTables);
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
        HelperTableJSON jsonHelperTable = mapper.readValue(jsonExpression, HelperTableJSON.class);

        // Se crean las columnas por defecto que debe especificar esta definición de helperTable
        Collection<HelperTableColumn> columns = Arrays.asList(new HelperTableColumn[]{new HelperTableColumn("id", false, true, true),new HelperTableColumn("description", false, true, true)});;

        return new HelperTable(jsonHelperTable.getTableId(), jsonHelperTable.getName(), jsonHelperTable.getDescription(), jsonHelperTable.getTablaName(), columns);
    }
}

/**
 * Esta clase tiene como propósito dar una representación simple de un record para ser transformado automáticamente
 * desde JSON.
 */
class JSONHelperTableRecord {

    /** El nombre de la tabla auxiliar */
    private long tableId;

    /** La llave primaria del registro */
    private long id;

    private Map<String, String> fields;

    public JSONHelperTableRecord() {
        this.fields = new HashMap<>();
    }

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }
}

/**
 * Esta clase tiene como propósito dar una representación simple de varios registros para ser transformado
 * automáticamente desde JSON.
 */
class JSONHelperTableRecords {

    /** El nombre de la tabla auxiliar */
    private long tableId;

    /** La llave primaria del registro */
    private long id;

    private List<Map<String, String>> records;

    public JSONHelperTableRecords() {
        this.records = new ArrayList<>();
    }

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Map<String, String>> getRecords() {
        return records;
    }

    public void setRecords(List<Map<String, String>> records) {
        this.records = records;
    }
}

/**
 * Esta clase tiene como propósito dar una representación simple de varios registros para ser transformado
 * automáticamente desde JSON.
 */
class HelperTableJSON {

    /** El nombre de la tabla auxiliar */
    private long tableId;

    /** Un nombre legible por humanos para la Tabla Auxiliar */
    private String name;

    /* Una breve descripción de la tabla auxiliar */
    private String description;

    /** El nombre de la tabla física */
    private String tablaName;

    /** El nombre de las columnas que posee la tabla física */
    private Collection<HelperTableColumn> columns;

    public HelperTableJSON() {
        this.columns = new ArrayList<>();
    }

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTablaName() {
        return tablaName;
    }

    public void setTablaName(String tablaName) {
        this.tablaName = tablaName;
    }

    public Collection<HelperTableColumn> getColumns() {
        return columns;
    }

    public void setColumns(Collection<HelperTableColumn> columns) {
        if (columns == null) {
            this.columns = new ArrayList<>();
        } else {
            this.columns = columns;
        }
    }
}

