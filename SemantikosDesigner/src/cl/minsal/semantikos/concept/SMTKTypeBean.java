package cl.minsal.semantikos.concept;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.AuditManager;
import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.ConceptSMTK;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

/**
 * @author Gustavo Punucura
 *         Created by des01c7 on 26-07-16.
 */

@ManagedBean(name = "smtkBean")
@SessionScoped
public class SMTKTypeBean implements Serializable {

    private String pattern;

    private ConceptSMTK conceptSelected;

    private List<ConceptSMTK> conceptSearchList;

    private List<ConceptSMTK> conceptSel;

    //@EJB
    private ConceptManager conceptManager = (ConceptManager) ServiceLocator.getInstance().getService(ConceptManager.class);

    @ManagedProperty(value = "#{findConceptBean}")
    private FindConcept findConceptBean;

    public List<ConceptSMTK> getConceptSearchInput(String patron) {
        FacesContext context = FacesContext.getCurrentInstance();
        Category cD = (Category) UIComponent.getCurrentComponent(context).getAttributes().get("targetDef");
        findConceptBean.setCategorySelected(cD);
        return findConceptBean.getConceptSearchInputAndCategories(patron);
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

    public FindConcept getFindConceptBean() {
        return findConceptBean;
    }

    public void setFindConceptBean(FindConcept findConceptBean) {
        this.findConceptBean = findConceptBean;
    }
}
