package cl.minsal.semantikos.session;


import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.QueryManager;
import cl.minsal.semantikos.kernel.componentsweb.TimeOutWeb;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        request.getSession().setMaxInactiveInterval(timeOutWeb.getTimeOut());
        timeOut=timeOutWeb.getTimeOut();

    }

    public void redirectSession() throws IOException {
        ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
        eContext.redirect(eContext.getRequestContextPath());
        return;
    }

    public int getTimeOut() {
        //return (1000 * (timeOut-1));
        return timeOut;
    }

    public void logout() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        // Invalidate current HTTP session.
        // Will call JAAS LoginModule logout() method
        request.getSession().invalidate();

        // Redirect the user to the secure web page.
        // Since the user is now logged out the
        // authentication form will be shown
        response.sendRedirect(request.getContextPath() + "/views/home.xhtml");
    }

}
