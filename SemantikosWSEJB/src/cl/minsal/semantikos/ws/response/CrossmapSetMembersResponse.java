package cl.minsal.semantikos.ws.response;

import cl.minsal.semantikos.model.crossmaps.Crossmap;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;
import cl.minsal.semantikos.model.crossmaps.IndirectCrossmap;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrés Farías on 12/13/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "crossmapSetMembersResponse", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "CrossmapSetMembersResponse", namespace = "http://service.ws.semantikos.minsal.cl/")
public class CrossmapSetMembersResponse {

    /** La lista de crossmaps indirectos (response) */
    @XmlElementWrapper(name = "crossmapSetMembers")
    @XmlElement(name = "crossmapSetMember")
    private List<CrossmapSetMemberResponse> crossmapSetMemberResponses;

    @XmlElement(name = "cantidadRegistros")
    private int quantity;

    public CrossmapSetMembersResponse() {
        this.crossmapSetMemberResponses = new ArrayList<>();
    }

    /**
     * Este constructor es responsable de poblar la lista de crossmapsSetMembers "response" a partir del  objeto de
     * negocio.
     *
     * @param crossmapSetMembers La lista de crossmapSetMembers de negocio.
     */
    public CrossmapSetMembersResponse(List<CrossmapSetMember> crossmapSetMembers) {
        this();

        if (crossmapSetMembers == null || crossmapSetMembers.isEmpty()){
            return;
        }

        for (CrossmapSetMember crossmapSetMember : crossmapSetMembers) {
            this.crossmapSetMemberResponses.add(new CrossmapSetMemberResponse(crossmapSetMember));
        }

        this.quantity = crossmapSetMembers.size();
    }

    public List<CrossmapSetMemberResponse> getCrossmapSetMemberResponses() {
        return crossmapSetMemberResponses;
    }

    public void setCrossmapSetMemberResponses(List<CrossmapSetMemberResponse> crossmapSetMemberResponses) {
        this.crossmapSetMemberResponses = crossmapSetMemberResponses;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
