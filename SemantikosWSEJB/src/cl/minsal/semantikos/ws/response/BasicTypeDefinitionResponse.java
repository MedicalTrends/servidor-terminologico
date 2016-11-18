package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Development on 2016-10-14.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "definicionTipoBasico")
public class BasicTypeDefinitionResponse implements Serializable {

    @XmlElement(name="nombre")
    private String name;
    @XmlElement(name="descripcion")
    private String description;
    @XmlElement(name="intervalo")
    private IntervalResponse interval;
    @XmlElement(name="tipo")
    private BasicTypeTypeResponse type;
    @XmlElementWrapper(name = "dominios")
    @XmlElement(name="dominio")
    private List<String> domain;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IntervalResponse getInterval() {
        return interval;
    }

    public void setInterval(IntervalResponse interval) {
        this.interval = interval;
    }

    public BasicTypeTypeResponse getType() {
        return type;
    }

    public void setType(BasicTypeTypeResponse type) {
        this.type = type;
    }

    public List<String> getDomain() {
        return domain;
    }

    public void setDomain(List<String> domain) {
        this.domain = domain;
    }
}
