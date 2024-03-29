package cl.minsal.semantikos.kernel.components;


import cl.minsal.semantikos.kernel.daos.CategoryDAO;
import cl.minsal.semantikos.kernel.daos.RelationshipDAO;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.kernel.businessrules.CategoryCreationBR;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.categories.CategoryFactory;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.users.User;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.EMPTY_LIST;

/**
 * @author Andrés Farías on 27-05-16.
 */
@Stateless
//@DeclareRoles("Administrador")
//@SecurityDomain("SemantikosDomain")
//@PermitAll
public class CategoryManagerImpl implements CategoryManager {

    private static final Logger logger = LoggerFactory.getLogger(CategoryManagerImpl.class);

    @EJB
    private CategoryDAO categoryDAO;

    @EJB
    private RelationshipDAO relationshipDAO;

    @EJB
    private DescriptionManager descriptionManager;

    @Resource
    private EJBContext context;

    @Override
    public void addAttribute(RelationshipDefinition attributeCategory, int idCategory) {

    }

    @Override
    public Category createCategory(Category category, User user) {

        logger.debug("Persistiendo la categoría: " + category);

        /* Se validan las reglas de negocio */
        new CategoryCreationBR().applyRules(category, user);

        /* Se persiste la categoría */
        categoryDAO.persist(category);

        /* Se persisten sus definiciones de relaciones */
        for (RelationshipDefinition relationshipDefinition : category.getRelationshipDefinitions()) {
            relationshipDAO.persist(relationshipDefinition);
        }

        logger.debug("Categoría persistida: " + category);

        /* Se retorna */
        return category;
    }

    @Override
    public ConceptSMTK categoryContains(Category category, String term) {

        List<Description> descriptions = descriptionManager.searchDescriptionsByTerm(term, Arrays.asList(category), EMPTY_LIST);

        /* Si la búsqueda resultó con al menos un término vigente, entonces si contiene */
        for (Description description : descriptions) {
            if (description.isValid()) {
                return description.getConceptSMTK();
            }
        }
        return null;
    }

    @Override
    public Category getCategoryById(long id) {
        return CategoryFactory.getInstance().findCategoryById(id);
        //return categoryDAO.getCategoryById(id);
    }

    @Override
    public Category getCategoryByName(String name) throws IllegalArgumentException {
        Category category= CategoryFactory.getInstance().findCategoryByName(name);
        if(category!=null) {
            return category;
        }
        else {
            throw new IllegalArgumentException("No existe una categoría de nombre: "+name);
        }
        //return this.categoryDAO.getCategoryByName(name);
    }

    @Override
    //@RolesAllowed("Administrador")
    //@PermitAll
    public List<Category> getCategories() {

        System.out.println(context.getCallerPrincipal().getName());

        logger.debug("Recuperando todas las categorías.");

        List<Category> categories = CategoryFactory.getInstance().getCategories();
        //List<Category> categories = categoryDAO.getAllCategories();
        logger.debug(categories.size() + "categorías recuperadas.");

        return categories;
    }

    @Override
    public List<Category> getRelatedCategories(Category category) {
        return categoryDAO.getRelatedCategories(category);
    }

    @Override
    public List<Category> findCategories(List<String> categoriesNames) {

        List<Category> res = new ArrayList<>();
        for ( String categoryName : categoriesNames ) {
            logger.debug("CategoryManager.findCategories: buscando '" + categoryName + "'");

            /* Las categorias de nombre NULL o vacias se ignoran simplemente, no generan error */
            if(categoryName == null || categoryName.trim().equals("")){
                continue;
            }

            Category found = this.getCategoryByName(categoryName.trim());
            if ( found != null ) {
                res.add(found);
            } else {
                throw new IllegalArgumentException("Categoria no encontrada: " + categoryName);
            }
        }

        return res;
    }

    @Override
    public CategoryFactory getCategoryFactory() {
        return CategoryFactory.getInstance();
    }

}
