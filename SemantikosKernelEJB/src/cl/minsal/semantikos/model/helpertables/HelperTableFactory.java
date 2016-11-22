package cl.minsal.semantikos.model.helpertables;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJBException;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cl.minsal.semantikos.kernel.util.StringUtils.underScoreToCamelCaseJSON;

/**
 * Esta enumeración almacena todas las tablas auxiliares existentes.
 */
public class HelperTableFactory {

    private static final Logger logger = LoggerFactory.getLogger(HelperTableFactory.class);

    public static String ES_UN_MAPEO_DE = "Es un mapeo de";

    public static String ISP = "ISP";

    public static String ATC = "ATC";

    /**
     * Este método es responsable de crear una lista de relaciones a partir de un arreglo json de relaciones.
     * <code>
     * {"tableName":"smtk_helper_table_atc","records":[{"description":"A01AC03"},
     * {"description":"A01AB16"},
     * {"description":"A01AB17"},
     * {"description":"A02BC53"}]}
     * </code>
     *
     * @param jsonExpression La expresión que contiene un arreglo JSON de RD_DTO.
     * @param helperTable    La tabla auxiliar a la que pertenecen los registros.
     *
     * @return Una lista de <code>HelperTableRecord</code>.
     */
    public List<HelperTableRecord> createHelperTableRecordsFromJSON(HelperTable helperTable, String jsonExpression) {

        ObjectMapper mapper = new ObjectMapper();
        HelperTableRecordDTO dtoRecords;
        try {

            dtoRecords = mapper.readValue(underScoreToCamelCaseJSON(jsonExpression), HelperTableRecordDTO.class);
        } catch (IOException e) {
            String errorMsg = "No se pudo parsear el RelationshipDefinition desde un JSON.";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

        List<HelperTableRecord> helperTableRecords = new ArrayList<>();
        for (Map<String, String> mapRecord : dtoRecords.getRecords()) {
            HelperTableRecord helperTableRecord = new HelperTableRecord(helperTable, mapRecord);
            /**
             * Se setea el id desde el fields para ser utilizado por el custom converter
             */
            helperTableRecord.setId(new Long(helperTableRecord.getFields().get("id")));
            helperTableRecords.add(helperTableRecord);
        }

        return helperTableRecords;
    }

    /**
     * Este método es responsable de crear una simple Tabla Auxiliar, no persistida.
     *
     * @return Un instancia fresca de una tabla auxiliar dummy.
     */
    public HelperTable createDummyHelperTable() {

        Collection<HelperTableColumn> columns = new ArrayList<>();
        columns.add(new HelperTableColumn("DESCRIPTION", false, true, false));
        columns.add(new HelperTableColumn("ID", false, false, false));

        return new HelperTable("HT Dummy", "Dummy", "dummy", columns);
    }

}

class HelperTableRecordDTO {

    private String tableName;

    private List<Map<String, String>> records;

    public HelperTableRecordDTO() {
        this.tableName = "";
        this.records = new ArrayList<>();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Map<String, String>> getRecords() {
        return records;
    }

    public void setRecords(@NotNull List<Map<String, String>> records) {
        if (records != null) {
            this.records = records;
        }
    }
}
