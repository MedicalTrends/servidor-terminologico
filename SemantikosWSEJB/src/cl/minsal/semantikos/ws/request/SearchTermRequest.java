package cl.minsal.semantikos.ws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Development on 2016-11-23.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "peticionBuscarTermino", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionBuscarTermino", namespace = "http://service.ws.semantikos.minsal.cl/")
public class SearchTermRequest implements Serializable {

    @XmlElement(required = true, name = "termino")
    private String term;
    @XmlElement(required = false, name = "nombreCategoria")
    private List<String> categoryNames;
    @XmlElement(required = false, name = "nombreRefSet")
    private List<String> refSetNames;
    @XmlElement(required = false, defaultValue = "0", name = "numeroPagina")
    private Integer pageNumber;
    @XmlElement(required = false, defaultValue = "10", name = "tamanoPagina")
    private Integer pageSize;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public List<String> getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(List<String> categoryNames) {
        this.categoryNames = categoryNames;
    }

    public List<String> getRefSetNames() {
        return refSetNames;
    }

    public void setRefSetNames(List<String> refSetNames) {
        this.refSetNames = refSetNames;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
