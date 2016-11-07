package cl.minsal.semantikos.designer_modeler.auth;

import cl.minsal.semantikos.kernel.auth.AuthenticationManager;
import cl.minsal.semantikos.kernel.auth.PasswordChangeException;
import cl.minsal.semantikos.kernel.auth.UserManager;
import cl.minsal.semantikos.model.Profile;
import cl.minsal.semantikos.model.User;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

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

    DualListModel<Profile> selectedUserProfileModel;


    String newPass1;
    String newPass2;

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

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {

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

        if(allUsers==null)
            allUsers = userManager.getAllUsers();

        return allUsers;
    }


    public void newUser(){
        selectedUser = new User();
        selectedUser.setIdUser(-1);
        updateAvailableProfiles(selectedUser);
    }

    public void saveUser(){

        try {
            selectedUser.setProfiles(selectedUserProfileModel.getTarget());


            if(selectedUser.getIdUser() == -1)
                userManager.createUser(selectedUser);
            else
                userManager.updateUser(selectedUser);


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

}
