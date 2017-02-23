package cl.minsal.semantikos.beans.helpertables;

import cl.minsal.semantikos.beans.concept.ConceptBean;
import cl.minsal.semantikos.designer_modeler.auth.AuthenticationBean;
import cl.minsal.semantikos.kernel.components.HelperTablesManager;
import cl.minsal.semantikos.kernel.components.HelperTablesManagerImpl;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.businessrules.HelperTableSearchBR;
import cl.minsal.semantikos.model.helpertables.*;
import cl.minsal.semantikos.model.relationships.*;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Blueprints on 1/27/2016.
 */
@ManagedBean(name = "helperTableBean")
@ViewScoped
public class HelperTableBean implements Serializable {

    private static final long serialVersionUID = 1L;


    List<HelperTable> fullDatabase;

    @EJB
    HelperTablesManager manager;

    @ManagedProperty(value = "#{authenticationBean}")
    private AuthenticationBean authenticationBean;

    @ManagedProperty(value = "#{conceptBean}")
    private ConceptBean conceptBean;

    public AuthenticationBean getAuthenticationBean() {
        return authenticationBean;
    }

    public void setAuthenticationBean(AuthenticationBean authenticationBean) {
        this.authenticationBean = authenticationBean;
    }

    public ConceptBean getConceptBean() {
        return conceptBean;

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
            if (table.getId() <= 21)
                administrableTables.add(table);
        }

        return administrableTables;
    }

    private List<HelperTable> getFullDatabase() {
        if (fullDatabase == null)
            fullDatabase = manager.getFullDatabase();

        return fullDatabase;
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

    public void onRowEdit(RowEditEvent event) {
        HelperTableRow row = (HelperTableRow) event.getObject();
        try {
            HelperTableRow updatedRow;
            if (row.isPersistent()) {
                updatedRow = manager.updateRow(row, this.authenticationBean.getUsername());
            } else {
                updatedRow = manager.insertRow(row, this.authenticationBean.getUsername());
            }
            row.setLastEditDate(updatedRow.getLastEditDate());
            row.setLastEditUsername(updatedRow.getLastEditUsername());
            row.setCells(updatedRow.getCells());
            row.setId(updatedRow.getId());


        } catch (HelperTablesManagerImpl.RowInUseException e) {
            String msg = "Conceptos que actualmente usan este registro: <br />";

            for (ConceptSMTK conceptSMTK : e.getConcepts()) {
                msg += conceptSMTK.getConceptID() + " <br />";
            }

            //showError("No se pudo guardar registro como no valido",msg);

            FacesContext.getCurrentInstance().addMessage("message-" + row.getHelperTableId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo guardar registro como no valido", msg));


            FacesContext.getCurrentInstance().validationFailed();
        }
    }


    public void addRow(HelperTable table) {

        HelperTableRow newRow = createNewHelperTableRow(table);

        table.getRows().add(0, newRow);

    }

    private HelperTableRow createNewHelperTableRow(HelperTable table) {
        HelperTableRow newRow = new HelperTableRow();
        newRow.setId(-1);
        newRow.setCreationDate(new Timestamp(System.currentTimeMillis()));
        newRow.setCreationUsername(authenticationBean.getUsername());
        newRow.setLastEditDate(new Timestamp(System.currentTimeMillis()));
        newRow.setLastEditUsername(authenticationBean.getUsername());

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

            case (int)HelperTableRecordFactory.U_VOLUMEN_ID:
                helperTableRowsFiltered = getValidTableRowsUnit(helperTableRows,HelperTableRecordFactory.COLUMN_U_VOL);
                if(helperTableRows.size()!=0){
                    return helperTableRowsFiltered;
                }
            case (int)HelperTableRecordFactory.U_POTENCIA_ID:
                helperTableRowsFiltered = getValidTableRowsUnit(helperTableRows,HelperTableRecordFactory.COLUMN_U_POTENCIA);
                if(helperTableRows.size()!=0){
                    return helperTableRowsFiltered;
                }
            case (int)HelperTableRecordFactory.U_UNIDAD_CANTIDAD_ID:
                helperTableRowsFiltered = getValidTableRowsUnit(helperTableRows,HelperTableRecordFactory.COLUMN_U_UNIDAD_CANTIDAD);
                if(helperTableRows.size()!=0){
                    return helperTableRowsFiltered;
                }
            case (int)HelperTableRecordFactory.U_PACK_MULTI_ID:
                helperTableRowsFiltered = getValidTableRowsUnit(helperTableRows,HelperTableRecordFactory.COLUMN_U_PACK_MULTI);
                if(helperTableRows.size()!=0){
                    return helperTableRowsFiltered;
                }
            case (int)HelperTableRecordFactory.U_VOLUMEN_TOT_ID:
                helperTableRowsFiltered = getValidTableRowsUnit(helperTableRows,HelperTableRecordFactory.COLUMN_U_VOLUMEN_TOT);
                if(helperTableRows.size()!=0){
                    return helperTableRowsFiltered;
                }
            default:
                return helperTableRows;
        }

    }

    public List<HelperTableRow> getValidTableRows(HelperTable table, RelationshipAttributeDefinition attributeDefinition, RelationshipDefinition relationshipDefinition) {

        List<HelperTableRow> someRows = new ArrayList<>();

        Relationship relationship = conceptBean.getRelationshipPlaceholders().get(relationshipDefinition.getId());

        for (RelationshipAttribute attribute : relationship.getAttributesByAttributeDefinition(attributeDefinition)) {
            someRows.addAll(manager.searchRows(table, String.valueOf(attribute.getTarget().getId()), Arrays.asList(new String[]{attributeDefinition.getName()})));
        }

        return someRows;
    }

    public List<HelperTableRow> getValidTableRowsRD( HelperTable table, long idRelationshipDefinition) {
        List<HelperTableRow> helperTableRows = getReferencedTableRows(table.getId());
        List<HelperTableRow> helperTableRowsFiltered;


        switch ((int)idRelationshipDefinition) {
            case (int)HelperTableRecordFactory.U_ASIST_ID:
                helperTableRowsFiltered = getValidTableRowsUnit(helperTableRows,HelperTableRecordFactory.COLUMN_U_ASIST);
                if(helperTableRows.size()!=0){
                    return helperTableRowsFiltered;
                }
            default:
                return helperTableRows;
        }
    }

    public List<HelperTableRow> getValidTableRowsUnit(List<HelperTableRow> helperTableRows, long idColumn) {
        List<HelperTableRow> helperTableRowsFiltered = new ArrayList<>();
        for (HelperTableRow helperTableRow : helperTableRows) {
            for (HelperTableData helperTableData : helperTableRow.getCells()) {
                if (helperTableData.getColumnId() == idColumn) {
                    if (helperTableData.isBooleanValue()) {
                        helperTableRowsFiltered.add(helperTableRow);
                        break;
                    }
                }

            }
        }
        return helperTableRowsFiltered;
    }


    public List<HelperTableRow> getReferencedTableRows(Long tableId) {
        List<HelperTableRow> validTableRows = manager.getValidTableRows(tableId);
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

    public int getMinQueryLength(HelperTable helperTable) {
        return HelperTableSearchBR.getMinQueryLength(helperTable);
    }

}
