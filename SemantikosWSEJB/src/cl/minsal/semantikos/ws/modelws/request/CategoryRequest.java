package cl.minsal.semantikos.ws.modelws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Esta clase representa una petición de servicio que recibe como argumento una categoría.
 *
 * @author Alonso Cornejo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "peticionPorCategoria", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionPorCategoria", namespace = "http://service.ws.semantikos.minsal.cl/")
public class CategoryRequest extends Request implements Serializable {

    @XmlElement(required = true, name = "nombreCategoria")
    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
