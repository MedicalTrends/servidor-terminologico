package cl.minsal.semantikos.institutions;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.AuditManager;
import cl.minsal.semantikos.kernel.components.InstitutionManager;
import cl.minsal.semantikos.kernel.components.UserManager;
import cl.minsal.semantikos.model.audit.InstitutionAuditAction;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.InstitutionFactory;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.users.AuthenticationBean;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cl.minsal.semantikos.model.audit.AuditActionType.INSTITUTION_CREATION;
import static cl.minsal.semantikos.model.audit.AuditActionType.INSTITUTION_DELETE;

/**
 * Created by BluePrints Developer on 14-07-2016.
 */

@ManagedBean(name = "institutionsBrowser")
@SessionScoped
public class InstitutionsBrowserBean {

    static private final Logger logger = LoggerFactory.getLogger(InstitutionsBrowserBean.class);

    //@EJB
    InstitutionManager institutionManager = (InstitutionManager) ServiceLocator.getInstance().getService(InstitutionManager.class);

    //@EJB
    AuditManager auditManager = (AuditManager) ServiceLocator.getInstance().getService(AuditManager.class);

    Institution selectedInstitution;

    List<Institution> allInstitutions;

    List<Institution> filteredInstitutions;

    Map<Long, List<InstitutionAuditAction>> auditActions = new HashMap<>();

    @ManagedProperty(value = "#{authenticationBean}")
    private AuthenticationBean authenticationBean;

    String deleteCause;

    //Inicializacion del Bean
    @PostConstruct
    protected void initialize() {
        allInstitutions = institutionManager.getAllInstitution();
        for (Institution institution : allInstitutions) {
            auditActions.put(institution.getId(),auditManager.getInstitutionAuditActions(institution));
        }
        RequestContext reqCtx = RequestContext.getCurrentInstance();
        reqCtx.execute("PF('institutionsTable').filter();");
    }

    public void newInstitution() {

        selectedInstitution = new Institution();
        selectedInstitution.setId(-1);

        ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();

        try {
            eContext.redirect(eContext.getRequestContextPath() + "/views/institutions/institutionEdit.xhtml?idInstitution=0");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Institution getSelectedInstitution() {
        return selectedInstitution;
    }

    public void setSelectedInstitution(Institution selectedInstitution) {
        this.selectedInstitution = institutionManager.getInstitutionById(selectedInstitution.getId());
    }

    public List<Institution> getAllInstitutions(){

        if(allInstitutions == null) {
            allInstitutions = institutionManager.getAllInstitution();
        }

        return allInstitutions;
    }

    public List<Institution> getFilteredInstitutions() {
        return filteredInstitutions;
    }

    public void setFilteredInstitutions(List<Institution> filteredInstitutions) {
        this.filteredInstitutions = filteredInstitutions;
    }


    public Institution getInstitutionById(long institutionId){
        return institutionManager.getInstitutionById(institutionId);
    }

    public AuditManager getAuditManager() {
        return auditManager;
    }

    public void setAuditManager(AuditManager auditManager) {
        this.auditManager = auditManager;
    }

    public Map<Long, List<InstitutionAuditAction>> getAuditActions() {
        return auditActions;
    }

    public void setAuditActions(Map<Long, List<InstitutionAuditAction>> auditActions) {
        this.auditActions = auditActions;
    }

    public String getCreationDate(List<InstitutionAuditAction> institutionAuditActions) {
        for (InstitutionAuditAction institutionAuditAction : institutionAuditActions) {
            if( institutionAuditAction.getAuditActionType().equals(INSTITUTION_CREATION)) {
                return institutionAuditAction.getActionDateFormat();
            }
        }
        return "";
    }

    public String getLastUpgradeDate(List<InstitutionAuditAction> institutionAuditActions) {
        for (InstitutionAuditAction institutionAuditAction : institutionAuditActions) {
            return institutionAuditAction.getActionDateFormat();
        }
        return "";
    }

    public String getLastUpgradeUser(List<InstitutionAuditAction> institutionAuditActions) {
        for (InstitutionAuditAction institutionAuditAction : institutionAuditActions) {
            return institutionAuditAction.getUser().getEmail();
        }
        return "";
    }

    public String getDeleteDate(List<InstitutionAuditAction> institutionAuditActions) {
        for (InstitutionAuditAction institutionAuditAction : institutionAuditActions) {
            if( institutionAuditAction.getAuditActionType().equals(INSTITUTION_DELETE)) {
                return institutionAuditAction.getActionDateFormat();
            }
        }
        return "";
    }

    public String getDeleteCause() {
        return deleteCause;
    }

    public void setDeleteCause(String deleteCause) {
        this.deleteCause = deleteCause;
    }

    public AuthenticationBean getAuthenticationBean() {
        return authenticationBean;
    }

    public void setAuthenticationBean(AuthenticationBean authenticationBean) {
        this.authenticationBean = authenticationBean;
    }

    public void deleteInstitution() {

        FacesContext facesContext = FacesContext.getCurrentInstance();

        try {
            if(deleteCause == null || deleteCause.isEmpty()) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar un motivo de eliminación"));
                return;
            }
            institutionManager.deleteInstitution(selectedInstitution, authenticationBean.getLoggedUser(), deleteCause);
            InstitutionFactory.getInstance().setInstitutions(institutionManager.getInstitutionFactory().getInstitutions());
            selectedInstitution = institutionManager.getInstitutionById(selectedInstitution.getId());
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "El establecimiento se ha eliminado y queda en estado No Vigente."));
            //RequestContext reqCtx = RequestContext.getCurrentInstance();
            //reqCtx.execute("PF('dlg').hide();");
            //reqCtx.execute("PF('institutionsTable').filter();");
            ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
            eContext.redirect(eContext.getRequestContextPath() + "/views/institutions/institutions.xhtml");
        } catch (Exception e){
            logger.error("error al eliminar establecimiento",e);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }
}
