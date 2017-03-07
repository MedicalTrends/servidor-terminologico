package cl.minsal.semantikos.designer_modeler.auth;

import cl.minsal.semantikos.kernel.auth.AuthenticationManager;
import cl.minsal.semantikos.kernel.auth.PasswordChangeException;
import cl.minsal.semantikos.kernel.auth.UserManager;
import cl.minsal.semantikos.kernel.components.InstitutionManager;
import cl.minsal.semantikos.kernel.util.StringUtils;
import cl.minsal.semantikos.model.Institution;
import cl.minsal.semantikos.model.Profile;
import cl.minsal.semantikos.model.User;
import cl.minsal.semantikos.model.businessrules.UserCreationBR;
import cl.minsal.semantikos.model.businessrules.UserCreationBRInterface;
import org.omnifaces.util.Ajax;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.TransactionRolledbackLocalException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by BluePrints Developer on 14-07-2016.
 */

@ManagedBean(name = "users")
@ViewScoped
public class UsersBean {

    static private final Logger logger = LoggerFactory.getLogger(UsersBean.class);

    @EJB
    UserManager userManager;

    @EJB
    InstitutionManager institutionManager;

    @EJB
    AuthenticationManager authenticationManager;

    @EJB
    UserCreationBRInterface userCreationBR;

    User selectedUser;

    List<User> allUsers;

    List<Profile> allProfiles;

    DualListModel<Profile> selectedUserProfileModel = new DualListModel<>();

    DualListModel<Institution> selectedUserInsitutionModel = new DualListModel<>();

    String userNameError = "";

    String nameError = "";

    String lastNameError = "";

    String rutError = "";

    String passwordError = "";

    String password2Error = "";

    String newPass1 = "";
    String newPass2 = "";

    //Inicializacion del Bean
    @PostConstruct
    protected void initialize() throws ParseException {
        newUser();
    }

    public String getNewPass2() {
        return newPass2;
    }

    public void setNewPass2(String newPass2) {
        if(!newPass2.isEmpty()) {
            this.newPass2 = newPass2;
        }
    }

    public String getNewPass1() {
        return newPass1;
    }

    public void setNewPass1(String newPass1) {
        if(!newPass1.isEmpty()) {
            this.newPass1 = newPass1;
        }
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {

        clean();
        newPass1 = "";
        newPass2 = "";

        this.selectedUser = userManager.getUser(selectedUser.getIdUser());

        //se debe actualizar la lista del picklist con los perfiles del usuario
        updateAvailableProfiles(this.selectedUser);

        //se debe actualizar la lista del picklist con las instituciones del usuario
        updateAvailableInsitutions(this.selectedUser);

    }

    private void updateAvailableProfiles(User selectedUser) {

        selectedUserProfileModel.setTarget(selectedUser.getProfiles());

        List<Profile> availableProfiles = new ArrayList<Profile>();

        availableProfiles.addAll(userManager.getAllProfiles());


        for (Profile p: selectedUser.getProfiles()){
            availableProfiles.remove(p);
        }

        selectedUserProfileModel.setSource(availableProfiles);
    }

    private void updateAvailableInsitutions(User selectedUser) {

        selectedUserInsitutionModel.setTarget(selectedUser.getInstitutions());

        List<Institution> availableInstitutions = new ArrayList<Institution>();

        availableInstitutions.addAll(institutionManager.getAllInstitution());


        for (Institution i: selectedUser.getInstitutions()){
            availableInstitutions.remove(i);
        }

        selectedUserInsitutionModel.setSource(availableInstitutions);
    }


    public List<User> getAllUsers(){

        if(allUsers==null) {
            allUsers = userManager.getAllUsers();
        }

        return allUsers;
    }


    public void newUser() {

        selectedUser = new User();
        selectedUser.setIdUser(-1);
        updateAvailableProfiles(selectedUser);
        updateAvailableInsitutions(selectedUser);
        newPass1 = "";
        clean();
    }

    public String getPassword2Error() {
        return password2Error;
    }

    public void setPassword2Error(String password2Error) {
        this.password2Error = password2Error;
    }

    public void clean() {
        userNameError = "";
        nameError = "";
        lastNameError = "";
        rutError = "";
        passwordError = "";
        password2Error = "";

    }

    public void formatRut() {

        if(!selectedUser.getRut().trim().isEmpty()) {
            selectedUser.setRut(StringUtils.formatRut(selectedUser.getRut()));
        }

    }

    public void saveUser() {

        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext rContext = RequestContext.getCurrentInstance();

        if(selectedUser.getUsername().trim().equals("")) {
            userNameError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar 'Nombre de usuario'"));
        }
        else {
            userNameError = "";
        }

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

        if(selectedUser.getRut().trim().equals("")) {
            rutError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar RUT"));
        }
        else if(!StringUtils.validateRutFormat(selectedUser.getRut())) {
            rutError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Formato de RUT no es corrrecto'"));
        }
        else if(!StringUtils.validateRutVerificationDigit(selectedUser.getRut())) {
            rutError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El dígito verificador no es correcto'"));
        }
        else {
            rutError = "";
        }

        /**
         * Si el usuario se está creando, validar password, existencia de rut y username
         */
        if(selectedUser.getIdUser()==-1) {

            if(newPass1.trim().equals("")) {
                passwordError = "ui-state-error";
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar una contraseña"));
            }
            else {
                passwordError = "";
            }

            if(newPass2.trim().equals("")) {
                password2Error = "ui-state-error";
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe confirmar la contraseña"));
            }
            else {
                password2Error = "";
            }

            if(!newPass1.equals(newPass2)) {
                password2Error = "ui-state-error";
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La confirmación de contraseña no coincide con la original"));
            }
            else {
                password2Error = "";
            }

            try{
                userCreationBR.br301UniqueRut(selectedUser);
            }
            catch (EJBException e) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
                rutError = "ui-state-error";
                return;
            }

            try{
                userCreationBR.br302UniqueUsername(selectedUser);
            }
            catch (EJBException e) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
                userNameError = "ui-state-error";
                return;
            }

        }

        if(!userNameError.concat(nameError).concat(lastNameError).concat(rutError).concat(passwordError).concat(password2Error).trim().equals("")) {
            return;
        }

        try {
            selectedUser.setProfiles(selectedUserProfileModel.getTarget());

            if(selectedUser.getIdUser() == -1) {
                try {
                    authenticationManager.createUserPassword(selectedUser,selectedUser.getUsername(),newPass1);
                    userManager.createUser(selectedUser);
                    rContext.execute("PF('editDialog').hide();");
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Usuario creado de manera exitosa!!"));
                    Ajax.update("singleDT");
                }
                catch (EJBException e) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
                }
            }
            else {
                userManager.updateUser(selectedUser);
                rContext.execute("PF('editDialog').hide();");
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Usuario modificado de manera exitosa!!"));
                Ajax.update("singleDT");
            }

        }catch (Exception e){
            logger.error("error al actualizar usuario",e);
        }
    }

    public DualListModel<Profile> getSelectedUserProfileModel(){

        if(selectedUserProfileModel == null) {
            selectedUserProfileModel = new DualListModel<Profile>();
        }
        return selectedUserProfileModel;
    }

    public void setSelectedUserProfileModel(DualListModel<Profile> selectedUserProfileModel) {
        this.selectedUserProfileModel = selectedUserProfileModel;
    }

    public DualListModel<Institution> getSelectedUserInsitutionModel() {
        return selectedUserInsitutionModel;
    }

    public void setSelectedUserInsitutionModel(DualListModel<Institution> selectedUserInsitutionModel) {
        if(!selectedUserInsitutionModel.getSource().isEmpty()) {
            this.selectedUserInsitutionModel = selectedUserInsitutionModel;
        }
    }

    public void changePass() {

        FacesContext context = FacesContext.getCurrentInstance();

        try {
            if(newPass1.trim().equals("")) {
                passwordError = "ui-state-error";
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar una contraseña"));
            }
            else {
                passwordError = "";
            }

            if(newPass2.trim().equals("")) {
                password2Error = "ui-state-error";
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe confirmar la contraseña"));
            }
            else {
                password2Error = "";
            }

            if(!newPass1.equals(newPass2)) {
                password2Error = "ui-state-error";
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La confirmación de contraseña no coincide con la original"));
            }
            else {
                password2Error = "";
            }

            if(!passwordError.concat(password2Error).trim().equals("")) {
                return;
            }

            authenticationManager.setUserPassword(selectedUser.getUsername(),newPass1);
        } catch (PasswordChangeException e) {
            e.printStackTrace();
        }
    }

    public Profile getProfileById(long profileId){
        return userManager.getProfileById(profileId);

    }

    public void unlockUser(){
        userManager.unlockUser(selectedUser.getUsername());
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

    public String getRutError() {
        return rutError;
    }

    public void setRutError(String rutError) {
        this.rutError = rutError;
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

}
