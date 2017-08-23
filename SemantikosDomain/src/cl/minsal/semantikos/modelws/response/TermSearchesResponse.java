package cl.minsal.semantikos.modelws.response;

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
@XmlRootElement(name = "respuestaBuscarTerminos", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RespuestaBuscarTerminos", namespace = "http://service.ws.semantikos.minsal.cl/")
public class TermSearchesResponse extends Response implements Serializable {

    @XmlElement(name = "pedible")
    private boolean requestable;

    @XmlElementWrapper(name = "categorias")
    @XmlElement(name = "categoria")
    private List<TermSearchResponse> termSearchResponses = new ArrayList<>();

    @XmlElement(name = "cantidadRegistros")
    private int quantity;

    public List<TermSearchResponse> getTermSearchResponses() {
        return termSearchResponses;
    }

    public void setTermSearchResponses(List<TermSearchResponse> termSearchResponses) {
        this.termSearchResponses = termSearchResponses;
    }

    public boolean isRequestable() {
        return requestable;
    }

    public void setRequestable(boolean requestable) {
        this.requestable = requestable;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
