package cl.minsal.semantikos.pendingterms;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.messages.MessageBean;
import cl.minsal.semantikos.users.AuthenticationBean;
import cl.minsal.semantikos.kernel.components.CategoryManager;
import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.DescriptionManager;
import cl.minsal.semantikos.kernel.components.PendingTermsManager;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.descriptions.PendingTerm;
import cl.minsal.semantikos.model.users.User;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "managerPendingTermsBean")
@SessionScoped
public class ManagerPendingTermsBean {

    //@EJB
    private ConceptManager conceptManager = (ConceptManager) ServiceLocator.getInstance().getService(ConceptManager.class);

    //@EJB
    private CategoryManager categoryManager = (CategoryManager) ServiceLocator.getInstance().getService(CategoryManager.class);

    //@EJB
    private PendingTermsManager pendingTermsManager = (PendingTermsManager) ServiceLocator.getInstance().getService(PendingTermsManager.class);

    //@EJB
    private DescriptionManager descriptionManager = (DescriptionManager) ServiceLocator.getInstance().getService(DescriptionManager.class);

    @ManagedProperty(value = "#{authenticationBean}")
    private AuthenticationBean authenticationBean;

    @ManagedProperty( value = "#{messageBean}")
    private MessageBean messageBean;

    private User user;

    private PendingTerm termSelected;

    private List<PendingTerm> pendingTerms;

    private List<PendingTerm> pendingTermsListFilter;

    private ConceptSMTK conceptPending;

    private List<Category> categories;

    private ConceptSMTK conceptSMTKSelected;

    private Category categorySelected;

    public Category getCategorySelected() {
        return categorySelected;
    }

    public void setCategorySelected(Category categorySelected) {
        this.categorySelected = categorySelected;
    }

    public ConceptSMTK getConceptSMTKSelected() {
        return conceptSMTKSelected;
    }

    public void setConceptSMTKSelected(ConceptSMTK conceptSMTKSelected) {
        this.conceptSMTKSelected = conceptSMTKSelected;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public ConceptSMTK getConceptPending() {
        return conceptPending;
    }

    public void setConceptPending(ConceptSMTK conceptPending) {
        this.conceptPending = conceptPending;
    }

    public List<PendingTerm> getPendingTermsListFilter() {
        return pendingTermsListFilter;
    }

    public void setPendingTermsListFilter(List<PendingTerm> pendingTermsListFilter) {
        this.pendingTermsListFilter = pendingTermsListFilter;
    }

    public PendingTerm getTermSelected() {
        return termSelected;
    }

    public void setTermSelected(PendingTerm termSelected) {
        this.termSelected = termSelected;
    }

    public List<PendingTerm> getPendingTerms() {
        return pendingTerms;
    }

    public void setPendingTerms(List<PendingTerm> pendingTerms) {
        this.pendingTerms = pendingTerms;
    }

    public AuthenticationBean getAuthenticationBean() {
        return authenticationBean;
    }

    public void setAuthenticationBean(AuthenticationBean authenticationBean) {
        this.authenticationBean = authenticationBean;
    }

    public void setMessageBean(MessageBean messageBean) {
        this.messageBean = messageBean;
    }

    @PostConstruct
    public void init() {
        conceptPending = conceptManager.getPendingConcept();
        categories = categoryManager.getCategories();
        pendingTerms = pendingTermsManager.getAllPendingTerms();


        user = authenticationBean.getLoggedUser();

    }

    public void translateDescription() {
        FacesContext context = FacesContext.getCurrentInstance();

        conceptPending.removeDescription(termSelected.getRelatedDescription());
        termSelected.getRelatedDescription().setConceptSMTK(conceptSMTKSelected);

        try {
            descriptionManager.moveDescriptionToConcept(conceptPending, termSelected.getRelatedDescription(), user);
        } catch (EJBException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));

        }
        pendingTerms = pendingTermsManager.getAllPendingTerms();

        conceptSMTKSelected = null;
        termSelected = null;
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", "La descripción fue trasladada exitosamente"));
    }

    public void createNewConcept(PendingTerm pendingT) throws IOException {

        ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
        if(categorySelected!=null){
            eContext.redirect(eContext.getRequestContextPath() + "/views/concept/conceptEdit.xhtml?editMode=true&idCategory=" + categorySelected.getId() +"&idConcept=0&favoriteDescription=&descriptionPending="+pendingT.getRelatedDescription().getId() );
        }else{
            eContext.redirect(eContext.getRequestContextPath() + "/views/concept/conceptEdit.xhtml?editMode=true&idCategory=" + pendingT.getCategory().getId() +"&idConcept=0&favoriteDescription=&descriptionPending="+pendingT.getRelatedDescription().getId() );
        }
    }

    public List<PendingTerm> pendingTermList;

    public List<PendingTerm> getPendingTermList() {
        return pendingTermList;
    }

    public void setPendingTermList(List<PendingTerm> pendingTermList) {
        this.pendingTermList = pendingTermList;
    }

    public void newConcept() throws IOException {
        if(!pendingTermList.isEmpty()){
            ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
            eContext.redirect(eContext.getRequestContextPath() + "/views/concept/conceptEdit.xhtml?editMode=true&idCategory=" + categorySelected.getId() +"&idConcept=0&favoriteDescription=&pendingTerms=true");

        }else{
            messageBean.messageError("No se han seleccionado términos");
        }
    }

    public void translateMultipleDescription() {
        FacesContext context = FacesContext.getCurrentInstance();

        for (PendingTerm pendingTerm : pendingTermList) {
            conceptPending.removeDescription(pendingTerm.getRelatedDescription());
            pendingTerm.getRelatedDescription().setConceptSMTK(conceptSMTKSelected);
            try {
                descriptionManager.moveDescriptionToConcept(conceptPending, pendingTerm.getRelatedDescription(), user);
            } catch (EJBException e) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));

            }
        }

        pendingTerms = pendingTermsManager.getAllPendingTerms();
        conceptSMTKSelected = null;
        pendingTermList= new ArrayList<>();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", "Las descripciones fueron trasladadas exitosamente"));
    }


}
