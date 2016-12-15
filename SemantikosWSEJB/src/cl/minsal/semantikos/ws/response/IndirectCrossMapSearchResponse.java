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


    @XmlElementWrapper(name = "indirectCrossmaps")
    @XmlElement(name = "indirectCrossmap")
    private List<IndirectCrossMapResponse> indirectCrossMapResponses;

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
    }


}
