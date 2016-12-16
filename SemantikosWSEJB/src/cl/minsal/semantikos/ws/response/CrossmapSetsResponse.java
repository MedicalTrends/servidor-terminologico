package cl.minsal.semantikos.ws.response;

import cl.minsal.semantikos.model.crossmaps.CrossmapSet;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrés Farías on 12/13/16.
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "crossmapSetMembersResponse", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "CrossmapSetMembersResponse", namespace = "http://service.ws.semantikos.minsal.cl/")
public class CrossmapSetsResponse {

    /** La lista de crossmaps indirectos (response) */
    private List<CrossmapSetResponse> crossmapSetResponses;

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
    }

    @XmlElementWrapper(name = "crossmapSets")
    @XmlElement(name = "crossmapSet")
    public List<CrossmapSetResponse> getCrossmapSetResponses() {
        return crossmapSetResponses;
    }

    public void setCrossmapSetResponses(List<CrossmapSetResponse> crossmapSetResponses) {
        this.crossmapSetResponses = crossmapSetResponses;
    }

    @XmlElementWrapper(name = "cantidadRegistros")
    @XmlElement(name = "cantidadRegistros")
    public int getCantidadRegistros(){
        return this.crossmapSetResponses.size();
    }
}
