package cl.minsal.semantikos.ws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Esta clase representa una petición de servicio que recibe como argumento una categoría.
 *
 * @author Alonso Cornejo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "peticionPorCategoriaPaginados", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionPorCategoriaPaginados", namespace = "http://service.ws.semantikos.minsal.cl/")
public class CategoryRequestPaginated extends Request implements Serializable {

    @XmlElement(required = true, name = "nombreCategoria")
    private String categoryName;

    @XmlElement(required = true, defaultValue = "0", name = "numeroPagina")
    private int pageNumber;

    @XmlElement(required = true, defaultValue = "30", name = "tamanoPagina")
    private int pageSize;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
