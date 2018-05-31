package cl.minsal.semantikos.modelws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Development on 2016-11-23.
 *
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "peticionBuscarTerminoSnomed", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionBuscarTerminoSnomed", namespace = "http://service.ws.semantikos.minsal.cl/")
public class SnomedSearchTermRequest extends Request implements Serializable {

    private String term;


    private int pageNumber;

    private int pageSize;

    @XmlElement(required = true, name = "terminoBuscar")
    public String getTerm() {
        return term;
    }
    public void setTerm(String term) {
        this.term = term;
    }

    @XmlElement(required = true, defaultValue = "0", name = "numeroPagina")
    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @XmlElement(required = true, defaultValue = "30", name = "tamanoPagina")
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
