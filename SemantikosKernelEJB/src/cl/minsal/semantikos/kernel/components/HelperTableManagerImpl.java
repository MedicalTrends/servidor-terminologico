package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.HelperTableDAO;
import cl.minsal.semantikos.model.User;
import cl.minsal.semantikos.model.businessrules.HelperTableSearchBR;
import cl.minsal.semantikos.model.helpertables.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.Reader;
import java.util.*;


/**
 * Este manager es responsable de proveer acceso a las distintas tablas auxiliares.
 * <p>
 * Las tablas auxiliares se han implementado de manera estática, es decir, se maneja una lista estática de las tablas
 * auxiliares.
 * </p>
 *
 * @author Andrés Farías
 * @see HelperTableManager
 */
@Stateless
public class HelperTableManagerImpl implements HelperTableManager {

    private static final Logger logger = LoggerFactory.getLogger(HelperTableManagerImpl.class);

    @EJB
    private HelperTableDAO helperTableDAO;

    @Override
    public HelperTableRecord insertRecord(HelperTable helperTable, HelperTableRecord record, User user) {
        return helperTableDAO.insertRecord(helperTable,record,user);
    }

    @Override
    public Collection<HelperTable> getHelperTables() {

        /* Esto se resuelve con delegación sobre el Factory, mientras las tablas estén en duro */
        return helperTableDAO.getHelperTables();
    }

    @Override
    public List<HelperTableRecord> getAllRecords(HelperTable helperTable, List<String> columnNames) {

        List<HelperTableRecord> allRecords = helperTableDAO.getAllRecords(helperTable);

        /* Se aplican reglas de negocio sobre los resultados retornados */
        new HelperTableSearchBR().applyPostActions(allRecords);
        logger.debug("Se recuperan " + allRecords.size() + " registros de la tabla " + helperTable);

        return allRecords;
    }

    @Override
    public List<HelperTableRecord> getAllRecords(HelperTable helperTable) {

        /* Como no se especifican las columnas a recuperar, se retornan todas las visibles y las de sistema */
        List<String> returnableColumns = helperTable.getShowableColumnsNames();
        for (HelperTableColumn systemColumn : HelperTable.getSystemColumns()) {
            returnableColumns.add(systemColumn.getColumnName());
        }

        /* Se delega en el método único para recuperar registros desde las tablas auxiliares */
        return getAllRecords(helperTable, returnableColumns);
    }

    @Override
    public List<HelperTableRecord> searchRecords(HelperTable helperTable, String columnName, String pattern, boolean validity) {

        /* Se validan las pre-condiciones de búsqueda */
        new HelperTableSearchBR().validatePreConditions(helperTable, columnName, pattern);

        /* Se delega la búsqueda al DAO, ya que pasaron las pre-condiciones */
        return helperTableDAO.findRecordsByPattern(helperTable, columnName, pattern, validity);
    }

    @Override
    public List<HelperTableRecord> searchRecords(HelperTable helperTable, List<String> searchColumns, String pattern, boolean validity) {

        List<HelperTableRecord> records = new ArrayList<>();

        for (String searchColumn : searchColumns) {
            records.addAll(searchRecords(helperTable, searchColumn, pattern, validity));
        }

        return records;
    }

    @Override
    public HelperTableRecord getRecord(long recordId) {
        return helperTableDAO.getHelperTableRecordFromId(recordId);
    }

    @Override
    public HelperTableRecord getRecord(HelperTable helperTable, long recordId) {
        return helperTableDAO.getHelperTableRecordFromId(helperTable, recordId);
    }

    @Override
    public HelperTable findHelperTableByID(long id) {
        return helperTableDAO.getHelperTableByID(id);
    }

    @Override
    public HelperTableImportReport loadFromFile(HelperTable helperTable, LoadMode mode, Reader in, User user) {

        /* Se cargan los datos desde el archivo */
        HelperTableImportReport helperTableReport = new HelperTableImportReport(helperTable, user);
        Iterable<CSVRecord> records;
        try {
            records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
        } catch (IOException e) {
            logger.error("Error al procesar archivo CSV para importación de tabla auxiliar.", e);
            helperTableReport.setStatus(LoadStatus.CANCELED);
            helperTableReport.appendException(e);

            return helperTableReport;
        }

        /* Se cargan los registros contenidos en el archivo CSV */
        List<HelperTableRecord> loadedRecords = loadRecordsFromCSV(helperTable, records);
        helperTableReport.setCSVLoadedRecords(loadedRecords);
        logger.info("Se han cargado " + loadedRecords.size() + " registros!");

        /* Se valida que los registros cargados tengan las mismas columnas que la tabla auxiliar */
        List<String> missingColumns = getMissingColumns(helperTable, extractLoadedColumns(loadedRecords));
        if (missingColumns.size() > 0) {
            logger.info("Problemas al cargar las columnas: " + missingColumns);
            helperTableReport.setStatus(LoadStatus.CANCELED);
            helperTableReport.appendException(new MissingColumnsException(missingColumns));
        }

        /* Ahora se realiza la transacción completa */
        else {
            loadHelperTable(helperTable, loadedRecords, mode, helperTableReport, user);
        }

        return helperTableReport;
    }

    /**
     * Este método es responsable de determinar las columnas que se intentan cargar en una tabla auxiliar que no
     * existen
     * en dicha tabla auxiliar.
     *
     * @param helperTable   La tabla auxiliar en la que se quiere hacer el import.
     * @param loadedColumns Las columnas que se desean insertar.
     *
     * @return La lista de las columnas que faltan en la Tabla Auxiliar.
     */
    private List<String> getMissingColumns(HelperTable helperTable, List<String> loadedColumns) {

        List<String> missingColumns = new ArrayList<>();

        for (String loadedColumn : loadedColumns) {
            if (!helperTable.getAllColumnsNames().contains(loadedColumn)) {
                missingColumns.add(loadedColumn);
            }
        }

        return missingColumns;
    }

    /**
     * Este método es responsable de recuperar el nombre de las columnas que contienen los archivos cargados.
     *
     * @param loadedRecords Los registros cargados.
     *
     * @return Una lista con los nombres de las columnas.
     */
    private List<String> extractLoadedColumns(@NotNull List<HelperTableRecord> loadedRecords) {

        /* Si la lista de registros está vacía es seguro que no vienen los títulos */
        if (loadedRecords.isEmpty()) {
            return Collections.emptyList();
        }

        /* Se asume que todos los registros contienen los títulos, en particular el primero */
        HelperTableRecord helperTableRecord = loadedRecords.get(0);

        /* Cada registro tiene un mapa de nombre de columna - valor, luego recuperamos la llave que corresponde al nombre de la columna */
        return new ArrayList<>(helperTableRecord.getFields().keySet());
    }

    /**
     * Este método es responsable de delegar la carga de los registros de una tabla auxiliar en función del modo
     * seleccionado.
     *
     * @param helperTable       La tabla en la cual se realiza la carga.
     * @param loadedRecords     Los registros a cargar.
     * @param mode              El modo de carga.
     * @param helperTableReport El reporte de la carga.
     * @param user              El usuario que realiza la carga.
     */
    private void loadHelperTable(HelperTable helperTable, List<HelperTableRecord> loadedRecords, LoadMode mode, HelperTableImportReport helperTableReport, User user) {

        /* Se delega al mecanismo apropiado según el modo */
        switch (mode) {
            case INITIAL_LOAD:
                loadHelperTableFromScratch(helperTable, loadedRecords, helperTableReport, user);
                break;
        }
    }

    /**
     * Este método es responsable de cargar todos los registros en la tabla auxiliar, eliminando todos los datos
     * existentes previamente.
     *
     * @param helperTable       La tabla que se desea cargar.
     * @param loadedRecords     Los registros a cargar.
     * @param helperTableReport El reporte de la carga.
     * @param user              El usuario que realiza la carga.
     */
    private void loadHelperTableFromScratch(HelperTable helperTable, List<HelperTableRecord> loadedRecords, HelperTableImportReport helperTableReport, User user) {

        /* Se insertan todos los registros */
        long insertedRecords = 0;
        for (HelperTableRecord loadedRecord : loadedRecords) {
            try {
                helperTableDAO.insertRecord(helperTable, loadedRecord, user);
                insertedRecords++;
            } catch (EJBException ejbE) {
                logger.error("Error al insertar un registro: ", ejbE);
                helperTableReport.appendException(ejbE);
            }
        }

        helperTableReport.setInsertedRecords(insertedRecords);
    }

    private List<HelperTableRecord> loadRecordsFromCSV(HelperTable helperTable, Iterable<CSVRecord> records) {
        List<HelperTableRecord> loadedRecords = new ArrayList<>();
        boolean firstTime = true;

        for (CSVRecord record : records) {

            /* El primer loop es para recuperar los nombres de las columnas */
            String[] columnNames = new String[0];
            if (firstTime) {
                int size = record.size();
                columnNames = new String[size];

                for (int i = 0; i < size; i++) {
                    columnNames[i] = record.get(1);
                }

                firstTime = false;
            }

            /* Los siguientes loops recogen la información */
            else {

                /* El registro que se creará a partir de la lectura de este registro */
                HelperTableRecord helperTableRecord = new HelperTableRecord(helperTable, new HashMap<String, String>());
                for (String columnName : columnNames) {
                    String columnValue = record.get(columnName);
                    helperTableRecord.addField(columnName, columnValue);
                }

                loadedRecords.add(helperTableRecord);
            }


        }
        return loadedRecords;
    }
}
