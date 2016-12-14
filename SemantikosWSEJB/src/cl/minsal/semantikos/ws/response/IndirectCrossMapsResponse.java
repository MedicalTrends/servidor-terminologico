package cl.minsal.semantikos.ws.response;

import cl.minsal.semantikos.model.crossmaps.IndirectCrossmap;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author Andrés Farías on 12/13/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "indirectCrossmap", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "IndirectCrossmap", namespace = "http://service.ws.semantikos.minsal.cl/")
public class IndirectCrossMapsResponse {

    /** La lista de crossmaps indirectos */
    @XmlElement(name="indirectCrossmaps")
    private List<IndirectCrossmap> indirectCrossmaps;

    public IndirectCrossMapsResponse() {
    }

    public IndirectCrossMapsResponse(List<IndirectCrossmap> indirectCrossmaps) {
        this.indirectCrossmaps = indirectCrossmaps;
    }
}
