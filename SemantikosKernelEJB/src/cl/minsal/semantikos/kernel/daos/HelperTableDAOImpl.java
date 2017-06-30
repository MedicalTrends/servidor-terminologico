package cl.minsal.semantikos.kernel.daos;


import cl.minsal.semantikos.kernel.daos.mappers.BasicTypeMapper;
import cl.minsal.semantikos.kernel.daos.mappers.HelperTableMapper;
import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.helpertables.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;

import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blueprints on 9/16/2015.
 */
@Stateless
public class HelperTableDAOImpl implements Serializable, HelperTableDAO {

    /** Logger de la clase */
    private static final Logger logger = LoggerFactory.getLogger(HelperTableDAOImpl.class);


    @EJB
    HelperTableRecordFactory helperTableRecordFactory;

    @EJB
    HelperTableMapper helperTableMapper;

    @EJB
    ConceptDAO conceptDAO;

    @Override
    public List<HelperTable> getAllTables() {

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

                for (HelperTable table: recordFromJSON) {
                    if(table.getColumns()==null)
                        table.setColumns(new ArrayList<HelperTableColumn>());
                }

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
    public HelperTableColumn createColumn(HelperTableColumn column) {

        ConnectionBD connect = new ConnectionBD();
        String UPDATE = "{call semantikos.create_helper_table_column(?,?,?,?,?,?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(UPDATE)) {

            call.setLong(1, column.getHelperTableDataTypeId());
            call.setLong(2, column.getHelperTableId());
            call.setLong(3, column.getForeignKeyHelperTableId());
            call.setString(4,  column.getName());
            call.setBoolean(5,column.isForeignKey());
            call.setString(6,column.getDescription());

            ResultSet rs = call.executeQuery();

            if (rs.next()) {
                column.setId(rs.getLong(1));
            } else {
                String errorMsg = "La columna no fue creada. Esta es una situación imposible. Contactar a Desarrollo";
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }

        } catch (SQLException e) {
            logger.error("Error al crear la columnas:" + column, e);
        }

        return column;
    }

    @Override
    public HelperTableColumn updateColumn(HelperTableColumn column) {

        // update_helper_table_column

        ConnectionBD connect = new ConnectionBD();
        String UPDATE = "{call semantikos.update_helper_table_column(?,?,?,?,?,?,?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(UPDATE)) {

            call.setLong(1, column.getId());
            call.setLong(2, column.getHelperTableDataTypeId());
            call.setLong(3, column.getHelperTableId());
            call.setLong(4, column.getForeignKeyHelperTableId());
            call.setString(5,  column.getName());
            call.setBoolean(6,column.isForeignKey());
            call.setString(7,column.getDescription());

            call.execute();
        } catch (SQLException e) {
            logger.error("Error al actualizar columna:" + column, e);
        }

        return column;

    }


    @Override
    public List<HelperTableDataType> getAllDataTypes(){

        ConnectionBD connectionBD = new ConnectionBD();
        String selectRecord = "{call semantikos.get_helper_table_data_types()}";
        List<HelperTableDataType> recordFromJSON;
        try (Connection connection = connectionBD.getConnection();
             CallableStatement call = connection.prepareCall(selectRecord)) {

            /* Se prepara y realiza la consulta */
            call.execute();
            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                recordFromJSON = this.helperTableRecordFactory.createHelperTablesDataTypesFromJSON(rs.getString(1));
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
    public List<HelperTableRow> getTableRows(long tableId) {

        ConnectionBD connectionBD = new ConnectionBD();
        String selectRecord = "{call semantikos.get_helper_table_rows(?)}";
        List<HelperTableRow> recordFromJSON;
        try (Connection connection = connectionBD.getConnection();
             CallableStatement call = connection.prepareCall(selectRecord)) {

            call.setLong(1,tableId);
            /* Se prepara y realiza la consulta */
            call.execute();
            ResultSet rs = call.getResultSet();
            if (rs.next()) {

                String json = rs.getString(1);
                if(json==null)
                    return new ArrayList<>();

                recordFromJSON = this.helperTableRecordFactory.createHelperTableRowsFromJSON(json);

                for (HelperTableRow helperTableRow : recordFromJSON) {
                    for (HelperTableData cell : helperTableRow.getCells()) {
                        cell.setColumn(getColumnById(cell.getColumnId()));
                    }
                }

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
    public List<HelperTableRow> getValidTableRows(long tableId) {

        ConnectionBD connectionBD = new ConnectionBD();
        String selectRecord = "{call semantikos.get_valid_helper_table_rows(?)}";
        List<HelperTableRow> recordFromJSON;
        try (Connection connection = connectionBD.getConnection();
             CallableStatement call = connection.prepareCall(selectRecord)) {

            call.setLong(1,tableId);
            /* Se prepara y realiza la consulta */
            call.execute();
            ResultSet rs = call.getResultSet();
            if (rs.next()) {

                String json = rs.getString(1);
                if(json==null)
                    return new ArrayList<>();

                recordFromJSON = this.helperTableRecordFactory.createHelperTableRowsFromJSON(json);

                for (HelperTableRow helperTableRow : recordFromJSON) {
                    for (HelperTableData cell : helperTableRow.getCells()) {
                        cell.setColumn(getColumnById(cell.getColumnId()));
                    }
                }

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

    /*
    crea solo el elemento de la fila sin las celdas
     */
    @Override
    public HelperTableRow createRow(HelperTableRow row) {

        ConnectionBD connect = new ConnectionBD();
        String UPDATE = "{call semantikos.create_helper_table_row(?,?,?,?,?,?,?,?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(UPDATE)) {


            call.setLong(1, row.getHelperTableId());
            call.setString(2, row.getDescription());
            call.setTimestamp(3, row.getCreationDate());
            call.setString(4, row.getCreationUsername());
            call.setTimestamp(5, row.getLastEditDate());
            call.setString(6, row.getLastEditUsername());
            call.setTimestamp(7, row.getValidityUntil()!=null?new Timestamp(row.getValidityUntil().getTime()):null);
            call.setBoolean(8, row.isValid());

            ResultSet rs = call.executeQuery();



            if (rs.next()) {
                row.setId(rs.getLong(1));
            } else {
                String errorMsg = "La columna no fue creada. Esta es una situación imposible. Contactar a Desarrollo";
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }

        } catch (SQLException e) {
            logger.error("Error al crear la row:" + row, e);
        }

        return row;
    }

    @Override
    public HelperTableData createData(HelperTableData cell) {


        ConnectionBD connect = new ConnectionBD();
        String UPDATE = "{call semantikos.create_helper_table_data(?,?,?,?,?,?,?,?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(UPDATE)) {

            call.setString(1, cell.getStringValue());

            call.setDate(2, cell.getDateValue()==null?null:new Date(cell.getDateValue().getTime()));

            if(cell.getFloatValue()==null)
                call.setNull(3, Types.REAL);
            else
                call.setFloat(3, cell.getFloatValue());

            if(cell.getIntValue()==null)
                call.setNull(4, Types.BIGINT);
            else
                call.setLong(4, cell.getIntValue());

            if(cell.getBooleanValue()==null)
                call.setNull(5, Types.BOOLEAN);
            else
                call.setBoolean(5,cell.getBooleanValue());

            if(cell.getForeignKeyValue()==null)
                call.setNull(6, Types.BIGINT);
            else
                call.setLong(6, cell.getForeignKeyValue());

            call.setLong(7,cell.getRowId());
            call.setLong(8,cell.getColumn().getId());


            ResultSet rs = call.executeQuery();

            if (rs.next()) {
                cell.setId(rs.getLong(1));
            } else {
                String errorMsg = "La columna no fue creada. Esta es una situación imposible. Contactar a Desarrollo";
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }


        } catch (SQLException e) {
            logger.error("Error al crear la row:" + cell, e);
        }

        return cell;

    }


    private HelperTableData updateData(HelperTableData cell) {


        ConnectionBD connect = new ConnectionBD();
        String UPDATE = "{call semantikos.update_helper_table_data(?,?,?,?,?,?,?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(UPDATE)) {

            call.setLong(1, cell.getId());
            call.setString(2, cell.getStringValue());
            call.setDate(3, cell.getDateValue()==null?null:new Date(cell.getDateValue().getTime()));

            if(cell.getFloatValue()==null)
                call.setNull(4, Types.REAL);
            else
                call.setFloat(4, cell.getFloatValue());

            if(cell.getIntValue()==null)
                call.setNull(5, Types.BIGINT);
            else
                call.setLong(5, cell.getIntValue());

            if(cell.getBooleanValue()==null)
                call.setNull(6, Types.BOOLEAN);
            else
                call.setBoolean(6,cell.getBooleanValue());
            if(cell.getForeignKeyValue()==null){
                call.setNull(7, Types.BIGINT);
            }else{
                call.setLong(7, cell.getForeignKeyValue());
            }


            call.execute();

        } catch (SQLException e) {
            logger.error("Error al crear la row:" + cell, e);
        }

        return cell;


    }


    @Override
    public HelperTableRow getRowById(long id) {

        ConnectionBD connectionBD = new ConnectionBD();
        String selectRecord = "{call semantikos.get_helper_table_row(?)}";
        HelperTableRow helperTableRow;

        try (Connection connection = connectionBD.getConnection();
             CallableStatement call = connection.prepareCall(selectRecord)) {

            call.setLong(1,id);
            /* Se prepara y realiza la consulta */
            call.execute();
            ResultSet rs = call.getResultSet();

            if (rs.next()) {
                helperTableRow = helperTableMapper.createHelperTableRowFromResultSet(rs);
                //jsonResult = rs.getString(1);
            } else {
                String errorMsg = "Un error imposible acaba de ocurrir";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        }

        return helperTableRow;
    }

    @Override
    public HelperTableRow getRowBy(long tableId, long id) {
        ConnectionBD connectionBD = new ConnectionBD();
        String selectRecord = "{call semantikos.get_helper_table_row(?,?)}";
        List<HelperTableRow> recordFromJSON;
        try (Connection connection = connectionBD.getConnection();
             CallableStatement call = connection.prepareCall(selectRecord)) {

            call.setLong(1,id);
            call.setLong(2,tableId);
            /* Se prepara y realiza la consulta */
            call.execute();
            ResultSet rs = call.getResultSet();
            if (rs.next()) {

                String json = rs.getString(1);
                if(json==null)
                    return null;

                recordFromJSON = this.helperTableRecordFactory.createHelperTableRowsFromJSON(json);

                for (HelperTableRow helperTableRow : recordFromJSON) {
                    for (HelperTableData cell : helperTableRow.getCells()) {
                        cell.setColumn(getColumnById(cell.getColumnId()));
                    }
                }

                if(recordFromJSON==null)
                    throw new EJBException("Error imposible en HelperTableDAOImpl");
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

        return recordFromJSON.get(0);

    }

    @Override
    public List<HelperTableRow> getRowBy(long tableId, boolean valid) {
        ConnectionBD connectionBD = new ConnectionBD();
        String selectRecord = "{call semantikos.get_helper_table_rows_by_valid(?,?)}";
        List<HelperTableRow> recordFromJSON;
        try (Connection connection = connectionBD.getConnection();
             CallableStatement call = connection.prepareCall(selectRecord)) {

            call.setLong(1,tableId);
            call.setBoolean(2,valid);

            /* Se prepara y realiza la consulta */
            call.execute();
            ResultSet rs = call.getResultSet();
            if (rs.next()) {

                String json = rs.getString(1);
                if(json==null)
                    return new ArrayList<>();

                recordFromJSON = this.helperTableRecordFactory.createHelperTableRowsFromJSON(json);

                for (HelperTableRow helperTableRow : recordFromJSON) {
                    for (HelperTableData cell : helperTableRow.getCells()) {
                        cell.setColumn(getColumnById(cell.getColumnId()));
                    }
                }

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
    public HelperTableColumn getColumnById(long id) {
        ConnectionBD connectionBD = new ConnectionBD();
        String selectRecord = "{call semantikos.get_helper_table_column(?)}";
        HelperTableColumn columnFromJSON;
        try (Connection connection = connectionBD.getConnection();
             CallableStatement call = connection.prepareCall(selectRecord)) {

            call.setLong(1,id);
            /* Se prepara y realiza la consulta */
            call.execute();
            ResultSet rs = call.getResultSet();
            if (rs.next()) {

                String json = rs.getString(1);
                if(json==null)
                    return null;

                columnFromJSON = this.helperTableRecordFactory.createHelperTableColumnFromJSON(json);

                if(columnFromJSON==null)
                    throw new EJBException("Error imposible en HelperTableDAOImpl");
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

        return columnFromJSON;
    }

    @Override
    public List<HelperTableData> getCellsByRow(HelperTableRow helperTableRow) {
        ConnectionBD connectionBD = new ConnectionBD();
        String selectRecord = "{call semantikos.get_helper_table_data_by_row(?)}";
        List<HelperTableData> cells;
        try (Connection connection = connectionBD.getConnection();
             CallableStatement call = connection.prepareCall(selectRecord)) {

            call.setLong(1,helperTableRow.getId());
            /* Se prepara y realiza la consulta */
            call.execute();
            ResultSet rs = call.getResultSet();

            cells = helperTableMapper.createCellsFromResultSet(rs, helperTableRow);

            rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        }

        return cells;
    }

    /*
    actualiza una fila y sus celdas
     */
    @Override
    public HelperTableRow updateRow(HelperTableRow row) {


        for (HelperTableData cell: row.getCells() ) {
            updateData(cell);
        }

        ConnectionBD connect = new ConnectionBD();
        String UPDATE = "{call semantikos.update_helper_table_row(?,?,?,?,?,?,?,?,?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(UPDATE)) {

            call.setLong(1, row.getId());
            call.setLong(2, row.getHelperTableId());
            call.setString(3, row.getDescription());
            call.setTimestamp(4, row.getCreationDate());
            call.setString(5, row.getCreationUsername());
            call.setTimestamp(6, row.getLastEditDate());
            call.setString(7, row.getLastEditUsername());
            call.setTimestamp(8, row.getValidityUntil()!=null?new Timestamp(row.getValidityUntil().getTime()):null);
            call.setBoolean(9, row.isValid());

            ResultSet rs = call.executeQuery();

        } catch (SQLException e) {
            logger.error("Error al crear la row:" + row, e);
        }

        return row;

    }

    @Override
    public HelperTable getHelperTableByID(long tableId) {

        ConnectionBD connectionBD = new ConnectionBD();
        String selectRecord = "{call semantikos.get_helper_table(?)}";
        List<HelperTable> recordFromJSON;
        try (Connection connection = connectionBD.getConnection();
             CallableStatement call = connection.prepareCall(selectRecord)) {

            /* Se prepara y realiza la consulta */

            call.setLong(1,tableId);
            call.execute();
            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                recordFromJSON = this.helperTableRecordFactory.createHelperTablesFromJSON(rs.getString(1));
                for (HelperTable table: recordFromJSON) {
                    if(table.getColumns()==null)
                        table.setColumns(new ArrayList<HelperTableColumn>());
                }
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

        return recordFromJSON.get(0);
    }

    @Override
    public List<HelperTableRow> searchRecords(HelperTable helperTable, String pattern) {
        ConnectionBD connectionBD = new ConnectionBD();
        String selectRecord = "{call semantikos.get_helper_table_rows(?,?)}";
        List<HelperTableRow> recordFromJSON;
        try (Connection connection = connectionBD.getConnection();
             CallableStatement call = connection.prepareCall(selectRecord)) {

            call.setLong(1,helperTable.getId());
            call.setString(2,pattern);

            /* Se prepara y realiza la consulta */
            call.execute();
            ResultSet rs = call.getResultSet();
            if (rs.next()) {

                String json = rs.getString(1);
                if(json==null)
                    return new ArrayList<>();

                recordFromJSON = this.helperTableRecordFactory.createHelperTableRowsFromJSON(json);

                for (HelperTableRow helperTableRow : recordFromJSON) {
                    for (HelperTableData cell : helperTableRow.getCells()) {
                        cell.setColumn(getColumnById(cell.getColumnId()));
                    }
                }

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
    public List<HelperTableRow> searchAllRecords(HelperTable helperTable, String pattern) {
        ConnectionBD connectionBD = new ConnectionBD();
        String selectRecord = "{call semantikos.get_all_helper_table_rows(?,?)}";
        List<HelperTableRow> recordFromJSON;
        try (Connection connection = connectionBD.getConnection();
             CallableStatement call = connection.prepareCall(selectRecord)) {

            call.setLong(1,helperTable.getId());
            call.setString(2,pattern.toLowerCase());

            /* Se prepara y realiza la consulta */
            call.execute();
            ResultSet rs = call.getResultSet();
            if (rs.next()) {

                String json = rs.getString(1);
                if(json==null)
                    return new ArrayList<>();

                recordFromJSON = this.helperTableRecordFactory.createHelperTableRowsFromJSON(json);

                for (HelperTableRow helperTableRow : recordFromJSON) {
                    for (HelperTableData cell : helperTableRow.getCells()) {
                        cell.setColumn(getColumnById(cell.getColumnId()));
                    }
                }

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
    public List<HelperTableRow> searchRecords(HelperTable helperTable, String pattern, String columnName) {
        ConnectionBD connectionBD = new ConnectionBD();
        String selectRecord = "{call semantikos.get_helper_table_rows(?,?,?)}";
        List<HelperTableRow> recordFromJSON;
        try (Connection connection = connectionBD.getConnection();
             CallableStatement call = connection.prepareCall(selectRecord)) {

            call.setLong(1,helperTable.getId());
            call.setString(2,pattern);
            call.setString(3, columnName);

            /* Se prepara y realiza la consulta */
            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {

                String json = rs.getString(1);
                if(json==null)
                    return new ArrayList<>();

                recordFromJSON = this.helperTableRecordFactory.createHelperTableRowsFromJSON(json);

                for (HelperTableRow helperTableRow : recordFromJSON) {
                    for (HelperTableData cell : helperTableRow.getCells()) {
                        cell.setColumn(getColumnById(cell.getColumnId()));
                    }
                }

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
    public List<ConceptSMTK> isRowUsed(HelperTableRow row) {

        ConnectionBD connectionBD = new ConnectionBD();
            String selectRecord = "{call semantikos.get_concepts_ids_by_helper_table_target(?)}";
            List<ConceptSMTK> result = new ArrayList<>();

            try (Connection connection = connectionBD.getConnection();
                 CallableStatement call = connection.prepareCall(selectRecord)) {

                call.setLong(1,row.getId());

            /* Se prepara y realiza la consulta */
                call.execute();
                ResultSet rs = call.getResultSet();
                while (rs.next()) {

                    Long conceptId = rs.getLong(1);
                    result.add(conceptDAO.getConceptByID(conceptId));

                }
                rs.close();
            } catch (SQLException e) {
                logger.error("Hubo un error al acceder a la base de datos.", e);
                throw new EJBException(e);
            }


        return result;
    }

    @Override
    public int countIsRowUser(HelperTableRow row) {
        ConnectionBD connectionBD = new ConnectionBD();
        String selectRecord = "{call semantikos.count_concepts_ids_by_helper_table_target(?)}";
        int result = 0;

        try (Connection connection = connectionBD.getConnection();
             CallableStatement call = connection.prepareCall(selectRecord)) {

            call.setLong(1,row.getId());

            /* Se prepara y realiza la consulta */
            call.execute();
            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                result = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        }


        return result;
    }

    @Override
    public List<ConceptSMTK> isRowUser(HelperTableRow row, int size, int page) {
        ConnectionBD connerctionBD = new ConnectionBD();
        List<ConceptSMTK> result= new ArrayList<>();

        try(Connection connection = connerctionBD.getConnection();
            CallableStatement call = connection.prepareCall("{call semantikos.get_concepts_ids_by_helper_table_target(?,?,?)}");){
            call.setLong(1,row.getId());
            call.setInt(2, size);
            call.setInt(3,page);
            call.execute();
            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                Long conceptId = rs.getLong(1);
                result.add(conceptDAO.getConceptByID(conceptId));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}