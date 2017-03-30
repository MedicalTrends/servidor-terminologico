package cl.minsal.semantikos.designer_modeler.auth;

import cl.minsal.semantikos.kernel.auth.AuthenticationManager;
import cl.minsal.semantikos.kernel.auth.PasswordChangeException;
import cl.minsal.semantikos.kernel.auth.UserManager;
import cl.minsal.semantikos.kernel.components.InstitutionManager;
import cl.minsal.semantikos.kernel.util.StringUtils;
import cl.minsal.semantikos.model.businessrules.UserCreationBRInterface;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.User;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BluePrints Developer on 14-07-2016.
 */

@ManagedBean(name = "usersBrowser")
@ViewScoped
public class UsersBroswerBean {

    static private final Logger logger = LoggerFactory.getLogger(UsersBroswerBean.class);

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

    //Inicializacion del Bean
    @PostConstruct
    protected void initialize() throws ParseException {

    }

    public void newUser() {

        selectedUser = new User();
        selectedUser.setIdUser(-1);

        ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();

        try {
            eContext.redirect(eContext.getRequestContextPath() + "/views/users/userEdit.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {

        this.selectedUser = userManager.getUser(selectedUser.getIdUser());

        ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();

        try {
            eContext.redirect(eContext.getRequestContextPath() + "/views/users/userEdit.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<User> getAllUsers(){

        //if(allUsers==null) {
            allUsers = userManager.getAllUsers();
        //}

        return allUsers;
    }


    public void formatRut() {

        if(!selectedUser.getRut().trim().isEmpty()) {
            selectedUser.setRut(StringUtils.formatRut(selectedUser.getRut()));
        }

    }

    public Profile getProfileById(long profileId){
        return userManager.getProfileById(profileId);

    }

    public void unlockUser(){
        userManager.unlockUser(selectedUser.getUsername());
    }


}