package cl.minsal.semantikos.kernel.components;


import cl.minsal.semantikos.kernel.daos.HelperTableDAO;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.User;
import cl.minsal.semantikos.model.businessrules.HelperTableSearchBR;
import cl.minsal.semantikos.model.helpertables.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.Reader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by BluePrints Developer on 14-12-2016.
 */

@Stateless
public class HelperTablesManagerImpl implements HelperTablesManager {

    private static final Logger logger = LoggerFactory.getLogger(HelperTablesManagerImpl.class);

    @EJB
    HelperTableDAO dao;

    @Override
    public HelperTable getById(long id) {
        return dao.getHelperTableByID(id);
    }

    @Override
    public List<HelperTable> findAll() {
        return dao.getAllTables();
    }


    @Override
    public HelperTableColumn updateColumn(HelperTableColumn column) {
        return dao.updateColumn(column);
    }

    @Override
    public List<HelperTableDataType> getAllDataTypes(){
        return dao.getAllDataTypes();
    }

    @Override
    public HelperTableColumn createColumn(HelperTableColumn column) {


        HelperTableColumn createdColumn = dao.createColumn(column);



        return createdColumn;
    }

    @Override
    public List<HelperTableRow> getTableRows(long tableId) {
        return dao.getTableRows(tableId);
    }



    @Override
    public HelperTableRow createEmptyRow(Long tableId, String username) {
        HelperTable table= getById(tableId);

        HelperTableRow newRow = new HelperTableRow();

        newRow.setCreationDate(new Timestamp(System.currentTimeMillis()));
        newRow.setCreationUsername(username);
        newRow.setLastEditDate(new Timestamp(System.currentTimeMillis()));
        newRow.setLastEditUsername(username);

        newRow.setDescription("Nuevo Elemento");
        newRow.setValid(false);
        newRow.setHelperTableId(table.getId());

        newRow = dao.createRow(newRow);

        newRow.setCells(new ArrayList<HelperTableData>());
        for (HelperTableColumn column: table.getColumns()) {
            HelperTableData data = createCell(column,newRow);
        }

        return dao.getRowById(newRow.getId());
    }


    /*
       inserta una fila no persistida
        */
    @Override
    public HelperTableRow insertRow(HelperTableRow newRow,String username) {


        newRow.setCreationDate(new Timestamp(System.currentTimeMillis()));
        newRow.setCreationUsername(username);
        newRow.setLastEditDate(new Timestamp(System.currentTimeMillis()));
        newRow.setLastEditUsername(username);

        newRow = dao.createRow(newRow);

        for (HelperTableData cell: newRow.getCells()) {
            cell.setRowId(newRow.getId());
            dao.createData(cell);
        }

        return dao.getRowById(newRow.getId());
    }

    private HelperTableData createCell(HelperTableColumn column, HelperTableRow row) {
        HelperTableData data = new HelperTableData();
        data.setColumn(column);
        data.setColumnId(column.getId());
        data.setRow(row);
        data.setRowId(row.getId());


        return dao.createData(data);
    }

    @Override
    public HelperTableRow updateRow(HelperTableRow row, String username) throws RowInUseException {

        if(!row.isValid()){
            List<ConceptSMTK> cons = isRowUsed(row);
            if(cons.size()>0)
                throw new RowInUseException(cons);
        }

        row.setLastEditDate(new Timestamp(System.currentTimeMillis()));
        row.setLastEditUsername(username);

        return dao.updateRow(row);
    }

    private List<ConceptSMTK> isRowUsed(HelperTableRow row){
        return dao.isRowUsed(row);
    }

    @Override
    public HelperTableRow getRowById(long idRow) {
        return dao.getRowById(idRow);
    }

    @Override
    public HelperTableColumn getColumnById(long idColumn) {
        return dao.getColumnById(idColumn);
    }

    @Override
    public List<HelperTableRow> searchRows(HelperTable helperTable, String pattern) {
        return dao.searchRecords( helperTable, pattern);
    }

    public List<HelperTableRow> searchRows(HelperTable helperTable, String pattern, String columnName) {
        /* Se validan las pre-condiciones de búsqueda */
        new HelperTableSearchBR().validatePreConditions(helperTable, columnName, pattern);

        /* Se delega la búsqueda al DAO, ya que pasaron las pre-condiciones */
        List<HelperTableRow> foundRows = dao.searchRecords(helperTable, pattern, columnName);

        /* Se aplican reglas de negocio sobre los resultados retornados */
        new HelperTableSearchBR().applyPostActions(foundRows);

        return foundRows;
    }

    @Override
    public List<HelperTableRow> searchRows(HelperTable helperTable, String pattern, List<String> searchColumns) {

        List<HelperTableRow> rows = new ArrayList<>();

        for (String searchColumn : searchColumns) {
            rows.addAll(searchRows(helperTable, pattern, searchColumn));
        }

        return rows;
    }

    @Override
    public HelperTableImportReport loadFromFile(HelperTable helperTable, LoadMode loadModeSelected, Reader in, User loggedUser) {
        throw new NotImplementedException();
    }

    @Override
    public List<HelperTableRow> getValidTableRows(long id) {
        return dao.getValidTableRows(id);
    }

    @Override
    public List<HelperTable> getFullDatabase() {
        List<HelperTable> tables= dao.getAllTables();

        for (HelperTable table : tables) {
            table.setRows(getTableRows(table.getId()));
        }

        return tables;
    }


    public class RowInUseException extends Exception{

        private List<ConceptSMTK> concepts;

        public RowInUseException(List<ConceptSMTK> concepts) {
            this.concepts = concepts;
        }

        public List<ConceptSMTK> getConcepts() {
            return concepts;
        }

        public void setConcepts(List<ConceptSMTK> concepts) {
            this.concepts = concepts;
        }
    }

}