package cl.minsal.semantikos.view.tests.descriptions;

import cl.minsal.semantikos.clients.RemoteEJBClientFactory;
import cl.minsal.semantikos.kernel.businessrules.ConceptDefinitionalGradeBR;
import cl.minsal.semantikos.users.AuthenticationBean;
import cl.minsal.semantikos.kernel.components.DescriptionManager;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 * @author Andrés Farías on 11/21/16.
 */

@ManagedBean(name = "descriptionTestsBean")
@ViewScoped
public class DescriptionBean {

    static final Logger logger = LoggerFactory.getLogger(DescriptionBean.class);

    //@EJB
    private DescriptionManager descriptionManager = (DescriptionManager) RemoteEJBClientFactory.getInstance().getManager(DescriptionManager.class);;

    /** El valor de negocio del <em>DESCRIPTION_ID</em> de la descripción */
    private String descriptionId;

    /** El objeto de negocio cuyo <em>DESCRIPTION_ID</em> corresponde a <code>descriptionId</code>. */
    private Description description;

    /** Bean de autenticacion para determinar el usuario conectado */
    @ManagedProperty(value = "#{authenticationBean}")
    private AuthenticationBean authenticationBean;

    /**
     * Constructor para inicializar por defecto ciertos valores.
     */
    public DescriptionBean() {
        /* Se usa un valor existente en la base de datos. Esto podria cambiar */
        this.descriptionId = "f9b4c7c5-5";
    }

    public String getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(String descriptionId) {
        this.descriptionId = descriptionId;
    }

    public AuthenticationBean getAuthenticationBean() {
        return authenticationBean;
    }

    public void setAuthenticationBean(AuthenticationBean authenticationBean) {
        this.authenticationBean = authenticationBean;
    }

    public Description getDescription() {
        return this.description;
    }

    /**
     * Este método de acción es responsable de incrementar el uso de una descripcion.
     */
    public void incrementUse() {

        logger.info("Se incrementara el uso de la descripcion DESCRIPTION_ID= " + this.descriptionId);
        FacesContext context = FacesContext.getCurrentInstance();


        User loggedUser = authenticationBean.getLoggedUser();
        try {
            this.description = descriptionManager.incrementDescriptionHits(descriptionId);
            logger.info("Descripción ID=" + descriptionId +  " incrementada " + description.getUses() + " usos!");
        } catch (EJBException bre) {
            context.addMessage(null, new FacesMessage("Problema!", bre.getMessage()));
            return;
        }

        /* Se avisa */
        context.addMessage(null, new FacesMessage("Éxito!", "Descripción con " + description.getUses() + " usos!"));
    }

}
