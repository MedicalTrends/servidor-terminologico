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
@XmlRootElement(name = "respuestaDescripcionesNoValidas", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RespuestaDescripcionesNoValidas", namespace = "http://service.ws.semantikos.minsal.cl/")
public class NoValidDescriptionsResponse implements Serializable {

    @XmlElementWrapper(name = "descripcionesNoValidas")
    @XmlElement(name = "descripcionNoValida")
    private List<NoValidDescriptionResponse> noValidDescriptionsResponse;

    @XmlElement(name = "cantidadRegistros")
    private int quantity;

    public NoValidDescriptionsResponse() {
        this.noValidDescriptionsResponse = new ArrayList<>();
        this.quantity = 0;
    }

    public NoValidDescriptionsResponse(List<NoValidDescriptionResponse> noValidDescriptionsResponse) {
        this.noValidDescriptionsResponse = noValidDescriptionsResponse;
        this.quantity = noValidDescriptionsResponse.size();
    }

    public List<NoValidDescriptionResponse> getNoValidDescriptionsResponse() {
        return noValidDescriptionsResponse;
    }

    public void setNoValidDescriptionsResponse(List<NoValidDescriptionResponse> noValidDescriptionsResponse) {
        this.noValidDescriptionsResponse = noValidDescriptionsResponse;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
