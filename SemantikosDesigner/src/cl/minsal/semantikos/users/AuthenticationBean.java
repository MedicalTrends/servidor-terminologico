package cl.minsal.semantikos.users;

import cl.minsal.semantikos.Constants;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.kernel.componentsweb.TimeOutWeb;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.descriptions.DescriptionTypeFactory;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;
import cl.minsal.semantikos.model.users.ProfileFactory;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.model.users.UserFactory;
import cl.minsal.semantikos.session.ProfilePermissionsBeans;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static cl.minsal.semantikos.model.users.ProfileFactory.*;

/**
 * Created by Francisco Mendez on 19-05-2016.
 */
@ManagedBean(name = "authenticationBean", eager = true)
@SessionScoped
public class AuthenticationBean {

    static public final String AUTH_KEY = "cl.minsal.semantikos.session.user";

    static private final Logger logger = LoggerFactory.getLogger(AuthenticationBean.class);

    private String email;
    private String password;

    private String emailError = "";

    private String passwordError = "";

    private User loggedUser;

    private String name;

    //@EJB(name = "AuthenticationManagerEJB")
    AuthenticationManager authenticationManager = (AuthenticationManager) ServiceLocator.getInstance().getService(AuthenticationManager.class);

    //@EJB
    TimeOutWeb timeOutWeb = (TimeOutWeb) ServiceLocator.getInstance().getService(TimeOutWeb.class);

    //@EJB
    private CategoryManager categoryManager = (CategoryManager) ServiceLocator.getInstance().getService(CategoryManager.class);

    //@EJB
    private TagSMTKManager tagSMTKManager = (TagSMTKManager) ServiceLocator.getInstance().getService(TagSMTKManager.class);

    private List<Category> categories;

    private transient MenuModel mainMenuModel;

    private transient MenuModel categoryMenuModel;

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
            eContext.redirect(eContext.getRequestContextPath() + Constants.VIEWS_FOLDER + Constants.HOME_PAGE);

            initMenu();

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
        context.invalidateSession();

        email = null;
        password = null;
        loggedUser = null;

        try {
            //context.redirect(context.getRequestContextPath() + "/" +Constants.VIEWS_FOLDER+ "/" + Constants.LOGIN_PAGE );
            context.redirect(context.getRequestContextPath() + Constants.VIEWS_FOLDER + Constants.LOGIN_PAGE );
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void initMenu() {

        categories =  categoryManager.getCategories();

        mainMenuModel = new DefaultMenuModel();

        categoryMenuModel = new DefaultMenuModel();

        //Inicio
        DefaultMenuItem item0 = new DefaultMenuItem("Inicio");
        item0.setUrl("/views/home");
        item0.setIcon("home");
        item0.setId("rm_home");
        item0.setUpdate("mainContent");

        mainMenuModel.addElement(item0);

        if(loggedUser.getProfiles().contains(DESIGNER_PROFILE) || loggedUser.getProfiles().contains(MODELER_PROFILE)) {

            //Categorias
            DefaultSubMenu categorySubmenu = new DefaultSubMenu("Categorías");
            categorySubmenu.setIcon("fa fa-list-alt");
            categorySubmenu.setId("rm_categories");

            for (Category category : categories) {
                DefaultMenuItem item = new DefaultMenuItem(category.getName());
                item.setUrl("/views/concepts/"+category.getId());
                //item.setUrl("#");
                item.setIcon("fa fa-list-alt");
                item.setStyleClass("loader-trigger");
                item.setId("rm_"+category.getName());
                //item.setCommand("#{mainMenuBean.redirect}");
                //item.setParam("idCategory",category.getId());
                //item.setAjax(true);
                item.setUpdate("mainContent");
                categorySubmenu.addElement(item);
            }

            mainMenuModel.addElement(categorySubmenu);
            categoryMenuModel.addElement(categorySubmenu);

            //Otros
            DefaultSubMenu otherSubmenu = new DefaultSubMenu("Otros");
            otherSubmenu.setIcon("more");
            otherSubmenu.setId("rm_others");

            DefaultMenuItem item1 = new DefaultMenuItem("Descripciones");
            item1.setUrl("/views/descriptions");
            item1.setIcon("fa fa-edit");
            item1.setStyleClass("loader-trigger");
            item1.setId("rm_descriptions");
            item1.setUpdate("mainContent");
            otherSubmenu.addElement(item1);

            DefaultMenuItem item2 = new DefaultMenuItem("Fármacos");
            item2.setUrl("/views/drugs");
            item2.setIcon("fa fa-medkit");
            item2.setStyleClass("loader-trigger");
            item2.setId("rm_drugs");
            item2.setUpdate("mainContent");
            otherSubmenu.addElement(item2);

            DefaultMenuItem item3 = new DefaultMenuItem("Pendientes");
            item3.setUrl("/views/descriptions/pending");
            item3.setIcon("fa fa-exclamation-triangle");
            item3.setStyleClass("loader-trigger");
            item3.setId("rm_pending");
            item3.setUpdate("mainContent");
            otherSubmenu.addElement(item3);

            DefaultMenuItem item4 = new DefaultMenuItem("No Válidos");
            item4.setUrl("/views/descriptions/not-valid");
            item4.setIcon("fa fa-ban");
            item4.setStyleClass("loader-trigger");
            item4.setId("rm_no_valids");
            item4.setUpdate("mainContent");
            otherSubmenu.addElement(item4);

            mainMenuModel.addElement(otherSubmenu);
        }

        if(loggedUser.getProfiles().contains(ADMINISTRATOR_PROFILE) || loggedUser.getProfiles().contains(REFSET_ADMIN_PROFILE)) {

            //Admin
            DefaultSubMenu adminSubmenu = new DefaultSubMenu("Administracion");
            adminSubmenu.setIcon("settings");
            adminSubmenu.setId("rm_admin");

            DefaultMenuItem item5 = new DefaultMenuItem("Usuarios");
            item5.setUrl("/views/users");
            item5.setIcon("people");
            item5.setStyleClass("loader-trigger");
            item5.setId("rm_users_web");
            item5.setUpdate("mainContent");
            adminSubmenu.addElement(item5);

            DefaultMenuItem item6 = new DefaultMenuItem("Establecimientos");
            item6.setUrl("/views/institutions");
            item6.setIcon("fa fa-bank");
            item6.setStyleClass("loader-trigger");
            item6.setId("rm_institutions");
            item6.setUpdate("mainContent");
            adminSubmenu.addElement(item6);

            DefaultMenuItem item7 = new DefaultMenuItem("RefSets");
            item7.setUrl("/views/refsets");
            item7.setIcon("fa fa-dropbox");
            item7.setId("rm_admin_refsets");
            item7.setStyleClass("loader-trigger");
            item7.setUpdate("mainContent");
            adminSubmenu.addElement(item7);

            DefaultMenuItem item8 = new DefaultMenuItem("Tablas");
            item8.setUrl("/views/helpertables");
            item8.setIcon("fa fa-columns");
            item8.setId("rm_helpertables");
            item8.setStyleClass("loader-trigger");
            item8.setUpdate("mainContent");
            adminSubmenu.addElement(item8);

            DefaultMenuItem item9 = new DefaultMenuItem("Snapshot");
            item9.setUrl("/views/snapshot/snapshot.xhtml");
            item9.setStyleClass("loader-trigger");
            item9.setIcon("fa fa-list-alt");
            item9.setId("rm_snapshot");
            item9.setUpdate("mainContent");
            adminSubmenu.addElement(item9);

            DefaultMenuItem item10 = new DefaultMenuItem("Extracción Fármacos");
            item10.setUrl("/views/extract");
            item10.setIcon("fa fa-file-excel-o");
            item10.setStyleClass("loader-trigger");
            item10.setId("rm_extraction");
            item10.setUpdate("mainContent");
            adminSubmenu.addElement(item10);

            mainMenuModel.addElement(adminSubmenu);
        }

    }

    public MenuModel getMainMenuModel() {
        return mainMenuModel;
    }

    public void setMainMenuModel(MenuModel mainMenuModel) {
        this.mainMenuModel = mainMenuModel;
    }

    public List<Category> getCategoriesByName() {

        List<Category> categoriesByName = new ArrayList<>();

        if(name == null || name.isEmpty()) {
            return categories;
        }

        for (Category category : categories) {
            if(category.getName().toLowerCase().contains(name.toLowerCase())) {
                categoriesByName.add(category);
            }
        }

        return  categoriesByName;
    }

}
