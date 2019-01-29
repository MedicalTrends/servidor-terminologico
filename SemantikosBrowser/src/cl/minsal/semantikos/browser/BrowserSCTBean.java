package cl.minsal.semantikos.browser;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.components.GuestPreferences;
import cl.minsal.semantikos.kernel.components.QueryManager;
import cl.minsal.semantikos.kernel.components.SnomedCTManager;
import cl.minsal.semantikos.model.descriptions.DescriptionTypeFactory;
import cl.minsal.semantikos.model.queries.SnomedQuery;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.snomedct.DescriptionSCT;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.primefaces.context.RequestContext;
import org.primefaces.event.data.PageEvent;
import org.primefaces.extensions.model.layout.LayoutOptions;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
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
import java.util.List;
import java.util.Map;

import static java.lang.System.currentTimeMillis;
import static org.primefaces.util.Constants.EMPTY_STRING;

@ManagedBean
@SessionScoped
public class BrowserSCTBean implements Serializable {

    private static final long serialVersionUID = 20120925L;

    /**
     * Variables para el layout
     */
    private String stateOne;
    private String stateTwo;
    private boolean layoutOneShown = true;

    private LayoutOptions layoutOptionsOne;
    private LayoutOptions layoutOptionsTwo;

    @ManagedProperty(value = "#{guestPreferences}")
    GuestPreferences guestPreferences;

    @ManagedProperty(value = "#{browserBean}")
    BrowserBean browserBean;

    /**
     * Variables para el browser
     */
    static final Logger logger = LoggerFactory.getLogger(BrowserSCTBean.class);

    /**
     * Objeto de consulta: contiene todos los filtros y columnas necesarios para el despliegue de los resultados en el navegador
     */
    private SnomedQuery snomedQuery;

    /**
     * Lista de conceptos para el despliegue del resultado de la consulta
     */
    private LazyDataModel<ConceptSCT> concepts;
    private ConceptSCT conceptSelected;

    private DescriptionSCT descriptionSelected;

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

    //@EJB
    private QueryManager queryManager = (QueryManager) ServiceLocator.getInstance().getService(QueryManager.class);

    //@EJB
    private SnomedCTManager snomedCTManager = (SnomedCTManager) ServiceLocator.getInstance().getService(SnomedCTManager.class);

    private transient MenuModel menu;

    @PostConstruct
    protected void initialize() {
        //guestPreferences.setTheme("teal");
        performSearch = true;
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

        //guestPreferences.setTheme("teal");

        init = currentTimeMillis();

        /**
         * Si el objeto de consulta no está inicializado, inicializarlo
         */
        if(snomedQuery == null) {
            snomedQuery = queryManager.getDefaultSnomedQuery();
        }

        snomedQuery.setTruncateMatch(false);

        /**
         * Si la consulta viene nula o vacía retornan inmediatamente
         */
        if(snomedQuery.getQuery() == null || snomedQuery.getQuery().isEmpty()) {
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
        concepts = new LazyDataModel<ConceptSCT>() {
            @Override
            public List<ConceptSCT> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                //List<ConceptSMTK> conceptSMTKs = conceptManager.findConceptsBy(category, first, pageSize);

                if(isFilterChanged) {
                    snomedQuery.setPageNumber(0);
                    RequestContext context = RequestContext.getCurrentInstance();
                    context.execute("PF('conceptTableSCT').getPaginator().setPage(0);");
                    this.setRowIndex(0);
                }
                else {
                    snomedQuery.setPageNumber(first);
                }

                isFilterChanged = false;

                browserBean.setShowCategories(false);

                snomedQuery.setPageSize(pageSize);
                //snomedQuery.setOrder(new Integer(sortField==null?"1":sortField));

                /*
                if(sortOrder.name().substring(0,3).toLowerCase().equals("asc")) {
                    snomedQuery.setAsc(sortOrder.name().substring(0, 3).toLowerCase());
                }
                else {
                    snomedQuery.setAsc(sortOrder.name().substring(0, 4).toLowerCase());
                }
                */

                List<ConceptSCT> conceptSCTs = queryManager.executeQuery(snomedQuery);

                snomedQuery.setTruncateMatch(true);

                for (ConceptSCT conceptSCT : queryManager.executeQuery(snomedQuery)) {
                    if(!conceptSCTs.contains(conceptSCT)) {
                        conceptSCTs.add(conceptSCT);
                    }
                }

                this.setRowCount(queryManager.countQueryResults(snomedQuery));

                results = this.getRowCount();
                seconds = (float) ((currentTimeMillis() - init)/1000.0);

                return conceptSCTs;
            }

        };


    }

    public List<DescriptionSCT> searchSuggestedDescriptions(String term) {
        isFilterChanged = true;
        snomedQuery.setQuery(term);
        List<DescriptionSCT> suggestedDescriptions = new ArrayList<>();
        DescriptionTypeFactory.DUMMY_DESCRIPTION.setTerm(EMPTY_STRING);
        suggestedDescriptions.add(DescriptionSCT.DUMMY_DESCRIPTION_SCT);
        suggestedDescriptions.addAll(snomedCTManager.searchDescriptionsSuggested(term));
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
        if(descriptionSelected.equals(DescriptionSCT.DUMMY_DESCRIPTION_SCT)) {
            descriptionSelected.setTerm(snomedQuery.getQuery());
        }
        else {
            //browserQuery.setQuery(descriptionSelected.getTerm());
            snomedQuery.setQuery(String.valueOf(descriptionSelected.getConceptId()));
        }
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        if(request.getRequestURI().equals("/views/home.xhtml")) {
            ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
            eContext.redirect(eContext.getRequestContextPath() + "/views/concepts-snomed");
        }
    }

    public void redirect() throws IOException {
        ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
        if(snomedQuery.getQuery() != null && snomedQuery.getQuery().length() >= 3) {
            performSearch = true;
            eContext.redirect(eContext.getRequestContextPath() + "/views/concepts-snomed");
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

    public LazyDataModel<ConceptSCT> getConcepts() {
        return concepts;
    }

    public void setConcepts(LazyDataModel<ConceptSCT> concepts) {
        this.concepts = concepts;
    }

    public ConceptSCT getConceptSelected() {
        return conceptSelected;
    }

    public void setConceptSelected(ConceptSCT conceptSelected) {
        this.conceptSelected = conceptSelected;
    }

    public SnomedQuery getSnomedQuery() {
        return snomedQuery;
    }

    public void setSnomedQuery(SnomedQuery snomedQuery) {
        this.snomedQuery = snomedQuery;
    }

    public QueryManager getQueryManager() {
        return queryManager;
    }

    public void setQueryManager(QueryManager queryManager) {
        this.queryManager = queryManager;
    }

    public SnomedCTManager getSnomedCTManager() {
        return snomedCTManager;
    }

    public void setSnomedCTManager(SnomedCTManager snomedCTManager) {
        this.snomedCTManager = snomedCTManager;
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

    public DescriptionSCT getDescriptionSelected() {
        return descriptionSelected;
    }

    public void setDescriptionSelected(DescriptionSCT descriptionSelected) {
        this.descriptionSelected = descriptionSelected;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public GuestPreferences getGuestPreferences() {
        return guestPreferences;
    }

    public void setGuestPreferences(GuestPreferences guestPreferences) {
        this.guestPreferences = guestPreferences;
    }

    public MenuModel getMenu() {
        return menu;
    }

    public void setMenu(MenuModel menu) {
        this.menu = menu;
    }

    public BrowserBean getBrowserBean() {
        return browserBean;
    }

    public void setBrowserBean(BrowserBean browserBean) {
        this.browserBean = browserBean;
    }

}