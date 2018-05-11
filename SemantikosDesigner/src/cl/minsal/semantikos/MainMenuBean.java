package cl.minsal.semantikos;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.kernel.componentsweb.ViewAugmenter;
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

@ManagedBean(name = "mainMenuBean", eager = false)
@ApplicationScoped
public class MainMenuBean implements Serializable {

    static final Logger logger = LoggerFactory.getLogger(MainMenuBean.class);

    private List<Category> categories;

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

        institutionFactory = institutionManager.getInstitutionFactory();

        InstitutionFactory.getInstance().setInstitutions(institutionFactory.getInstitutions());

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
}

