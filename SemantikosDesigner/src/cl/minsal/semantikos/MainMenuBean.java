package cl.minsal.semantikos;

import cl.minsal.semantikos.category.CategoryBean;
import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.kernel.componentsweb.ViewAugmenter;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.descriptions.DescriptionType;
import cl.minsal.semantikos.model.descriptions.DescriptionTypeFactory;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;
import cl.minsal.semantikos.model.relationships.RelationshipAttributeDefinition;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.users.InstitutionFactory;
import cl.minsal.semantikos.model.users.UserFactory;
import cl.minsal.semantikos.modelweb.ConceptSMTKWeb;
import cl.minsal.semantikos.modelweb.RelationshipAttributeDefinitionWeb;
import cl.minsal.semantikos.modelweb.RelationshipDefinitionWeb;
import cl.minsal.semantikos.session.ProfilePermissionsBeans;
import org.primefaces.event.MenuActionEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.menu.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cl.minsal.semantikos.model.relationships.SnomedCTRelationship.ES_UN_MAPEO_DE;


/**
 * Created by diego on 26/06/2016.
 */

@ManagedBean(name = "mainMenuBean", eager = true)
@ApplicationScoped
public class MainMenuBean implements Serializable {

    static final Logger logger = LoggerFactory.getLogger(MainMenuBean.class);

    private List<Category> categories;

    private String name;

    private transient MenuModel mainMenuModel;

    private transient MenuModel categoryMenuModel;

    //@EJB
    private CategoryManager categoryManager = (CategoryManager) ServiceLocator.getInstance().getService(CategoryManager.class);

    private TagSMTKManager tagSMTKManager = (TagSMTKManager) ServiceLocator.getInstance().getService(TagSMTKManager.class);

    private DescriptionManager descriptionManager = (DescriptionManager) ServiceLocator.getInstance().getService(DescriptionManager.class);

    HelperTablesManager helperTablesManager = (HelperTablesManager) ServiceLocator.getInstance().getService(HelperTablesManager.class);

    ViewAugmenter viewAugmenter = (ViewAugmenter) ServiceLocator.getInstance().getService(ViewAugmenter.class);

    UserManager userManager = (UserManager) ServiceLocator.getInstance().getService(UserManager.class);

    InstitutionManager institutionManager = (InstitutionManager) ServiceLocator.getInstance().getService(InstitutionManager.class);

    private TagSMTKFactory tagSMTKFactory;

    private DescriptionTypeFactory descriptionTypeFactory;

    private Map<Long, RelationshipDefinitionWeb> relationshipDefinitiosnWeb = new HashMap<>();

    private UserFactory userFactory;

    private InstitutionFactory institutionFactory;

    private transient TreeNode root;

    @PostConstruct
    public void init() {

        categories =  categoryManager.getCategories();

        root = new DefaultTreeNode(new Category(-1, "Dispositivo", "Dispositivo", false, "black", TagSMTKFactory.getInstance().findTagSMTKByName("objeto físico")));

        mapConcepts(categories, root, true);

        tagSMTKFactory = tagSMTKManager.getTagSMTKFactory();

        descriptionTypeFactory = descriptionManager.getDescriptionTypeFactory();

        userFactory = userManager.getUserFactory();

        TagSMTKFactory.getInstance().setTagsSMTK(tagSMTKFactory.getTagsSMTK());
        TagSMTKFactory.getInstance().setTagsSMTKByName(tagSMTKFactory.getTagsSMTKByName());

        DescriptionTypeFactory.getInstance().setDescriptionTypes(descriptionTypeFactory.getDescriptionTypes());

        UserFactory.getInstance().setUsersById(userFactory.getUsersById());

    }

    public TreeNode mapConcepts(List<Category> categories, TreeNode treeNode, boolean expanded) {

        Category devicesCategory = new Category(-1, "Dispositivo", "Dispositivo", false, "black", TagSMTKFactory.getInstance().findTagSMTKByName("objeto físico"));

        TreeNode devicesTreeNode = null;

        boolean flagDevices = false;

        Category drugsCategory = new Category(-1, "Fármacos", "Fármacos", false, "black", TagSMTKFactory.getInstance().findTagSMTKByName("producto"));

        TreeNode drugsTreeNode = null;

        boolean flagDrugs = false;

        for (Category category : categories) {

            TreeNode childTreeNode = new DefaultTreeNode(category, treeNode);

            if(category.getName().contains("Dispositivo")) {

                if(!flagDevices) {
                    devicesTreeNode = new DefaultTreeNode(devicesCategory, treeNode);
                    flagDevices = true;
                }

                devicesTreeNode.getChildren().add(childTreeNode);
            }
            else if(category.getName().contains("Fármacos")) {

                if(!flagDrugs) {
                    drugsTreeNode = new DefaultTreeNode(drugsCategory, treeNode);
                    flagDrugs = true;
                }

                drugsTreeNode.getChildren().add(childTreeNode);
            }
            else {
                treeNode.getChildren().add(childTreeNode);
            }

        }

        return root;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TagSMTKFactory getTagSMTKFactory() {
        return tagSMTKFactory;
    }

    public void setTagSMTKFactory(TagSMTKFactory tagSMTKFactory) {
        this.tagSMTKFactory = tagSMTKFactory;
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

    public MenuModel getMainMenuModel() {
        return mainMenuModel;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

