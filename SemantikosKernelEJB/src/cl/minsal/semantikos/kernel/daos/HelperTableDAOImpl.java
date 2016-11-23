package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.User;
import cl.minsal.semantikos.model.helpertables.*;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import static java.util.Collections.emptyList;

/**
 * @author Andrés Farías
 */
@Stateless
public class HelperTableDAOImpl implements HelperTableDAO {

    /** Logger de la clase */
    private static final Logger logger = LoggerFactory.getLogger(HelperTableDAOImpl.class);

    @EJB
    HelperTableRecordFactory helperTableRecordFactory;

    private Map<Long, HelperTable> helperTablesMap;

    public HelperTableDAOImpl() {
        this.helperTablesMap = new HashMap<>();
    }

    @Override
    public HelperTableRecord insertRecord(HelperTable helperTable, HelperTableRecord record, User user) {

        /*
         * La inserción de registros se hace indicando:
         *   - la tabla auxiliar (por su nombre de tabla).
         *   - un arreglo con todos los nombres de los campos.
         *   - un arreglo con los valores de los campos.
         *   - ID usuario que realiza la carga.
         */
        String selectRecord = "{call semantikos.helper_tables_insert_record2(?,?,?)}";
        ConnectionBD connectionBD = new ConnectionBD();
        try (Connection connection = connectionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(selectRecord)) {

            /* Se agregan las columnas y valores de sistema */
            //record.addField("id_user", Long.toString(user.getIdUser()));
            //record.addField("creation_date", new Timestamp(System.currentTimeMillis()).toString());

            /* Se preparan los parámetros de la función */
            Map<String, String> recordFields = record.getFields();
            Array column_names = connection.createArrayOf("text", recordFields.keySet().toArray(new String[recordFields.size()]));
            Array column_values = connection.createArrayOf("text", recordFields.values().toArray(new String[recordFields.size()]));

            /* Se prepara y realiza la consulta */
            callableStatement.setString(1, helperTable.getName());
            callableStatement.setArray(2, column_names);
            callableStatement.setArray(3, column_values);
            //callableStatement.setLong(4, user.getIdUser());

            ResultSet rs =callableStatement.executeQuery();

            rs.next();
            record.setId(rs.getBigDecimal(1).longValue());

            return record;

        } catch (SQLException e) {
            logger.error("Error al realizar una inserción en las tablas auxiliares", e);
            throw new EJBException(e);
        }


    }

    @Override
    public Map<String, String> getRecord(HelperTable helperTable, long idRecord) {

        Map<String, String> record = new HashMap<>();
        ConnectionBD connectionBD = new ConnectionBD();

        String selectRecord = "{call semantikos.get_record(?)}";
        try (Connection connection = connectionBD.getConnection();
             CallableStatement callableStatement = connection.prepareCall(selectRecord)) {

            /* Se prepara y realiza la consulta */
            callableStatement.setLong(1, idRecord);
            ResultSet resultSet = callableStatement.executeQuery();

            /* Se recuperan los valores de las columnas de la tabla auxiliar */
            for (HelperTableColumn helperTableColumn : helperTable.getColumns()) {
                String columnName = helperTableColumn.getColumnName();
                String columnValue = resultSet.getString(columnName);

                record.put(columnName, columnValue);
            }

        } catch (SQLException e) {
            logger.error("Error al realizar una transacción sobre las tablas auxiliares", e);
        }

        return record;
    }

    @Override
    public List<Map<String, String>> findRecordsByPattern(HelperTable helperTable, String pattern) {

        List<Map<String, String>> records = new ArrayList<>();
        ConnectionBD connectionBD = new ConnectionBD();

        // TODO: Crear esta función y las tablas.
        String selectRecord = "{call semantikos.find_records_by_pattern(?,?)}";
        try (Connection connection = connectionBD.getConnection();
             CallableStatement preparedStatement = connection.prepareCall(selectRecord)) {

            /* Se prepara y realiza la consulta */
            preparedStatement.setString(1, helperTable.getName());
            preparedStatement.setString(2, pattern);
            ResultSet resultSet = preparedStatement.executeQuery();

            /* Por cada elemento del resultset se extrae un registro */
            while (resultSet.next()) {

                Map<String, String> record = new HashMap<>();
                /* Se recuperan los valores de las columnas de la tabla auxiliar */
                for (HelperTableColumn helperTableColumn : helperTable.getColumns()) {
                    String columnName = helperTableColumn.getColumnName();
                    String columnValue = resultSet.getString(columnName);

                    record.put(columnName, columnValue);
                }

                records.add(record);
            }
        } catch (SQLException e) {
            String errorMsg = "Error al realizar una consulta sobre las tablas auxiliares";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg);
        }

        return records;
    }

    @Override
    public List<HelperTableRecord> findRecordsByPattern(HelperTable helperTable, String columnName, String pattern, boolean validity) {

        List<Map<String, String>> records = new ArrayList<>();
        ConnectionBD connectionBD = new ConnectionBD();

        /*
        semantikos.find_records_by_pattern(
            tablename text,
            search_column,
            return_columns text[],
            pattern text)
         */
        String selectRecord = "{call semantikos.find_records_by_pattern(?,?,?,?)}";
        String jsonResult = null;
        try (Connection connection = connectionBD.getConnection();
             CallableStatement preparedStatement = connection.prepareCall(selectRecord)) {

            /* Se prepara y realiza la consulta */
            preparedStatement.setString(1, helperTable.getName());
            List<String> showableColumnsNames = helperTable.getShowableColumnsNames();
            Array names = connection.createArrayOf("text", showableColumnsNames.toArray(new String[showableColumnsNames.size()]));
            preparedStatement.setString(2, columnName);
            preparedStatement.setArray(3, names);
            preparedStatement.setString(4, pattern);

            ResultSet resultSet = preparedStatement.executeQuery();

            /* Por cada elemento del resultset se extrae un registro */
            if (resultSet.next()) {
                jsonResult = resultSet.getString(1);
            }
        } catch (SQLException e) {
            String errorMsg = "Error al realizar una consulta sobre las tablas auxiliares";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg);
        }

        return new HelperTableFactory().createHelperTableRecordsFromJSON(helperTable, jsonResult);
    }

    @Override
    public List<HelperTableRecord> getAllRecords(HelperTable helperTable, List<String> columnNames) {

        ConnectionBD connectionBD = new ConnectionBD();
        List<HelperTableRecord> helperTableRecords = new ArrayList<>();

        String selectRecord = "{call semantikos.get_all_records_from_helper_table(?,?)}";
        try (Connection connection = connectionBD.getConnection();
             CallableStatement call = connection.prepareCall(selectRecord)) {

            /* Se envían los nombres de columna que se desea recuperar con un Arreglo */
            String[] columnsNames = columnNames.toArray(new String[columnNames.size()]);
            Array columnsNamesArray = connection.createArrayOf("text", columnsNames);

            /* Se prepara y realiza la consulta */
            call.setString(1, helperTable.getName());
            call.setArray(2, columnsNamesArray);
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                String jsonExpression = rs.getString(1);
                if (jsonExpression != null) {
                    helperTableRecords = this.helperTableRecordFactory.createHelperRecordsFromJSON(jsonExpression);
                    /**
                     * Se setea el id desde el fields para ser utilizado por el custom converter
                     */
                    for (HelperTableRecord helperTableRecord : helperTableRecords) {
                        helperTableRecord.setId(new Long(helperTableRecord.getFields().get("id")));
                    }
                } else {
                    helperTableRecords = emptyList();
                }
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        } catch (IOException e) {
            logger.error("Hubo un error procesar los resultados con JSON.", e);
            throw new EJBException(e);
        }

        return helperTableRecords;

    }

    @Override
    public List<HelperTableRecord> getRecords(HelperTable helperTable, List<String> columnNames, List<HelperTableWhereCondition> whereConditions) {

        ConnectionBD connectionBD = new ConnectionBD();
        List<HelperTableRecord> helperTableRecords = new ArrayList<>();

        String selectRecord = "{call semantikos.get_records_from_helper_table_where(?,?,?)}";
        try (Connection connection = connectionBD.getConnection();
             CallableStatement call = connection.prepareCall(selectRecord)) {

            /* Se envían los nombres de columna que se desea recuperar con un Arreglo */
            String[] columnsNames = columnNames.toArray(new String[columnNames.size()]);
            Array columnsNamesArray = connection.createArrayOf("text", columnsNames);

            /* Se envían las condiciones de columna en un Arreglo */
            String[] theWhereConditions = new String[whereConditions.size()];
            int i = 0;
            for (HelperTableWhereCondition whereCondition : whereConditions) {
                theWhereConditions[i++] = whereCondition.toString();
            }
            Array whereConditionsArray = connection.createArrayOf("text", theWhereConditions);



            /* Se prepara y realiza la consulta */
            call.setString(1, helperTable.getName());
            call.setArray(2, columnsNamesArray);
            call.setArray(3, whereConditionsArray);
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                String jsonExpression = rs.getString(1);
                if (jsonExpression != null) {
                    helperTableRecords = this.helperTableRecordFactory.createHelperRecordsFromJSON(jsonExpression);
                } else {
                    helperTableRecords = emptyList();
                }
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        } catch (IOException e) {
            logger.error("Hubo un error procesar los resultados con JSON.", e);
            throw new EJBException(e);
        }

        return helperTableRecords;
    }

    @Override
    public List<HelperTableRecord> getAllRecords(HelperTable helperTable) {

        /* Como no se especifican las columnas a recuperar, se retornan todas las visibles y las de sistema */
        List<String> returnableColumns = helperTable.getShowableColumnsNames();
        for (HelperTableColumn systemColumn : HelperTable.getSystemColumns()) {
            returnableColumns.add(systemColumn.getColumnName());
        }

        /* Luego se delega al método que recibe las columnas a retornar */
        return this.getAllRecords(helperTable, returnableColumns);
    }

    @Override
    public HelperTableRecord getHelperTableRecordFromId(long idHelperTableRecord) {

        ConnectionBD connectionBD = new ConnectionBD();

        String selectRecord = "{call semantikos.get_record_by_id_auxiliary(?)}";
        HelperTableRecord recordFromJSON;
        try (Connection connection = connectionBD.getConnection();
             CallableStatement call = connection.prepareCall(selectRecord)) {

            /* Se prepara y realiza la consulta */
            call.setLong(1, idHelperTableRecord);
            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                recordFromJSON = this.helperTableRecordFactory.createRecordFromJSON(rs.getString(1));
            } else {
                throw new EJBException("Error imposible en HelperTableDAOImpl");
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        } catch (IOException e) {
            logger.error("Hubo un error procesar los resultados con JSON.", e);
            throw new EJBException(e);
        }

        return recordFromJSON;
    }

    @Override
    public HelperTableRecord getHelperTableRecordFromId(HelperTable helperTable, long idHelperTableRecord) {

        ConnectionBD connectionBD = new ConnectionBD();

        String selectRecord = "{call semantikos.get_record_by_id_auxiliary(?,?)}";
        HelperTableRecord recordFromJSON;
        try (Connection connection = connectionBD.getConnection();
             CallableStatement call = connection.prepareCall(selectRecord)) {

            /* Se prepara y realiza la consulta */
            call.setLong(1, helperTable.getId());
            call.setLong(2, idHelperTableRecord);
            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                recordFromJSON = this.helperTableRecordFactory.createRecordFromJSON(rs.getString(1));
            } else {
                throw new EJBException("Error imposible en HelperTableDAOImpl");
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        } catch (IOException e) {
            logger.error("Hubo un error procesar los resultados con JSON.", e);
            throw new EJBException(e);
        }

        return recordFromJSON;
    }

    @Override
    public Collection<HelperTable> getHelperTables() {

        ConnectionBD connectionBD = new ConnectionBD();
        String selectRecord = "{call semantikos.get_helper_tables()}";
        List<HelperTable> recordFromJSON;
        try (Connection connection = connectionBD.getConnection();
             CallableStatement call = connection.prepareCall(selectRecord)) {

            /* Se prepara y realiza la consulta */
            call.execute();
            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                recordFromJSON = this.helperTableRecordFactory.createHelperTablesFromJSON(rs.getString(1));
            } else {
                throw new EJBException("Error imposible en HelperTableDAOImpl");
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        } catch (IOException e) {
            logger.error("Hubo un error procesar los resultados con JSON.", e);
            throw new EJBException(e);
        }

        return recordFromJSON;
    }

    @Override
    public HelperTable getHelperTableByID(long id) {

        /* Si no se ha recuperado se recupera ahora */
        if (!helperTablesMap.containsKey(id)) {

            ConnectionBD connectionBD = new ConnectionBD();
            String selectRecord = "{call semantikos.get_helper_table_by_id(?)}";
            HelperTable recordFromJSON;
            try (Connection connection = connectionBD.getConnection();
                 CallableStatement call = connection.prepareCall(selectRecord)) {

                /* Se prepara y realiza la consulta */
                call.setLong(1, id);
                call.execute();
                ResultSet rs = call.getResultSet();
                if (rs.next()) {
                    recordFromJSON = this.helperTableRecordFactory.createHelperTableFromJSON(rs.getString(1));
                } else {
                    throw new EJBException("Error imposible en HelperTableDAOImpl");
                }
                rs.close();
            } catch (SQLException e) {
                logger.error("Hubo un error al acceder a la base de datos.", e);
                throw new EJBException(e);
            } catch (IOException e) {
                logger.error("Hubo un error procesar los resultados con JSON.", e);
                throw new EJBException(e);
            }

            /* Se agrega al mapa de tablas */
            helperTablesMap.put(recordFromJSON.getId(), recordFromJSON);
        }

        /* Se retorna la tabla desde le mapa */
        return helperTablesMap.get(id);
    }


    public long persistAuxilary(long idRecord, long idTableName) {

        ConnectionBD connectionBD = new ConnectionBD();
        String persistAuxiliary = "{call semantikos.create_auxiliary(?,?)}";
        long idPersist;
        try (Connection connection = connectionBD.getConnection();
             CallableStatement call = connection.prepareCall(persistAuxiliary)) {

                /* Se prepara y realiza la consulta */
            call.setLong(1, idRecord);
            call.setLong(2, idTableName);
            call.execute();
            ResultSet rs = call.getResultSet();
            rs.next();
            idPersist = rs.getLong(1);
            if (idPersist == -1) {
                throw new EJBException("Error, no se pudo persistir auxiliary");
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        }
        return idPersist;
    }

    @Override
    public long updateAuxiliary(Relationship relationship) {

        ConnectionBD connectionBD = new ConnectionBD();
        String updateAuxiliary = "{call semantikos.update_auxiliary_by_relationship(?,?)}";
        long idAuxiliary;
        try (Connection connection = connectionBD.getConnection();
             CallableStatement call = connection.prepareCall(updateAuxiliary)) {

            /* Se prepara y realiza la consulta */
            call.setLong(1, relationship.getId());
            call.setLong(2, relationship.getTarget().getId());
            call.execute();
            ResultSet rs = call.getResultSet();
            rs.next();
            idAuxiliary = rs.getLong(1);
            if (idAuxiliary == -1) {
                throw new EJBException("Error, no se pudo persistir auxiliary");
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        }
        return idAuxiliary;
    }

    @Override
    public long updateAuxiliary(RelationshipAttribute relationshipAttribute) {

        ConnectionBD connectionBD = new ConnectionBD();
        String updateAuxiliary = "{call semantikos.update_auxiliary_by_relationship_attribute(?,?)}";
        long idAuxiliary;
        try (Connection connection = connectionBD.getConnection();
             CallableStatement call = connection.prepareCall(updateAuxiliary)) {

            /* Se prepara y realiza la consulta */
            call.setLong(1, relationshipAttribute.getIdRelationshipAttribute());
            call.setLong(2, relationshipAttribute.getTarget().getId());
            call.execute();
            ResultSet rs = call.getResultSet();
            rs.next();
            idAuxiliary = rs.getLong(1);
            if (idAuxiliary == -1) {
                throw new EJBException("Error, no se pudo persistir auxiliary");
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        }
        return idAuxiliary;
    }


}

