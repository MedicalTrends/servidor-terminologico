package cl.minsal.semantikos.beans.helpertables;

import cl.minsal.semantikos.designer_modeler.auth.AuthenticationBean;
import cl.minsal.semantikos.kernel.components.HelperTablesManager;
import cl.minsal.semantikos.kernel.components.HelperTablesManagerImpl;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.helpertables.*;
import org.primefaces.event.RowEditEvent;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Blueprints on 1/27/2016.
 */
@ManagedBean(name="helperTableBean")
@ViewScoped
public class HelperTableBean implements Serializable{

    private static final long serialVersionUID = 1L;


    List<HelperTable> fullDatabase;

    @EJB
    HelperTablesManager manager;


    @ManagedProperty(value = "#{authenticationBean}")
    private AuthenticationBean authenticationBean;

    public AuthenticationBean getAuthenticationBean() {
        return authenticationBean;
    }

    public void setAuthenticationBean(AuthenticationBean authenticationBean) {
        this.authenticationBean = authenticationBean;
    }

    public HelperTablesManager getHelperTablesManager(){
        return manager;
    }


    public List<HelperTable> getAdministrableTables(){

        List<HelperTable> administrableTables = new ArrayList<>();

        for (HelperTable table : getFullDatabase()) {
            if(table.getId()<=17)
                administrableTables.add(table);
        }

        return administrableTables;
    }

    private List<HelperTable> getFullDatabase() {
        if(fullDatabase==null)
            fullDatabase = manager.getFullDatabase();

        return fullDatabase;
    }


    public void onRowEditCancel(RowEditEvent event) {
        HelperTableRow row = (HelperTableRow) event.getObject();

        if(row.isPersistent())
            return;

        Long tableId = row.getHelperTableId();

        for (HelperTable helperTable : fullDatabase) {
            if(helperTable.getId()==tableId) {
                helperTable.getRows().remove(row);
            }
        }

    }

    public void onRowEdit(RowEditEvent event) {
        HelperTableRow row = (HelperTableRow) event.getObject();
        try {
            HelperTableRow updatedRow;
            if(row.isPersistent()) {
                updatedRow = manager.updateRow(row, this.authenticationBean.getUsername());
            }
            else{
                updatedRow = manager.insertRow(row, this.authenticationBean.getUsername());
            }
            row.setLastEditDate(updatedRow.getLastEditDate());
            row.setLastEditUsername(updatedRow.getLastEditUsername());
            row.setCells(updatedRow.getCells());
            row.setId(updatedRow.getId());


        } catch (HelperTablesManagerImpl.RowInUseException e) {
            String msg = "Conceptos que actualmente usan este registro: <br />";

            for (ConceptSMTK conceptSMTK : e.getConcepts()) {
                msg += conceptSMTK.getConceptID()+" <br />";
            }

            //showError("No se pudo guardar registro como no valido",msg);

            FacesContext.getCurrentInstance().addMessage("message-"+row.getHelperTableId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo guardar registro como no valido", msg));


            FacesContext.getCurrentInstance().validationFailed();
        }
    }


    public void addRow(HelperTable table){

        HelperTableRow newRow = createNewHelperTableRow(table);

        table.getRows().add(0,newRow);

    }

    private HelperTableRow createNewHelperTableRow(HelperTable table) {
        HelperTableRow newRow = new HelperTableRow();
        newRow.setId(-1);
        newRow.setCreationDate(new Date());
        newRow.setCreationUsername(authenticationBean.getUsername());
        newRow.setLastEditDate(new Date());
        newRow.setLastEditUsername(authenticationBean.getUsername());

        newRow.setDescription("Nuevo Elemento");
        newRow.setValid(false);
        newRow.setHelperTableId(table.getId());


        newRow.setCells(new ArrayList<HelperTableData>());
        for (HelperTableColumn column: table.getColumns()) {
            HelperTableData data = createCell(column,newRow);
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




    public List<HelperTableRow> getValidTableRows(HelperTable table){
        return getReferencedTableRows(table.getId());
    }

    public List<HelperTableRow> getReferencedTableRows(Long tableId){
        List<HelperTableRow> validTableRows = manager.getValidTableRows(tableId);
        return validTableRows;
    }
}
