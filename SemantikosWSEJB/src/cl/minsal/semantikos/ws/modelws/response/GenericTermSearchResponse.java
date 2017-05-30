package cl.minsal.semantikos.ws.modelws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "respuestaBuscarTerminoGenerica", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RespuestaBuscarTerminoGenerica", namespace = "http://service.ws.semantikos.minsal.cl/")
public class GenericTermSearchResponse implements Serializable {

    @XmlElement(name="terminoBuscar")
    private String pattern;

    @XmlElement(name="descripcionesPerfectMatch")
    private PerfectMatchDescriptionsResponse perfectMatchDescriptions;

    @XmlElement(name="descripcionesNoValidas")
    private NoValidDescriptionsResponse noValidDescriptions;

    @XmlElement(name="descripcionesPendientes")
    private PendingDescriptionsResponse pendingDescriptions;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public PerfectMatchDescriptionsResponse getPerfectMatchDescriptions() {
        return perfectMatchDescriptions;
    }

    public void setPerfectMatchDescriptions(PerfectMatchDescriptionsResponse perfectMatchDescriptions) {
        this.perfectMatchDescriptions = perfectMatchDescriptions;
    }

    public NoValidDescriptionsResponse getNoValidDescriptions() {
        return noValidDescriptions;
    }

    public void setNoValidDescriptions(NoValidDescriptionsResponse noValidDescriptions) {
        this.noValidDescriptions = noValidDescriptions;
    }

    public PendingDescriptionsResponse getPendingDescriptions() {
        return pendingDescriptions;
    }

    public void setPendingDescriptions(PendingDescriptionsResponse pendingDescriptions) {
        this.pendingDescriptions = pendingDescriptions;
    }
}
