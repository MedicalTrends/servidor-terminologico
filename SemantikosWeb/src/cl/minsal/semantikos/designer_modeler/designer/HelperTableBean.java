package cl.minsal.semantikos.designer_modeler.designer;

import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.HelperTableManager;
import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRecord;
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

        /* Si el patrón viene vacío o es menor a tres caracteres, no se hace nada */
         if ( patron == null || patron.length() <= 1 ) {
            return emptyList();
        }

        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext context2 = RequestContext.getCurrentInstance();

        HelperTable helperTable = (HelperTable) UIComponent.getCurrentComponent(context).getAttributes().get("helperTable");
        RelationshipDefinition relationshipDefinition = (RelationshipDefinition) UIComponent.getCurrentComponent(context).getAttributes().get("relationshipDefinition");

        List<HelperTableRecord> someRecords = new ArrayList<>();

        if(relationshipDefinition.isATC())
            someRecords = helperTableManager.searchRecords(helperTable, Arrays.asList(new String[]{"codigo_atc","dsc_completa_atc"}), patron, true);
        else
            someRecords = helperTableManager.searchRecords(helperTable, Arrays.asList(new String[]{HelperTable.SYSTEM_COLUMN_DESCRIPTION.getColumnName()}), patron, true);

        if(relationshipDefinition.isISP() && someRecords.isEmpty()){
            context2.execute("PF('dialogISP').show();");
        }

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
