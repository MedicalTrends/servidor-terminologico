package cl.minsal.semantikos.designer_modeler.designer;

import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.HelperTableManager;
import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRecord;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;
import cl.minsal.semantikos.model.relationships.RelationshipAttributeDefinition;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.*;

import static java.util.Collections.emptyList;

/**
 * Created by des01c7 on 26-07-16.
 */

@ManagedBean(name = "helperTableBean")
@ViewScoped
public class HelperTableBean implements Serializable {

    private String pattern;

    private HelperTableRecord recordPlaceHolder = new HelperTableRecord();

    @EJB
    private HelperTableManager helperTableManager;

    @PostConstruct
    public void init() {
    }

    public List<HelperTableRecord> getRecordSearchInput(String patron) {

        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext context2 = RequestContext.getCurrentInstance();

        HelperTable helperTable = (HelperTable) UIComponent.getCurrentComponent(context).getAttributes().get("helperTable");
        RelationshipDefinition relationshipDefinition = (RelationshipDefinition) UIComponent.getCurrentComponent(context).getAttributes().get("relationshipDefinition");

        List<HelperTableRecord> someRecords;
        String[] columnNames = new String[]{};

        if(relationshipDefinition != null) {
            if (relationshipDefinition.isATC()) {
                columnNames = new String[]{"codigo_atc", "dsc_completa_atc"};
            } else if (relationshipDefinition.isISP()) {
                columnNames = new String[]{"registro"};
            } else if (relationshipDefinition.isBioequivalente()) {
                columnNames = new String[]{"registro", "nombre"};
            }
        }
        if(columnNames.length == 0) {
            columnNames = new String[]{HelperTable.SYSTEM_COLUMN_DESCRIPTION.getColumnName()};
        }

        someRecords = helperTableManager.searchRecords(helperTable, Arrays.asList(columnNames), patron, true);

        if(relationshipDefinition!= null && relationshipDefinition.isISP() && someRecords.isEmpty()){
            context2.execute("PF('dialogISP').show();");
        }

        return someRecords;
    }

    public List<HelperTableRecord> getAllRecord() {

        FacesContext context = FacesContext.getCurrentInstance();

        HelperTable helperTable = (HelperTable) UIComponent.getCurrentComponent(context).getAttributes().get("helperTable");

        List<HelperTableRecord> someRecords;

        someRecords= helperTableManager.getAllRecords(helperTable);

        return someRecords;
    }


    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public HelperTableManager getHelperTableManager() {
        return helperTableManager;
    }

    public void setHelperTableManager(HelperTableManager helperTableManager) {
        this.helperTableManager = helperTableManager;
    }

    public HelperTableRecord getRecordPlaceHolder() {
        return recordPlaceHolder;
    }

    public void setRecordPlaceHolder(HelperTableRecord recordPlaceHolder) {
        this.recordPlaceHolder = recordPlaceHolder;
    }


}
