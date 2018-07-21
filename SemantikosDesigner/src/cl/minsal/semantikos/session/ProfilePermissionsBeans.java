package cl.minsal.semantikos.session;

import cl.minsal.semantikos.users.AuthenticationBean;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.ProfileFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/**
 * Created by des01c7 on 03-02-17.
 */
@ManagedBean(name = "profilePermissionsBeans")
@SessionScoped
public class ProfilePermissionsBeans {

    @ManagedProperty(value = "#{authenticationBean}")
    private AuthenticationBean authenticationBean;

    @PostConstruct
    public void init(){

    }

    public boolean isModeler() {
        for (Profile profile : authenticationBean.getLoggedUser().getProfiles()) {
            if(profile.equals(ProfileFactory.MODELER_PROFILE)){
                return true;
            }
        }
        return false;
    }

    public boolean isWsConsumer() {
        for (Profile profile : authenticationBean.getLoggedUser().getProfiles()) {
            if(profile.equals(ProfileFactory.WS_CONSUMER_PROFILE)){
                return true;
            }
        }
        return false;
    }

    public boolean isAdmin() {
        for (Profile profile : authenticationBean.getLoggedUser().getProfiles()) {
            if(profile.equals(ProfileFactory.ADMINISTRATOR_PROFILE)){
                return true;
            }
        }
        return false;
    }

    public boolean isRefSetAdmin() {
        for (Profile profile : authenticationBean.getLoggedUser().getProfiles()) {
            if(profile.equals(ProfileFactory.REFSET_ADMIN_PROFILE)){
                return true;
            }
        }
        return false;
    }

    public boolean isDesigner() {
        for (Profile profile : authenticationBean.getLoggedUser().getProfiles()) {
            if(profile.equals(ProfileFactory.DESIGNER_PROFILE)){
                return true;
            }
        }
        return false;
    }

    public boolean permissionsBy(Category category) {
        if(category == null) {
            return false;
        }
        if(!category.isRestriction() && isDesigner() ){
            return true;
        }
        if(isModeler()){
            return true;
        }
        return false;
    }

    public void setAuthenticationBean(AuthenticationBean authenticationBean) {
        this.authenticationBean = authenticationBean;
    }
}
