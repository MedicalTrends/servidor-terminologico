package cl.minsal.semantikos.designer_modeler.designer;

import cl.minsal.semantikos.kernel.auth.UserManager;

import javax.faces.bean.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;

/**
 * Created by root on 22-03-17.
 */
@ManagedBean(name = "accountActivation")
@RequestScoped
public class AccountActivation {

    @ManagedProperty(value="#{param.key}")
    private String key;

    private boolean valid;

    @EJB
    UserManager userManager;

    @PostConstruct
    public void init() {
        valid = check(key); // And auto-login if valid?
    }

    public boolean check(String key) {
        return userManager.activateAccount(key);
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
