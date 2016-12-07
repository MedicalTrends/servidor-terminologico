package cl.minsal.semantikos.model.helpertables;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import javax.ejb.EJB;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HelperTableRecordFactoryTest {

    /** La instancia a probar */
    @EJB
    private HelperTableRecordFactory helperTableRecordFactory;

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Este test verifica la creación local de un simple Record.
     *
     * @throws Exception
     */
    @Test
    public void createHelperTableDummyRecordsTest() throws Exception {
        JSONHelperTableRecords singleColumnDummyRecord = createHelperTableDummyRecords();
        assertEquals(singleColumnDummyRecord.getRecords().size(), 2);
    }

    /**
     * Este test permite verificar que un JSON que representa dos registros de una tabla (generado en la BDD) es
     * parseado a la clase intermedia correctamente.
     *
     * @throws Exception
     */
    @Test
    public void transformJSON2Records() throws Exception {
        JSONHelperTableRecords twoRecords = mapper.readValue(createJSONRocords(), JSONHelperTableRecords.class);

        assertTrue(twoRecords.getTableId()==1);
        assertEquals(twoRecords.getRecords().size(), 2);
    }

    /**
     * Este test permite verificar que un JSON que representa un registro único de una tabla auxiliar es parseado
     * correctamente a la clase intermedia.
     *
     * @throws Exception
     */
    @Test
    public void transformJSON2IntermediateRecord() throws Exception {
        JSONHelperTableRecord aRecord = mapper.readValue(createJSONRocord(), JSONHelperTableRecord.class);

        assertTrue(aRecord.getTableId()==1);
        assertEquals(aRecord.getFields().size(), 3);
    }

    @Test
    public void transformJSON2SingleRecord() throws Exception {
        String jsonRecord = createJSONRocord();
        HelperTableRecord recordFromJSON = this.helperTableRecordFactory.createRecordFromJSON(jsonRecord);
        assertEquals(3, recordFromJSON.getFields().size());
    }

    @Test
    public void transformJSON2SingleRecord02() throws Exception {
        String jsonRocord = createJSONRocord02();
        HelperTableRecord recordFromJSON = this.helperTableRecordFactory.createRecordFromJSON(jsonRocord);
        assertEquals(4, recordFromJSON.getFields().size());
    }

    private String createJSONRocords() {
        String json = "{\"tableName\":\"helper_table_atc\",\"records\":[{\"id\":1,\"codigo_atc\":\"atc1\",\"descripcion_atc\":\"Esta es una descripción HT_ATC_NAME\"}, \n" +
                " {\"id\":2,\"codigo_atc\":\"atc2\",\"descripcion_atc\":\"Esta es otra descripción HT_ATC_NAME\"}]}";
        System.out.println(json);
        return json;
    }

    private String createJSONRocord() {
        String json = "{\"tableName\":\"helper_table_atc\",\"fields\":{\"id\":1,\"codigo_atc\":\"atc1\",\"descripcion_atc\":\"Esta es una descripción HT_ATC_NAME\"}}";
        System.out.println(json);
        return json;
    }

    private String createJSONRocord02() {
        String json = "{\"tableName\":\"helper_table_atc\",\"fields\":{\"id\":1,\"codigo_atc\":\"atc1\",\"descripcion_atc\":\"Esta es una descripción HT_ATC_NAME\",\"is_valid\":true}}";
        System.out.println(json);
        return json;
    }

    private JSONHelperTableRecord createDummyRecord() {
        Map<String, String> records = new HashMap<>();
        records.put("id", "1");
        records.put("cod", "A");

        JSONHelperTableRecord jsonHelperTableRecord = new JSONHelperTableRecord();
        jsonHelperTableRecord.setFields(records);
        jsonHelperTableRecord.setTableId(1);

        return jsonHelperTableRecord;
    }

    private JSONHelperTableRecords createHelperTableDummyRecords() {

        Map<String, String> record1 = new HashMap<>();
        record1.put("id", "1");
        record1.put("cod", "A");

        Map<String, String> record2 = new HashMap<>();
        record2.put("id", "2");
        record2.put("cod", "B");

        List<Map<String, String>> records = Arrays.asList(record1, record2);

        JSONHelperTableRecords jsonHelperTableRecord = new JSONHelperTableRecords();
        jsonHelperTableRecord.setRecords(records);
        jsonHelperTableRecord.setTableId(1);

        return jsonHelperTableRecord;
    }

    private String createJSONManyRecords() {
        return "{\"tableName\":\"helper_table_dci\",\"records\":[{\"id\":1,\"description\":\"descripcion dci 1\",\"creation_date\":null,\"user_register\":1,\"is_valid\":true,\"delete_date\":null}, \n" +
                " {\"id\":2,\"description\":\"descripcion dci 2\",\"creation_date\":null,\"user_register\":1,\"is_valid\":true,\"delete_date\":null}]}";
    }
}