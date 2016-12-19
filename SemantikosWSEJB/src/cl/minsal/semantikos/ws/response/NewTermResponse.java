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

    @XmlElement(name="id_descripcion")
    private String descriptionID;

    public NewTermResponse() {
    }

    public NewTermResponse(String descriptionId) {
        this();

        this.descriptionID = descriptionId;
    }

    public String getDescriptionID() {
        return descriptionID;
    }

    public void setDescriptionID(String descriptionID) {
        this.descriptionID = descriptionID;
    }
}
