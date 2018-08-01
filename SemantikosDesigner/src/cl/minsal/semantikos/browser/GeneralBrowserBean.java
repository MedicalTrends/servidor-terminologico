package cl.minsal.semantikos.browser;

import cl.minsal.semantikos.Constants;
import cl.minsal.semantikos.clients.ServiceLocator;
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
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by diego on 26/06/2016.
 */

@ManagedBean(name = "generalBrowserBean")
@ViewScoped
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

    private boolean showSettings;

    // Placeholders para los targets de los filtros, dados como elementos seleccionables
    private BasicTypeValue basicTypeValue = new BasicTypeValue(null);

    private HelperTableRow helperTableRecord = null;

    private ConceptSMTK conceptSMTK = null;

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
        }

        /**
         * Ejecutar la consulta
         */
        concepts = new LazyDataModel<ConceptSMTK>() {
            @Override
            public List<ConceptSMTK> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                //List<ConceptSMTK> conceptSMTKs = conceptManager.findConceptBy(category, first, pageSize);

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
        String query = "";
        if(generalQuery.getQuery() != null && concepts.getRowCount()==0) {
            query = generalQuery.getQuery();
        }
        if(query == null || query.isEmpty()) {
            query = "*";
        }

        eContext.redirect(eContext.getRequestContextPath() + Constants.VIEWS_FOLDER + "/concepts/new/" + idCategory + "/0/" + query);
    }

    public void onRowToggle(ToggleEvent event) {


        System.out.println(event.getVisibility());

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

}

