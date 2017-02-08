package cl.minsal.semantikos.ws.response;

import cl.minsal.semantikos.model.ConceptSMTK;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "respuestaBuscarTermino", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RespuestaBuscarTermino", namespace = "http://service.ws.semantikos.minsal.cl/")
public class TermSearchResponse implements Serializable {

    @XmlElementWrapper(name = "conceptos")
    @XmlElement(name = "concepto")
    private List<ConceptLightResponse> concepts;

    public TermSearchResponse() {
        this.concepts = new ArrayList<>();
    }

    /**
     * Este constructor incializa la lista de conceptos pedibles.
     *
     * @param requestableConcepts Los conceptos pedibles con los que se inicializa la búsqueda.
     */
    public TermSearchResponse(List<ConceptSMTK> requestableConcepts) {
        for (ConceptSMTK requestableConcept : requestableConcepts) {
            concepts.add(new ConceptLightResponse(requestableConcept));
        }
    }

    public List<ConceptLightResponse> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<ConceptLightResponse> concepts) {
        this.concepts = concepts;
    }
}
