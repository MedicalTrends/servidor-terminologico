package cl.minsal.semantikos.users;

import cl.minsal.semantikos.Constants;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.AuthenticationManager;
import cl.minsal.semantikos.kernel.components.InstitutionManager;
import cl.minsal.semantikos.kernel.components.UserManager;
import cl.minsal.semantikos.kernel.componentsweb.TimeOutWeb;
import cl.minsal.semantikos.model.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Francisco Mendez on 19-05-2016.
 */
@ManagedBean(name = "authenticationBean", eager = true)
@SessionScoped
public class AuthenticationBean {

    static public final String AUTH_KEY = "bp.session.user";

    static private final Logger logger = LoggerFactory.getLogger(AuthenticationBean.class);

    private String email;
    private String password;

    private String emailError = "";

    private String passwordError = "";

    private User loggedUser;

    //@EJB(name = "AuthenticationManagerEJB")
    AuthenticationManager authenticationManager = (AuthenticationManager) ServiceLocator.getInstance().getService(AuthenticationManager.class);

    //@EJB
    TimeOutWeb timeOutWeb = (TimeOutWeb) ServiceLocator.getInstance().getService(TimeOutWeb.class);


    public boolean isLoggedIn() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(AUTH_KEY) != null;
    }

    public void warn() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!", "Watch out for PrimeFaces."));
    }

    public void login() {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        request.getSession().setMaxInactiveInterval(timeOutWeb.getTimeOut());
        try {
            //valida user y pass
            if(email.trim().equals("")) {
                emailError = "ui-state-error";
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar 'e-mail'"));
            }
            else {
                emailError = "";
            }

            if(password.trim().equals("")) {
                passwordError = "ui-state-error";
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe ingresar 'Contraseña'"));
            }
            else {
                passwordError = "";
            }

            if(!emailError.concat(passwordError).trim().isEmpty()) {
                return;
            }

            if(!isValidEmailAddress(email)) {
                emailError = "ui-state-error";
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El formato del 'e-mail' no es válido"));
            }
            else {
                emailError = "";
            }

            if(!emailError.concat(passwordError).trim().isEmpty()) {
                return;
            }


            //authenticationManager.authenticate(email,password,request);
            authenticationManager.authenticate(email,password);

            //quitar password de la memoria
            password=null;

            //poner datos de usuario en sesión
            loggedUser = authenticationManager.getUserDetails(email);
            ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
            eContext.getSessionMap().put(AUTH_KEY, email);

            //redirigir a pagina de inicio
            eContext.redirect(eContext.getRequestContextPath() + Constants.HOME_PAGE);

            logger.info("Usuario [{}] ha iniciado sesión.", email);


        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error intentando redirigir usuario a página de Inicio.", e.getMessage()));
            logger.error("Error intentando redirigir usuario a página de inicio {}", Constants.HOME_PAGE , e);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al Ingresar!", e.getMessage()));
            logger.error("Error trying to login", e);
        }
    }

    private boolean canLogin(User loggedUser) {
        return !(loggedUser == null || loggedUser.getUsername() == null);
    }

    public void logout() {

        logger.info("Usuario: " + email + " ha cerrado su sesión.");

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.getSessionMap().remove(AUTH_KEY);

        email = null;
        password = null;
        loggedUser = null;

        try {
            //context.redirect(context.getRequestContextPath() + "/" +Constants.VIEWS_FOLDER+ "/" + Constants.LOGIN_PAGE );
            context.redirect(context.getRequestContextPath() + Constants.LOGIN_PAGE );
        } catch (IOException e) {
            logger.error("Error en logout", e);
        }
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public void testException() {
        logger.debug("Throwing test exception");
        throw new RuntimeException("This is a test exception");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void refreshLoggedUser(User user) {
        if(loggedUser.equals(user)) {
            loggedUser = user;
        }
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public String getEmailError() {
        return emailError;
    }

    public void setEmailError(String emailError) {
        this.emailError = emailError;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }


}
