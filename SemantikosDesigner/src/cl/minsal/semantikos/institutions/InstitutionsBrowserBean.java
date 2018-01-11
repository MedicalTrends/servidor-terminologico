package cl.minsal.semantikos.institutions;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.AuditManager;
import cl.minsal.semantikos.kernel.components.InstitutionManager;
import cl.minsal.semantikos.kernel.components.UserManager;
import cl.minsal.semantikos.model.audit.InstitutionAuditAction;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.User;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
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
@ViewScoped
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
}
