package cl.minsal.semantikos.users;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.concept.SMTKTypeBean;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.audit.ConceptAuditAction;
import cl.minsal.semantikos.model.audit.UserAuditAction;
import cl.minsal.semantikos.model.exceptions.PasswordChangeException;

import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.User;

import cl.minsal.semantikos.model.users.UserFactory;
import cl.minsal.semantikos.util.StringUtils;
import org.omnifaces.util.Ajax;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BluePrints Developer on 14-07-2016.
 */

@ManagedBean(name = "users")
@SessionScoped
public class UsersBean {

    static private final Logger logger = LoggerFactory.getLogger(UsersBean.class);

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

    User selectedUser;

    User originalUser;

    private List<UserAuditAction> auditAction;

    List<User> allUsers;

    List<Profile> allProfiles;

    DualListModel<Profile> selectedUserProfileModel = new DualListModel<>();

    DualListModel<Institution> selectedUserInsitutionModel = new DualListModel<>();

    String emailError = "";

    String userNameError = "";

    String nameError = "";

    String lastNameError = "";

    String documentNumberError = "";

    String passwordError = "";

    String password2Error = "";

    String oldPasswordError = "";

    String profileError = "";

    String institutionError = "";

    String newPass1 = "";
    String newPass2 = "";

    String oldPass = "";

    long idUser;

    //Inicializacion del Bean
    @PostConstruct
    protected void initialize() {
        //createOrUpdateUser();
    }

    public void createOrUpdateUser() {
        if(idUser == 0 /*&& selectedUser == null*/) {
            newUser();
        }
        if(idUser != 0 /*&& !selectedUser.isPersistent()*/ ) {
            getUser(idUser);
        }
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {

        clean();
        newPass1 = "";
        newPass2 = "";

        this.selectedUser = userManager.getUser(selectedUser.getId());

        //se debe actualizar la lista del picklist con los perfiles del usuario
        updateAvailableProfiles(this.selectedUser);

        //se debe actualizar la lista del picklist con las instituciones del usuario
        updateAvailableInsitutions(this.selectedUser);

    }

    public User getOriginalUser() {
        return originalUser;
    }

    public void setOriginalUser(User originalUser) {
        this.originalUser = originalUser;
    }

    private void updateAvailableProfiles(User selectedUser) {

        selectedUserProfileModel.setTarget(selectedUser.getProfiles());

        List<Profile> availableProfiles = new ArrayList<Profile>();

        availableProfiles.addAll(profileManager.getAllProfiles());

        for (Profile p: selectedUser.getProfiles()){
            availableProfiles.remove(p);
        }

        selectedUserProfileModel.setSource(availableProfiles);
    }

    private void updateAvailableInsitutions(User selectedUser) {

        selectedUserInsitutionModel.setTarget(selectedUser.getInstitutions());

        List<Institution> availableInstitutions = new ArrayList<Institution>();

        availableInstitutions.addAll(institutionManager.getValidInstitution());


        for (Institution i: selectedUser.getInstitutions()){
            availableInstitutions.remove(i);
        }

        selectedUserInsitutionModel.setSource(availableInstitutions);
    }


    public void newUser() {

        selectedUser = new User();
        selectedUser.setId(-1);
        updateAvailableProfiles(selectedUser);
        updateAvailableInsitutions(selectedUser);
        clean();
    }

    public void getUser(long idUser) {

        selectedUser = userManager.getUser(idUser);
        originalUser = new User(selectedUser);
        auditAction = auditManager.getUserAuditActions(selectedUser);
        updateAvailableProfiles(selectedUser);
        updateAvailableInsitutions(selectedUser);
        clean();
    }

    public String getPassword2Error() {
        return password2Error;
    }

    public void setPassword2Error(String password2Error) {
        this.password2Error = password2Error;
    }

    public String getOldPasswordError() {
        return oldPasswordError;
    }

    public void setOldPasswordError(String oldPasswordError) {
        this.oldPasswordError = oldPasswordError;
    }

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass2() {
        return newPass2;
    }

    public void setNewPass2(String newPass2) {
        this.newPass2 = newPass2;
    }

    public String getNewPass1() {
        return newPass1;
    }

    public void setNewPass1(String newPass1) {
        this.newPass1 = newPass1;
    }

    public void clean() {
        emailError = "";
        userNameError = "";
        nameError = "";
        lastNameError = "";
        documentNumberError = "";
        passwordError = "";
        password2Error = "";
        oldPasswordError = "";
        oldPass = "";
        newPass1 = "";
        newPass2 = "";
        profileError = "";
        institutionError = "";
    }

    public void formatRut() {

        if(!selectedUser.getDocumentNumber().trim().isEmpty() && selectedUser.isDocumentRut()) {
            selectedUser.setDocumentNumber(StringUtils.formatRut(selectedUser.getDocumentNumber()));
        }

    }

    public void saveUser() {

        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext rContext = RequestContext.getCurrentInstance();

        if(selectedUser.getName().trim().equals("")) {
            nameError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar nombres"));
        }
        else {
            nameError = "";
        }

        if(selectedUser.getLastName().trim().equals("")) {
            lastNameError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar apellido paterno"));
        }
        else {
            lastNameError = "";
        }

        if(selectedUser.getDocumentNumber().trim().equals("")) {
            documentNumberError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar RUT"));
        }
        else {
            documentNumberError = "";
        }

        if(selectedUser.isDocumentRut()) {

            if(!StringUtils.validateRutFormat(selectedUser.getDocumentNumber())) {
                documentNumberError = "ui-state-error";
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Formato de RUT no es corrrecto'"));
            }
            else if(!StringUtils.validateRutVerificationDigit(selectedUser.getDocumentNumber())) {
                documentNumberError = "ui-state-error";
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El dígito verificador no es correcto'"));
            }
            else {
                documentNumberError = "";
            }
        }
        else {

        }

        if(selectedUser.getEmail().trim().equals("")) {
            emailError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar 'e-mail'"));
        }
        else {
            emailError = "";
        }

        if(!StringUtils.isValidEmailAddress(selectedUser.getEmail())) {
            emailError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El formato del 'e-mail' no es válido"));
            return;
        }
        else {
            emailError = "";
        }

        if(!userNameError.concat(nameError).concat(lastNameError).concat(documentNumberError).concat(passwordError).concat(password2Error).trim().equals("")) {
            return;
        }

        selectedUser.setProfiles(selectedUserProfileModel.getTarget());

        if(selectedUser.getProfiles().isEmpty()) {
            profileError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe asignar por lo menos 1 perfil"));
            return;
        }
        else {
            profileError = "";
        }

        selectedUser.setInstitutions(selectedUserInsitutionModel.getTarget());

        if(selectedUser.getInstitutions().isEmpty()) {
            institutionError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe asignar por lo menos 1 establecimiento"));
            return;
        }
        else {
            institutionError = "";
        }

        try {

            FacesContext facesContext = FacesContext.getCurrentInstance();

            if(selectedUser.getId() == -1) {
                try {
                    HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();

                    selectedUser = userManager.getUser(userManager.createUser(selectedUser, getURLWithContextPath(request), authenticationBean.getLoggedUser()));
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "1° Usuario creado de manera exitosa!!"));
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "2° Se ha enviado un correo de notificación al usuario para activar esta cuenta."));
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "3° Este usuario permanecerá bloqueado hasta que él active su cuenta"));
                }
                catch (EJBException e) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
                }
            }
            else {
                userManager.update(originalUser, selectedUser, authenticationBean.getLoggedUser());
                getUser(selectedUser.getId());
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Usuario: "+selectedUser.getEmail()+" modificado de manera exitosa!!"));
            }

            refreshUserFactory();

            //facesContext.getExternalContext().redirect(((HttpServletRequest) facesContext.getExternalContext().getRequest()).getRequestURI());

        } catch (Exception e){
            logger.error("error al actualizar usuario",e);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public DualListModel<Profile> getSelectedUserProfileModel(){

        if(selectedUserProfileModel == null) {
            selectedUserProfileModel = new DualListModel<Profile>();
        }
        return selectedUserProfileModel;
    }

    public void setSelectedUserProfileModel(DualListModel<Profile> selectedUserProfileModel) {
        if(selectedUserProfileModel == null) {
            return;
        }
        if(this.selectedUserProfileModel.getSource().size() == selectedUserProfileModel.getSource().size()) {
            return;
        }
        this.selectedUserProfileModel = selectedUserProfileModel;
    }

    public String getURLWithContextPath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()  + request.getContextPath();
    }

    public DualListModel<Institution> getSelectedUserInsitutionModel() {
        if(selectedUserInsitutionModel == null) {
            selectedUserInsitutionModel = new DualListModel<Institution>();
        }
        return selectedUserInsitutionModel;
    }

    public void setSelectedUserInsitutionModel(DualListModel<Institution> selectedUserInsitutionModel) {
        if(!selectedUserInsitutionModel.getSource().isEmpty()) {
            this.selectedUserInsitutionModel = selectedUserInsitutionModel;
        }
    }

    public Profile getProfileById(long profileId){
        return profileManager.getProfileById(profileId);
    }

    public Institution getInstitutionById(long institutionId) {
        return institutionManager.getInstitutionById(institutionId);
    }

    public void unlockUser() {
        //userManager.unlockUser(selectedUser.getUsername());
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        try {
            userManager.resetAccount(selectedUser, getURLWithContextPath(request), authenticationBean.getLoggedUser());
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "1° Se ha enviado un correo de notificación al usuario para activar esta cuenta."));
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "2° Este usuario permanecerá bloqueado hasta que él active su cuenta"));
        } catch (Exception e){
            logger.error("error al actualizar usuario",e);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public void deleteUser() {
        //userManager.unlockUser(selectedUser.getUsername());
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            userManager.deleteUser(selectedUser, authenticationBean.getLoggedUser());
            selectedUser = userManager.getUser(selectedUser.getId());
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "El usuario se ha eliminado y queda en estado No Vigente."));
        } catch (Exception e){
            logger.error("error al actualizar usuario",e);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public void showProfileHistory() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Info", "En construcción"));
    }

    public void changePass() {

        FacesContext context = FacesContext.getCurrentInstance();

        if(oldPass.trim().equals("")) {
            oldPasswordError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar contraseña actual"));
            return;
        }
        else {
            oldPasswordError = "";
        }

        if(newPass1.trim().equals("")) {
            passwordError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar una nueva contraseña"));
            return;
        }
        else {
            passwordError = "";
        }

        if(newPass2.trim().equals("")) {
            password2Error = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe confirmar la nueva contraseña"));
            return;
        }
        else {
            password2Error = "";
        }

        if(!oldPasswordError.concat(passwordError).concat(password2Error).trim().equals("")) {
            return;
        }

        if(!authenticationManager.checkPassword(selectedUser, selectedUser.getEmail(), oldPass)) {
            oldPasswordError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La contraseña actual no es correcta"));
            return;
        }
        else {
            oldPasswordError = "";
        }

        if(!newPass1.equals(newPass2)) {
            password2Error = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La confirmación de contraseña no coincide con la original"));
            return;
        }
        else {
            password2Error = "";
        }

        if(!oldPasswordError.concat(passwordError).concat(password2Error).trim().equals("")) {
            return;
        }

        try {
            authenticationManager.setUserPassword(selectedUser.getEmail(),newPass1);
        } catch (PasswordChangeException e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
            return;
        }
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Contraseña modificada de manera exitosa!!"));

    }

    public String getUserNameError() {
        return userNameError;
    }

    public void setUserNameError(String userNameError) {
        this.userNameError = userNameError;
    }

    public String getNameError() {
        return nameError;
    }

    public void setNameError(String nameError) {
        this.nameError = nameError;
    }

    public String getDocumentNumberError() {
        return documentNumberError;
    }

    public void setDocumentNumberError(String documentNumberError) {
        this.documentNumberError = documentNumberError;
    }

    public String getLastNameError() {
        return lastNameError;
    }

    public void setLastNameError(String lastNameError) {
        this.lastNameError = lastNameError;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    public String getEmailError() {
        return emailError;
    }

    public void setEmailError(String emailError) {
        this.emailError = emailError;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
        if(selectedUser == null) {
            createOrUpdateUser();
        }
    }

    public void refreshUserFactory() {
        UserFactory.getInstance().setUsersById(userManager.getUserFactory().getUsersById());
        authenticationBean.refreshLoggedUser(selectedUser);
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

    public String getProfileError() {
        return profileError;
    }

    public void setProfileError(String profileError) {
        this.profileError = profileError;
    }

    public String getInstitutionError() {
        return institutionError;
    }

    public void setInstitutionError(String institutionError) {
        this.institutionError = institutionError;
    }

    public List<UserAuditAction> getAuditAction() {
        return auditAction;
    }

    public void setAuditAction(List<UserAuditAction> auditAction) {
        this.auditAction = auditAction;
    }

}
