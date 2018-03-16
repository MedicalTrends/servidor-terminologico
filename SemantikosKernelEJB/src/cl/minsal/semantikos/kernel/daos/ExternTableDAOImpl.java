package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.DaoTools;
import cl.minsal.semantikos.model.ConceptSMTK;
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
        return null;
    }

    @Override
    public List<ExternTableColumn> getColumns() {
        return null;
    }

    @Override
    public List<ExternTableReference> getReferences(ExternTable externTable) {
        return null;
    }

    @Override
    public List<ExternTableRow> getRows(ExternTable table) {
        return null;
    }

    @Override
    public List<ExternTableRelationship> getRelationships(ExternTableRow row) {
        return null;
    }

    public HelperTable createHelperTableFromResultSet(ResultSet rs) {


        try {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            ExternTable externTable = new ExternTable(id, name);

            externTable.setColumns();

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