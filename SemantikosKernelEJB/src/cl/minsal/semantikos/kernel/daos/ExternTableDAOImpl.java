package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.DaoTools;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.crossmaps.CrossmapSet;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetFactory;
import cl.minsal.semantikos.model.externtables.*;
import cl.minsal.semantikos.model.helpertables.*;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blueprints on 9/16/2015.
 */
@Stateless
public class ExternTableDAOImpl implements Serializable, ExternTableDAO {

    /** Logger de la clase */
    private static final Logger logger = LoggerFactory.getLogger(ExternTableDAOImpl.class);

    @EJB
    ConceptDAO conceptDAO;

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;


    @Override
    public List<ExternTable> getTables() {

        String sql = "begin ? := stk.stk_pck_extern_table.get_extern_tables; end;";

        List<ExternTable> externTables = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se prepara y realiza la consulta */
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                ExternTable externTable = createExternTableFromResultSet(rs);
                externTable.setReferences(getReferences(externTable));
                externTables.add(externTable);
            }
            rs.close();

            ExternTableFactory.getInstance().setExternTables(externTables);

        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        }

        return externTables;
    }

    @Override
    public List<ExternTableColumn> getColumns() {

        List<ExternTableColumn> externTableColumns = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_extern_table.get_extern_table_columns; end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);

            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            /* Se recuperan las columnas */
            while(rs.next()) {
                externTableColumns.add(createExternTableColumnFromResultSet(rs, null));
            }

            rs.close();

            /* Se setea la lista de tagsSMTK */
            ExternTableColumnFactory.getInstance().setExternTableColumns(externTableColumns);

        } catch (SQLException e) {
            String errorMsg = "Error al intentar recuperar la lista de columnas de tablas auxiliares de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return externTableColumns;
    }

    @Override
    public List<ExternTableReference> getReferences(ExternTable externTable) {

        String sql = "begin ? := stk.stk_pck_extern_table.get_extern_table_references(?); end;";

        List<ExternTableReference> references = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se prepara y realiza la consulta */
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, externTable.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                references.add(createExternTableRefereceFromResultSet(rs, externTable));
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        }

        return references;
    }

    @Override
    public List<ExternTableColumn> getColumns(ExternTable table) {

        String sql = "begin ? := stk.stk_pck_extern_table.get_columns(?); end;";

        List<ExternTableColumn> externTableColumns = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2,table.getId());
            /* Se prepara y realiza la consulta */
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                externTableColumns.add(createExternTableColumnFromResultSet(rs, table));
            }

            rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        }

        table.setColumns(externTableColumns);

        return externTableColumns;
    }

    @Override
    public List<ExternTableRow> getRows(ExternTable table) {

        String sql = "begin ? := stk.stk_pck_extern_table.get_extern_table_rows(?); end;";

        List<ExternTableRow> helperTableRows = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2,table.getId());
            /* Se prepara y realiza la consulta */
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                helperTableRows.add(createExternTableRowFromResultSet(rs));
            }

            rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        }

        return helperTableRows;
    }

    @Override
    public List<ExternTableData> getCellsByRow(ExternTableRow externTableRow) {

        String sql = "begin ? := stk.stk_pck_helper_table.get_extern_table_data_by_row(?); end;";

        List<ExternTableData> cells;
        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2,externTableRow.getId());
            /* Se prepara y realiza la consulta */
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            cells = createCellsFromResultSet(rs, externTableRow);

            rs.close();
        } catch (SQLException e) {
            logger.error("Hubo un error al acceder a la base de datos.", e);
            throw new EJBException(e);
        }

        return cells;
    }


    @Override
    public List<ExternTableRelationship> getRelationships(ExternTableRow row) {
        return null;
    }

    public ExternTable createExternTableFromResultSet(ResultSet rs) {

        ExternTable externTable = null;

        try {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            CrossmapSet crossmapSet = CrossmapSetFactory.getInstance().findCrossmapSetsById(rs.getLong("id_crossmap_set"));
            externTable = new ExternTable(id, name, crossmapSet);

            externTable.setColumns(getColumns(externTable));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return externTable;

    }

    public ExternTableReference createExternTableRefereceFromResultSet(ResultSet rs, ExternTable origin) {

        ExternTableReference externTableReference = null;

        try {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            ExternTable destiny = ExternTableFactory.getInstance().findTableById(rs.getLong("id_destiny"));
            Cardinality cardinality = Cardinality.getValue(rs.getString("cardinality"));

            externTableReference = new ExternTableReference(id, origin, destiny, name, cardinality);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return externTableReference;

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
    public ExternTableColumn createExternTableColumnFromResultSet(ResultSet rs, ExternTable table) {

        ExternTableColumn externTableColumn = new ExternTableColumn();

        try {
            externTableColumn.setId(rs.getLong("id"));
            externTableColumn.setName(rs.getString("name"));
            if(table == null) {
                table = ExternTableFactory.getInstance().findTableById(rs.getLong("id_extern_table"));
            }
            externTableColumn.setExternTable(table);
            externTableColumn.setDataType(ExternTableDataType.valueOf(rs.getInt("data_type_id")));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return externTableColumn;

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
    public ExternTableRow createExternTableRowFromResultSet(ResultSet rs) {

        ExternTableRow externTableRow = new ExternTableRow();

        try {
            externTableRow.setId(rs.getLong("id"));
            externTableRow.setTable(ExternTableFactory.getInstance().findTableById(rs.getLong("extern_table_id")));
            externTableRow.setCells(getCellsByRow(externTableRow));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return externTableRow;

    }

    /**
     * Este método es responsable de crear un arreglo de objetos de auditoría a partir de una expresión JSON de la
     * forma:
     *
     * @param rs La expresión JSON a partir de la cual se crean los elementos de auditoría.
     *
     * @return Una lista de objetos auditables.
     */
    public List<ExternTableData> createCellsFromResultSet(ResultSet rs, ExternTableRow externTableRow) {

        List<ExternTableData> cells =new ArrayList<>();

        try {

            while(rs.next()) {

                ExternTableData cell = new ExternTableData();

                cell.setId(rs.getLong("id"));
                cell.setIntValue(DaoTools.getLong(rs, "int_value"));
                cell.setFloatValue(DaoTools.getFloat(rs, "float_value"));
                cell.setStringValue(DaoTools.getString(rs, "string_value"));
                cell.setDateValue(DaoTools.getTimestamp(rs, "date_value"));
                cell.setBooleanValue(DaoTools.getBoolean(rs, "boolean_value"));
                cell.setRow(externTableRow);
                cell.setColumn(ExternTableColumnFactory.getInstance().findColumnById(rs.getLong("column_id")));

                cells.add(cell);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cells;
    }

}