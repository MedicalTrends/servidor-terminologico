package cl.minsal.semantikos.ws.response;

import cl.minsal.semantikos.model.crossmaps.IndirectCrossmap;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrés Farías on 12/13/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "indirectCrossmapsSearch", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "IndirectCrossmapsSearch", namespace = "http://service.ws.semantikos.minsal.cl/")
public class IndirectCrossMapSearchResponse {

    /** La lista de crossmaps indirectos (response) */
    @XmlElementWrapper(name = "indirectCrossmaps")
    @XmlElement(name = "indirectCrossmap")
    private List<IndirectCrossMapResponse> indirectCrossMapResponses;

    @XmlElement(name = "cantidadRegistros")
    private int quantity;


    public IndirectCrossMapSearchResponse() {
        this.indirectCrossMapResponses = new ArrayList<>();
    }

    /**
     * Este constructor es responsable de poblar la lista de crossmaps indirectos "response" a partir del  objeto de
     * negocio.
     *
     * @param indirectCrossmaps La lista de crossmaps indirectos de negocio.
     */
    public IndirectCrossMapSearchResponse(List<IndirectCrossmap> indirectCrossmaps) {
        this();

        for (IndirectCrossmap indirectCrossmap : indirectCrossmaps) {
            this.indirectCrossMapResponses.add(new IndirectCrossMapResponse(indirectCrossmap));
        }

        this.quantity = indirectCrossmaps.size();
    }

    public List<IndirectCrossMapResponse> getIndirectCrossMapResponses() {
        return indirectCrossMapResponses;
    }

    public void setIndirectCrossMapResponses(List<IndirectCrossMapResponse> indirectCrossMapResponses) {
        this.indirectCrossMapResponses = indirectCrossMapResponses;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
