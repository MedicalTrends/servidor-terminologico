package cl.minsal.semantikos.browser;

import cl.minsal.semantikos.clients.RemoteEJBClientFactory;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.users.AuthenticationBean;


import cl.minsal.semantikos.model.queries.DescriptionQuery;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.DescriptionType;
import cl.minsal.semantikos.model.descriptions.DescriptionTypeFactory;
import cl.minsal.semantikos.model.refsets.RefSet;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * Created by diego on 26/06/2016.
 */

@ManagedBean(name = "descriptionsBrowserBean")
@ViewScoped
public class DescriptionsBrowserBean implements Serializable {

    static final Logger logger = LoggerFactory.getLogger(DescriptionsBrowserBean.class);

    //@EJB
    QueryManager queryManager = (QueryManager) RemoteEJBClientFactory.getInstance().getManager(QueryManager.class);

    //@EJB
    RefSetManager refSetManager = (RefSetManager) RemoteEJBClientFactory.getInstance().getManager(RefSetManager.class);

    //@EJB
    private CategoryManager categoryManager = (CategoryManager) RemoteEJBClientFactory.getInstance().getManager(CategoryManager.class);

    //@EJB
    private ConceptManager conceptManager = (ConceptManager) RemoteEJBClientFactory.getInstance().getManager(ConceptManager.class);

    /**
     * Objeto de consulta: contiene todos los filtros y columnas necesarios para el despliegue de los resultados en el navegador
     */
    private DescriptionQuery descriptionQuery;

    /**
     * Lista de categorías para el despliegue del filtro por categorías
     */
    private List<Category> categories = new ArrayList<Category>();

    /**
     * Lista de tipos de descripción para el despliegue del filtro por tipo
     */
    private List<DescriptionType> descriptionTypes = new ArrayList<DescriptionType>();

    /**
     * Lista de RefSets para el despliegue del filtro por refsets
     */
    private List<RefSet> refSets = new ArrayList<RefSet>();

    /**
     * Lista de conceptos para el despliegue del resultado de la consulta
     */
    private LazyDataModel<Description> descriptions;


    /**
     * Indica si cambió algún filtro. Se utiliza para resetear la páginación al comienzo si se ha filtrado

     */
    private boolean isFilterChanged;


    @ManagedProperty(value = "#{authenticationBean}")
    private AuthenticationBean authenticationBean;


    @PostConstruct
    public void init(){
        categories = categoryManager.getCategories();
        descriptionTypes = DescriptionTypeFactory.getInstance().getDescriptionTypes();
        refSets = refSetManager.getValidRefSets();
    }

    /**
     * Este método es el responsable de ejecutar la consulta
     */
    public void executeQuery() {

        /**
         * Si el objeto de consulta no está inicializado, inicializarlo
         */
        if(descriptionQuery == null)
            descriptionQuery = queryManager.getDefaultDescriptionQuery();

        /**
         * Ejecutar la consulta
         */
        descriptions = new LazyDataModel<Description>() {
            @Override
            public List<Description> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                if(isFilterChanged)
                    descriptionQuery.setPageNumber(0);
                else
                    descriptionQuery.setPageNumber(first);

                isFilterChanged = false;

                descriptionQuery.setPageSize(pageSize);
                descriptionQuery.setOrder(new Integer(sortField));

                if(sortOrder.name().substring(0,3).toLowerCase().equals("asc"))
                    descriptionQuery.setAsc(sortOrder.name().substring(0,3).toLowerCase());
                else
                    descriptionQuery.setAsc(sortOrder.name().substring(0,4).toLowerCase());

                List<Description> descriptions = null;
                try {
                    descriptions = queryManager.executeQuery(descriptionQuery);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                this.setRowCount(queryManager.countQueryResults(descriptionQuery));

                return descriptions;
            }

        };

    }

    public DescriptionQuery getDescriptionQuery() {
        return descriptionQuery;
    }

    public List<RefSet> getRefSetsSearchInput(String patron) {

        return refSetManager.getRefsetsBy(Arrays.asList(descriptionQuery.getCategoryValues()), patron);
    }

    public List<Category> getCategoriesSearchInput(String patron) {

        List<Category> someCategories = new ArrayList<>();

        for (Category category : categoryManager.getCategories()) {
            if(category.getName().toLowerCase().contains(patron.toLowerCase()))
                someCategories.add(category);
        }

        return someCategories;
    }

    public LazyDataModel<Description> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(LazyDataModel<Description> descriptions) {
        this.descriptions = descriptions;
    }

    public CategoryManager getCategoryManager() {
        return categoryManager;
    }

    public void setCategoryManager(CategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

    public ConceptManager getConceptManager() {
        return conceptManager;
    }

    public void setConceptManager(ConceptManager conceptManager) {
        this.conceptManager = conceptManager;
    }

    public AuthenticationBean getAuthenticationBean() {
        return authenticationBean;
    }

    public void setAuthenticationBean(AuthenticationBean authenticationBean) {
        this.authenticationBean = authenticationBean;
    }

    public RefSetManager getRefSetManager() {
        return refSetManager;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<DescriptionType> getDescriptionTypes() {
        return descriptionTypes;
    }

    public List<RefSet> getRefSets() {
        return refSets;
    }

    public boolean isFilterChanged() {
        return isFilterChanged;
    }

    public void setFilterChanged(boolean filterChanged) {
        isFilterChanged = filterChanged;
    }
}

