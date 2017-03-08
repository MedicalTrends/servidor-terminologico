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
@XmlRootElement(name = "respuestaDescripcionesPendientes", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RespuestaDescripcionesPendientes", namespace = "http://service.ws.semantikos.minsal.cl/")
public class PendingDescriptionsResponse implements Serializable {

    @XmlElementWrapper(name = "descripcionesPendientes")
    @XmlElement(name = "descripcionPendiente")
    private List<PendingDescriptionResponse> pendingDescriptionsResponse;

    @XmlElement(name = "cantidadRegistros")
    private int quantity;

    public PendingDescriptionsResponse() {
        this.pendingDescriptionsResponse = new ArrayList<>();
        this.quantity = 0;
    }

    public PendingDescriptionsResponse(List<PendingDescriptionResponse> pendingDescriptionsResponse) {
        this.pendingDescriptionsResponse = pendingDescriptionsResponse;
        this.quantity = pendingDescriptionsResponse.size();
    }

    public List<PendingDescriptionResponse> getPendingDescriptionsResponse() {
        return pendingDescriptionsResponse;
    }

    public void setPendingDescriptionsResponse(List<PendingDescriptionResponse> pendingDescriptionsResponse) {
        this.pendingDescriptionsResponse = pendingDescriptionsResponse;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
