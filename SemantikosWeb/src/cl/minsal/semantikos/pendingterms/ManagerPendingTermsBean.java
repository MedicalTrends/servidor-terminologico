package cl.minsal.semantikos.pendingterms;

import cl.minsal.semantikos.designer_modeler.auth.AuthenticationBean;
import cl.minsal.semantikos.beans.concept.ConceptBean;
import cl.minsal.semantikos.kernel.components.CategoryManager;
import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.DescriptionManager;
import cl.minsal.semantikos.kernel.components.PendingTermsManager;
import cl.minsal.semantikos.model.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

/**
 * Created by des01c7 on 22-11-16.
 */


@ManagedBean(name = "managerPendingTermsBean")
@ViewScoped
public class ManagerPendingTermsBean {

    @EJB
    private ConceptManager conceptManager;

    @EJB
    private CategoryManager categoryManager;

    @EJB
    private PendingTermsManager pendingTermsManager;

    @EJB
    private DescriptionManager descriptionManager;

    @ManagedProperty(value = "#{authenticationBean}")
    private AuthenticationBean authenticationBean;

    @ManagedProperty(value="#{conceptBean}")
    private ConceptBean conceptBean;

    public ConceptBean getConceptBean() {
        return conceptBean;
    }

    public void setConceptBean(ConceptBean conceptBean) {
        this.conceptBean = conceptBean;
    }

    private User user;

    private PendingTerm termSelected;

    private List<PendingTerm> pendingTerms;

    private List<PendingTerm> pendingTermsListFilter;

    private ConceptSMTK conceptPending;

    private List<Category> categories;

    private ConceptSMTK conceptSMTKSelected;


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
    @PostConstruct
    public void init() {
        conceptPending = conceptManager.getPendingConcept();
        categories = categoryManager.getCategories();
        pendingTerms = pendingTermsManager.getAllPendingTerms();


        user = authenticationBean.getLoggedUser();
        Profile modelerProfile = new Profile(3, "Modelador", "Usuario Modelador");
        user.getProfiles().add(modelerProfile);
    }

    public void translateDescription() {
        FacesContext context = FacesContext.getCurrentInstance();

        conceptPending.removeDescription(termSelected.getRelatedDescription());
        termSelected.getRelatedDescription().setConceptSMTK(conceptSMTKSelected);

        try {
            descriptionManager.moveDescriptionToConcept(conceptPending, termSelected.getRelatedDescription(), user);
        }catch (EJBException e){
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));

        }
        pendingTerms = pendingTermsManager.getAllPendingTerms();

        conceptSMTKSelected = null;
        termSelected = null;
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", "La descripción fue trasladada exitosamente"));
    }

    public void createNewConcept(PendingTerm pendingT) throws IOException {
        ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
        eContext.redirect(eContext.getRequestContextPath() + "/views/concept/conceptEdit.xhtml?editMode=true&idCategory=" + pendingT.getCategory().getId() +"&idConcept=0&favoriteDescription=&descriptionPending="+pendingT.getRelatedDescription().getId() );
    }


}
