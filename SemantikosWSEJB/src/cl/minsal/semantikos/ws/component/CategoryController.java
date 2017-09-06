package cl.minsal.semantikos.ws.component;

import cl.minsal.semantikos.kernel.components.CategoryManager;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.modelws.response.CategoriesResponse;
import cl.minsal.semantikos.modelws.response.CategoryResponse;
import cl.minsal.semantikos.modelws.fault.NotFoundFault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

/**
 * Created by Development on 2016-11-18.
 */
@Stateless
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @EJB
    private CategoryManager categoryManager;

    /**
     * Este método es responsable de recuperar los objetos de negocio que representan a las categorías dadas por sus
     * nombres en las palabras.
     *
     * @param categoriesNames Los nombres de las categorías que se desea buscar. Si la lista está vacía, se
     *                        interpreta como todas las categorías.
     * @return Una lista de todas las categorías.
     * @throws NotFoundFault Arrojado si uno de los nombres no tiene una representación en objetos.
     */
    public List<Category> findCategories(List<String> categoriesNames) throws NotFoundFault {

        /* Si no se indican categorías, se retornan todas */
        if (categoriesNames == null || categoriesNames.isEmpty()) {
            return this.categoryManager.getCategories();
        }

        /* Si no, se delega al Manager */
        try {
            return this.categoryManager.findCategories(categoriesNames);
        } catch (Exception e) {
            throw new NotFoundFault(e.getMessage());
        }
    }

    /**
     * Este método es responsable de recuperar las categorías y encapsularlas en un objeto XML.
     *
     * @return Un objeto XML que encapsula la lista de categorías.
     */
    public CategoriesResponse categoryList() {

        /* Se recuperan las categorías */
        List<Category> categories = this.categoryManager.getCategories();
        logger.debug("CategoryController.categoryList() : " +categories.size());

        return new CategoriesResponse(categories);
    }

    public CategoryResponse getResponse(Category category) throws NotFoundFault {
        if (category == null) {
            throw new NotFoundFault("Categoria no encontrada");
        }
        return new CategoryResponse(category);
    }

}
