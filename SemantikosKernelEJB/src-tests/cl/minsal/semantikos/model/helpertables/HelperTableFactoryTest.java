package cl.minsal.semantikos.model.helpertables;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class HelperTableFactoryTest {

    @Test
    public void testCreateHelperTableRecordsFromJSON() throws Exception {

        String json = "{\"tableName\":\"smtk_helper_table_atc\",\"records\":[{\"description\":\"A01AC03\"}, \n" +
                "     {\"description\":\"A01AB16\"}, \n" +
                "     {\"description\":\"A01AB17\"},\n" +
                "     {\"description\":\"A02BC53\"}]}";


        HelperTableFactory helperTableFactory = new HelperTableFactory();
        HelperTable dummyHelperTable = helperTableFactory.createDummyHelperTable();

        List<HelperTableRecord> helperTableRecordsFromJSON = helperTableFactory.createHelperTableRecordsFromJSON(dummyHelperTable, json);

        assertEquals(4, helperTableRecordsFromJSON.size());
    }
}