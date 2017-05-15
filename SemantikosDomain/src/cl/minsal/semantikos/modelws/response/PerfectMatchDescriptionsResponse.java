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
@XmlRootElement(name = "respuestaDescripcionesPerfectMatch", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RespuestaDescripcionesPerfectMatch", namespace = "http://service.ws.semantikos.minsal.cl/")
public class PerfectMatchDescriptionsResponse implements Serializable {

    @XmlElementWrapper(name = "descripcionesPerfectMatch")
    @XmlElement(name = "descripcionPerfectMatch")
    private List<PerfectMatchDescriptionResponse> perfectMatchDescriptionsResponse;

    @XmlElement(name = "cantidadRegistros")
    private int quantity;

    public PerfectMatchDescriptionsResponse() {
        this.perfectMatchDescriptionsResponse = new ArrayList<>();
        this.quantity = 0;
    }

    public PerfectMatchDescriptionsResponse(List<PerfectMatchDescriptionResponse> perfectMatchDescriptionsResponse) {
        this.perfectMatchDescriptionsResponse = perfectMatchDescriptionsResponse;
        this.quantity = perfectMatchDescriptionsResponse.size();
    }

    public List<PerfectMatchDescriptionResponse> getPerfectMatchDescriptionsResponse() {
        return perfectMatchDescriptionsResponse;
    }

    public void setPerfectMatchDescriptionsResponse(List<PerfectMatchDescriptionResponse> perfectMatchDescriptionsResponse) {
        this.perfectMatchDescriptionsResponse = perfectMatchDescriptionsResponse;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
