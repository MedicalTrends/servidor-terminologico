package cl.minsal.semantikos.modelws.response;

import cl.minsal.semantikos.model.crossmaps.CrossmapSet;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrés Farías on 12/13/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "crossmapSetsResponse", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "CrossmapSetsResponse", namespace = "http://service.ws.semantikos.minsal.cl/")
public class CrossmapSetsResponse implements Serializable {

    /** La lista de crossmaps indirectos (response) */
    @XmlElementWrapper(name = "crossmapSets")
    @XmlElement(name = "crossmapSet")
    private List<CrossmapSetResponse> crossmapSetResponses;

    @XmlElement(name = "cantidadRegistros")
    public int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public CrossmapSetsResponse() {
        this.crossmapSetResponses = new ArrayList<>();
    }

    /**
     * Este constructor es responsable de poblar la lista de crossmapsSets "response" a partir del  objeto de
     * negocio.
     *
     * @param crossmapSets La lista de crossmapSets de negocio.
     */
    public CrossmapSetsResponse(List<CrossmapSet> crossmapSets) {
        this();

        for (CrossmapSet crossmapSet : crossmapSets) {
            this.crossmapSetResponses.add(new CrossmapSetResponse(crossmapSet));
        }

        this.quantity = crossmapSetResponses.size();
    }

    public List<CrossmapSetResponse> getCrossmapSetResponses() {
        return crossmapSetResponses;
    }

    public void setCrossmapSetResponses(List<CrossmapSetResponse> crossmapSetResponses) {
        this.crossmapSetResponses = crossmapSetResponses;
        this.quantity = crossmapSetResponses.size();
    }

}
