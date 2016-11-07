package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.helpertables.*;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import java.io.IOException;
import java.math.BigDecimal;
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
            logger.error("Error al realizar una consulta sobre las tablas auxiliares", e);
        }

        return records;
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
                if(jsonExpression != null) {
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


        return this.getAllRecords(helperTable);
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


    public long persistAuxilary(long idRecord, long idTableName){

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
            idPersist=rs.getLong(1);
            if (idPersist==-1){
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
            idAuxiliary=rs.getLong(1);
            if (idAuxiliary==-1){
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
            idAuxiliary=rs.getLong(1);
            if (idAuxiliary==-1){
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
