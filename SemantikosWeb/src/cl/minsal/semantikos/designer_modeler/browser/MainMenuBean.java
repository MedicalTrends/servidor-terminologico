package cl.minsal.semantikos.designer_modeler.browser;

import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.categories.Category;
import org.primefaces.event.MenuActionEvent;
import org.primefaces.model.menu.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;


/**
 * Created by diego on 26/06/2016.
 */

@ManagedBean(name = "mainMenuBean")
@ViewScoped
public class MainMenuBean implements Serializable {

    static final Logger logger = LoggerFactory.getLogger(MainMenuBean.class);

    private List<Category> categories;

    private MenuModel categoryMenuModel;

    @EJB
    private CategoryManager categoryManager;


    @PostConstruct
    public void init() {

        categories = categoryManager.getCategories();

        categoryMenuModel = new DefaultMenuModel();

        //First submenu
        DefaultSubMenu categorySubmenu = new DefaultSubMenu("Categorías");
        categorySubmenu.setIcon("fa fa-list-alt");
        categorySubmenu.setId("rm_categories");

        for (Category category : categories) {
            DefaultMenuItem item = new DefaultMenuItem(category.getName());
            item.setUrl("/views/browser/generalBrowser.xhtml?idCategory="+category.getId());
            //item.setUrl("#");
            item.setIcon("fa fa-list-alt");
            item.setId("rm_"+category.getName());
            //item.setCommand("#{mainMenuBean.redirect}");
            //item.setParam("idCategory",category.getId());
            //item.setAjax(true);
            item.setUpdate("mainContent");
            categorySubmenu.addElement(item);
        }

        categoryMenuModel.addElement(categorySubmenu);

    }

    public void redirect(ActionEvent event) throws IOException {
        // Si el concepto está persistido, invalidarlo

        MenuItem menuItem = ((MenuActionEvent) event).getMenuItem();

        ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();

        Long idCategory = Long.parseLong(String.valueOf(menuItem.getParams().get("idCategory").get(0)));

        eContext.redirect(eContext.getRequestContextPath() + "/views/browser/generalBrowser.xhtml?idCategory="+idCategory);
    }



    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public MenuModel getCategoryMenuModel() {
        return categoryMenuModel;
    }
}

