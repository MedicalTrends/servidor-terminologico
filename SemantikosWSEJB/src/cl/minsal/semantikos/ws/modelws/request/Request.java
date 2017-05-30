package cl.minsal.semantikos.ws.modelws.request;

import javax.xml.bind.annotation.*;

/**
 * Created by root on 09-05-17.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "Request", namespace = "http://service.ws.semantikos.minsal.cl/")
public class Request {

    @XmlElement(required = true, defaultValue = "1", name = "idEstablecimiento")
    private String idStablishment;

    public String getIdStablishment() {
        return idStablishment;
    }

    public void setIdStablishment(String idStablishment) {
        this.idStablishment = idStablishment;
    }
}
