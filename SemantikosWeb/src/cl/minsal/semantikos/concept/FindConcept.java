package cl.minsal.semantikos.concept;

import cl.minsal.semantikos.clients.RemoteEJBClientFactory;
import cl.minsal.semantikos.kernel.components.AuthenticationManager;
import cl.minsal.semantikos.kernel.components.CategoryManager;
import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.componentsweb.TimeOutWeb;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.ConceptSMTK;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.singletonList;
import static org.apache.commons.lang.ArrayUtils.EMPTY_LONG_ARRAY;
import static org.apache.commons.lang.ArrayUtils.EMPTY_LONG_OBJECT_ARRAY;

/**
 * Created by des01c7 on 23-08-16.
 */
@ManagedBean(name = "findConceptBean")
@ViewScoped
public class FindConcept implements Serializable{

    private List<ConceptSMTK> findConcepts;

    private ConceptSMTK conceptSMTK;

    private List<Category> categoryList;

    private Category categorySelected;

    private List<Category> selectedCategories = new ArrayList<>();

    private String pattern;

    //@EJB
    private ConceptManager conceptManager = (ConceptManager) RemoteEJBClientFactory.getInstance().getManager(ConceptManager.class);

    //@EJB
    private CategoryManager categoryManager = (CategoryManager) RemoteEJBClientFactory.getInstance().getManager(CategoryManager.class);

    @PostConstruct
    public void init() {
        selectedCategories = new ArrayList<>();
        findConcepts = new ArrayList<>();
        categoryList = categoryManager.getCategories();
    }

    /**
     * Método encargado de obtener los conceptos por categoría
     */
    public void getConceptByCategory(){
       findConcepts =conceptManager.findConcepts(pattern, selectedCategories,EMPTY_LIST,null);
    }

    /**
     * Este método es el encargado de relaizar la búsqueda por patrón de texto y categorías seleccionadas
     * @param pattern
     * @return
     */
    public List<ConceptSMTK> getConceptSearchInputAndCategories(String pattern) {
        RequestContext.getCurrentInstance().update("::conceptTranslate");

        if (pattern != null) {
            if (pattern.trim().length() >= 2) {
                if(standardizationPattern(pattern).length() <= 1) {
                    return null;
                }
                findConcepts=conceptManager.findConcepts(pattern,selectedCategories,EMPTY_LIST,true);
                return findConcepts;
            }
        }
        return null;
    }

    /**
     * Este método es el encargado de relaizar la búsqueda por patrón de texto y categorías seleccionadas
     * @param pattern
     * @return
     */
    public List<ConceptSMTK> getConceptSearchInputCategoryContext(String pattern) {

        if (pattern != null) {

            if (pattern.trim().length() >= 2) {
                if(standardizationPattern(pattern).length()<=1)return null;
                findConcepts=conceptManager.findConcepts(pattern,selectedCategories,EMPTY_LIST,true);
                return findConcepts;
            }
        }
        return null;
    }


    /**
     * Este método realiza la búsqueda de concepto por todas las categorías
     * @param pattern
     * @return
     */
    public List<ConceptSMTK> findConceptAllCategories(String pattern) {
        if (pattern != null) {
            if (pattern.trim().length() >= 2) {
                if(standardizationPattern(pattern).length() <= 1) {
                    return null;
                }
                findConcepts=conceptManager.findConcepts(pattern,EMPTY_LIST,EMPTY_LIST,true);
                return findConcepts;
            }
        }
        return null;
    }


    /**
     * Meotodo encargado de setear el texto de acuerdo al estandar de búsqueda
     * @param pattern
     * @return
     */

    private String standardizationPattern(String pattern) {

        if (pattern != null) {
            pattern = Normalizer.normalize(pattern, Normalizer.Form.NFD);
            pattern = pattern.toLowerCase();
            pattern = pattern.replaceAll("[^\\p{ASCII}]", "");
            pattern = pattern.replaceAll("\\p{Punct}+", "");
        }
        return pattern;
    }


    /**
     * Getter and Setter
     *
     */

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
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public Category getCategorySelected() {
        return categorySelected;
    }

    public void setCategorySelected(Category categorySelected) {
        this.categorySelected = categorySelected;
        if(categorySelected != null) {
            this.selectedCategories = singletonList(categorySelected);
        }
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public List<Category> getSelectedCategories() {
        return selectedCategories;
    }

    public void setSelectedCategories(List<Category> selectedCategories) {
        if(selectedCategories != null) {
            this.selectedCategories = selectedCategories;
        }
    }
}