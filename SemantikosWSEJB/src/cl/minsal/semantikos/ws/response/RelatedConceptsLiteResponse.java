package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Development on 2016-11-04.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "respuestaConceptosRelacionadosLite", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RespuestaConceptosRelacionadosLite", namespace = "http://service.ws.semantikos.minsal.cl/")
public class RelatedConceptsLiteResponse implements Serializable {

    @XmlElementWrapper(name="conceptosRelacionados")
    @XmlElement(name="concepto")
    private List<ConceptLightResponse> relatedConcepts;

    public List<ConceptLightResponse> getRelatedConcepts() {
        return relatedConcepts;
    }

    public void setRelatedConcepts(List<ConceptLightResponse> relatedConcepts) {
        this.relatedConcepts = relatedConcepts;
    }
}
