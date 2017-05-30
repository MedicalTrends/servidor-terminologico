package cl.minsal.semantikos.ws.modelws.response;

import cl.minsal.semantikos.model.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrés Farías on 16-01-17.
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "respuestaCategorias", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RespuestaCategorias", namespace = "http://service.ws.semantikos.minsal.cl/")
public class CategoriesResponse {

    private final static Logger logger = LoggerFactory.getLogger(CategoriesResponse.class);

    /** Lista de categorías (en su envoltorio XML) */
    private List<CategoryResponse> categoryResponses;

    public CategoriesResponse() {
        this.categoryResponses = new ArrayList<>();
    }

    public CategoriesResponse(List<Category> categories) {
        this();

        /* No se hace nada si la lista viene vacia o nula */
        if (categories == null || categories.isEmpty()){
            logger.debug("CategoriesResponse(categorias) :: La lista de categorías venia vacia.");
            return;
        }

        /* Se construye el contenido a partir de los objetos de negocio */
        for (Category category : categories) {
            this.categoryResponses.add(new CategoryResponse(category));
        }
        logger.debug("CategoriesResponse(categories) iniciado con " + categoryResponses.size() + " categorias XML.");
    }

    @XmlElementWrapper(name = "categorias")
    @XmlElement(name = "categoria")
    public List<CategoryResponse> getCategoryResponses() {
        return categoryResponses;
    }

    public void setCategoryResponses(List<CategoryResponse> categoryResponses) {
        this.categoryResponses = categoryResponses;
    }
}
