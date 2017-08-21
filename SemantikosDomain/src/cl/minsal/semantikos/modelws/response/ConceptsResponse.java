package cl.minsal.semantikos.modelws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;

/**
 * Esta clase representa una respuesta XML con una lista de conceptos.
 *
 * @author Alfonso Cornejo on 2016-10-11.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "respuestaConceptosPorCategoriaPaginados", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RespuestaConceptosPorCategoriaPaginados", namespace = "http://service.ws.semantikos.minsal.cl/")
public class ConceptsResponse extends Response implements Serializable {

    @XmlElement(name = "nombreCategoria")
    private String category;

    @XmlElementWrapper(name = "conceptos")
    @XmlElement(name = "concepto")
    private List<ConceptResponse> conceptResponses;

    @XmlElement(name = "cantidadRegistros")
    private int quantity;

    public ConceptsResponse() {
        this.setCategory(EMPTY_STRING);
        this.conceptResponses = new ArrayList<>();
        this.quantity = 0;
    }

    public ConceptsResponse(List<ConceptResponse> conceptResponses) {
        this.conceptResponses = conceptResponses;
        this.quantity = conceptResponses.size();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<ConceptResponse> getConceptResponses() {
        return conceptResponses;
    }

    public void setConceptResponses(List<ConceptResponse> conceptResponses) {
        this.conceptResponses = conceptResponses;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
