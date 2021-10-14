package cl.minsal.semantikos.browser;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.components.GuestPreferences;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.kernel.componentsweb.TimeOutWeb;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.DescriptionTypeFactory;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.queries.BrowserQuery;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.snomedct.RelationshipSCT;
import cl.minsal.semantikos.model.system.SystemFactory;
import cl.minsal.semantikos.model.tags.Tag;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.primefaces.context.RequestContext;
import org.primefaces.event.data.PageEvent;
import org.primefaces.extensions.model.layout.LayoutOptions;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.TreeNode;
import org.primefaces.model.menu.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.lang.System.currentTimeMillis;
import static org.primefaces.util.Constants.EMPTY_STRING;

@ManagedBean
@ViewScoped
public class BrowserBean implements Serializable {

    private static final long serialVersionUID = 20120925L;

    /**
     * Variables para el browser
     */
    static final Logger logger = LoggerFactory.getLogger(BrowserBean.class);

    /**
     * Objeto de consulta: contiene todos los filtros y columnas necesarios para el despliegue de los resultados en el navegador
     */
    private BrowserQuery browserQuery;

    /**
     * Lista de categorías para el despliegue del filtro por categorías
     */
    private List<Category> categories = new ArrayList<Category>();

    /**
     * Lista de tags para el despliegue del filtro por tags
     */
    private List<Tag> tags = new ArrayList<Tag>();

    /**
     * Lista de conceptos para el despliegue del resultado de la consulta
     */
    private LazyDataModel<ConceptSMTK> concepts;
    private ConceptSMTK conceptSelected;

    private Description descriptionSelected = DescriptionTypeFactory.DUMMY_DESCRIPTION;

    private Category categorySelected;

    /**
     * Indica si cambió algún filtro. Se utiliza para resetear la páginación al comienzo si se ha filtrado
     */
    private boolean isFilterChanged;

    /**
     * Indica si se debe realizar una búsqueda
     */
    private boolean performSearch = false;

    private boolean showFilters = false;

    private boolean showCategories = false;

    private int results;

    private float seconds;

    private long init;

    private int page = 1;

    private int pages = 1;

    private boolean sort;

    private transient TreeNode root;

    @ManagedProperty(value = "#{guestPreferences}")
    GuestPreferences guestPreferences;

    @ManagedProperty(value = "#{conceptBean}")
    ConceptBean conceptBean;

    @ManagedProperty(value = "#{conceptSCTBean}")
    ConceptSCTBean conceptSCTBean;

    //@EJB
    private QueryManager queryManager = (QueryManager) ServiceLocator.getInstance().getService(QueryManager.class);

    //@EJB
    private TagManager tagManager = (TagManager) ServiceLocator.getInstance().getService(TagManager.class);

    //@EJB
    private CategoryManager categoryManager = (CategoryManager) ServiceLocator.getInstance().getService(CategoryManager.class);

    //@EJB
    private ConceptManager conceptManager = (ConceptManager) ServiceLocator.getInstance().getService(ConceptManager.class);

    //@EJB
    private DescriptionManager descriptionManager = (DescriptionManager) ServiceLocator.getInstance().getService(DescriptionManager.class);

    //@EJB
    private RelationshipManager relationshipManager = (RelationshipManager) ServiceLocator.getInstance().getService(RelationshipManager.class);

    private transient MenuModel menu = new DefaultMenuModel();

    private boolean snomedCT;

    @PostConstruct
    protected void initialize() {

        guestPreferences.setTheme("indigo");

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        request.getSession().setMaxInactiveInterval(SystemFactory.getTimeout());

        //ServiceLocator.getInstance().closeContext();
        tags = tagManager.getAllTags();
        categories = categoryManager.getCategories();

        browserQuery = queryManager.getDefaultBrowserQuery();

        setRoot(new DefaultTreeNode(new Object(), null));

        try {
            test();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setSnomedCT(false);

    }

    public int getResults() {
        return results;
    }

    public void setResults(int results) {
        this.results = results;
    }

    public float getSeconds() {
        return seconds;
    }

    public void setSeconds(float seconds) {
        this.seconds = seconds;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    /**
     * Este método es el responsable de ejecutar la consulta
     */
    public void executeQuery() {

        resetTheme();

        init = currentTimeMillis();

        /**
         * Si el objeto de consulta no está inicializado, inicializarlo
         */
        if(browserQuery == null) {
            browserQuery = queryManager.getDefaultBrowserQuery();
        }

        browserQuery.setTruncateMatch(false);

        /**
         * Si la consulta viene nula o vacía retornan inmediatamente
         */
        if(browserQuery.getQuery() == null || browserQuery.getQuery().isEmpty()) {
            return;
        }

        if(!performSearch) {
            return;
        }
        else {
            performSearch = false;
        }

        /**
         * Ejecutar la consulta
         */
        concepts = new LazyDataModel<ConceptSMTK>() {
            @Override
            public List<ConceptSMTK> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                //List<ConceptSMTK> conceptSMTKs = conceptManager.findConceptsBy(category, first, pageSize);

               if(isFilterChanged) {
                    browserQuery.setPageNumber(0);
                    RequestContext context = RequestContext.getCurrentInstance();
                    context.execute("PF('conceptTable').getPaginator().setPage(0);");
                    this.setRowIndex(0);
                }
                else {
                    browserQuery.setPageNumber(first);
                }

                isFilterChanged = false;

                setShowCategories(false);

                browserQuery.setPageSize(pageSize);
                //browserQuery.setOrder(new Integer(sortField==null?"0":sortField));

                /*
                if(sortOrder.name().substring(0,3).toLowerCase().equals("asc")) {
                    browserQuery.setAsc(sortOrder.name().substring(0, 3).toLowerCase());
                }
                else {
                    browserQuery.setAsc(sortOrder.name().substring(0, 4).toLowerCase());
                }
                */

                int rowCount = 0;

                List<ConceptSMTK> conceptSMTKs = queryManager.executeQuery(browserQuery);

                rowCount = queryManager.countQueryResults(browserQuery);

                browserQuery.setTruncateMatch(true);

                for (ConceptSMTK conceptSMTK : queryManager.executeQuery(browserQuery)) {
                    if(!conceptSMTKs.contains(conceptSMTK)) {
                        conceptSMTKs.add(conceptSMTK);
                    }
                }


                if(rowCount < conceptSMTKs.size()) {
                    rowCount = queryManager.countQueryResults(browserQuery);
                    //rowCount = rowCount + queryManager.countQueryResults(browserQuery);
                }

                this.setRowCount(rowCount);

                results = this.getRowCount();
                seconds = (float) ((currentTimeMillis() - init)/1000.0);

                if((results % 15) == 0) {
                    pages = results/15;
                }
                else {
                    pages = results/15 + 1;
                }

                if(results == 0) {
                    page = 1;
                }

                return conceptSMTKs;
            }

            @Override
            public Object getRowKey(ConceptSMTK conceptSMTK) {
                return conceptSMTK != null ? conceptSMTK.getConceptID() : null;
            }

            @Override
            public ConceptSMTK getRowData(String rowKey) {
                List<ConceptSMTK> conceptSMTKList = getWrappedData();
                String value = String.valueOf(rowKey);

                for (ConceptSMTK conceptSMTK : conceptSMTKList) {
                    if (conceptSMTK.getConceptID().equals(value)) {
                        return conceptSMTK;
                    }
                }

                return null;
            }

        };

    }

    public void resetTheme() {
        getGuestPreferences().setTheme("indigo");
    }

    public List<Description> searchSuggestedDescriptions(String term) {
        isFilterChanged = true;
        browserQuery.setQuery(term);
        List<Description> suggestedDescriptions = new ArrayList<>();
        DescriptionTypeFactory.DUMMY_DESCRIPTION.setTerm(EMPTY_STRING);
        suggestedDescriptions.add(DescriptionTypeFactory.DUMMY_DESCRIPTION);
        suggestedDescriptions.addAll(descriptionManager.searchDescriptionsSuggested(term, categories, null, null));
        return suggestedDescriptions;
    }

    public void test() throws IOException {

        /*
        if(descriptionSelected == null) {
            descriptionSelected = DescriptionTypeFactory.DUMMY_DESCRIPTION;
        }
        */

        performSearch = true;
        /**
         * Si no se ha seleccionado ninguna descripción sugerida,
         */
        if(descriptionSelected.equals(DescriptionTypeFactory.DUMMY_DESCRIPTION)) {
            descriptionSelected.setTerm(browserQuery.getQuery());
        }
        else {
            //browserQuery.setQuery(descriptionSelected.getTerm());
            browserQuery.setQuery(descriptionSelected.getConceptSMTK().getConceptID());
        }
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        if(request.getRequestURI().equals("/views/home.xhtml")) {
            ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
            eContext.redirect(eContext.getRequestContextPath() + "/views/concepts");
        }
    }

    public void redirect() throws IOException {
        ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();

        if(browserQuery.getQuery() != null && browserQuery.getQuery().length() >= 3) {
            performSearch = true;
            eContext.redirect(eContext.getRequestContextPath() + "/");
        }
    }

    public void redirectSnomedCT() throws IOException {

        ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
        if(conceptBean != null) {
            conceptBean.selectedConcept = null;
        }
        if(conceptSCTBean != null) {
            conceptSCTBean.selectedConcept = null;
        }

        if(snomedCT) {
            eContext.redirect(eContext.getRequestContextPath() + "/views/snomed/concepts");
        }
        else {
            eContext.redirect(eContext.getRequestContextPath() + "/");
        }
    }

    public void invalidate() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        request.getSession().invalidate();
        context.getExternalContext().redirect(context.getExternalContext().getRequestContextPath());
    }

    public void updatePage(PageEvent event) {
        int pageindex = event.getPage();
        page = pageindex + 1;
        RequestContext reqCtx = RequestContext.getCurrentInstance();
        //reqCtx.execute("PF('conceptTableExcel').getPaginator().setPage("+pageindex+")");
    }

    public String getRelationship(Object first, Object second) {
        if(first instanceof ConceptSMTK && second instanceof ConceptSMTK) {
            return getRelationshipSMTK((ConceptSMTK) first, (ConceptSMTK) second);
        }
        if(first instanceof ConceptSCT && second instanceof ConceptSCT) {
            return getRelationshipSCT((ConceptSCT) first, (ConceptSCT) second);
        }
        if(first instanceof ConceptSMTK && second instanceof ConceptSCT) {
            return getRelationshipCrossmap((ConceptSMTK) first, (ConceptSCT) second);
        }
        return EMPTY_STRING;
    }

    public String getRelationshipSMTK(ConceptSMTK first, ConceptSMTK second) {

        for (Relationship relationship : first.getRelationships()) {
            if(relationship.getRelationshipDefinition().getTargetDefinition().isSMTKType()) {
                ConceptSMTK child = (ConceptSMTK) relationship.getTarget();
                if(child.equals(second)) {
                    return relationship.getRelationshipDefinition().getName();
                }
            }
        }

        for (ConceptSMTK concept : conceptManager.getRelatedConcepts(first)) {
            if(concept.equals(second)) {
                return "es un[a]";
            }
        }

        return EMPTY_STRING;
    }

    public String getRelationshipSCT(ConceptSCT first, ConceptSCT second) {

        for (RelationshipSCT relationship : first.getRelationships()) {
            ConceptSCT child = relationship.getDestinationConcept();
            if(child.equals(second)) {
                return relationship.getTypeConcept().getDescriptionFavouriteSynonymous().getTerm();
            }
        }

        return EMPTY_STRING;
    }

    public String getRelationshipCrossmap(ConceptSMTK first, ConceptSCT second) {

        for (Relationship relationship : first.getRelationships()) {
            if(relationship.getRelationshipDefinition().getTargetDefinition().isSnomedCTType()) {
                ConceptSCT child = (ConceptSCT) relationship.getTarget();
                if(child.equals(second)) {
                    HelperTableRow helperTableRow = (HelperTableRow) relationship.getRelationshipTypeAttribute().getTarget();
                    return helperTableRow.getDescription();
                }
            }
        }

        return EMPTY_STRING;
    }

    public LazyDataModel<ConceptSMTK> getConcepts() {
        return concepts;
    }

    public void setConcepts(LazyDataModel<ConceptSMTK> concepts) {
        this.concepts = concepts;
    }

    public ConceptSMTK getConceptSelected() {
        return conceptSelected;
    }

    public void setConceptSelected(ConceptSMTK conceptSelected) {
        this.conceptSelected = conceptSelected;
    }

    public Category getCategorySelected() {
        return categorySelected;
    }

    public void setCategorySelected(Category categorySelected) {
        this.categorySelected = categorySelected;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
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

    public BrowserQuery getBrowserQuery() {
        return browserQuery;
    }

    public void setBrowserQuery(BrowserQuery browserQuery) {
        this.browserQuery = browserQuery;
    }

    public QueryManager getQueryManager() {
        return queryManager;
    }

    public void setQueryManager(QueryManager queryManager) {
        this.queryManager = queryManager;
    }

    public TagManager getTagManager() {
        return tagManager;
    }

    public void setTagManager(TagManager tagManager) {
        this.tagManager = tagManager;
    }

    public boolean isFilterChanged() {
        return isFilterChanged;
    }

    public void setFilterChanged(boolean filterChanged) {
        isFilterChanged = filterChanged;
    }

    public boolean isShowFilters() {
        return showFilters;
    }

    public void setShowFilters(boolean showFilters) {
        this.showFilters = showFilters;
    }

    public DescriptionManager getDescriptionManager() {
        return descriptionManager;
    }

    public void setDescriptionManager(DescriptionManager descriptionManager) {
        this.descriptionManager = descriptionManager;
    }

    public RelationshipManager getRelationshipManager() {
        return relationshipManager;
    }

    public void setRelationshipManager(RelationshipManager relationshipManager) {
        this.relationshipManager = relationshipManager;
    }

    public Description getDescriptionSelected() {
        return descriptionSelected;
    }

    public void setDescriptionSelected(Description descriptionSelected) {
        this.descriptionSelected = descriptionSelected;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }


    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public GuestPreferences getGuestPreferences() {
        return guestPreferences;
    }

    public void setGuestPreferences(GuestPreferences guestPreferences) {
        this.guestPreferences = guestPreferences;
    }

    public boolean isPerformSearch() {
        return performSearch;
    }

    public void setPerformSearch(boolean performSearch) {
        this.performSearch = performSearch;
    }

    public boolean isSort() {
        return sort;
    }

    public void setSort(boolean sort) {
        this.sort = sort;
    }

    public void changeSort() {
        if(sort) {
            browserQuery.setAsc("desc");
        }
        else {
            browserQuery.setAsc("asc");
        }
        performSearch = true;
    }

    public boolean isSnomedCT() {
        return snomedCT;
    }

    public void setSnomedCT(boolean snomedCT) {
        this.snomedCT = snomedCT;
    }

    public ConceptSCTBean getConceptSCTBean() {
        return conceptSCTBean;
    }

    public void setConceptSCTBean(ConceptSCTBean conceptSCTBean) {
        this.conceptSCTBean = conceptSCTBean;
    }

    public ConceptBean getConceptBean() {
        return conceptBean;
    }

    public void setConceptBean(ConceptBean conceptBean) {
        this.conceptBean = conceptBean;
    }

    public MenuModel getMenu() {
        return menu;
    }

    public void setMenu(MenuModel menu) {
        this.menu = menu;
    }

    public boolean isShowCategories() {
        return showCategories;
    }

    public void setShowCategories(boolean showCategories) {
        this.showCategories = showCategories;
    }

}