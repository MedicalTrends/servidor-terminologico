package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.User;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRecord;
import cl.minsal.semantikos.model.helpertables.HelperTableWhereCondition;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;

import javax.ejb.Local;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Andrés Farías
 */
@Local
public interface HelperTableDAO {

    /**
     * Este método es responsable de insertar un registro en la tabla auxiliar.
     *
     * @param helperTable La tabla auxiliar donde se cargará el registro.
     * @param record      El registro que se desea insertar.
     * @param user        El usuario que realiza la carga.
     */
    public HelperTableRecord insertRecord(HelperTable helperTable, HelperTableRecord record, User user);

    /**
     * Este método es responsable de recuperar un registro desde una tabla auxiliar <code>helperTable</code>.
     *
     * @param helperTable La tabla auxiliar desde la cual se recupera el registro.
     * @param idRecord    El ID del registro a recuperar en la tabla <code>helperTable</code>.
     *
     * @return Un registro constituido como un mapping entre nombres de columna y valores.
     */
    public Map<String, String> getRecord(HelperTable helperTable, long idRecord);

    /**
     * Este método es responsable de buscar registros en una tabla auxiliar que contengan el patrón en alguna de sus
     * columnas buscables.
     *
     * @param helperTable La tabla en la cual buscar.
     * @param pattern     El patrón a buscar en los registros.
     *
     * @return Los registros que satisfacen el criterio de búsqueda.
     */
    public List<Map<String, String>> findRecordsByPattern(HelperTable helperTable, String pattern);

    /**
     * Este método es responsable de recuperar registros de una tabla bajo ciertos criterios, tales como un patrón de
     * búsqueda sobre una columna, y la validez de los registros.
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
    public List<HelperTableRecord> findRecordsByPattern(HelperTable helperTable, String columnName, String pattern, boolean validity);

    /**
     * Este método es responsable de recuperar todos los registros de una tabla auxiliar, recuperando sólo las columnas
     * indicadas como <i>mostrables</i> (showables == <code>true</code>)en la tabla.
     *
     * @param helperTable La tabla cuyos registros son recuperados.
     *
     * @return Una lista de registros (en forma de mappings) de la tabla indicada.
     */
    public List<HelperTableRecord> getAllRecords(HelperTable helperTable);

    /**
     * Este método es responsable de recuperar todos los registros de una tabla auxiliar, recuperando sólo las columnas
     * indicadas.
     *
     * @param helperTable La tabla cuyos registros son recuperados.
     * @param columnNames Las columnas de los registros a recuperar (más el ID).
     *
     * @return Una lista de registros (en forma de mappings) de la tabla indicada.
     */
    public List<HelperTableRecord> getAllRecords(HelperTable helperTable, List<String> columnNames);

    /**
     * Este método es responsable de recuperar todos lso registros de una tabla auxiliar que cumplen una cierta
     * condición respecto a sus columnas.
     *
     * @param helperTable     La tabla cuyos registros son recuperados.
     * @param columnNames     Las columnas de los registros a recuperar (más el ID).
     * @param whereConditions Condiciones que deben satisfacer los registros.
     *
     * @return Una lista de registros (en forma de mappings) de la tabla indicada que cumplen las condiciones.
     */
    public List<HelperTableRecord> getRecords(HelperTable helperTable, List<String> columnNames, List<HelperTableWhereCondition> whereConditions);

    /**
     * Este método es responsable de recuperar un registro desde una tabla auxiliar/plana.
     *
     * @param idHelperTableRecord El ID del HelperTableRecord.
     *
     * @return Un registro (<code>HelperTableRecord</code>).
     */
    public HelperTableRecord getHelperTableRecordFromId(long idHelperTableRecord);

    public HelperTableRecord getHelperTableRecordFromId(HelperTable helperTable, long idHelperTableRecord);

    /**
     * Este método recupera todas las tablas auxiliares.
     *
     * @return Las tablas auxiliares.
     */
    public Collection<HelperTable> getHelperTables();

    /**
     * Este método es responsable de recuperar una tabla auxiliar por su ID.
     *
     * @param id el ID en la bdd
     *
     * @return La tabla auxiliar.
     */
    public HelperTable getHelperTableByID(long id);

    /**
     * Este metodo es encargado de persistir el valor seleccionado de un helper table
     *
     * @param idRecord    id del registro
     * @param idTableName id del nombre de la tabla
     */
    public long persistAuxilary(long idRecord, long idTableName);

    /**
     * Este metodo es encargado de actualizar el valor seleccionado de un helper table
     *
     * @param relationship la relacion
     */
    public long updateAuxiliary(Relationship relationship);

    /**
     * Este metodo es encargado de actualizar el valor seleccionado de un helper table
     *
     * @param relationshipAttribute la relacion
     */
    public long updateAuxiliary(RelationshipAttribute relationshipAttribute);
}
