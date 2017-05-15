package cl.minsal.semantikos.modelws.response;

import cl.minsal.semantikos.model.refsets.RefSet;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrés Farías on 12-01-17.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "respuestaRefSets", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RespuestaRefSets", namespace = "http://service.ws.semantikos.minsal.cl/")
public class RefSetsResponse {

    @XmlElementWrapper(name = "refsets")
    @XmlElement(name = "refset")
    private List<RefSetResponse> refSetResponses;

    @XmlElement(name = "cantidadRegistros")
    private int quantity;

    public RefSetsResponse() {
        this.refSetResponses = new ArrayList<>();
    }

    public RefSetsResponse(List<RefSet> refSets) {
        this();

        if (refSets == null || refSets.isEmpty()){
            return;
        }

        for (RefSet refSet : refSets) {
            refSetResponses.add(new RefSetResponse(refSet));
        }

        this.quantity = refSets.size();
    }

    public List<RefSetResponse> getRefSetResponses() {
        return refSetResponses;
    }

    public void setRefSetResponses(List<RefSetResponse> refSetResponses) {
        this.refSetResponses = refSetResponses;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
