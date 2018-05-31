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
@XmlRootElement(name = "respuestaDescripcionesSnomedPerfectMatch", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RespuestaDescripcionesSnomedPerfectMatch", namespace = "http://service.ws.semantikos.minsal.cl/")
public class SnomedMatchDescriptionsResponse implements Serializable {

    @XmlElementWrapper(name = "descripcionesEncontradas")
    @XmlElement(name = "DescripcionEncontrada")
    private List<SnomedMatchDescriptionResponse> matchDescriptionsResponse;

    @XmlElement(name = "cantidadRegistros")
    private int quantity;

    public SnomedMatchDescriptionsResponse() {
        this.matchDescriptionsResponse = new ArrayList<>();
        this.quantity = 0;
    }

    public SnomedMatchDescriptionsResponse(List<SnomedMatchDescriptionResponse> matchDescriptionsResponse) {
        this.matchDescriptionsResponse = matchDescriptionsResponse;
        this.quantity = matchDescriptionsResponse.size();
    }

    public List<SnomedMatchDescriptionResponse> getMatchDescriptionsResponse() {
        return matchDescriptionsResponse;
    }

    public void setMatchDescriptionsResponse(List<SnomedMatchDescriptionResponse> matchDescriptionsResponse) {
        this.matchDescriptionsResponse = matchDescriptionsResponse;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
