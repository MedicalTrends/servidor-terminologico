package cl.minsal.semantikos.users;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.UserManager;
import cl.minsal.semantikos.model.audit.ConceptAuditAction;
import cl.minsal.semantikos.model.audit.InstitutionAuditAction;
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
import java.util.List;

import static cl.minsal.semantikos.model.audit.AuditActionType.*;

/**
 * Created by BluePrints Developer on 14-07-2016.
 */

@ManagedBean(name = "usersBrowser")
@ViewScoped
public class UsersBroswerBean {

    static private final Logger logger = LoggerFactory.getLogger(UsersBroswerBean.class);

    //@EJB
    UserManager userManager = (UserManager) ServiceLocator.getInstance().getService(UserManager.class);

    User selectedUser;

    List<User> allUsers;

    List<User> filteredUsers;

    //Inicializacion del Bean
    @PostConstruct
    protected void initialize() {
        RequestContext reqCtx = RequestContext.getCurrentInstance();
        reqCtx.execute("PF('usersTable').filter();");
    }

    public void newUser() {

        selectedUser = new User();
        selectedUser.setId(-1);

        ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();

        try {
            eContext.redirect(eContext.getRequestContextPath() + "/views/users/userEdit.xhtml?idUser=0");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {

        this.selectedUser = userManager.getUser(selectedUser.getId());
    }

    public List<User> getAllUsers(){

        if(allUsers==null) {
            allUsers = userManager.getAllUsers();
        }

        return allUsers;
    }

    public List<User> getFilteredUsers() {
        return filteredUsers;
    }

    public void setFilteredUsers(List<User> filteredUsers) {
        this.filteredUsers = filteredUsers;
    }


    public Profile getProfileById(long profileId){
        return userManager.getProfileById(profileId);

    }


}
