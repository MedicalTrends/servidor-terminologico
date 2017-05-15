package cl.minsal.semantikos.modelws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-10-11.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "paginacion", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "Paginacion", namespace = "http://service.ws.semantikos.minsal.cl/")
public class PaginationResponse implements Serializable {

    @XmlElement(name="totalRegistros")
    private Integer totalCount;
    @XmlElement(name="paginaActual")
    private Integer currentPage;
    @XmlElement(name="registrosPorPagina")
    private Integer pageSize;
    @XmlElement(name="mostrandoDesde")
    private Integer showingFrom;
    @XmlElement(name="mostrandoHasta")
    private Integer showingTo;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getShowingFrom() {
        return showingFrom;
    }

    public void setShowingFrom(Integer showingFrom) {
        this.showingFrom = showingFrom;
    }

    public Integer getShowingTo() {
        return showingTo;
    }

    public void setShowingTo(Integer showingTo) {
        this.showingTo = showingTo;
    }
}
