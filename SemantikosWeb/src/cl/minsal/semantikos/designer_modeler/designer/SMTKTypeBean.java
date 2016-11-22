package cl.minsal.semantikos.designer_modeler.designer;

import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.ConceptSMTK;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gustavo Punucura
 *         Created by des01c7 on 26-07-16.
 */

@ManagedBean(name = "smtkBean")
@ViewScoped
public class SMTKTypeBean implements Serializable {

    private String pattern;

    private ConceptSMTK conceptSelected;

    private Map<Long, LazyDataModel<ConceptSMTK>> conceptSearchMap;

    private List<ConceptSMTK> conceptSearchList;
    private List<ConceptSMTK> conceptSel;

    public List<ConceptSMTK> getConceptSearchList() {
        return conceptSearchList;
    }

    public void setConceptSearchList(List<ConceptSMTK> conceptSearchList) {
        this.conceptSearchList = conceptSearchList;
    }

    public List<ConceptSMTK> getConceptSel() {
        return conceptSel;
    }

    public void setConceptSel(List<ConceptSMTK> conceptSel) {
        this.conceptSel = conceptSel;
    }

    @EJB
    private ConceptManager conceptManager;


    public LazyDataModel<ConceptSMTK> getConceptSearchForRDId(final Long idRelationshipDefinition, final Category targetDefinition) {

        if (conceptSearchMap == null)
            conceptSearchMap = new HashMap<Long, LazyDataModel<ConceptSMTK>>();

        if (!conceptSearchMap.containsKey(idRelationshipDefinition)) {
            LazyDataModel<ConceptSMTK> conceptSearch;
            conceptSearch = new LazyDataModel<ConceptSMTK>() {
                @Override
                public List<ConceptSMTK> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    List<ConceptSMTK> conceptSMTKs;
                    Long[] categoryArr = new Long[1];
                    categoryArr[0] = targetDefinition.getId();
                    conceptSMTKs = conceptManager.findConceptBy(pattern, categoryArr, first, pageSize);
                    this.setRowCount(conceptManager.countConceptBy(pattern, categoryArr));
                    return conceptSMTKs;
                }
            };
            conceptSearchMap.put(idRelationshipDefinition, conceptSearch);
        }

        return conceptSearchMap.get(idRelationshipDefinition);
    }


    public List<ConceptSMTK> getConceptSearchInput(String patron) {

        FacesContext context = FacesContext.getCurrentInstance();
        Category cD = (Category) UIComponent.getCurrentComponent(context).getAttributes().get("targetDef");
        conceptSearchList = new ArrayList<ConceptSMTK>();
        final Long[] categoryArr = new Long[1];

        if (patron != null) {
            if (patron.length() >= 2) {
                categoryArr[0] = cD.getId();
                conceptSearchList = conceptManager.findConceptBy(patron, categoryArr, 0, 30);
                return conceptSearchList;
            }
        }
        return conceptSearchList;
    }

    @PostConstruct
    public void init() {
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public ConceptManager getConceptManager() {
        return conceptManager;
    }

    public void setConceptManager(ConceptManager conceptManager) {
        this.conceptManager = conceptManager;
    }

    public ConceptSMTK getConceptSelected() {
        return conceptSelected;
    }

    public void setConceptSelected(ConceptSMTK conceptSelected) {
        this.conceptSelected = conceptSelected;
    }


}
