package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.User;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableImportReport;
import cl.minsal.semantikos.model.helpertables.HelperTableRecord;
import cl.minsal.semantikos.model.helpertables.LoadMode;

import javax.validation.constraints.NotNull;
import java.io.Reader;
import java.util.Collection;
import java.util.List;

/**
 * @author Andrés Farías
 */
public interface HelperTableManager {

    /**
     * Este método es responsable de proveer una lista de objetos que representan las Tablas Auxiliares.
     *
     * @return Una lista de <code>HelperTable</code> disponibles.
     */
    public Collection<HelperTable> getHelperTables();

    /**
     * Este método es responsable de recuperar los registros (limitado a una cierta cantidad de columnas) de una cierta
     * tabla.
     *
     * @param helperTable La tabla cuyos registros se desea recuperar.
     * @param columnNames El nombre de las columnas que se desea recuperar.
     *
     * @return Una lista de registros de la tabla <code>helperTable</code> con las columnas <code>columnNames</code>
     * indicadas.
     */
    public List<HelperTableRecord> getAllRecords(@NotNull HelperTable helperTable, List<String> columnNames);

    /**
     * Este método es responsable de recuperar los registros de una cierta tabla.
     *
     * @param helperTable La tabla cuyos registros se desea recuperar.
     *
     * @return La lista de todos los registros de la tabla solicitada.
     */
    public List<HelperTableRecord> getAllRecords(HelperTable helperTable);

    /**
     * Este método es responsable de recuperar registros de una tabla auxiliar de acuerdo a un patrón de búsqueda sobre
     * una de sus columnas, y su validez.
     *
     * @param helperTable La tabla sobre la cual se realiza la búsqueda.
     * @param columnName  La columna sobre la cuál se realiza la búsqueda.
     * @param pattern     El patrón utilizado para la búsqueda.
     * @param validity    <code>true</code> si se quieren recuperar registros válidos y <code>false</code> si se quiere
     *                    recuperar todos los registros.
     *
     * @return La lista de registros en la tabla <code>helperTable</code> que cumplen con el <code>pattern</code> de
     * búsqueda.
     */
    public List<HelperTableRecord> searchRecords(HelperTable helperTable, String columnName, String pattern, boolean validity);

    /**
     * Este método es responsable de recuperar registros de una tabla auxiliar de acuerdo a un patrón de búsqueda sobre
     * una de sus columnas, y su validez.
     *
     * @param helperTable   La tabla sobre la cual se realiza la búsqueda.
     * @param searchColumns Las columnas sobre la cuales se realiza la búsqueda.
     * @param pattern       El patrón utilizado para la búsqueda.
     * @param validity      <code>true</code> si se quieren recuperar registros válidos y <code>false</code> si se
     *                      quiere recuperar todos los registros.
     *
     * @return La lista de registros en la tabla <code>helperTable</code> que cumplen con el <code>pattern</code> de
     * búsqueda.
     */
    public List<HelperTableRecord> searchRecords(HelperTable helperTable, List<String> searchColumns, String pattern, boolean validity);

    public HelperTableRecord getRecord(long recordId);

    public HelperTableRecord getRecord(HelperTable helperTable, long recordId);

    /**
     * Este método es responsable de recuperar una tabla dado su identificador.
     *
     * @param id El ID de la tabla
     *
     * @return La tabla auxiliar.
     */
    public HelperTable findHelperTableByID(long id);

    /**
     * Este método es responsable de realizar la importación de una Tabla Auxiliar con datos provenientes de un archivo
     * CSV.
     *
     * @param helperTable La tabla auxiliar que se actualizará
     * @param mode        El modo de importación
     * @param in          La entrada de datos.
     *
     * @return El reporte de la carga realizada.
     */
    public HelperTableImportReport loadFromFile(HelperTable helperTable, LoadMode mode, Reader in, User user);
}
