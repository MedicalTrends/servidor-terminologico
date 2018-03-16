package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.exceptions.RowInUseException;
import cl.minsal.semantikos.model.externtables.*;
import cl.minsal.semantikos.model.helpertables.*;

import javax.ejb.Remote;
import java.util.List;

/**
 * Created by BluePrints Developer on 09-01-2017.
 */
@Remote
public interface ExternTablesManager {


    List<ExternTable> getTables();

    List<ExternTableColumn> getColumns();

    List<ExternTableReference> getReferences(ExternTable externTable);

    List<ExternTableRow> getRows(ExternTable table);

    List<ExternTableRelationship> getRelationships(ExternTableRow row);

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

    ExternTableRow getRowById(long idRow);

    ExternTableRow getRowBy(ExternTable helperTable, long idRow);

    /**
     * Este método es responsable de recuperar registros de una tabla auxiliar de acuerdo a un patrón de búsqueda sobre
     * una de sus descripcion.
     *
     * @param externTable La tabla sobre la cual se realiza la búsqueda.
     * @param pattern     El patrón utilizado para la búsqueda.
     *
     * @return La lista de registros en la tabla <code>helperTable</code> que cumplen con el <code>pattern</code> de
     * búsqueda.
     */
    List<ExternTableRow> searchRows(ExternTable externTable, String pattern);


    /**
     * Este método es responsable de recuperar registros de una tabla auxiliar de acuerdo a un patrón de búsqueda sobre:
     * Su descriṕción y campos adicionales
     *
     * @param externTable La tabla sobre la cual se realiza la búsqueda.
     * @param pattern     El patrón utilizado para la búsqueda.
     * @param pattern     Los nombres de los campos de búsqueda adicionales
     *
     * @return La lista de registros en la tabla <code>helperTable</code> que cumplen con el <code>pattern</code> de
     * búsqueda.
     */
    List<ExternTableRow> searchRows(ExternTable externTable, String pattern, List<String> columnNames);

    /*
    inserta una fila no persistida
     */
    HelperTableRow insertRow(HelperTableRow row, String username);



}
