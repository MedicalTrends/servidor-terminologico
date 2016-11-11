package cl.minsal.semantikos.kernel.components;


import cl.minsal.semantikos.kernel.daos.CategoryDAO;
import cl.minsal.semantikos.kernel.daos.RelationshipDAO;
import cl.minsal.semantikos.kernel.util.StringUtils;
import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.Description;
import cl.minsal.semantikos.model.User;
import cl.minsal.semantikos.model.businessrules.CategoryCreationBR;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Andrés Farías on 27-05-16.
 */
@Stateless
public class CategoryManagerImpl implements CategoryManager {

    @PersistenceContext(unitName = "SEMANTIKOS_PU")
    private EntityManager entityManager;

    @EJB
    private CategoryDAO categoryDAO;

    @EJB
    private RelationshipDAO relationshipDAO;

    private static final Logger logger = LoggerFactory.getLogger(CategoryManagerImpl.class);

    @EJB
    private DescriptionManager descriptionManager;

    @Override
    public List<RelationshipDefinition> getCategoryMetaData(int id) {
       return categoryDAO.getCategoryMetaData(id);
    }

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
    public boolean categoryContains(Category category, String term) {

        List<Description> descriptions = descriptionManager.searchDescriptionsByTerm(term, Arrays.asList(category));

        /* Si la búsqueda resultó con al menos un término vigente, entonces si contiene */
        for (Description description : descriptions) {
            if (description.isValid()){
                return true;
            }
        }
        return false;
    }

    @Override
    public Category getCategoryById(int id) throws ParseException {
        return categoryDAO.getCategoryById(id);
    }

    @Override
    public Category getCategoryByName(String name) {
        return this.categoryDAO.getCategoryByName(StringUtils.toSQLLikePattern(name));
    }

    @Override
    public List<Category> getCategories() {

        logger.debug("Recuperando todas las categorías.");

        List<Category> categories = categoryDAO.getAllCategories();
        logger.debug(categories.size() + "categorías recuperadas.");

        return categories;
    }
}
