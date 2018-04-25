package cl.minsal.semantikos.category;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.CategoryManager;
import cl.minsal.semantikos.kernel.componentsweb.ViewAugmenter;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.modelweb.RelationshipDefinitionWeb;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by des01c7 on 14-10-16.
 */
@ManagedBean(name = "categoryBean", eager = true)
@ApplicationScoped
public class CategoryBean {

    //@EJB
    ViewAugmenter viewAugmenter = (ViewAugmenter) ServiceLocator.getInstance().getService(ViewAugmenter.class);

    //@EJB
    CategoryManager categoryManager = (CategoryManager) ServiceLocator.getInstance().getService(CategoryManager.class);

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

}
