package cl.minsal.semantikos.session;


import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.componentsweb.TimeOutWeb;
import cl.minsal.semantikos.model.system.SystemFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by des01c7 on 15-12-16.
 */

@ManagedBean(name = "timeOutSessionBean")
@RequestScoped
public class TimeOutSessionBean {

    //@EJB
    private TimeOutWeb timeOutWeb= (TimeOutWeb) ServiceLocator.getInstance().getService(TimeOutWeb.class);

    private static int timeOut;

    @PostConstruct
    public void init() {
        //timeOut = timeOutWeb.getTimeOut();
        timeOut = SystemFactory.getTimeout();
    }

    public void redirectSession() throws IOException {
        ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) eContext.getRequest();
        /*Se invalida la sesión http*/
        request.getSession().invalidate();
        /*Se invalida la sesión en el servidor*/
        eContext.invalidateSession();
        eContext.redirect("/");
        return;
    }

    public int getTimeOut() {
        return (1000 * (timeOut-1));
    }

}
