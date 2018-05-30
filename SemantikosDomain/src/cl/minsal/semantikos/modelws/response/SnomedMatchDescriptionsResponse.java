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
public class SnomedMatchDescriptionsResponse implements Serializable {

    @XmlElementWrapper(name = "descripcionesEncontradas")
    @XmlElement(name = "DescripcionEncontrada")
    private List<SnomedMatchDescriptionResponse> perfectMatchDescriptionsResponse;

    @XmlElement(name = "cantidadRegistros")
    private int quantity;

    public SnomedMatchDescriptionsResponse() {
        this.perfectMatchDescriptionsResponse = new ArrayList<>();
        this.quantity = 0;
    }

    public SnomedMatchDescriptionsResponse(List<SnomedMatchDescriptionResponse> perfectMatchDescriptionsResponse) {
        this.perfectMatchDescriptionsResponse = perfectMatchDescriptionsResponse;
        this.quantity = perfectMatchDescriptionsResponse.size();
    }

    public List<SnomedMatchDescriptionResponse> getPerfectMatchDescriptionsResponse() {
        return perfectMatchDescriptionsResponse;
    }

    public void setPerfectMatchDescriptionsResponse(List<SnomedMatchDescriptionResponse> perfectMatchDescriptionsResponse) {
        this.perfectMatchDescriptionsResponse = perfectMatchDescriptionsResponse;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
