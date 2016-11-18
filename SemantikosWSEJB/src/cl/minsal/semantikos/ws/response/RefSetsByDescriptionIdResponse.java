package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Development on 2016-11-03.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "refSetsPorIdDescripcion")
public class RefSetsByDescriptionIdResponse implements Serializable {

    @XmlElementWrapper(name="refSets")
    @XmlElement(name="refSet")
    private List<RefSetResponse> refSets;
    @XmlElementWrapper(name="conceptos")
    @XmlElement(name="concepto")
    private List<ConceptResponse> concepts;

    public List<RefSetResponse> getRefSets() {
        return refSets;
    }

    public void setRefSets(List<RefSetResponse> refSets) {
        this.refSets = refSets;
    }

    public List<ConceptResponse> getConcepts() {
        return concepts;
    }

    public void setConcepts(List<ConceptResponse> concepts) {
        this.concepts = concepts;
    }
}
