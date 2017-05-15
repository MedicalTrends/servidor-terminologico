package cl.minsal.semantikos.beans.session;


import cl.minsal.semantikos.kernel.componentsweb.TimeOutWeb;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * Created by des01c7 on 15-12-16.
 */

@ManagedBean(name = "timeOutSessionBean")
@RequestScoped
public class TimeOutSessionBean {

    @EJB
    private TimeOutWeb timeOutWeb;

    private static int timeOut;

    @PostConstruct
    public void init(){
        timeOut=timeOutWeb.getTimeOut();
    }

    public void redirectSession() throws IOException {
        ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
        eContext.redirect(eContext.getRequestContextPath());
        return;
    }

    public int getTimeOut() {
        return (1000 * (timeOut-1));
    }

}
