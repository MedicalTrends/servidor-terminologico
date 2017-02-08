package cl.minsal.semantikos.ws.request;

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
public class CategoryRequest implements Serializable {

    @XmlElement(required = true, name = "nombreCategoria")
    private String categoryName;

    @XmlElement(required = true, defaultValue = "1", name = "idEstablecimiento")
    private String idStablishment;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getIdStablishment() {
        return idStablishment;
    }

    public void setIdStablishment(String idStablishment) {
        this.idStablishment = idStablishment;
    }
}
