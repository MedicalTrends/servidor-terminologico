package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Development on 2016-10-28.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "conceptosPorRefSet")
public class ConceptsByRefsetResponse implements Serializable {

    @XmlElement(name="refSet")
    private RefSetResponse refSet;
    @XmlElement(name="paginacion")
    private PaginationResponse pagination;
    @XmlElementWrapper(name="conceptos")
    @XmlElement(name="concepto")
    private List<ConceptResponse> concepts;

    public RefSetResponse getRefSet() {
        return refSet;
    }

    public void setRefSet(RefSetResponse refSet) {
        this.refSet = refSet;
    }

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
