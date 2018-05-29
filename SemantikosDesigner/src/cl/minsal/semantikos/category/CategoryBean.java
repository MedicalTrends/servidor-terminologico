package cl.minsal.semantikos.category;

import cl.minsal.semantikos.MainMenuBean;
import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.CategoryManager;
import cl.minsal.semantikos.kernel.components.HelperTablesManager;
import cl.minsal.semantikos.kernel.componentsweb.ViewAugmenter;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.helpertables.HelperTable;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;
import cl.minsal.semantikos.model.relationships.RelationshipAttributeDefinition;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.modelweb.ConceptSMTKWeb;
import cl.minsal.semantikos.modelweb.RelationshipAttributeDefinitionWeb;
import cl.minsal.semantikos.modelweb.RelationshipDefinitionWeb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static cl.minsal.semantikos.model.relationships.SnomedCTRelationship.ES_UN_MAPEO_DE;

/**
 * Created by des01c7 on 14-10-16.
 */
@ManagedBean(name = "categoryBean")
@SessionScoped
public class CategoryBean {

    //@EJB
    ViewAugmenter viewAugmenter = (ViewAugmenter) ServiceLocator.getInstance().getService(ViewAugmenter.class);

    //@EJB
    CategoryManager categoryManager = (CategoryManager) ServiceLocator.getInstance().getService(CategoryManager.class);

    //@EJB
    HelperTablesManager helperTableManager = (HelperTablesManager) ServiceLocator.getInstance().getService(HelperTablesManager.class);

    static final Logger logger = LoggerFactory.getLogger(CategoryBean.class);

    public static Map<Long, List<RelationshipDefinitionWeb> > relationshipDefinitionsWeb = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        for (Category category : categoryManager.getCategories()) {
            List<RelationshipDefinitionWeb> relationshipDefinitionsWeb = new ArrayList<>();
            for (RelationshipDefinition relationshipDefinition : category.getRelationshipDefinitions()) {
                relationshipDefinitionsWeb.add(viewAugmenter.augmentRelationshipDefinition(category, relationshipDefinition));
            }
            Collections.sort(relationshipDefinitionsWeb);
            this.relationshipDefinitionsWeb.put(category.getId(), relationshipDefinitionsWeb);
        }
    }

    public List<RelationshipDefinitionWeb> getRelationshipDefinitionsByCategory(Category category) {
        return relationshipDefinitionsWeb.get(category.getId());
    }

    public RelationshipDefinitionWeb getRelationshipDefinitionById(Category category, RelationshipDefinition relationshipDefinition) {
        for (RelationshipDefinitionWeb relationshipDefinitionWeb : getRelationshipDefinitionsByCategory(category)) {
            if(relationshipDefinition.getId() == relationshipDefinitionWeb.getId()) {
                return relationshipDefinitionWeb;
            }
        }
        return null;
    }

    public List<RelationshipDefinitionWeb> getCrossmapTypeDefinitionsByCategory(Category category) {

        List<RelationshipDefinitionWeb> relationshipDefinitions = new ArrayList();

        for (RelationshipDefinitionWeb relationshipDefinitionWeb : relationshipDefinitionsWeb.get(category.getId())) {
            if(relationshipDefinitionWeb.getTargetDefinition().isCrossMapType()) {
                relationshipDefinitions.add(relationshipDefinitionWeb);
            }
        }

        return relationshipDefinitions;
    }

    /**
     * Este método retorna una lista ordenada de definiciones propias de semantikos.
     *
     * @return Una lista ordenada de las relaciones de la categoría.
     */
    public List<RelationshipDefinitionWeb> getSMTKDefinitionsByCategory(Category category) {

        List<RelationshipDefinitionWeb> smtkRelationshipDefinitions = new ArrayList<>();

        for (RelationshipDefinitionWeb relationshipDefinition : getRelationshipDefinitionsByCategory(category)) {
            if(!relationshipDefinition.getTargetDefinition().isSnomedCTType() && !relationshipDefinition.getTargetDefinition().isCrossMapType()) {
                smtkRelationshipDefinitions.add(relationshipDefinition);
            }
        }
        return smtkRelationshipDefinitions;
    }

    public List<RelationshipDefinitionWeb> getSnomedDefinitionsByCategory(Category category) {

        List<RelationshipDefinitionWeb> snomedRelationshipDefinitions = new ArrayList<>();

        for (RelationshipDefinitionWeb relationshipDefinition : getRelationshipDefinitionsByCategory(category)) {
            if(relationshipDefinition.getTargetDefinition().isSnomedCTType()) {
                snomedRelationshipDefinitions.add(relationshipDefinition);
            }
        }
        return snomedRelationshipDefinitions;
    }

    public void refreshDefinitions(Category category) {
        if(category == null) {
            return;
        }
        for (RelationshipDefinitionWeb relationshipDefinitionWeb : getRelationshipDefinitionsByCategory(category)) {
            relationshipDefinitionWeb.setMultiplicitySatisfied(true);
        }
    }

    public void augmentRelationshipPlaceholders(Category category, ConceptSMTKWeb concept, Map<Long, Relationship> relationshipPlaceholders) {

        for (RelationshipDefinitionWeb relationshipDefinitionWeb : getRelationshipDefinitionsByCategory(category)) {

            if (!relationshipDefinitionWeb.getRelationshipAttributeDefinitions().isEmpty() && relationshipDefinitionWeb.getMultiplicity().isCollection()) {

                Relationship r;

                r = new Relationship(concept, null, relationshipDefinitionWeb, new ArrayList<RelationshipAttribute>(), null);
                relationshipPlaceholders.put(relationshipDefinitionWeb.getId(), r);

                for (RelationshipAttributeDefinitionWeb relAttrDefWeb : relationshipDefinitionWeb.getRelationshipAttributeDefinitionWebs()) {

                    if(relAttrDefWeb.getDefaultValue() != null) {
                        RelationshipAttribute ra = new RelationshipAttribute(relAttrDefWeb.getRelationshipAttributeDefinition(), r, relAttrDefWeb.getDefaultValue());
                        r.getRelationshipAttributes().add(ra);
                    }
                }

                // Si esta definición de relación es de tipo CROSSMAP, Se agrega el atributo tipo de relacion = "ES_UN_MAPEO_DE" (por defecto)
                if (relationshipDefinitionWeb.getTargetDefinition().isCrossMapType()) {
                    for (RelationshipAttributeDefinition attDef : relationshipDefinitionWeb.getRelationshipAttributeDefinitions()) {
                        if (attDef.isRelationshipTypeAttribute()) {
                            Relationship rel = relationshipPlaceholders.get(relationshipDefinitionWeb.getId());
                            HelperTable helperTable = (HelperTable) attDef.getTargetDefinition();

                            List<HelperTableRow> relationshipTypes = helperTableManager.searchRows(helperTable, ES_UN_MAPEO_DE);

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
