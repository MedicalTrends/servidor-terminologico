package cl.minsal.semantikos.ws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-11-23.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "peticionConceptosPorRefSet", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionConceptosPorRefSet", namespace = "http://service.ws.semantikos.minsal.cl/")
public class ConceptsByRefsetRequest implements Serializable {

    @XmlElement(required = true, name = "nombreRefSet")
    private String refSetName;
    @XmlElement(required = false, defaultValue = "0", name = "numeroPagina")
    private Integer pageNumber;
    @XmlElement(required = false, defaultValue = "10", name = "tamanoPagina")
    private Integer pageSize;

    public String getRefSetName() {
        return refSetName;
    }

    public void setRefSetName(String refSetName) {
        this.refSetName = refSetName;
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
