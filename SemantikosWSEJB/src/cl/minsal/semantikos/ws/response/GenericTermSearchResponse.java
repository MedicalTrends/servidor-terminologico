package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "respuestaBuscarTerminoGenerica", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RespuestaBuscarTerminoGenerica", namespace = "http://service.ws.semantikos.minsal.cl/")
public class GenericTermSearchResponse implements Serializable {

    @XmlElementWrapper(name="descripcionesPerfectMatch")
    @XmlElement(name="descripcionPerfectMatch")
    private List<PerfectMatchDescriptionResponse> perfectMatchDescriptions;

    @XmlElementWrapper(name="descripcionesNoValidas")
    @XmlElement(name="descripcionNoValida")
    private List<NoValidDescriptionResponse> noValidDescriptions;

    @XmlElementWrapper(name="descripcionesPendientes")
    @XmlElement(name="descripcionPendiente")
    private List<PendingDescriptionResponse> pendingDescriptions;

    public List<PerfectMatchDescriptionResponse> getPerfectMatchDescriptions() {
        return perfectMatchDescriptions;
    }

    public void setPerfectMatchDescriptions(List<PerfectMatchDescriptionResponse> perfectMatchDescriptions) {
        this.perfectMatchDescriptions = perfectMatchDescriptions;
    }

    public List<NoValidDescriptionResponse> getNoValidDescriptions() {
        return noValidDescriptions;
    }

    public void setNoValidDescriptions(List<NoValidDescriptionResponse> noValidDescriptions) {
        this.noValidDescriptions = noValidDescriptions;
    }

    public List<PendingDescriptionResponse> getPendingDescriptions() {
        return pendingDescriptions;
    }

    public void setPendingDescriptions(List<PendingDescriptionResponse> pendingDescriptions) {
        this.pendingDescriptions = pendingDescriptions;
    }
}
