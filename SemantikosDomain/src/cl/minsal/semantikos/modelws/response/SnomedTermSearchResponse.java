package cl.minsal.semantikos.modelws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "respuestaBuscarTerminoSnomed", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RespuestaBuscarTerminoSnomed", namespace = "http://service.ws.semantikos.minsal.cl/")
public class SnomedTermSearchResponse implements Serializable {

    @XmlElement(name="terminoBuscar")
    private String pattern;

    @XmlElement(name="descripcionesEncontradas")
    private SnomedMatchDescriptionsResponse matchDescriptions;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public SnomedMatchDescriptionsResponse getMatchDescriptions() {
        return matchDescriptions;
    }

    public void setMatchDescriptions(SnomedMatchDescriptionsResponse matchDescriptions) {
        this.matchDescriptions = matchDescriptions;
    }
}
