package cl.minsal.semantikos.helpertables;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.concept.ConceptBean;
import cl.minsal.semantikos.kernel.businessrules.HelperTableSearchBR;
import cl.minsal.semantikos.messages.MessageBean;
import cl.minsal.semantikos.users.AuthenticationBean;
import cl.minsal.semantikos.kernel.components.HelperTablesManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.helpertables.*;
import cl.minsal.semantikos.model.relationships.*;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.data.PageEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

import static java.util.Collections.EMPTY_LIST;

/**
 * Created by Blueprints on 1/27/2016.
 */
@ManagedBean(name = "helperTableBean")
@ViewScoped
public class HelperTableBean implements Serializable {

    private static final long serialVersionUID = 1L;

    List<HelperTable> fullDatabase;

    LazyDataModel<ConceptSMTK> conceptSMTKs;

    Map<Long,List<HelperTableRow>> validRow;

    HelperTable helperTableSelected;

    @ManagedProperty(value = "#{authenticationBean}")
    private AuthenticationBean authenticationBean;

    @ManagedProperty(value = "#{conceptBean}")
    private ConceptBean conceptBean;

    @ManagedProperty(value = "#{messageBean}")
    private MessageBean messageBean;

    //@EJB
    HelperTablesManager manager = (HelperTablesManager) ServiceLocator.getInstance().getService(HelperTablesManager.class);

    HelperTableSearchBR helperTableSearchBR = (HelperTableSearchBR) ServiceLocator.getInstance().getService(HelperTableSearchBR.class);

    HelperTableRow dummyRow = new HelperTableRow("Seleccionar...");

    @PostConstruct
    protected void initialize() {
    }

    public AuthenticationBean getAuthenticationBean() {
        return authenticationBean;
    }

    public void setAuthenticationBean(AuthenticationBean authenticationBean) {
        this.authenticationBean = authenticationBean;
    }

    public ConceptBean getConceptBean() {
        return conceptBean;

    }

    public HelperTable getHelperTableSelected() {
        return helperTableSelected;
    }

    public void setHelperTableSelected(HelperTable helperTableSelected) {
        this.helperTableSelected = helperTableSelected;
    }

    public void setConceptBean(ConceptBean conceptBean) {
        this.conceptBean = conceptBean;
    }


    public HelperTablesManager getHelperTablesManager() {
        return manager;
    }


    public List<HelperTable> getAdministrableTables() {

        List<HelperTable> administrableTables = new ArrayList<>();

        for (HelperTable table : getFullDatabase()) {
            //if (table.getId() <= 21)
                administrableTables.add(table);
        }

        return administrableTables;
    }

    private List<HelperTable> getFullDatabase() {
        if (fullDatabase == null)
            fullDatabase = manager.getLiteDatabase();

        return fullDatabase;
    }

    public void chargeRow(HelperTable helperTable){
        helperTable.setRows(manager.getTableRows(helperTable.getId()));
    }


    public void onRowEditCancel(RowEditEvent event) {
        HelperTableRow row = (HelperTableRow) event.getObject();

        if (row.isPersistent())
            return;

        Long tableId = row.getHelperTableId();

        for (HelperTable helperTable : fullDatabase) {
            if (helperTable.getId() == tableId) {
                helperTable.getRows().remove(row);
            }
        }

    }

    private HelperTableRow rowSelected;

    public void onRowEdit(RowEditEvent event) {
        HelperTableRow row = (HelperTableRow) event.getObject();
        try {
            HelperTableRow updatedRow;
            if (row.isPersistent()) {
                if(!row.isValid()){
                    if(!manager.isRowUsed(row,10,0).isEmpty()){
                        rowSelected= row;
                        messageBean.messageError("No se puede cambiar la vigencia, existen conceptos asociados");
                        RequestContext context = RequestContext.getCurrentInstance();
                        final HelperTableRow r= row;
                        conceptSMTKs= new LazyDataModel<ConceptSMTK>() {
                            @Override
                            public List<ConceptSMTK> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                                List<ConceptSMTK> conceptSMTKs = null;
                                conceptSMTKs = manager.isRowUsed(r,pageSize,first);
                                setRowCount(manager.countIsRowUsed(r));
                                return  conceptSMTKs;
                            }
                        };
                        context.execute("PF('dialog-concept-related').show();");
                        context.execute("PF('edit-data-dialog-var').hide();");
                        return;
                    }else{
                        updatedRow = manager.updateRow(row, this.authenticationBean.getEmail());
                    }
                }else{
                    updatedRow = manager.updateRow(row, this.authenticationBean.getEmail());
                }

            } else {
                updatedRow = manager.insertRow(row, this.authenticationBean.getEmail());
            }
            row.setLastEditDate(updatedRow.getLastEditDate());
            row.setLastEditUsername(updatedRow.getLastEditUsername());
            row.setCells(updatedRow.getCells());
            row.setId(updatedRow.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(PageEvent event){
        int page = event.getPage();
        //conceptSMTKs= manager.isRowUsed(rowSelected,10,page);
    }


    public void addRow(HelperTable table) {

        HelperTableRow newRow = createNewHelperTableRow(table);

        table.getRows().add(0, newRow);

    }

    private HelperTableRow createNewHelperTableRow(HelperTable table) {
        HelperTableRow newRow = new HelperTableRow();
        newRow.setId(-1);
        newRow.setCreationDate(new Timestamp(System.currentTimeMillis()));
        newRow.setCreationUsername(authenticationBean.getEmail());
        newRow.setLastEditDate(new Timestamp(System.currentTimeMillis()));
        newRow.setLastEditUsername(authenticationBean.getEmail());

        newRow.setDescription("Nuevo Elemento");
        newRow.setValid(false);
        newRow.setHelperTableId(table.getId());


        newRow.setCells(new ArrayList<HelperTableData>());
        for (HelperTableColumn column : table.getColumns()) {
            HelperTableData data = createCell(column, newRow);
            newRow.getCells().add(data);
        }
        return newRow;
    }


    private HelperTableData createCell(HelperTableColumn column, HelperTableRow row) {
        HelperTableData data = new HelperTableData();
        data.setId(-1);
        data.setColumn(column);
        data.setColumnId(column.getId());
        data.setRow(row);
        data.setRowId(row.getId());


        return data;
    }


    public List<HelperTableRow> getValidTableRows(HelperTable table) {
        return getReferencedTableRows(table.getId());
    }

    public List<HelperTableRow> getValidTableRows(HelperTable table, RelationshipAttributeDefinition relationshipAttributeDefinition) {
        List<HelperTableRow> helperTableRows = getReferencedTableRows(table.getId());
        List<HelperTableRow> helperTableRowsFiltered;

        switch ((int)relationshipAttributeDefinition.getId()) {

            case (int) HelperTableColumnFactory.U_VOLUMEN_ID:
                helperTableRowsFiltered = getValidTableRowsUnit(helperTableRows,HelperTableColumnFactory.COLUMN_U_VOL);
                if(helperTableRows.size()!=0){
                    return helperTableRowsFiltered;
                }
                break;
            case (int)HelperTableColumnFactory.U_POTENCIA_ID:
                helperTableRowsFiltered = getValidTableRowsUnit(helperTableRows,HelperTableColumnFactory.COLUMN_U_POTENCIA);
                if(helperTableRows.size()!=0){
                    return helperTableRowsFiltered;
                }
                break;
            case (int)HelperTableColumnFactory.U_UNIDAD_CANTIDAD_ID:
                helperTableRowsFiltered = getValidTableRowsUnit(helperTableRows,HelperTableColumnFactory.COLUMN_U_UNIDAD_CANTIDAD);
                if(helperTableRows.size()!=0){
                    return helperTableRowsFiltered;
                }
                break;
            case (int)HelperTableColumnFactory.U_PACK_MULTI_ID:
                helperTableRowsFiltered = getValidTableRowsUnit(helperTableRows,HelperTableColumnFactory.COLUMN_U_PACK_MULTI);
                if(helperTableRows.size()!=0){
                    return helperTableRowsFiltered;
                }
                break;
            case (int)HelperTableColumnFactory.U_VOLUMEN_TOT_ID:
                helperTableRowsFiltered = getValidTableRowsUnit(helperTableRows,HelperTableColumnFactory.COLUMN_U_VOLUMEN_TOT);
                if(helperTableRows.size()!=0){
                    return helperTableRowsFiltered;
                }
                break;
            default:
                return helperTableRows;
        }

        return helperTableRows;
    }

    public List<HelperTableRow> getValidTableRows(HelperTable table, RelationshipAttributeDefinition attributeDefinition, RelationshipDefinition relationshipDefinition) {

        List<HelperTableRow> someRows = new ArrayList<>();

        Relationship relationship = conceptBean.getRelationshipPlaceholders().get(relationshipDefinition.getId());

        for (RelationshipAttribute attribute : relationship.getAttributesByAttributeDefinition(attributeDefinition)) {
            someRows.addAll(manager.searchRows(table, String.valueOf(attribute.getTarget().getId()), Arrays.asList(new String[]{attributeDefinition.getName()})));
        }

        return someRows;
    }

    public List<HelperTableRow> getRelatedRows(HelperTableRow helperTableRow, HelperTableColumn helperTableColumn) {

        List<HelperTableRow> relatedRows = new ArrayList<>();

        if(helperTableRow == null) {
            return relatedRows;
        }

        relatedRows.add(dummyRow);
        relatedRows.addAll(manager.getRelatedRows(helperTableRow, helperTableColumn));

        return relatedRows;
    }

    public List<HelperTableColumn> getRelatedColumns(HelperTable helperTable) {

        return manager.getRelatedColumns(helperTable);
    }

    public List<HelperTableRow> getValidTableRowsRD( HelperTable table, long idRelationshipDefinition) {
        List<HelperTableRow> helperTableRows = getReferencedTableRows(table.getId());
        List<HelperTableRow> helperTableRowsFiltered;

        //@mromero cambie el switch por un if
        /*switch ((int)idRelationshipDefinition) {
            case (int)HelperTableColumnFactory.U_ASIST_ID:
                helperTableRowsFiltered = getValidTableRowsUnit(helperTableRows,HelperTableColumnFactory.COLUMN_U_ASIST);
                if(helperTableRows.size()!=0){
                    return helperTableRowsFiltered;
                }
            default:
                return helperTableRows;
        }*/

        if(idRelationshipDefinition == HelperTableColumnFactory.U_ASIST_ID){
            helperTableRowsFiltered = getValidTableRowsUnit(helperTableRows,HelperTableColumnFactory.COLUMN_U_ASIST);
            if(!helperTableRowsFiltered.isEmpty())
                return helperTableRowsFiltered;
        }
        return helperTableRows;
    }

    public List<HelperTableRow> getValidTableRowsUnit(List<HelperTableRow> helperTableRows, long idColumn) {
        List<HelperTableRow> helperTableRowsFiltered = new ArrayList<>();
        for (HelperTableRow helperTableRow : helperTableRows) {
            for (HelperTableData helperTableData : helperTableRow.getCells()) {
                if (helperTableData.getColumnId() == idColumn) {
                    if (helperTableData.getBooleanValue()) {
                        helperTableRowsFiltered.add(helperTableRow);
                        break;
                    }
                }

            }
        }
        return helperTableRowsFiltered;
    }


    public List<HelperTableRow> getReferencedTableRows(Long tableId) {
        if(validRow==null){
            validRow= new HashMap<>();
        }
        if(validRow.get(tableId)!=null){
            return validRow.get(tableId);
        }
        List<HelperTableRow> validTableRows = manager.getValidTableRows(tableId);
        validRow.put(tableId,validTableRows);
        return validTableRows;
    }

    public List<HelperTableRow> getRecordSearchInput(String patron) {

        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext context2 = RequestContext.getCurrentInstance();

        HelperTable helperTable = (HelperTable) UIComponent.getCurrentComponent(context).getAttributes().get("helperTable");
        RelationshipDefinition relationshipDefinition = (RelationshipDefinition) UIComponent.getCurrentComponent(context).getAttributes().get("relationshipDefinition");

        List<HelperTableRow> someRows;

        if(relationshipDefinition.isATC()) {

            List<String> columnNames = new ArrayList<>();

            columnNames.add("codigo atc");

            someRows = manager.searchRows(helperTable, patron, columnNames);

        }
        else {
            someRows = manager.searchRows(helperTable, patron);
        }

        if(relationshipDefinition!= null && relationshipDefinition.isISP() && someRows.isEmpty()){
            context2.execute("PF('dialogISP').show();");
        }

        return someRows;
    }

    public List<HelperTableRow> getRecordSearchInput(HelperTable helperTable, String patron) {

        List<HelperTableRow> someRows;
        someRows = manager.searchAllRows(helperTable, patron);
        helperTable.setRows(someRows);
        return someRows;
    }

    public List<HelperTableRow> getRecordSearchValid(HelperTable helperTable, String valid) {
        List<HelperTableRow> someRows=null;
        if(valid.equals("si")){
            someRows = manager.getRowBy(helperTable, true);
            helperTable.setRows(someRows);
        }else{
            if(valid.equals("no")){
                someRows = manager.getRowBy(helperTable, false);
                helperTable.setRows(someRows);
            }else{
                chargeRow(helperTable);
            }
        }


        return someRows;
    }

    public HelperTableRow getRecordSearchID(HelperTable helperTable, long id) {

        HelperTableRow someRow;
        someRow = manager.getRowBy(helperTable, id);
        helperTable.setRows(new ArrayList<HelperTableRow>());
        if(someRow != null) {
            helperTable.getRows().add(someRow);
        }
        if(id==0) {
            chargeRow(helperTable);
        }


        return someRow;
    }

    public HelperTableRow getRow(String id) {

        HelperTableRow someRow = null;
        if(!id.isEmpty()) {
            someRow = manager.getRowById(Long.parseLong(id));
        }
        return someRow;
    }

    public int getMinQueryLength(HelperTable helperTable) {
        return helperTableSearchBR.getMinQueryLength(helperTable);
    }

    private String pattern;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String valid;

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public MessageBean getMessageBean() {
        return messageBean;
    }

    public void setMessageBean(MessageBean messageBean) {
        this.messageBean = messageBean;
    }

    public LazyDataModel<ConceptSMTK> getConceptSMTKs() {
        return conceptSMTKs;
    }

    public void setConceptSMTKs(LazyDataModel<ConceptSMTK> conceptSMTKs) {
        this.conceptSMTKs = conceptSMTKs;
    }
}
