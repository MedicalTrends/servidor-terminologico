package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.factories.DataSourceFactory;
import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.kernel.util.DaoTools;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.helpertables.*;
import oracle.jdbc.OracleTypes;
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
    ConceptDAO conceptDAO;

    @Override
    public List<HelperTable> getAllTables() {

        //ConnectionBD connectionBD = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_helper_table.get_helper_tables; end;";

        List<HelperTable> helperTables = new ArrayList<>();

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se prepara y realiza la consulta */
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                helperTables.add(createHelperTableFromResultSet(rs));
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        }

        return helperTables;
    }

    @Override
    public HelperTableColumn createColumn(HelperTableColumn column) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_helper_table.create_helper_table_column(?,?,?,?,?,?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, column.getHelperTableDataTypeId());
            call.setLong(3, column.getHelperTableId());
            call.setLong(4, column.getForeignKeyHelperTableId());
            call.setString(5,  column.getName());
            call.setBoolean(6,column.isForeignKey());
            call.setString(7,column.getDescription());

            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

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

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_helper_table.update_helper_table_column(?,?,?,?,?,?,?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, column.getId());
            call.setLong(3, column.getHelperTableDataTypeId());
            call.setLong(4, column.getHelperTableId());
            call.setLong(5, column.getForeignKeyHelperTableId());
            call.setString(6,  column.getName());
            call.setBoolean(7,column.isForeignKey());
            call.setString(8,column.getDescription());

            call.execute();

        } catch (SQLException e) {
            logger.error("Error al actualizar columna:" + column, e);
        }

        return column;

    }

    @Override
    public List<HelperTableRow> getTableRows(long tableId) {

        //ConnectionBD connectionBD = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_helper_table.get_helper_table_rows(?); end;";

        List<HelperTableRow> helperTableRows = new ArrayList<>();
        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2,tableId);
            /* Se prepara y realiza la consulta */
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                helperTableRows.add(createHelperTableRowFromResultSet(rs));
            }

            rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        }

        return helperTableRows;
    }

    @Override
    public List<HelperTableRow> getValidTableRows(long tableId) {

        //ConnectionBD connectionBD = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_helper_table.get_valid_helper_table_rows(?); end;";

        List<HelperTableRow> helperTableRows = new ArrayList<>();

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2,tableId);
            /* Se prepara y realiza la consulta */
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                helperTableRows.add(createHelperTableRowFromResultSet(rs));
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        }

        return helperTableRows;
    }

    /*
    crea solo el elemento de la fila sin las celdas
     */
    @Override
    public HelperTableRow createRow(HelperTableRow row) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_helper_table.create_helper_table_row(?,?,?,?,?,?,?,?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.NUMERIC);
            call.setLong(2, row.getHelperTableId());
            call.setString(3, row.getDescription());
            call.setTimestamp(4, row.getCreationDate());
            call.setString(5, row.getCreationUsername());
            call.setTimestamp(6, row.getLastEditDate());
            call.setString(7, row.getLastEditUsername());
            call.setTimestamp(8, row.getValidityUntil()!=null?new Timestamp(row.getValidityUntil().getTime()):null);
            call.setBoolean(9, row.isValid());

            call.execute();

            //ResultSet rs = (ResultSet) call.getObject(1);

            if (call.getLong(1) > 0) {
                row.setId(call.getLong(1));
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

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_helper_table.create_helper_table_data(?,?,?,?,?,?,?,?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.NUMERIC);
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
                call.setNull(6, Types.NUMERIC);
            else
                call.setBoolean(6,cell.getBooleanValue());

            if(cell.getForeignKeyValue()==null)
                call.setNull(7, Types.BIGINT);
            else
                call.setLong(7, cell.getForeignKeyValue());

            call.setLong(8,cell.getRowId());
            call.setLong(9,cell.getColumn().getId());

            call.execute();

            //ResultSet rs = (ResultSet) call.getObject(1);

            if (call.getLong(1) > 0) {
                cell.setId(call.getLong(1));
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


        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_helper_table.update_helper_table_data(?,?,?,?,?,?,?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.NUMERIC);
            call.setLong(2, cell.getId());
            call.setString(3, cell.getStringValue());
            call.setDate(4, cell.getDateValue()==null?null:new Date(cell.getDateValue().getTime()));

            if(cell.getFloatValue()==null)
                call.setNull(5, Types.REAL);
            else
                call.setFloat(5, cell.getFloatValue());

            if(cell.getIntValue()==null)
                call.setNull(6, Types.BIGINT);
            else
                call.setLong(6, cell.getIntValue());

            if(cell.getBooleanValue()==null)
                call.setNull(7, Types.BOOLEAN);
            else
                call.setBoolean(7,cell.getBooleanValue());
            if(cell.getForeignKeyValue()==null){
                call.setNull(8, Types.BIGINT);
            }else{
                call.setLong(8, cell.getForeignKeyValue());
            }

            call.execute();

        } catch (SQLException e) {
            logger.error("Error al crear la row:" + cell, e);
        }

        return cell;

    }

    @Override
    public HelperTableRow getRowById(long id) {

        //ConnectionBD connectionBD = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_helper_table.get_helper_table_row(?); end;";

        HelperTableRow helperTableRow;

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2,id);
            /* Se prepara y realiza la consulta */
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                helperTableRow = createHelperTableRowFromResultSet(rs);
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
        //ConnectionBD connectionBD = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_helper_table.get_helper_table_row(?,?); end;";

        HelperTableRow helperTableRow;

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2,id);
            call.setLong(3,tableId);
            /* Se prepara y realiza la consulta */
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                helperTableRow = createHelperTableRowFromResultSet(rs);
            }
            else {
                throw new EJBException("Error imposible en HelperTableDAOImpl");
            }

            rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        }

        return helperTableRow;

    }

    @Override
    public List<HelperTableRow> getRowBy(long tableId, boolean valid) {
        //ConnectionBD connectionBD = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_helper_table.get_helper_table_rows_by_valid(?,?); end;";

        List<HelperTableRow> helperTableRows = new ArrayList<>();

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2,tableId);
            call.setBoolean(3,valid);

            /* Se prepara y realiza la consulta */
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                helperTableRows.add(createHelperTableRowFromResultSet(rs));
            }

            rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        }

        return helperTableRows;
    }

    @Override
    public HelperTableColumn getColumnById(long id) {
        //ConnectionBD connectionBD = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_helper_table.get_helper_table_column(?); end;";

        HelperTableColumn helperTableColumn;

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2,id);
            /* Se prepara y realiza la consulta */
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                helperTableColumn = createHelperTableColumnFromResultSet(rs);
            }
            else {
                throw new EJBException("Error imposible en HelperTableDAOImpl");
            }

            rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        }

        return helperTableColumn;
    }

    @Override
    public List<HelperTableData> getCellsByRow(HelperTableRow helperTableRow) {
        //ConnectionBD connectionBD = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_helper_table.get_helper_table_data_by_row(?); end;";

        List<HelperTableData> cells;
        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2,helperTableRow.getId());
            /* Se prepara y realiza la consulta */
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            cells = createCellsFromResultSet(rs, helperTableRow);

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

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_helper_table.update_helper_table_row(?,?,?,?,?,?,?,?,?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.NUMERIC);
            call.setLong(2, row.getId());
            call.setLong(3, row.getHelperTableId());
            call.setString(4, row.getDescription());
            call.setTimestamp(5, row.getCreationDate());
            call.setString(6, row.getCreationUsername());
            call.setTimestamp(7, row.getLastEditDate());
            call.setString(8, row.getLastEditUsername());
            call.setTimestamp(9, row.getValidityUntil()!=null?new Timestamp(row.getValidityUntil().getTime()):null);
            call.setBoolean(10, row.isValid());

            call.execute();

            //ResultSet rs = (ResultSet) call.getObject(1);

        } catch (SQLException e) {
            logger.error("Error al crear la row:" + row, e);
        }

        return row;

    }

    @Override
    public HelperTable getHelperTableByID(long tableId) {

        //ConnectionBD connectionBD = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_helper_table.get_helper_table(?); end;";

        HelperTable helperTable;

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se prepara y realiza la consulta */
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2,tableId);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                helperTable = createHelperTableFromResultSet(rs);
            }
            else {
                throw new EJBException("Error imposible en HelperTableDAOImpl");
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        }

        return helperTable;
    }

    @Override
    public List<HelperTableRow> searchRecords(HelperTable helperTable, String pattern) {
        //ConnectionBD connectionBD = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_helper_table.find_rows_by_pattern(?,?); end;";

        List<HelperTableRow> helperTableRows = new ArrayList<>();

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2,helperTable.getId());
            call.setString(3,pattern);

            /* Se prepara y realiza la consulta */
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                helperTableRows.add(createHelperTableRowFromResultSet(rs));
            }

            rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        }

        return helperTableRows;
    }

    @Override
    public List<HelperTableRow> searchAllRecords(HelperTable helperTable, String pattern) {
        //ConnectionBD connectionBD = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_helper_table.get_all_helper_table_rows(?,?); end;";

        List<HelperTableRow> helperTableRows = new ArrayList<>();

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2,helperTable.getId());
            call.setString(3,pattern.toLowerCase());

            /* Se prepara y realiza la consulta */
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                helperTableRows.add(createHelperTableRowFromResultSet(rs));
            }

            rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        }

        return helperTableRows;

    }

    @Override
    public List<HelperTableRow> searchRecords(HelperTable helperTable, String pattern, String columnName) {
        //ConnectionBD connectionBD = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_helper_table.find_rows_by_pattern_and_column(?,?,?); end;";

        List<HelperTableRow> helperTableRows = new ArrayList<>();

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2,helperTable.getId());
            call.setString(3,pattern);
            call.setString(4, columnName);

            /* Se prepara y realiza la consulta */
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                helperTableRows.add(createHelperTableRowFromResultSet(rs));
            }

            rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        }

        return helperTableRows;
    }

    @Override
    public List<ConceptSMTK> isRowUsed(HelperTableRow row) {

        //ConnectionBD connectionBD = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_helper_table.get_concepts_ids_by_helper_table_target(?); end;";

            List<ConceptSMTK> result = new ArrayList<>();

            try (Connection connection = DataSourceFactory.getInstance().getConnection();
                 CallableStatement call = connection.prepareCall(sql)) {

                call.registerOutParameter (1, OracleTypes.CURSOR);
                call.setLong(2,row.getId());

                /* Se prepara y realiza la consulta */
                call.execute();

                ResultSet rs = (ResultSet) call.getObject(1);

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
        //ConnectionBD connectionBD = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_helper_table.count_concepts_ids_by_helper_table_target(?); end;";
        int result = 0;

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.NUMERIC);
            call.setLong(2,row.getId());

            /* Se prepara y realiza la consulta */
            call.execute();

            //ResultSet rs = (ResultSet) call.getObject(1);
            result = call.getInt(1);
            //rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        }


        return result;
    }

    @Override
    public List<ConceptSMTK> isRowUser(HelperTableRow row, int size, int page) {
        //ConnectionBD connerctionBD = new ConnectionBD();
        List<ConceptSMTK> result= new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_helper_table.get_concepts_ids_by_helper_table_target(?,?,?); end;";

        try(Connection connection = DataSourceFactory.getInstance().getConnection();
            CallableStatement call = connection.prepareCall(sql);) {
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2,row.getId());
            call.setInt(3, size);
            call.setInt(4,page);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

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

    /**
     * Este método es responsable de crear un HelperTable Record a partir de un objeto JSON.
     *
     * @param rs El objeto JSON a partir del cual se crea el objeto. El formato JSON será:
     *                       <code>{"TableName":"helper_table_atc","records":[{"id":1,"codigo_atc":"atc1"}</code>
     *
     * @return Un objeto fresco de tipo <code>HelperTableRecord</code> creado a partir del objeto JSON.
     *
     * @throws IOException Arrojada si hay un problema.
     */
    public HelperTable createHelperTableFromResultSet(ResultSet rs) {

        HelperTable helperTable = new HelperTable();

        try {
            helperTable.setId(rs.getLong("id"));
            helperTable.setName(rs.getString("name"));
            helperTable.setDescription(rs.getString("description"));

            helperTable.setColumns(HelperTableColumnFactory.getInstance().findColumnsByHelperTable(rs.getLong("id")));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return helperTable;

    }

    /**
     * Este método es responsable de crear un HelperTable Record a partir de un objeto JSON.
     *
     * @param rs El objeto JSON a partir del cual se crea el objeto. El formato JSON será:
     *                       <code>{"TableName":"helper_table_atc","records":[{"id":1,"codigo_atc":"atc1"}</code>
     *
     * @return Un objeto fresco de tipo <code>HelperTableRecord</code> creado a partir del objeto JSON.
     *
     * @throws IOException Arrojada si hay un problema.
     */
    public HelperTableColumn createHelperTableColumnFromResultSet(ResultSet rs) {

        HelperTableColumn helperTableColumn = new HelperTableColumn();

        try {
            helperTableColumn.setId(rs.getLong("id"));
            helperTableColumn.setName(rs.getString("name"));
            helperTableColumn.setHelperTableId(rs.getLong("helper_table_id"));
            helperTableColumn.setHelperTableDataTypeId(rs.getInt("helper_table_data_type_id"));
            helperTableColumn.setForeignKeyHelperTableId(rs.getInt("foreign_key_table_id"));
            helperTableColumn.setForeignKey(rs.getBoolean("foreign_key"));
            helperTableColumn.setDescription(rs.getString("description"));
            helperTableColumn.setSearchable(rs.getBoolean("searchable"));
            helperTableColumn.setSearchable(rs.getBoolean("showable"));
            helperTableColumn.setSearchable(rs.getBoolean("editable"));
            helperTableColumn.setSearchable(rs.getBoolean("sortable"));
            helperTableColumn.setSearchable(rs.getBoolean("required"));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return helperTableColumn;

    }


    /**
     * Este método es responsable de crear un HelperTable Record a partir de un objeto JSON.
     *
     * @param rs El objeto JSON a partir del cual se crea el objeto. El formato JSON será:
     *                       <code>{"TableName":"helper_table_atc","records":[{"id":1,"codigo_atc":"atc1"}</code>
     *
     * @return Un objeto fresco de tipo <code>HelperTableRecord</code> creado a partir del objeto JSON.
     *
     * @throws IOException Arrojada si hay un problema.
     */
    public HelperTableRow createHelperTableRowFromResultSet(ResultSet rs) {

        HelperTableRow helperTableRow = new HelperTableRow();

        try {
            helperTableRow.setId(rs.getLong("id"));
            helperTableRow.setCreationDate(rs.getTimestamp("creation_date"));
            helperTableRow.setLastEditDate(rs.getTimestamp("last_edit_date"));
            helperTableRow.setValid(rs.getBoolean("valid"));
            helperTableRow.setValidityUntil(rs.getTimestamp("validity_until"));
            helperTableRow.setDescription(rs.getString("description"));
            helperTableRow.setCreationUsername(rs.getString("creation_user"));
            helperTableRow.setLastEditUsername(rs.getString("last_edit_user"));
            helperTableRow.setHelperTableId(rs.getLong("helper_table_id"));

            helperTableRow.setCells(getCellsByRow(helperTableRow));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return helperTableRow;

    }

    /**
     * Este método es responsable de crear un arreglo de objetos de auditoría a partir de una expresión JSON de la
     * forma:
     *
     * @param rs La expresión JSON a partir de la cual se crean los elementos de auditoría.
     *
     * @return Una lista de objetos auditables.
     */
    public List<HelperTableData> createCellsFromResultSet(ResultSet rs, HelperTableRow helperTableRow) {

        List<HelperTableData> cells =new ArrayList<>();

        try {

            while(rs.next()) {

                HelperTableData cell = new HelperTableData();

                cell.setId(rs.getLong("id"));
                cell.setIntValue(DaoTools.getLong(rs, "int_value"));
                cell.setFloatValue(DaoTools.getFloat(rs, "float_value"));
                cell.setStringValue(DaoTools.getString(rs, "string_value"));
                cell.setDateValue(DaoTools.getTimestamp(rs, "date_value"));
                cell.setBooleanValue(DaoTools.getBoolean(rs, "boolean_value"));
                cell.setForeignKeyValue(DaoTools.getLong(rs, "foreign_key_value"));
                cell.setRowId(rs.getLong("row_id"));
                cell.setRow(helperTableRow);

                HelperTableColumn helperTableColumn = HelperTableColumnFactory.getInstance().findColumnById(rs.getLong("column_id"));

                cell.setColumn(helperTableColumn);
                cell.setColumnId(helperTableColumn.getId());

                cells.add(cell);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cells;
    }

}