package cl.minsal.semantikos.modelws.response;

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

    @XmlElement(name = "nombreCategoria")
    private String category;

    @XmlElementWrapper(name = "conceptos")
    @XmlElement(name = "concepto")
    private List<ConceptLightResponse> concepts = new ArrayList<>();

    @XmlElement(name = "cantidadRegistros")
    private int quantity;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<ConceptLightResponse> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<ConceptLightResponse> concepts) {
        this.concepts = concepts;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
