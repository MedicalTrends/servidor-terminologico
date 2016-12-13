package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "respuestaBuscarTermino", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RespuestaBuscarTermino", namespace = "http://service.ws.semantikos.minsal.cl/")
public class TermSearchResponse implements Serializable {

    @XmlElement(name="paginacion")
    private PaginationResponse pagination;
    @XmlElementWrapper(name="conceptos")
    @XmlElement(name="concepto")
    private List<ConceptResponse> concepts;

    public PaginationResponse getPagination() {
        return pagination;
    }

    public void setPagination(PaginationResponse pagination) {
        this.pagination = pagination;
    }

    public List<ConceptResponse> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<ConceptResponse> concepts) {
        this.concepts = concepts;
    }
}
