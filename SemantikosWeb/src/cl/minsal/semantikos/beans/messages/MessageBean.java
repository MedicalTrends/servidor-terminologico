package cl.minsal.semantikos.beans.messages;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 * @author Gustavo Punucura
 */
@ManagedBean(name = "messageBean")
@ViewScoped
public class MessageBean {

    /**
     * Método encargado de agregar mensajes de error en la vista.
     *
     * @param msg mensaje que se muestra.
     */
    public void messageError(String msg) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
    }
    /**
     * Método encargado de agregar mensajes de éxito o informativos en la vista.
     *
     * @param title Título del mensaje.
     * @param msg Mensaje que se muestra.
     */
    public void messageSuccess(String title,String msg) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,title, msg));
    }

}
