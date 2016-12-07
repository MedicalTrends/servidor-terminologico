package cl.minsal.semantikos.designer_modeler.browser;

import cl.minsal.semantikos.designer_modeler.auth.AuthenticationBean;
import cl.minsal.semantikos.kernel.auth.UserManager;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.browser.ConceptQuery;
import cl.minsal.semantikos.model.browser.ConceptQueryFilter;
import cl.minsal.semantikos.model.helpertables.HelperTableRecord;
import cl.minsal.semantikos.model.relationships.*;
import org.omnifaces.util.Ajax;
import org.primefaces.extensions.model.fluidgrid.FluidGridItem;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * Created by diego on 26/06/2016.
 */

@ManagedBean(name = "conceptBrowserBean")
@ViewScoped
public class ConceptBrowserBean implements Serializable {

    static final Logger logger = LoggerFactory.getLogger(ConceptBrowserBean.class);

    @EJB
    ConceptQueryManager conceptQueryManager;

    @EJB
    TagManager tagManager;

    @EJB
    HelperTableManager helperTableManager;

    @EJB
    UserManager userManager;

    /**
     * Objeto de consulta: contiene todos los filtros y columnas necesarios para el despliegue de los resultados en el navegador
     */
    private ConceptQuery conceptQuery;

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


    // Placeholders para los targets de los filtros, dados como elementos seleccionables
    private BasicTypeValue basicTypeValue = new BasicTypeValue(null);

    private HelperTableRecord helperTableRecord = null;

    private ConceptSMTK conceptSMTK = null;

    @ManagedProperty(value = "#{authenticationBean}")
    private AuthenticationBean authenticationBean;

    @EJB
    private CategoryManager categoryManager;

    @EJB
    private ConceptManager conceptManager;

    @PostConstruct
    public void init(){
        tags = tagManager.getAllTags();
        users = userManager.getAllUsers();
    }

    /**
     * Este método es el responsable de ejecutar la consulta
     */
    public void executeQuery() {

        /**
         * Si la categoría no está seteada, retornar inmediatamente
         */
        if(category == null)
            return;

        /**
         * Si el objeto de consulta no está inicializado, inicializarlo
         */
        if(conceptQuery == null)
            conceptQuery = conceptQueryManager.getDefaultQueryByCategory(category);

        /**
         * Ejecutar la consulta
         */
        concepts = new LazyDataModel<ConceptSMTK>() {
            @Override
            public List<ConceptSMTK> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                //List<ConceptSMTK> conceptSMTKs = conceptManager.findConceptBy(category, first, pageSize);

                conceptQuery.setPageNumber(first);
                conceptQuery.setPageSize(pageSize);
                conceptQuery.setOrder(new Integer(sortField));

                if(sortOrder.name().substring(0,3).toLowerCase().equals("asc"))
                    conceptQuery.setAsc(sortOrder.name().substring(0,3).toLowerCase());
                else
                    conceptQuery.setAsc(sortOrder.name().substring(0,4).toLowerCase());

                List<ConceptSMTK> conceptSMTKs = conceptQueryManager.executeQuery(conceptQuery);
                this.setRowCount(conceptQueryManager.countConceptQuery(conceptQuery));

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

    public List<Tag> getRecordSearchInput(String patron) {

        FacesContext context = FacesContext.getCurrentInstance();


        List<Tag> someTags = new ArrayList<Tag>();

        for (Tag tag : getTags()) {
            if(tag.getName().toLowerCase().contains(patron.trim().toLowerCase()))
                someTags.add(tag);
        }

        return someTags;
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

    public ConceptQuery getConceptQuery() {
        return conceptQuery;
    }

    public void setConceptQuery(ConceptQuery conceptQuery) {
        this.conceptQuery = conceptQuery;
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

    public HelperTableManager getHelperTableManager() {
        return helperTableManager;
    }

    public void setHelperTableManager(HelperTableManager helperTableManager) {
        this.helperTableManager = helperTableManager;
    }

    public BasicTypeValue getBasicTypeValue() {
        return basicTypeValue;
    }

    public void setBasicTypeValue(BasicTypeValue basicTypeValue) {
        this.basicTypeValue = basicTypeValue;
    }

    public HelperTableRecord getHelperTableRecord() {
        return helperTableRecord;
    }

    public void setHelperTableRecord(HelperTableRecord helperTableRecord) {
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

        // Se busca el filtro
        for (ConceptQueryFilter conceptQueryFilter : conceptQuery.getFilters()) {
            if (conceptQueryFilter.getDefinition().equals(relationshipDefinition)) {
                if(conceptQueryFilter.getTargets().isEmpty()) //Si la lista de targets está vacía, se agrega el target
                    conceptQueryFilter.getTargets().add(target);
                else //Si no, se modifica
                    conceptQueryFilter.getTargets().set(0, target);
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

        // Se busca el filtro
        for (ConceptQueryFilter conceptQueryFilter : conceptQuery.getFilters()) {
            if (conceptQueryFilter.getDefinition().equals(relationshipDefinition)) {
                conceptQueryFilter.getTargets().remove(target);
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
        if(concepts.getRowCount()==0)
            query=conceptQuery.getQuery();
        eContext.redirect(eContext.getRequestContextPath() + "/views/concept/conceptEdit.xhtml?editMode=true&idCategory=" + idCategory +"&idConcept=0&favoriteDescription=" + query);
    }

    public String stringifyTags(List<Tag> tags){
        if(tags.isEmpty())
            return "Etiquetas...";

        String stringTags= "";

        for (Tag tag : tags) {
            stringTags= stringTags.concat(tag.getName()).concat(", ");
        }
        return  stringTags;
    }

}

