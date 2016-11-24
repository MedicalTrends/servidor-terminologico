package cl.minsal.semantikos.admin_refset;

import cl.minsal.semantikos.designer_modeler.auth.AuthenticationBean;
import cl.minsal.semantikos.designer_modeler.designer.ConceptBean;
import cl.minsal.semantikos.kernel.components.CategoryManager;
import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.RefSetManager;
import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.RefSet;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import static java.lang.System.currentTimeMillis;

/**
 * @author Gustavo Punucura on 20-09-16.
 */
@ManagedBean(name = "refsetsBean")
@ViewScoped
public class RefSetsBean {


    private RefSet refSetToCreate;

    private List<RefSet> refSetList;

    private List<RefSet> conceptRefSetList;

    private List<Category> categories;

    private Category categorySelected;

    private LazyDataModel<ConceptSMTK> conceptsToCategory;

    private LazyDataModel<ConceptSMTK> conceptsToDescription;

    private String pattern;

    private RefSet refSetEdit;

    private RefSet refSetSelect;

    @EJB
    private CategoryManager categoryManager;

    @EJB
    private ConceptManager conceptManager;

    @EJB
    private RefSetManager refSetManager;

    @ManagedProperty(value = "#{authenticationBean}")
    private AuthenticationBean authenticationBean;

    @ManagedProperty(value ="#{conceptBean}")
    private ConceptBean conceptBean;


    @PostConstruct
    public void init() {
        categories = categoryManager.getCategories();
        //refSetToCreate = new RefSet(null, authenticationBean.getLoggedUser().getInstitutions().get(0), null);
        refSetList = refSetManager.getAllRefSets();

    }

    public void createRefset() {
        refSetToCreate = refSetManager.createRefSet(refSetToCreate, authenticationBean.getLoggedUser());
        refSetToCreate = new RefSet(null, authenticationBean.getLoggedUser().getInstitutions().get(0), null);
        conceptsToCategory = null;
        conceptsToDescription = null;
        refSetList = refSetManager.getAllRefSets();
    }

    public void invalidRefset() {
        refSetEdit.setValidityUntil(new Timestamp(currentTimeMillis()));
        refSetManager.updateRefSet(refSetEdit, authenticationBean.getLoggedUser());
        refSetList = refSetManager.getAllRefSets();
    }

    public void selectCategoryEvent() {


        conceptsToCategory = new LazyDataModel<ConceptSMTK>() {
            @Override
            public List<ConceptSMTK> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                List<ConceptSMTK> conceptSMTKs;
                Long[] idCategory;
                if (categorySelected == null) {
                    idCategory = new Long[0];
                } else {
                    idCategory = new Long[1];
                    idCategory[0] = categorySelected.getId();
                }

                conceptSMTKs = conceptManager.findConceptBy(null, idCategory, first, pageSize);
                this.setRowCount(conceptManager.countConceptBy(null, idCategory));

                return conceptSMTKs;
            }

        };

    }


    public void patternEvent() {

        if (pattern != null) {
            if (pattern.length() > 2) {
                conceptsToDescription = new LazyDataModel<ConceptSMTK>() {
                    @Override
                    public List<ConceptSMTK> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                        List<ConceptSMTK> conceptSMTKs;
                        conceptSMTKs = conceptManager.findConceptBy(pattern, new Long[0], first, pageSize);
                        this.setRowCount(conceptManager.countConceptBy(pattern, new Long[0]));

                        return conceptSMTKs;
                    }

                };

            } else {
                conceptsToDescription = null;
            }

        }
    }


    public void addConcept(RefSet refSet, ConceptSMTK conceptSMTK) {
        refSet.bindConceptTo(conceptSMTK);
        if (refSet.isPersistent()) {
            refSetManager.bindConceptToRefSet(conceptSMTK, refSet, authenticationBean.getLoggedUser());
        }
        if(conceptRefSetList!=null){
            conceptRefSetList=refSetManager.getRefsetsBy(conceptBean.getConcept());
        }
    }

    public void removeConcept(RefSet refSet, ConceptSMTK conceptSMTK) {
        refSet.unbindConceptTo(conceptSMTK);
        if (refSet.isPersistent()) {
            refSetManager.unbindConceptToRefSet(conceptSMTK, refSet, authenticationBean.getLoggedUser());
        }
        if(conceptRefSetList!=null){
            conceptRefSetList=refSetManager.getRefsetsBy(conceptBean.getConcept());
        }
    }


    public RefSet getRefSetToCreate() {
        return refSetToCreate;
    }

    public void setRefSetToCreate(RefSet refSetToCreate) {
        this.refSetToCreate = refSetToCreate;
    }

    public List<RefSet> getRefSetList() {
        return refSetList;
    }

    public void setRefSetList(List<RefSet> refSetList) {
        this.refSetList = refSetList;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Category getCategorySelected() {
        return categorySelected;
    }

    public void setCategorySelected(Category categorySelected) {
        this.categorySelected = categorySelected;
    }


    public CategoryManager getCategoryManager() {
        return categoryManager;
    }

    public LazyDataModel<ConceptSMTK> getConceptsToCategory() {
        return conceptsToCategory;
    }

    public void setConceptsToCategory(LazyDataModel<ConceptSMTK> conceptsToCategory) {
        this.conceptsToCategory = conceptsToCategory;
    }

    public LazyDataModel<ConceptSMTK> getConceptsToDescription() {
        return conceptsToDescription;
    }

    public void setConceptsToDescription(LazyDataModel<ConceptSMTK> conceptsToDescription) {
        this.conceptsToDescription = conceptsToDescription;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }


    public RefSet getRefSetEdit() {
        return refSetEdit;
    }

    public void setRefSetEdit(RefSet refSetEdit) {
        this.refSetEdit = refSetEdit;
    }

    public AuthenticationBean getAuthenticationBean() {
        return authenticationBean;
    }

    public void setAuthenticationBean(AuthenticationBean authenticationBean) {
        this.authenticationBean = authenticationBean;
    }

    public List<RefSet> getConceptRefSetList() {
        conceptRefSetList= refSetManager.getRefsetsBy(conceptBean.getConcept());
        return conceptRefSetList;
    }

    public void setConceptRefSetList(List<RefSet> conceptRefSetList) {
        this.conceptRefSetList = conceptRefSetList;
    }

    public ConceptBean getConceptBean() {
        return conceptBean;
    }

    public void setConceptBean(ConceptBean conceptBean) {
        this.conceptBean = conceptBean;
    }

    public RefSet getRefSetSelect() {
        return refSetSelect;
    }

    public void setRefSetSelect(RefSet refSetSelect) {
        this.refSetSelect = refSetSelect;
    }
}
