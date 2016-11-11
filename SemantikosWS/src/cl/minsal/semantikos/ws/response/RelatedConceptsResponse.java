package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Development on 2016-11-04.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "conceptosRelacionados")
public class RelatedConceptsResponse implements Serializable {

    @XmlElement(name="conceptoBuscado")
    private ConceptResponse searchedConcept;
    @XmlElementWrapper(name="conceptosRelacionados")
    @XmlElement(name="concepto")
    private List<ConceptResponse> relatedConcepts;

    public ConceptResponse getSearchedConcept() {
        return searchedConcept;
    }

    public void setSearchedConcept(ConceptResponse searchedConcept) {
        this.searchedConcept = searchedConcept;
    }

    public List<ConceptResponse> getRelatedConcepts() {
        return relatedConcepts;
    }

    public void setRelatedConcepts(List<ConceptResponse> relatedConcepts) {
        this.relatedConcepts = relatedConcepts;
    }
}
