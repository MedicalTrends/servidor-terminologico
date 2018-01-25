package cl.minsal.semantikos.institutions;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.audit.InstitutionAuditAction;
import cl.minsal.semantikos.model.audit.UserAuditAction;
import cl.minsal.semantikos.model.exceptions.PasswordChangeException;
import cl.minsal.semantikos.model.users.*;
import cl.minsal.semantikos.users.AuthenticationBean;
import cl.minsal.semantikos.util.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BluePrints Developer on 14-07-2016.
 */

@ManagedBean(name = "institutions")
@ViewScoped
public class InstitutionsBean {

    static private final Logger logger = LoggerFactory.getLogger(InstitutionsBean.class);

    //@EJB
    UserManager userManager = (UserManager) ServiceLocator.getInstance().getService(UserManager.class);

    //@EJB
    ProfileManager profileManager = (ProfileManager) ServiceLocator.getInstance().getService(ProfileManager.class);

    //@EJB
    InstitutionManager institutionManager = (InstitutionManager) ServiceLocator.getInstance().getService(InstitutionManager.class);

    //@EJB
    AuthenticationManager authenticationManager = (AuthenticationManager) ServiceLocator.getInstance().getService(AuthenticationManager.class);

    //@EJB
    AuditManager auditManager = (AuditManager) ServiceLocator.getInstance().getService(AuditManager.class);

    @ManagedProperty(value = "#{authenticationBean}")
    private AuthenticationBean authenticationBean;

    Institution selectedInstitution;

    Institution originalInstitution;

    private List<InstitutionAuditAction> auditAction;

    String codeError = "";

    String nameError = "";

    long idInstitution;

    String deleteCause;

    //Inicializacion del Bean
    @PostConstruct
    protected void initialize() {
        //createOrUpdateUser();
    }

    public void createOrUpdateInstitution() {
        if(idInstitution == 0 /*&& selectedUser == null*/) {
            newInstitution();
        }
        if(idInstitution != 0 /*&& !selectedUser.isPersistent()*/ ) {
            getInstitution(idInstitution);
        }
    }

    public Institution getSelectedInstitution() {
        return selectedInstitution;
    }

    public void setSelectedUser(User selectedUser) {

        clean();
        this.selectedInstitution = institutionManager.getInstitutionById(selectedInstitution.getId());

    }

    public Institution getOriginalInstitution() {
        return originalInstitution;
    }

    public void setOriginalInstitution(Institution institution) {
        this.originalInstitution = institution;
    }

    public void newInstitution() {

        selectedInstitution = new Institution();
        selectedInstitution.setId(-1);
        clean();
    }

    public void getInstitution(long idInstitution) {

        selectedInstitution = institutionManager.getInstitutionById(idInstitution);
        originalInstitution = new Institution(selectedInstitution);
        auditAction = auditManager.getInstitutionAuditActions(selectedInstitution);
        clean();
    }

    public void clean() {
        nameError = "";
        codeError = "";
    }

    public void saveInstitution() {

        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext rContext = RequestContext.getCurrentInstance();

        if(selectedInstitution.getCode() == null) {
            codeError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar código"));
            return;
        }
        else {
            codeError = "";
        }

        if(selectedInstitution.getName().trim().equals("")) {
            nameError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar nombre"));
            return;
        }
        else {
            nameError = "";
        }

        try {

            FacesContext facesContext = FacesContext.getCurrentInstance();

            if(selectedInstitution.getId() == -1) {
                try {
                    HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();

                    selectedInstitution = institutionManager.getInstitutionById(institutionManager.createInstitution(selectedInstitution, authenticationBean.getLoggedUser()));
                    getInstitution(selectedInstitution.getId());
                    // Actualizar cache local
                    InstitutionFactory.getInstance().setInstitutions(institutionManager.getInstitutionFactory().getInstitutions());
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Establecimiento creado de manera exitosa!!"));
                }
                catch (EJBException e) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
                }
            }
            else {
                institutionManager.update(originalInstitution, selectedInstitution, authenticationBean.getLoggedUser());
                // Actualizar cache local
                InstitutionFactory.getInstance().setInstitutions(institutionManager.getInstitutionFactory().getInstitutions());
                getInstitution(selectedInstitution.getId());
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Establecimiento: "+selectedInstitution.getName()+" modificado de manera exitosa!!"));
            }

            //facesContext.getExternalContext().redirect(((HttpServletRequest) facesContext.getExternalContext().getRequest()).getRequestURI());

        } catch (Exception e) {
            logger.error("error al actualizar establecimiento",e);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public Institution getInstitutionById(long institutionId) {
        return institutionManager.getInstitutionById(institutionId);
    }

    public void deleteInstitution() {

        FacesContext facesContext = FacesContext.getCurrentInstance();

        try {
            if(deleteCause == null || deleteCause.isEmpty()) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar un motivo de eliminación"));
                return;
            }
            institutionManager.deleteInstitution(selectedInstitution, authenticationBean.getLoggedUser(), deleteCause);
            selectedInstitution = institutionManager.getInstitutionById(selectedInstitution.getId());
            // Actualizar cache local
            InstitutionFactory.getInstance().setInstitutions(institutionManager.getInstitutionFactory().getInstitutions());
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "El establecimiento se ha eliminado y queda en estado No Vigente."));
            RequestContext reqCtx = RequestContext.getCurrentInstance();
            reqCtx.execute("PF('dlg').hide();");
        } catch (Exception e){
            logger.error("error al eliminar establecimiento",e);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public long getIdInstitution() {
        return idInstitution;
    }

    public void setIdInstitution(long idInstitution) {
        this.idInstitution = idInstitution;
        if(selectedInstitution == null) {
            createOrUpdateInstitution();
        }
    }

    public AuthenticationBean getAuthenticationBean() {
        return authenticationBean;
    }

    public void setAuthenticationBean(AuthenticationBean authenticationBean) {
        this.authenticationBean = authenticationBean;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    public List<InstitutionAuditAction> getAuditAction() {
        return auditAction;
    }

    public void setAuditAction(List<InstitutionAuditAction> auditAction) {
        this.auditAction = auditAction;
    }

    public String getCodeError() {
        return codeError;
    }

    public void setCodeError(String codeError) {
        this.codeError = codeError;
    }

    public String getNameError() {
        return nameError;
    }

    public void setNameError(String nameError) {
        this.nameError = nameError;
    }

    public String getDeleteCause() {
        return deleteCause;
    }

    public void setDeleteCause(String deleteCause) {
        this.deleteCause = deleteCause;
    }
}
