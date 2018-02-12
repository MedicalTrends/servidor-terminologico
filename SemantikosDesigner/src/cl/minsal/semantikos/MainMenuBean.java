package cl.minsal.semantikos;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.kernel.componentsweb.ViewAugmenter;
import cl.minsal.semantikos.model.descriptions.DescriptionTypeFactory;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;
import cl.minsal.semantikos.model.relationships.RelationshipAttributeDefinition;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.users.UserFactory;
import cl.minsal.semantikos.modelweb.ConceptSMTKWeb;
import cl.minsal.semantikos.modelweb.RelationshipAttributeDefinitionWeb;
import cl.minsal.semantikos.modelweb.RelationshipDefinitionWeb;
import org.primefaces.event.MenuActionEvent;
import org.primefaces.model.menu.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
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

    private transient MenuModel mainMenuModel;

    private transient MenuModel categoryMenuModel;

    //@EJB
    private CategoryManager categoryManager = (CategoryManager) ServiceLocator.getInstance().getService(CategoryManager.class);

    private TagSMTKManager tagSMTKManager = (TagSMTKManager) ServiceLocator.getInstance().getService(TagSMTKManager.class);

    private DescriptionManager descriptionManager = (DescriptionManager) ServiceLocator.getInstance().getService(DescriptionManager.class);

    HelperTablesManager helperTablesManager = (HelperTablesManager) ServiceLocator.getInstance().getService(HelperTablesManager.class);

    ViewAugmenter viewAugmenter = (ViewAugmenter) ServiceLocator.getInstance().getService(ViewAugmenter.class);

    UserManager userManager = (UserManager) ServiceLocator.getInstance().getService(UserManager.class);

    private TagSMTKFactory tagSMTKFactory;

    private DescriptionTypeFactory descriptionTypeFactory;

    private Map<Long, RelationshipDefinitionWeb> relationshipDefinitiosnWeb = new HashMap<>();

    private UserFactory userFactory;

    @PostConstruct
    public void init() {

        categories =  categoryManager.getCategories();

        tagSMTKFactory = tagSMTKManager.getTagSMTKFactory();

        descriptionTypeFactory = descriptionManager.getDescriptionTypeFactory();

        userFactory = userManager.getUserFactory();

        TagSMTKFactory.getInstance().setTagsSMTK(tagSMTKFactory.getTagsSMTK());
        TagSMTKFactory.getInstance().setTagsSMTKByName(tagSMTKFactory.getTagsSMTKByName());

        DescriptionTypeFactory.getInstance().setDescriptionTypes(descriptionTypeFactory.getDescriptionTypes());

        UserFactory.getInstance().setUsersById(userFactory.getUsersById());

        mainMenuModel = new DefaultMenuModel();

        categoryMenuModel = new DefaultMenuModel();

        //Inicio
        DefaultMenuItem item0 = new DefaultMenuItem("Inicio");
        item0.setUrl("/views/home.xhtml");
        item0.setIcon("home");
        item0.setId("rm_home");
        item0.setUpdate("mainContent");

        mainMenuModel.addElement(item0);

        //Categorias
        DefaultSubMenu categorySubmenu = new DefaultSubMenu("Categorías");
        categorySubmenu.setIcon("fa fa-list-alt");
        categorySubmenu.setId("rm_categories");

        for (Category category : categories) {
            DefaultMenuItem item = new DefaultMenuItem(category.getName());
            item.setUrl("/views/browser/generalBrowser.xhtml?idCategory="+category.getId());
            item.setIcon("fa fa-list-alt");
            item.setId("rm_"+category.getName());
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
        item1.setUrl("/views/browser/descriptionBrowser.xhtml");
        item1.setIcon("fa fa-edit");
        item1.setId("rm_descriptions");
        item1.setUpdate("mainContent");
        otherSubmenu.addElement(item1);

        DefaultMenuItem item2 = new DefaultMenuItem("Fármacos");
        item2.setUrl("/views/browser/drugsBrowser.xhtml");
        item2.setIcon("fa fa-medkit");
        item2.setId("rm_drugs");
        item2.setUpdate("mainContent");
        otherSubmenu.addElement(item2);

        DefaultMenuItem item3 = new DefaultMenuItem("Pendientes");
        item3.setUrl("/views/browser/pendingBrowser.xhtml");
        item3.setIcon("fa fa-exclamation-triangle");
        item3.setId("rm_pending");
        item3.setUpdate("mainContent");
        otherSubmenu.addElement(item3);

        DefaultMenuItem item4 = new DefaultMenuItem("No Válidos");
        item4.setUrl("/views/browser/noValidBrowser.xhtml");
        item4.setIcon("fa fa-ban");
        item4.setId("rm_no_valids");
        item4.setUpdate("mainContent");
        otherSubmenu.addElement(item4);

        mainMenuModel.addElement(otherSubmenu);

        //Admin
        DefaultSubMenu adminSubmenu = new DefaultSubMenu("Administracion");
        adminSubmenu.setIcon("settings");
        adminSubmenu.setId("rm_admin");

        DefaultMenuItem item5 = new DefaultMenuItem("Usuarios");
        item5.setUrl("/views/users/users.xhtml");
        item5.setIcon("people");
        item5.setId("rm_users_web");
        item5.setUpdate("mainContent");
        adminSubmenu.addElement(item5);

        DefaultMenuItem item6 = new DefaultMenuItem("Establecimientos");
        item6.setUrl("/views/institutions/institutions.xhtml");
        item6.setIcon("fa fa-bank");
        item6.setId("rm_institutions");
        item6.setUpdate("mainContent");
        adminSubmenu.addElement(item6);

        DefaultMenuItem item7 = new DefaultMenuItem("RefSets");
        item7.setUrl("/views/refsets/admin_refsets.xhtml");
        item7.setIcon("fa fa-dropbox");
        item7.setId("rm_admin_refsets");
        item7.setUpdate("mainContent");
        adminSubmenu.addElement(item7);

        DefaultMenuItem item8 = new DefaultMenuItem("Tablas");
        item8.setUrl("/views/helpertables/helpertables.xhtml");
        item8.setIcon("fa fa-columns");
        item8.setId("rm_helpertables");
        item8.setUpdate("mainContent");
        adminSubmenu.addElement(item8);

        DefaultMenuItem item9 = new DefaultMenuItem("Snapshot");
        item9.setUrl("/views/snapshot/snapshot.xhtml");
        item9.setIcon("fa fa-list-alt");
        item9.setId("rm_snapshot");
        item9.setUpdate("mainContent");
        adminSubmenu.addElement(item9);

        DefaultMenuItem item10 = new DefaultMenuItem("Extracción Fármacos");
        item10.setUrl("/views/concept/extractor.xhtml");
        item10.setIcon("fa fa-file-excel-o");
        item10.setId("rm_extraction");
        item10.setUpdate("mainContent");
        adminSubmenu.addElement(item10);

        mainMenuModel.addElement(adminSubmenu);
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

    public MenuModel getMainMenuModel() {
        return mainMenuModel;
    }

    public void augmentRelationshipPlaceholders(Category category, ConceptSMTKWeb concept, Map<Long, Relationship> relationshipPlaceholders) {

        for (RelationshipDefinition relationshipDefinition : category.getRelationshipDefinitions()) {

            if (!relationshipDefinition.getRelationshipAttributeDefinitions().isEmpty() && relationshipDefinition.getMultiplicity().isCollection()) {

                if(!relationshipDefinitiosnWeb.containsKey(relationshipDefinition.getId())) {
                    relationshipDefinitiosnWeb.put(relationshipDefinition.getId(), viewAugmenter.augmentRelationshipDefinition(category, relationshipDefinition));
                }

                RelationshipDefinitionWeb relationshipDefinitionWeb = relationshipDefinitiosnWeb.get(relationshipDefinition.getId());

                Relationship r;

                r = new Relationship(concept, null, relationshipDefinition, new ArrayList<RelationshipAttribute>(), null);
                relationshipPlaceholders.put(relationshipDefinition.getId(), r);

                for (RelationshipAttributeDefinitionWeb relAttrDefWeb : relationshipDefinitionWeb.getRelationshipAttributeDefinitionWebs()) {
                    if(relAttrDefWeb.getDefaultValue()!=null) {
                        RelationshipAttribute ra = new RelationshipAttribute(relAttrDefWeb.getRelationshipAttributeDefinition(), r, relAttrDefWeb.getDefaultValue());
                        r.getRelationshipAttributes().add(ra);
                    }
                }

                // Si esta definición de relación es de tipo CROSSMAP, Se agrega el atributo tipo de relacion = "ES_UN_MAPEO_DE" (por defecto)
                if (relationshipDefinition.getTargetDefinition().isCrossMapType()) {
                    for (RelationshipAttributeDefinition attDef : relationshipDefinition.getRelationshipAttributeDefinitions()) {
                        if (attDef.isRelationshipTypeAttribute()) {
                            Relationship rel = relationshipPlaceholders.get(relationshipDefinition.getId());
                            HelperTable helperTable = (HelperTable) attDef.getTargetDefinition();

                            List<HelperTableRow> relationshipTypes = helperTablesManager.searchRows(helperTable, ES_UN_MAPEO_DE);

                            RelationshipAttribute ra;

                            if (relationshipTypes.size() == 0) {
                                logger.error("No hay datos en la tabla de TIPOS DE RELACIONES.");
                            }

                            ra = new RelationshipAttribute(attDef, rel, relationshipTypes.get(0));
                            rel.getRelationshipAttributes().add(ra);
                        }
                    }
                }
            }
        }
        //return relationshipPlaceholders;
    }

    public MenuModel getCategoryMenuModel() {
        return categoryMenuModel;
    }

    public void setCategoryMenuModel(MenuModel categoryMenuModel) {
        this.categoryMenuModel = categoryMenuModel;
    }
}

