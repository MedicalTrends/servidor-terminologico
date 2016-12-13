package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "respuestaBuscarTermino", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RespuestaBuscarTermino", namespace = "http://service.ws.semantikos.minsal.cl/")
public class GenericTermSearchResponse implements Serializable {

    @XmlElementWrapper(name="perfectMatch")
    private List<PerfectMatchDescription> perfectMatchDescriptions;

    @XmlElementWrapper(name="noValid")
    private List<NoValidDescription> noValidDescriptions;

    @XmlElementWrapper(name="pending")
    private List<PendingDescription> pendingDescriptions;

    public List<PerfectMatchDescription> getPerfectMatchDescriptions() {
        return perfectMatchDescriptions;
    }

    public void setPerfectMatchDescriptions(List<PerfectMatchDescription> perfectMatchDescriptions) {
        this.perfectMatchDescriptions = perfectMatchDescriptions;
    }

    public List<NoValidDescription> getNoValidDescriptions() {
        return noValidDescriptions;
    }

    public void setNoValidDescriptions(List<NoValidDescription> noValidDescriptions) {
        this.noValidDescriptions = noValidDescriptions;
    }

    public List<PendingDescription> getPendingDescriptions() {
        return pendingDescriptions;
    }

    public void setPendingDescriptions(List<PendingDescription> pendingDescriptions) {
        this.pendingDescriptions = pendingDescriptions;
    }
}
