package cl.minsal.semantikos.modelws.response;

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

    @XmlElement(name="descriptionID")
    private String descriptionID;

    @XmlElement(name="terminoPropuesto")
    private String term;

    public NewTermResponse() {
    }

    public NewTermResponse(String descriptionId) {
        this();

        this.descriptionID = descriptionId;
    }

    public NewTermResponse(String descriptionId, String term) {
        this();

        this.descriptionID = descriptionId;
        this.term = term;
    }

    public String getDescriptionID() {
        return descriptionID;
    }

    public void setDescriptionID(String descriptionID) {
        this.descriptionID = descriptionID;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
