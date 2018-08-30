package cl.minsal.semantikos.browser;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.kernel.componentsweb.TimeOutWeb;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.DescriptionTypeFactory;
import cl.minsal.semantikos.model.queries.BrowserQuery;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.tags.Tag;
import cl.minsal.semantikos.view.components.GuestPreferences;
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
@SessionScoped
public class BrowserBean implements Serializable {

    private static final long serialVersionUID = 20120925L;

    /**
     * Variables para el layout
     */
    private String stateOne;
    private String stateTwo;
    private boolean layoutOneShown = true;

    private LayoutOptions layoutOptionsOne;
    private LayoutOptions layoutOptionsTwo;

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

    private Description descriptionSelected;

    /**
     * Indica si cambió algún filtro. Se utiliza para resetear la páginación al comienzo si se ha filtrado
     */
    private boolean isFilterChanged;

    /**
     * Indica si se debe realizar una búsqueda
     */
    private boolean performSearch = false;

    private boolean showFilters = false;

    private int results;

    private float seconds;

    private long init;

    private int page = 1;

    @ManagedProperty(value = "#{guestPreferences}")
    GuestPreferences guestPreferences;

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

    //@EJB
    private TimeOutWeb timeOutWeb = (TimeOutWeb) ServiceLocator.getInstance().getService(TimeOutWeb.class);

    private transient MenuModel menu;

    private transient MenuModel navegation;

    private CircularFifoQueue<Target> circularFifoQueue;

    @PostConstruct
    protected void initialize() {

        guestPreferences.setTheme("indigo");

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        request.getSession().setMaxInactiveInterval(timeOutWeb.getTimeOut());

        //ServiceLocator.getInstance().closeContext();
        tags = tagManager.getAllTags();
        categories = categoryManager.getCategories();

        initMenu();

    }

    public void initMenu() {

        menu = new DefaultMenuModel();

        //Inicio
        DefaultMenuItem item0 = new DefaultMenuItem("Inicio");
        item0.setUrl("/");
        item0.setIcon("fa fa-home");
        item0.setId("rm_home");

        menu.addElement(item0);

        //Conceptos
        DefaultMenuItem item1 = new DefaultMenuItem("Conceptos");
        item1.setUrl("/views/concepts.xhtml");
        item1.setIcon("fa fa-search");
        item1.setId("rm_volver");

        menu.addElement(item1);

        //Snomed
        DefaultMenuItem item2 = new DefaultMenuItem("Snomed-CT");
        item2.setUrl("/views/snomed/concepts");
        item2.setIcon("fa fa-arrow-left");
        item2.setId("rm_snomed");

        menu.addElement(item2);


        //Últimos visitados
        DefaultSubMenu conceptSubmenu = new DefaultSubMenu("Recientes");
        conceptSubmenu.setIcon("fa fa-list");
        conceptSubmenu.setId("rm_concepts");
        conceptSubmenu.setExpanded(true);

        menu.addElement(conceptSubmenu);

        circularFifoQueue = new CircularFifoQueue<Target>(5);
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

    /**
     * Este método es el responsable de ejecutar la consulta
     */
    public void executeQuery() {

        resetNavigation();

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

                List<ConceptSMTK> conceptSMTKs = queryManager.executeQuery(browserQuery);

                browserQuery.setTruncateMatch(true);

                for (ConceptSMTK conceptSMTK : queryManager.executeQuery(browserQuery)) {
                    if(!conceptSMTKs.contains(conceptSMTK)) {
                        conceptSMTKs.add(conceptSMTK);
                    }
                }

                this.setRowCount(queryManager.countQueryResults(browserQuery));

                results = this.getRowCount();
                seconds = (float) ((currentTimeMillis() - init)/1000.0);

                return conceptSMTKs;
            }

        };

    }

    public void resetNavigation() {

        navegation = new DefaultMenuModel();

        //Inicio
        DefaultMenuItem item0 = new DefaultMenuItem("Inicio");
        item0.setUrl("/views/home.xhtml");
        item0.setId("rm_home");

        navegation.addElement(item0);

        //Volver
        DefaultMenuItem item1 = new DefaultMenuItem("Conceptos");
        item1.setUrl("/views/concepts.xhtml");
        item1.setId("rm_volver");

        navegation.addElement(item1);
    }

    public void resetTheme() {
        getGuestPreferences().setTheme("indigo");
    }

    public void refreshLastVisitedMenu() {

        for (MenuElement menuElement : getMenu().getElements()) {
            if (menuElement.getId().equals("3")) {
                DefaultSubMenu conceptSubmenu = (DefaultSubMenu) menuElement;
                conceptSubmenu.getElements().clear();
                for (Object o : Arrays.asList(getCircularFifoQueue().toArray())) {
                    enqueque((Target) o, conceptSubmenu);
                }
            }
        }
    }

    public void enqueque(Target target, DefaultSubMenu subMenu) {

        if(target instanceof ConceptSMTK) {
            enqueueConcept((ConceptSMTK) target, subMenu);
        }
        if(target instanceof ConceptSCT) {
            enqueueConceptSCT((ConceptSCT) target, subMenu);
        }
    }

    public void enqueueConcept(ConceptSMTK conceptSMTK, DefaultSubMenu subMenu) {
        DefaultMenuItem item = new DefaultMenuItem(conceptSMTK.getDescriptionFSN());
        item.setUrl("/views/concept/"+conceptSMTK.getConceptID());
        //item.setIcon("fa fa-list-alt");
        item.setStyleClass("loader-trigger");
        item.setId("rm_"+conceptSMTK.getConceptID());
        if(!subMenu.getElements().contains(item)) {
            subMenu.addElement(item);
        }
    }

    public void enqueueConceptSCT(ConceptSCT conceptSCT, DefaultSubMenu subMenu) {
        DefaultMenuItem item = new DefaultMenuItem(conceptSCT.getDescriptionFSN());
        item.setUrl("/views/snomed/concept/"+conceptSCT.getId());
        //item.setIcon("fa fa-list-alt");
        item.setStyleClass("loader-trigger");
        item.setId("rm_"+conceptSCT.getId());
        if(!subMenu.getElements().contains(item)) {
            subMenu.addElement(item);
        }
    }

    public List<Description> searchSuggestedDescriptions(String term) {
        isFilterChanged = true;
        browserQuery.setQuery(term);
        List<Description> suggestedDescriptions = new ArrayList<>();
        DescriptionTypeFactory.DUMMY_DESCRIPTION.setTerm(EMPTY_STRING);
        suggestedDescriptions.add(DescriptionTypeFactory.DUMMY_DESCRIPTION);
        suggestedDescriptions.addAll(descriptionManager.searchDescriptionsSuggested(term, categories, null));
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
            eContext.redirect(eContext.getRequestContextPath() + "/views/concepts");
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
        reqCtx.execute("PF('conceptTableExcel').getPaginator().setPage("+pageindex+")");
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

    public String getStateOne() {
        return stateOne;
    }

    public void setStateOne(String stateOne) {
        this.stateOne = stateOne;
    }

    public String getStateTwo() {
        return stateTwo;
    }

    public void setStateTwo(String stateTwo) {
        this.stateTwo = stateTwo;
    }

    public void toogleLayout(ActionEvent event) {
        layoutOneShown = !layoutOneShown;
    }

    public boolean isLayoutOneShown() {
        return layoutOneShown;
    }

    public LayoutOptions getLayoutOptionsOne() {
        return layoutOptionsOne;
    }

    public LayoutOptions getLayoutOptionsTwo() {
        return layoutOptionsTwo;
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

    public CircularFifoQueue getCircularFifoQueue() {
        return circularFifoQueue;
    }

    public void setCircularFifoQueue(CircularFifoQueue circularFifoQueue) {
        this.circularFifoQueue = circularFifoQueue;
    }

    public MenuModel getMenu() {
        return menu;
    }

    public void setMenu(MenuModel menu) {
        this.menu = menu;
    }

    public MenuModel getNavegation() {
        return navegation;
    }

    public void setNavegation(MenuModel navegation) {
        this.navegation = navegation;
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

}