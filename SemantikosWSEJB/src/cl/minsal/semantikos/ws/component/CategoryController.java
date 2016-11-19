package cl.minsal.semantikos.ws.component;

import cl.minsal.semantikos.kernel.components.CategoryManager;
import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.ws.fault.NotFoundFault;
import cl.minsal.semantikos.ws.mapping.CategoryMapper;
import cl.minsal.semantikos.ws.response.CategoryResponse;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Development on 2016-11-18.
 *
 */
@Stateless
public class CategoryController {

    @EJB
    private CategoryManager categoryManager;

    public List<CategoryResponse> categoryList() throws NotFoundFault {
        List<CategoryResponse> res = new ArrayList<>();
        List<Category> categories = this.categoryManager.getCategories();
        if ( categories != null ) {
            for ( Category category : categories ) {
                res.add(this.getResponse(category));
            }
        }
        return res;
    }

    public CategoryResponse getResponse(Category category) throws NotFoundFault {
        if ( category == null ) {
            throw new NotFoundFault("Categoria no encontrada");
        }
        return CategoryMapper.map(category);
    }

}
