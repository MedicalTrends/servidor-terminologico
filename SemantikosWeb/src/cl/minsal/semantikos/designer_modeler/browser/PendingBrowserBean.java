package cl.minsal.semantikos.designer_modeler.browser;

import cl.minsal.semantikos.designer_modeler.auth.AuthenticationBean;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.browser.NoValidQuery;
import cl.minsal.semantikos.model.browser.PendingQuery;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
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

@ManagedBean(name = "pendingBrowserBean")
@SessionScoped
public class PendingBrowserBean implements Serializable {

    static final Logger logger = LoggerFactory.getLogger(PendingBrowserBean.class);

    @EJB
    QueryManager queryManager;

    @EJB
    private PendingTermsManager pendingTermsManager;

    /**
     * Objeto de consulta: contiene todos los filtros y columnas necesarios para el despliegue de los resultados en el navegador
     */
    private PendingQuery pendingQuery;


    /**
     * Lista de categorías para el despliegue del filtro por tipo de observación
     */
    private List<Category> categories = new ArrayList<Category>();

    private PendingTerm termSelected;

    private List<PendingTerm> termsSelected = new ArrayList<>();

    private List<PendingTerm> dataSource = new ArrayList<>();

    ConceptSMTK conceptSelected = null;

    private ConceptSMTK conceptPending;

    private Category categorySelected;

    private User user;

    /**
     * Lista de términos pendientes para el despliegue del resultado de la consulta
     */
    private LazyDataModel<PendingTerm> pendingTerms;

    @ManagedProperty(value = "#{authenticationBean}")
    private AuthenticationBean authenticationBean;

    @EJB
    private DescriptionManager descriptionManager;

    @EJB
    private ConceptManager conceptManager;

    @EJB
    private CategoryManager categoryManager;

    @PostConstruct
    public void init(){
        conceptPending = conceptManager.getPendingConcept();

        categories = categoryManager.getCategories();
        user = authenticationBean.getLoggedUser();
    }

    /**
     * Este método es el responsable de ejecutar la consulta
     */
    public void executeQuery() {

        /**
         * Si el objeto de consulta no está inicializado, inicializarlo
         */
        if(pendingQuery == null)
            pendingQuery = queryManager.getDefaultPendingQuery();

        /**
         * Ejecutar la consulta
         */
        pendingTerms = new LazyDataModel<PendingTerm>() {
            @Override
            public List<PendingTerm> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

                //List<ConceptSMTK> conceptSMTKs = conceptManager.findConceptBy(category, first, pageSize);

                pendingQuery.setPageNumber(first);
                pendingQuery.setPageSize(pageSize);
                pendingQuery.setOrder(new Integer(sortField));

                if(sortOrder.name().substring(0,3).toLowerCase().equals("asc"))
                    pendingQuery.setAsc(sortOrder.name().substring(0,3).toLowerCase());
                else
                    pendingQuery.setAsc(sortOrder.name().substring(0,4).toLowerCase());

                List<PendingTerm> pendingTerms = queryManager.executeQuery(pendingQuery);

                //if(dataSource.isEmpty())
                dataSource = pendingTerms;

                this.setRowCount(30);
                //this.setRowCount(queryManager.countQueryResults(noValidQuery));

                return pendingTerms;
            }

            @Override
            public Object getRowKey(PendingTerm pendingTerm) {
                return pendingTerm != null ? pendingTerm.getId() : null;
            }

            @Override
            public PendingTerm getRowData(String rowKey) {
                List<PendingTerm> pendingTerms = dataSource;
                //List<PendingTerm> pendingTerms = (List<PendingTerm>)getWrappedData();
                Integer value = Integer.valueOf(rowKey);

                for (PendingTerm pendingTerm : pendingTerms) {
                    if (pendingTerm.getId()==value) {
                        return pendingTerm;
                    }
                }

                return null;
            }

        };

    }

    public PendingQuery getPendingQuery() {
        return pendingQuery;
    }

    public LazyDataModel<PendingTerm> getPendingTerms() {
        if(pendingTerms.getWrappedData()==null)
            pendingTerms.setWrappedData(dataSource);
        return pendingTerms;
    }

    public void setPendingTerms(LazyDataModel<PendingTerm> pendingTerms) {
        this.pendingTerms = pendingTerms;
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

    public ConceptSMTK getConceptSelected() {
        return conceptSelected;
    }

    public void setConceptSelected(ConceptSMTK conceptSelected) {
        this.conceptSelected = conceptSelected;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public PendingTerm getTermSelected() {
        return termSelected;
    }

    public void setTermSelected(PendingTerm termSelected) {
        this.termSelected = termSelected;
    }

    public Category getCategorySelected() {
        return categorySelected;
    }

    public void setCategorySelected(Category categorySelected) {
        this.categorySelected = categorySelected;
    }

    public ConceptSMTK getConceptPending() {
        return conceptPending;
    }

    public void setConceptPending(ConceptSMTK conceptPending) {
        this.conceptPending = conceptPending;
    }

    public void translateDescription() {
        FacesContext context = FacesContext.getCurrentInstance();

        if(termSelected == null){
            for (PendingTerm pendingTerm : termsSelected) {
                conceptPending.removeDescription(pendingTerm.getRelatedDescription());
                pendingTerm.getRelatedDescription().setConceptSMTK(conceptSelected);
                try {
                    descriptionManager.moveDescriptionToConcept(conceptPending, pendingTerm.getRelatedDescription(), user);
                } catch (EJBException e) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));

                }
            }
        }
        else{
            conceptPending.removeDescription(termSelected.getRelatedDescription());
            termSelected.getRelatedDescription().setConceptSMTK(conceptSelected);

            try {
                descriptionManager.moveDescriptionToConcept(conceptPending, termSelected.getRelatedDescription(), user);

            } catch (EJBException e) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));

            }
        }

        conceptSelected = null;
        termSelected = null;
        termsSelected = new ArrayList<>();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", "La descripción fue trasladada exitosamente"));

    }

    public void createNewConcept() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if(termSelected != null){
            ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
            if(categorySelected!=null){
                eContext.redirect(eContext.getRequestContextPath() + "/views/concept/conceptEdit.xhtml?editMode=true&idCategory=" + categorySelected.getId() +"&idConcept=0&favoriteDescription=&descriptionPending="+termSelected.getRelatedDescription().getId() );
            }else{
                eContext.redirect(eContext.getRequestContextPath() + "/views/concept/conceptEdit.xhtml?editMode=true&idCategory=" + termSelected.getCategory().getId() +"&idConcept=0&favoriteDescription=&descriptionPending="+termSelected.getRelatedDescription().getId() );
            }
        }
        else{
            if(!termsSelected.isEmpty()){
                ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
                eContext.redirect(eContext.getRequestContextPath() + "/views/concept/conceptEdit.xhtml?editMode=true&idCategory=" + categorySelected.getId() +"&idConcept=0&favoriteDescription=&pendingTerms=true");

            }else{
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se han seleccionado términos"));
            }
        }

    }

    public List<PendingTerm> getTermsSelected() {
        return termsSelected;
    }

    public void setTermsSelected(List<PendingTerm> termsSelected) {
        this.termsSelected = termsSelected;
    }


}

