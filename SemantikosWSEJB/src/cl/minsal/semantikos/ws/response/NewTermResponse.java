package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-11-02.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "respuestaCodificacionDeNuevoTermino", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RespuestaCodificacionDeNuevoTermino", namespace = "http://service.ws.semantikos.minsal.cl/")
public class NewTermResponse implements Serializable {

    @XmlElement(name="estado")
    private String status;
    @XmlElement(name="concepto")
    private ConceptResponse conceptResponse;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ConceptResponse getConceptResponse() {
        return conceptResponse;
    }

    public void setConceptResponse(ConceptResponse conceptResponse) {
        this.conceptResponse = conceptResponse;
    }
}
