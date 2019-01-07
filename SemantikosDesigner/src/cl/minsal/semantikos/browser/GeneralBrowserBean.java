package cl.minsal.semantikos.browser;

import cl.minsal.semantikos.Constants;
import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.concept.ChangeMarketedBean;
import cl.minsal.semantikos.model.audit.ConceptAuditAction;
import cl.minsal.semantikos.model.audit.EliminationCausal;
import cl.minsal.semantikos.users.AuthenticationBean;

import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.queries.GeneralQuery;
import cl.minsal.semantikos.model.queries.QueryFilter;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.queries.QueryFilterAttribute;
import cl.minsal.semantikos.model.relationships.*;
import cl.minsal.semantikos.model.tags.Tag;
import cl.minsal.semantikos.model.users.User;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ToggleEvent;
import org.primefaces.event.data.PageEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static cl.minsal.semantikos.model.audit.AuditActionType.CONCEPT_INVALIDATION;


/**
 * Created by diego on 26/06/2016.
 */

@ManagedBean(name = "generalBrowserBean")
@SessionScoped
public class GeneralBrowserBean implements Serializable {

    static final Logger logger = LoggerFactory.getLogger(GeneralBrowserBean.class);

    /**
     * Objeto de consulta: contiene todos los filtros y columnas necesarios para el despliegue de los resultados en el navegador
     */
    private GeneralQuery generalQuery;

    /**
     * Lista de tags para el despliegue del filtro por tags
     */
    private List<Tag> tags = new ArrayList<Tag>();

    /**
     * Lista de usuarios para el despliegue del filtro por usuarios
     */
    private List<User> users = new ArrayList<User>();

    /**
     * Lista de conceptos para el despliegue del resultado de la consulta
     */
    private LazyDataModel<ConceptSMTK> concepts;

    /**
     * Categoría sobre la cual se está navegando
     */
    private Category category;

    /**
     * id de la categoría sobre la cual se esta navegando. Usado como enlace entre la petición desde el MainMenu y la
     * categoría
     */
    private int idCategory;

    /**
     * Indica si cambió algún filtro. Se utiliza para resetear la páginación al comienzo si se ha filtrado
     */
    private boolean isFilterChanged;

    /**
     * Indica si cambió la categoría. Se utiliza para resetear el estado del lazyDataModel
     */
    private boolean isCategoryChanged = false;

    private boolean showSettings;

    private int results;

    private int page = 1;

    // Placeholders para los targets de los filtros, dados como elementos seleccionables
    private BasicTypeValue basicTypeValue = new BasicTypeValue(null);

    private HelperTableRow helperTableRecord = null;

    private ConceptSMTK conceptSMTK = null;

    private List<EliminationCausal> eliminationCausals = new ArrayList<>();

    private EliminationCausal selectedCausal;

    private List<ConceptAuditAction> auditActionQueue = new ArrayList<>();

    private List<ConceptSMTK> relatedConcepts = new ArrayList<>();

    private ConceptSMTK selectedConcept;

    @ManagedProperty(value = "#{authenticationBean}")
    private transient AuthenticationBean authenticationBean;

    //@EJB
    QueryManager queryManager = (QueryManager) ServiceLocator.getInstance().getService(QueryManager.class);

    //@EJB
    TagManager tagManager = (TagManager) ServiceLocator.getInstance().getService(TagManager.class);

    //@EJB
    HelperTablesManager helperTablesManager = (HelperTablesManager) ServiceLocator.getInstance().getService(HelperTablesManager.class);

    //@EJB
    UserManager userManager = (UserManager) ServiceLocator.getInstance().getService(UserManager.class);

    //@EJB
    CategoryManager categoryManager = (CategoryManager) ServiceLocator.getInstance().getService(CategoryManager.class);

    //@EJB
    ConceptManager conceptManager = (ConceptManager) ServiceLocator.getInstance().getService(ConceptManager.class);

    @PostConstruct
    public void init() {
        tags = tagManager.getAllTags();
        users = userManager.getAllUsers();
        generalQuery = null;
        eliminationCausals = Arrays.asList(EliminationCausal.values());
    }

    /**
     * Este método es el responsable de ejecutar la consulta
     */
    public void executeQuery() {

        /**
         * Si la categoría no está seteada, retornar inmediatamente
         */
        if(category == null) {
            return;
        }

        /**
         * Si el objeto de consulta no está inicializado, inicializarlo
         */
        if(generalQuery == null) {
            generalQuery = queryManager.getDefaultGeneralQuery(category);
            isCategoryChanged = true;
        }

        /**
         * Si el patrón de consulta tiene menos de 3 caracteres retornar inmediatamente
         */
        if(generalQuery.getQuery().length() > 0 && generalQuery.getQuery().length() < 3) {
            return;
        }

        /**
         * Ejecutar la consulta
         */
        concepts = new LazyDataModel<ConceptSMTK>() {
            @Override
            public List<ConceptSMTK> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                //List<ConceptSMTK> conceptSMTKs = conceptManager.findConceptBy(category, first, pageSize);

                if(isCategoryChanged) {
                    first = 0;
                    RequestContext reqCtx = RequestContext.getCurrentInstance();
                    reqCtx.execute("PF('conceptTable').getPaginator().setPage(0)");
                    isCategoryChanged = false;
                }

                if(isFilterChanged) {
                    generalQuery.setPageNumber(0);
                }
                else {
                    generalQuery.setPageNumber(first);
                }

                isFilterChanged = false;

                generalQuery.setPageSize(pageSize);
                //generalQuery.setOrder(new Integer(sortField));

                /*
                if(sortOrder.name().substring(0,3).toLowerCase().equals("asc")) {
                    generalQuery.setAsc(sortOrder.name().substring(0,3).toLowerCase());
                }
                else {
                    generalQuery.setAsc(sortOrder.name().substring(0,4).toLowerCase());
                }
                */

                List<ConceptSMTK> conceptSMTKs = null;

                conceptSMTKs = queryManager.executeQuery(generalQuery);

                if(conceptSMTKs.isEmpty()) {
                    generalQuery.setTruncateMatch(true);
                    conceptSMTKs = queryManager.executeQuery(generalQuery);;
                }

                this.setRowCount(queryManager.countQueryResults(generalQuery));
                generalQuery.setTruncateMatch(false);

                results = this.getRowCount();

                return conceptSMTKs;
            }

        };

    }

    /**
     * Este método se encarga de setear el idCategory. En la práctica este metodo es gatillado al realizar el request
     * desde el mainMenu. Se setea además la categoría, que será utilizada posteriormente para obtener el objeto de consulta
     * @param idCategory
     */
    public void setIdCategory(int idCategory) {
        if(this.idCategory != idCategory) {
            this.generalQuery = null;
        }
        this.idCategory = idCategory;
        this.category = categoryManager.getCategoryById(idCategory);
    }

    public LazyDataModel<ConceptSMTK> getConcepts() {
        return concepts;
    }

    public void setConcepts(LazyDataModel<ConceptSMTK> concepts) {
        this.concepts = concepts;
    }

    public CategoryManager getCategoryManager() {
        return categoryManager;
    }

    public void setCategoryManager(CategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public GeneralQuery getGeneralQuery() {
        return generalQuery;
    }

    public void setGeneralQuery(GeneralQuery generalQuery) {
        this.generalQuery = generalQuery;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public HelperTablesManager getHelperTablesManager() {
        return helperTablesManager;
    }

    public void setHelperTablesManager(HelperTablesManager helperTablesManager) {
        this.helperTablesManager = helperTablesManager;
    }

    public BasicTypeValue getBasicTypeValue() {
        return basicTypeValue;
    }

    public void setBasicTypeValue(BasicTypeValue basicTypeValue) {
        this.basicTypeValue = basicTypeValue;
    }

    public HelperTableRow getHelperTableRecord() {
        return helperTableRecord;
    }

    public void setHelperTableRecord(HelperTableRow helperTableRecord) {
        this.helperTableRecord = helperTableRecord;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public ConceptSMTK getConceptSMTK() {
        return conceptSMTK;
    }

    public void setConceptSMTK(ConceptSMTK conceptSMTK) {
        this.conceptSMTK = conceptSMTK;
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

    /**
     * Este método se encarga de agregar o cambiar el filtro para el caso de selección simple
     */
    public void setSimpleSelection(RelationshipDefinition relationshipDefinition, Target target) {

        if(target == null)
            return;

        setFilterChanged(true);

        // Se busca el filtro
        for (QueryFilter queryFilter : generalQuery.getFilters()) {
            if (queryFilter.getDefinition().equals(relationshipDefinition)) {
                if(queryFilter.getTargets().isEmpty()) //Si la lista de targets está vacía, se agrega el target
                    queryFilter.getTargets().add(target);
                else //Si no, se modifica
                    queryFilter.getTargets().set(0, target);
                break;
            }
        }
        // Se resetean los placeholder para los target de las relaciones
        basicTypeValue = new BasicTypeValue(null);
        helperTableRecord = null;
        conceptSMTK = null;
        //Ajax.update("@(.conceptBrowserTable)");
    }

    public void removeTarget(RelationshipDefinition relationshipDefinition, Target target){

        if(target == null)
            return;

        setFilterChanged(true);

        // Se busca el filtro
        for (QueryFilter queryFilter : generalQuery.getFilters()) {
            if (queryFilter.getDefinition().equals(relationshipDefinition)) {
                queryFilter.getTargets().remove(target);
                break;
            }
        }
    }

    /**
     * Este método se encarga de agregar o cambiar el filtro para el caso de selección simple
     */
    public void setSimpleSelection(RelationshipAttributeDefinition relationshipAttributeDefinition, Target target) {

        if(target == null)
            return;

        setFilterChanged(true);

        // Se busca el filtro
        for (QueryFilterAttribute queryFilter : generalQuery.getAttributeFilters()) {
            if (queryFilter.getDefinition().equals(relationshipAttributeDefinition)) {
                if(queryFilter.getTargets().isEmpty()) //Si la lista de targets está vacía, se agrega el target
                    queryFilter.getTargets().add(target);
                else //Si no, se modifica
                    queryFilter.getTargets().set(0, target);
                break;
            }
        }
        // Se resetean los placeholder para los target de las relaciones
        basicTypeValue = new BasicTypeValue(null);
        helperTableRecord = null;
        conceptSMTK = null;
        //Ajax.update("@(.conceptBrowserTable)");
    }

    public void removeTarget(RelationshipAttributeDefinition relationshipAttributeDefinition, Target target){

        if(target == null)
            return;

        setFilterChanged(true);

        // Se busca el filtro
        for (QueryFilterAttribute queryFilter : generalQuery.getAttributeFilters()) {
            if (queryFilter.getDefinition().equals(relationshipAttributeDefinition)) {
                queryFilter.getTargets().remove(target);
                break;
            }
        }
    }


    public void deleteConcept(ConceptSMTK concept) throws IOException {

        FacesContext context = FacesContext.getCurrentInstance();

        // Si el concepto está persistido, invalidarlo
        if (concept.isPersistent() && !concept.isModeled()) {
            conceptManager.delete(concept, authenticationBean.getLoggedUser());
            context.addMessage(null, new FacesMessage("Successful", "Concepto eliminado"));

        } else {
            conceptManager.invalidate(concept, authenticationBean.getLoggedUser());
            context.addMessage(null, new FacesMessage("Successful", "Concepto invalidado"));
        }

    }

    public void createConcept() throws IOException {
        // Si el concepto está persistido, invalidarlo
        ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
        FacesContext context = FacesContext.getCurrentInstance();
        String query = "";
        if(generalQuery.getQuery() != null && concepts.getRowCount() == 0) {
            query = generalQuery.getQuery();
        }
        if(query == null || query.isEmpty()) {
            query = "*";
        }

        ConceptSMTK aConcept = categoryManager.categoryContains(category, query);

        String msg = "Ya existe el concepto: '" + aConcept + "' en la categoría '" + category;

        if (aConcept != null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg ));
            return;
        }

        eContext.redirect(eContext.getRequestContextPath() + Constants.VIEWS_FOLDER + "/concepts/new/" + idCategory + "/0/" + query);
    }

    public void invalidateConcept() throws IOException {

        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext rContext = RequestContext.getCurrentInstance();
        ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();

        try {
            // Si el concepto está persistido, invalidarlo
            if(selectedCausal == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha seleccionado la causal de eliminación" ));
                return;
            }
            if(selectedConcept == null) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se ha seleccionado el concepto" ));
                return;
            }
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            User user = authenticationBean.getLoggedUser();
            ConceptAuditAction conceptAuditAction = new ConceptAuditAction(selectedConcept, CONCEPT_INVALIDATION, timestamp, user, selectedConcept);
            conceptAuditAction.getDetails().add("Causal de Eliminación: " + selectedCausal.getName());
            getAuditActionQueue().add(conceptAuditAction);

            conceptManager.invalidate(selectedConcept, user, auditActionQueue);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Acción exitosa", "Concepto invalidado" ));

        }
        catch (Exception e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));

            // Si ocurrió una excepción, puede ser por conceptos relacionados, manejar este caso mostrando el dialogo de conceptos relacionados
            relatedConcepts = conceptManager.getRelatedConcepts(selectedConcept);

            if(!relatedConcepts.isEmpty()) {
                rContext.execute("PF('conceptRelated').show();");
            }
        }
    }

    public void onRowToggle(ToggleEvent event) {


        System.out.println(event.getVisibility());

    }

    public void updatePage(PageEvent event) {
        int pageindex = event.getPage();
        page = pageindex + 1;
        //RequestContext reqCtx = RequestContext.getCurrentInstance();
        //reqCtx.execute("PF('conceptTableExcel').getPaginator().setPage("+pageindex+")");
    }

    public boolean isFilterChanged() {
        return isFilterChanged;
    }

    public void setFilterChanged(boolean filterChanged) {
        isFilterChanged = filterChanged;
    }

    public boolean isShowSettings() {
        return showSettings;
    }

    public void setShowSettings(boolean showSettings) {
        this.showSettings = showSettings;
    }

    public int getResults() {
        return results;
    }

    public void setResults(int results) {
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<EliminationCausal> getEliminationCausals() {
        return eliminationCausals;
    }

    public void setEliminationCausals(List<EliminationCausal> eliminationCausals) {
        this.eliminationCausals = eliminationCausals;
    }

    public EliminationCausal getSelectedCausal() {
        return selectedCausal;
    }

    public void setSelectedCausal(EliminationCausal selectedCausal) {
        this.selectedCausal = selectedCausal;
    }

    public List<ConceptAuditAction> getAuditActionQueue() {
        return auditActionQueue;
    }

    public void setAuditActionQueue(List<ConceptAuditAction> auditActionQueue) {
        this.auditActionQueue = auditActionQueue;
    }

    public List<ConceptSMTK> getRelatedConcepts() {
        return relatedConcepts;
    }

    public void setRelatedConcepts(List<ConceptSMTK> relatedConcepts) {
        this.relatedConcepts = relatedConcepts;
    }

    public ConceptSMTK getSelectedConcept() {
        return selectedConcept;
    }

    public void setSelectedConcept(ConceptSMTK selectedConcept) {
        this.selectedConcept = selectedConcept;
    }

}

