package browser;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.crossmaps.DirectCrossmap;
import cl.minsal.semantikos.model.crossmaps.IndirectCrossmap;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.DescriptionTypeFactory;
import cl.minsal.semantikos.model.queries.BrowserQuery;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.SnomedCTRelationship;
import cl.minsal.semantikos.model.tags.Tag;
import org.primefaces.extensions.model.layout.LayoutOptions;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.EMPTY_LIST;
import static org.primefaces.util.Constants.EMPTY_STRING;

@ManagedBean
@ViewScoped
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

    //@EJB
    QueryManager queryManager;

    //@EJB
    TagManager tagManager;

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

    //@EJB
    private CategoryManager categoryManager;

    //@EJB
    private ConceptManager conceptManager;

    //@EJB
    private DescriptionManager descriptionManager = (DescriptionManager) ServiceLocator.getInstance().getService(DescriptionManager.class);

    //@EJB
    private RelationshipManager relationshipManager = (RelationshipManager) ServiceLocator.getInstance().getService(RelationshipManager.class);

    @PostConstruct
    protected void initialize() {

        queryManager = (QueryManager) ServiceLocator.getInstance().getService(QueryManager.class);
        tagManager = (TagManager) ServiceLocator.getInstance().getService(TagManager.class);
        categoryManager = (CategoryManager) ServiceLocator.getInstance().getService(CategoryManager.class);
        conceptManager = (ConceptManager) ServiceLocator.getInstance().getService(ConceptManager.class);

        //ServiceLocator.getInstance().closeContext();

        tags = tagManager.getAllTags();
        categories = categoryManager.getCategories();

        // 1. set options for first layout  
        layoutOptionsOne = new LayoutOptions();

        // options for all panes (center and west)  
        LayoutOptions panes = new LayoutOptions();
        panes.addOption("slidable", false);
        panes.addOption("resizeWhileDragging", true);
        layoutOptionsOne.setPanesOptions(panes);

        // options for west pane  
        LayoutOptions west = new LayoutOptions();
        west.addOption("size", 300);
        west.addOption("minSize", 0);
        west.addOption("maxSize", 300);
        layoutOptionsOne.setWestOptions(west);

        // 2. set options for second layout  
        layoutOptionsTwo = new LayoutOptions();

        // options for all panes  
        panes = new LayoutOptions();
        panes.addOption("slidable", false);
        panes.addOption("resizeWhileDragging", true);
        layoutOptionsTwo.setPanesOptions(panes);

        // options for east pane  
        LayoutOptions east = new LayoutOptions();
        //panes.addOption("resizable", false);
        //panes.addOption("closable", false);
        east.addOption("size", "50%");
        layoutOptionsTwo.setEastOptions(west);

        // options for nested east layout  
        LayoutOptions childEastOptions = new LayoutOptions();
        east.setChildOptions(childEastOptions);

        // options for east-south pane  
        LayoutOptions eastSouth = new LayoutOptions();
        eastSouth.addOption("size", "50%");
        childEastOptions.setSouthOptions(eastSouth);
    }

    /**
     * Este método es el responsable de ejecutar la consulta
     */
    public void executeQuery() {

        /**
         * Si el objeto de consulta no está inicializado, inicializarlo
         */
        if(browserQuery == null) {
            browserQuery = queryManager.getDefaultBrowserQuery();
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
                }
                else {
                    browserQuery.setPageNumber(first);
                }

                isFilterChanged = false;

                browserQuery.setPageSize(pageSize);
                browserQuery.setOrder(new Integer(sortField));

                if(sortOrder.name().substring(0,3).toLowerCase().equals("asc")) {
                    browserQuery.setAsc(sortOrder.name().substring(0, 3).toLowerCase());
                }
                else {
                    browserQuery.setAsc(sortOrder.name().substring(0, 4).toLowerCase());
                }

                List<ConceptSMTK> conceptSMTKs = queryManager.executeQuery(browserQuery);

                if(conceptSMTKs.isEmpty()) {
                    browserQuery.setTruncateMatch(true);
                    conceptSMTKs = queryManager.executeQuery(browserQuery);;
                }

                this.setRowCount(queryManager.countQueryResults(browserQuery));

                return conceptSMTKs;
            }

        };

    }

    public List<Description> searchSuggestedDescriptions(String term) {
        browserQuery.setQuery(term);
        List<Description> suggestedDescriptions = new ArrayList<>();
        DescriptionTypeFactory.DUMMY_DESCRIPTION.setTerm(EMPTY_STRING);
        suggestedDescriptions.add(DescriptionTypeFactory.DUMMY_DESCRIPTION);
        suggestedDescriptions.addAll(descriptionManager.searchDescriptionsSuggested(term, categories, null));
        return suggestedDescriptions;
    }

    public void test() {
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

    public List<Description> getOtherDescriptions(ConceptSMTK concept) {

        if(concept == null) {
            return null;
        }

        List<Description> otherDescriptions = new ArrayList<Description>();

        for (Description description : concept.getDescriptions()) {
            if(DescriptionTypeFactory.getInstance().getDescriptionTypesButFSNandFavorite().contains(description.getDescriptionType()))
                otherDescriptions.add(description);
        }

        return otherDescriptions;
    }

    public List<Relationship> getSnomedCTRelationships() {

        if(conceptSelected == null) {
            return null;
        }

        if(!conceptSelected.isRelationshipsLoaded()) {
            conceptSelected.setRelationships(relationshipManager.getRelationshipsBySourceConcept(conceptSelected));
        }

        List<Relationship> snomedCTRelationships = new ArrayList<Relationship>();

        for (SnomedCTRelationship relationship : conceptSelected.getRelationshipsSnomedCT()) {
            snomedCTRelationships.add(relationship);
        }

        return snomedCTRelationships;
    }

    public List<Relationship> getSMTKRelationships() {

        if(conceptSelected == null) {
            return null;
        }

        if(!conceptSelected.isRelationshipsLoaded()) {
            conceptSelected.setRelationships(relationshipManager.getRelationshipsBySourceConcept(conceptSelected));
        }

        List<Relationship> smtkRelationships = new ArrayList<Relationship>();

        for (Relationship relationship : conceptSelected.getRelationships()) {
            if(!relationship.getRelationshipDefinition().getTargetDefinition().isSnomedCTType() &&
                    !relationship.getRelationshipDefinition().getTargetDefinition().isCrossMapType() &&
                    !relationship.getRelationshipDefinition().getTargetDefinition().isGMDNType() ) {
                smtkRelationships.add(relationship);
            }
        }

        return smtkRelationships;
    }

    public List<Relationship> getDirectCrossmapsRelationships() {

        if(conceptSelected == null) {
            return null;
        }

        if(!conceptSelected.isRelationshipsLoaded()) {
            conceptSelected.setRelationships(relationshipManager.getRelationshipsBySourceConcept(conceptSelected));
        }

        List<Relationship> smtkRelationships = new ArrayList<Relationship>();

        for (Relationship relationship : conceptSelected.getRelationships()) {
            if(relationship instanceof DirectCrossmap) {
                smtkRelationships.add(relationship);
            }
        }

        return smtkRelationships;
    }

    public List<Relationship> getIndirectCrossmapsRelationships() {

        if(conceptSelected == null) {
            return null;
        }

        if(!conceptSelected.isRelationshipsLoaded()) {
            conceptSelected.setRelationships(relationshipManager.getRelationshipsBySourceConcept(conceptSelected));
        }

        List<Relationship> smtkRelationships = new ArrayList<Relationship>();

        for (Relationship relationship : conceptSelected.getRelationships()) {
            if(relationship instanceof IndirectCrossmap) {
                smtkRelationships.add(relationship);
            }
        }

        return smtkRelationships;
    }

    public List<Relationship> getGMDNRelationships() {

        if(conceptSelected == null) {
            return null;
        }

        if(!conceptSelected.isRelationshipsLoaded()) {
            conceptSelected.setRelationships(relationshipManager.getRelationshipsBySourceConcept(conceptSelected));
        }

        List<Relationship> gmdnRelationships = new ArrayList<Relationship>();

        for (Relationship relationship : conceptSelected.getRelationships()) {
            if(relationship.getRelationshipDefinition().getTargetDefinition().isGMDNType()) {
                gmdnRelationships.add(relationship);
            }
        }

        return gmdnRelationships;
    }

}  