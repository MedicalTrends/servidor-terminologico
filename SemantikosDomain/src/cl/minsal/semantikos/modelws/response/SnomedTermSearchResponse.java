package cl.minsal.semantikos.modelws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "respuestaBuscarTermino", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RespuestaBuscarTermino", namespace = "http://service.ws.semantikos.minsal.cl/")
public class SnomedTermSearchResponse implements Serializable {

    @XmlElement(name="terminoBuscar")
    private String pattern;

    @XmlElement(name="descripcionesEncontradas")
    private SnomedMatchDescriptionsResponse perfectMatchDescriptions;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public SnomedMatchDescriptionsResponse getPerfectMatchDescriptions() {
        return perfectMatchDescriptions;
    }

    public void setPerfectMatchDescriptions(SnomedMatchDescriptionsResponse perfectMatchDescriptions) {
        this.perfectMatchDescriptions = perfectMatchDescriptions;
    }

}
