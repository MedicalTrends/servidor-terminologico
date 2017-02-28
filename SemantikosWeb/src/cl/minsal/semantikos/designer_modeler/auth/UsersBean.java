package cl.minsal.semantikos.designer_modeler.auth;

import cl.minsal.semantikos.kernel.auth.AuthenticationManager;
import cl.minsal.semantikos.kernel.auth.PasswordChangeException;
import cl.minsal.semantikos.kernel.auth.UserManager;
import cl.minsal.semantikos.model.Profile;
import cl.minsal.semantikos.model.User;
import org.omnifaces.util.Ajax;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
    AuthenticationManager authenticationManager;

    User selectedUser;

    List<User> allUsers;

    List<Profile> allProfiles;

    DualListModel<Profile> selectedUserProfileModel = new DualListModel<>();

    String userNameError = "";

    String nameError = "";

    String lastNameError = "";

    String rutError = "";

    String passwordError = "";

    String newPass1 = "";
    String newPass2;

    //Inicializacion del Bean
    @PostConstruct
    protected void initialize() throws ParseException {
        newUser();
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
        if(!newPass1.isEmpty()) {
            this.newPass1 = newPass1;
        }
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {

        clean();
        newPass1 = "*************************";

        this.selectedUser = userManager.getUser(selectedUser.getIdUser());

        //se debe actualizar la lista del picklist con los perfiles del usuario
        updateAvailableProfiles(this.selectedUser);

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
        newPass1 = "";
        clean();
    }

    public void clean() {
        userNameError = "";
        nameError = "";
        lastNameError = "";
        rutError = "";
    }

    public void validateRut() {
        String rut = selectedUser.getRut();

        Pattern p = Pattern.compile( "^0*(\\d{1,3}(\\.?\\d{3})*)\\-?([\\dkK])$" );
        Matcher m = p.matcher( rut );

        if(!m.matches()) {
            rutError = "ui-state-error";
        }

        rut.replace("-","");
        rut.replace(".","");

        int num = Integer.parseInt(rut.substring(0,rut.length()-1));

        if(!validarRut(num,rut.charAt(rut.length()))) {
            rutError = "ui-state-error";
        }

        rutError = "";
    }

    public static boolean validarRut(int rut, char dv)
    {
        int m = 0, s = 1;
        for (; rut != 0; rut /= 10)
        {
            s = (s + rut % 10 * (9 - m++ % 6)) % 11;
        }
        return dv == (char) (s != 0 ? s + 47 : 75);
    }

    public String formatRut(String rut) {
        rut.replace("-","");
        rut.replace(".","");

        int num = Integer.parseInt(rut.substring(0,rut.length()-1));

        DecimalFormat df = new DecimalFormat("##.###.###");

        String fRut = df.format(num);

        fRut.concat("-");

        fRut.concat(rut.substring(rut.length()-1));

        return fRut;
    }

    public void saveUser() {

        FacesContext context = FacesContext.getCurrentInstance();

        if(selectedUser.getUsername().trim().equals("")) {
            userNameError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar 'Nombre de usuario'"));
        }
        else {
            userNameError = "";
        }

        if(selectedUser.getName().trim().equals("")) {
            nameError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar nombre"));
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
        else {
            rutError = "";
        }

        if(newPass1.trim().equals("")) {
            passwordError = "ui-state-error";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar una contraseña"));
        }
        else {
            passwordError = "";
        }

        if(!userNameError.concat(nameError).concat(rutError).concat(passwordError).trim().equals("")) {
            return;
        }

        try {
            selectedUser.setProfiles(selectedUserProfileModel.getTarget());


            if(selectedUser.getIdUser() == -1) {
                authenticationManager.createUserPassword(selectedUser,selectedUser.getUsername(),newPass1);
                userManager.createUser(selectedUser);
            }
            else {
                userManager.updateUser(selectedUser);
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



    public void changePass(){
        try {
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
