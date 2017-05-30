package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase representa una respuesta XML con una lista de conceptos.
 *
 * @author Alfonso Cornejo on 2016-10-11.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "respuestaSugerenciasDeDescripciones", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RespuestaSugerenciasDeDescripciones", namespace = "http://service.ws.semantikos.minsal.cl/")
public class SuggestedDescriptionsResponse implements Serializable {

    @XmlElement(name = "terminoBuscar")
    private String pattern;

    @XmlElementWrapper(name = "descripcionesSugeridas")
    @XmlElement(name = "descripcionSugerida")
    private List<SuggestedDescriptionResponse> suggestedDescriptionResponses;

    @XmlElement(name = "cantidadRegistros")
    private int quantity;

    public SuggestedDescriptionsResponse() {
        this.suggestedDescriptionResponses = new ArrayList<>();
        this.quantity = 0;
    }

    public SuggestedDescriptionsResponse(List<SuggestedDescriptionResponse> suggestedDescriptionResponses) {
        this.suggestedDescriptionResponses = suggestedDescriptionResponses;
        this.quantity = suggestedDescriptionResponses.size();
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public List<SuggestedDescriptionResponse> getSuggestedDescriptionResponses() {
        return suggestedDescriptionResponses;
    }

    public void setSuggestedDescriptionResponses(List<SuggestedDescriptionResponse> suggestedDescriptionResponses) {
        this.suggestedDescriptionResponses = suggestedDescriptionResponses;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
