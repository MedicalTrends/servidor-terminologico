package cl.minsal.semantikos.designer_modeler.designer;

import cl.minsal.semantikos.kernel.components.CategoryManager;
import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.ConceptSMTK;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by des01c7 on 23-08-16.
 */
@ManagedBean(name = "findConceptBean")
@ViewScoped
public class FindConcept implements Serializable{

    @EJB
    private ConceptManager conceptManager;

    @EJB
    private CategoryManager categoryManager;

    private List<ConceptSMTK> findConcepts;

    private ConceptSMTK conceptSMTK;

    private List<Category> categoryList;

    private Category[] categorySelect;

    private Long[] categoryArrayID;



    @PostConstruct
    public void init() {
        findConcepts = new ArrayList<ConceptSMTK>();
        categoryList = categoryManager.getCategories();
    }

    public List<ConceptSMTK> getConceptSearchInput(String pattern) {

        if (pattern != null) {
            if (pattern.length() >= 2) {
                findConcepts=conceptManager.findConceptBy(pattern);
                return findConcepts;
            }
        }
        return findConcepts;
    }
    public List<ConceptSMTK> getConceptSearchInputAndCategories(String pattern) {
        RequestContext.getCurrentInstance().update("::conceptTranslate");
        if (pattern != null) {
            if (pattern.trim().length() >= 2) {
                if(standardizationPattern(pattern).length()<=1)return null;
                findConcepts=conceptManager.findConceptBy(pattern,categoryArrayID,0,conceptManager.countConceptBy(pattern,categoryArrayID));
                return findConcepts;
            }
        }
        return null;
    }

    public List<ConceptSMTK> findConceptAllCategories(String pattern) {
        if (pattern != null) {
            if (pattern.trim().length() >= 2) {
                if(standardizationPattern(pattern).length()<=1)return null;
                findConcepts=conceptManager.findConceptBy(pattern,new Long[0],0,conceptManager.countConceptBy(pattern,new Long[0]));
                return findConcepts;
            }
        }
        return null;
    }

    private String standardizationPattern(String pattern) {

        if (pattern != null) {
            pattern = Normalizer.normalize(pattern, Normalizer.Form.NFD);
            pattern = pattern.toLowerCase();
            pattern = pattern.replaceAll("[^\\p{ASCII}]", "");
            pattern = pattern.replaceAll("\\p{Punct}+", "");
        }
        return pattern;
    }

    public List<ConceptSMTK> getFindConcepts() {
        return findConcepts;
    }

    public void setFindConcepts(List<ConceptSMTK> findConcepts) {
        this.findConcepts = findConcepts;
    }

    public ConceptSMTK getConceptSMTK() {
        return conceptSMTK;
    }

    public void setConceptSMTK(ConceptSMTK conceptSMTK) {
        this.conceptSMTK = conceptSMTK;
    }

    public List<Category> getCategoryList() {
        System.out.println("getCategoryList");
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public Category[] getCategorySelect() {
        return categorySelect;
    }

    public void setCategorySelect(Category[] categorySelect) {
        this.categorySelect = categorySelect;
    }

    public Long[] getCategoryArrayID() {
        return categoryArrayID;
    }

    public void setCategoryArrayID(Long[] categoryArrayID) {
        this.categoryArrayID = categoryArrayID;
    }
}
