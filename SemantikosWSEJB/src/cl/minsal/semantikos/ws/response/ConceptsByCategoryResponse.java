package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Development on 2016-10-11.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "conceptosPorCategoria")
public class ConceptsByCategoryResponse implements Serializable {

    @XmlElement(name="categoria")
    private CategoryResponse categoryResponse;
    @XmlElement(name="paginacion")
    private PaginationResponse paginationResponse;
    @XmlElementWrapper(name="conceptos")
    @XmlElement(name="concepto")
    private List<ConceptResponse> conceptResponses;

    public CategoryResponse getCategoryResponse() {
        return categoryResponse;
    }

    public void setCategoryResponse(CategoryResponse categoryResponse) {
        this.categoryResponse = categoryResponse;
    }

    public PaginationResponse getPaginationResponse() {
        return paginationResponse;
    }

    public void setPaginationResponse(PaginationResponse paginationResponse) {
        this.paginationResponse = paginationResponse;
    }

    public List<ConceptResponse> getConceptResponses() {
        return conceptResponses;
    }

    public void setConceptResponses(List<ConceptResponse> conceptResponses) {
        this.conceptResponses = conceptResponses;
    }
}
