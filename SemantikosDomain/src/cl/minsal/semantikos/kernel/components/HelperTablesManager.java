package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.exceptions.RowInUseException;
import cl.minsal.semantikos.model.helpertables.*;
import cl.minsal.semantikos.model.users.User;

import java.util.List;

/**
 * Created by BluePrints Developer on 09-01-2017.
 */
public interface HelperTablesManager {

    HelperTable getById(long id);

    List<HelperTable> findAll();

    HelperTableColumn updateColumn(HelperTableColumn column);

    List<HelperTableDataType> getAllDataTypes();

    HelperTableColumn createColumn(HelperTableColumn column);

    List<HelperTableRow> getTableRows(long tableId);

    /*
    crea una nueva fila con campos por defecto para la tabla proporcionada
     */
    HelperTableRow createEmptyRow(Long tableId, String username);

    /*
    actualiza la fila. verifica que si se deja no valida, la fila no sea referencia de otras tablas
     */
    HelperTableRow updateRow(HelperTableRow row, String username) throws RowInUseException;

    List<ConceptSMTK> isRowUsed(HelperTableRow helperTableRow, int size, int page);

    int countIsRowUsed(HelperTableRow helperTableRow);

    HelperTableRow getRowById(long idRow);

    HelperTableRow getRowBy(HelperTable helperTable, long idRow);

    List<HelperTableRow> getRowBy(HelperTable helperTable, boolean valid);

    HelperTableColumn getColumnById(long idColumn);

    /**
     * Este método es responsable de recuperar registros de una tabla auxiliar de acuerdo a un patrón de búsqueda sobre
     * una de sus descripcion.
     *
     * @param helperTable La tabla sobre la cual se realiza la búsqueda.
     * @param pattern     El patrón utilizado para la búsqueda.
     *
     * @return La lista de registros en la tabla <code>helperTable</code> que cumplen con el <code>pattern</code> de
     * búsqueda.
     */
    List<HelperTableRow> searchRows(HelperTable helperTable, String pattern);

    /**
     * Este método es responsable de recuperar registros vigentes y no vigentes de una tabla auxiliar de acuerdo a un
     * patrón de búsqueda sobre una de sus descripcion.
     *
     * @param helperTable La tabla sobre la cual se realiza la búsqueda.
     * @param pattern     El patrón utilizado para la búsqueda.
     *
     * @return La lista de registros en la tabla <code>helperTable</code> que cumplen con el <code>pattern</code> de
     * búsqueda.
     */
    List<HelperTableRow> searchAllRows(HelperTable helperTable, String pattern);


    /**
     * Este método es responsable de recuperar registros de una tabla auxiliar de acuerdo a un patrón de búsqueda sobre:
     * Su descriṕción y campos adicionales
     *
     * @param helperTable La tabla sobre la cual se realiza la búsqueda.
     * @param pattern     El patrón utilizado para la búsqueda.
     * @param pattern     Los nombres de los campos de búsqueda adicionales
     *
     * @return La lista de registros en la tabla <code>helperTable</code> que cumplen con el <code>pattern</code> de
     * búsqueda.
     */
    List<HelperTableRow> searchRows(HelperTable helperTable, String pattern, List<String> columnNames);


    HelperTableImportReport loadFromFile(HelperTable helperTable, LoadMode loadModeSelected, User loggedUser);


    List<HelperTableRow> getValidTableRows(long id);

    List<HelperTable> getFullDatabase();

    List<HelperTable> getLiteDatabase();

    /*
    inserta una fila no persistida
     */
    HelperTableRow insertRow(HelperTableRow row, String username);

    /**
     * Este método es responsable de recuperar los registros hijos de un registro, dada una columna de búsqueda
     *
     * @param parentRow El registro padre
     * @param helperTableColumn  La columna de búsqueda
     *
     * @return La lista de registros en la tabla <code>helperTable</code> que cumplen con el <code>pattern</code> de
     * búsqueda.
     */
    List<HelperTableRow> getRelatedRows(HelperTableRow parentRow, HelperTableColumn helperTableColumn);

    /**
     * Este método es responsable de recuperar las columnas que referencian a otra tabla auxiliar, dada una tabla auxiliar
     *
     * @param helperTable La tabla auxiliar padre
     *
     * @return La lista de registros en la tabla <code>helperTable</code> que cumplen con el <code>pattern</code> de
     * búsqueda.
     */
    List<HelperTableColumn> getRelatedColumns(HelperTable helperTable);

}
