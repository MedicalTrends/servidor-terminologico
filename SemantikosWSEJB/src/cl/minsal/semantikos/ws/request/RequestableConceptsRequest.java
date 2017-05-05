package cl.minsal.semantikos.ws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author Alfonso Cornejo on 2016-11-23.
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "peticionObtenerTerminosPedibles", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionObtenerTerminosPedibles", namespace = "http://service.ws.semantikos.minsal.cl/")
public class RequestableConceptsRequest implements Serializable {

    private boolean requestable;

    private List<String> categoryNames;

    private List<String> refSetNames;

    private String idStablishment;

    @XmlElement(name = "nombreCategoria")
    public List<String> getCategoryNames() {
        return categoryNames;
    }
    public void setCategoryNames(List<String> categoryNames) {
        this.categoryNames = categoryNames;
    }

    /*
    @XmlElement(name = "nombreRefSet")
    public List<String> getRefSetNames() {
        return refSetNames;
    }
    public void setRefSetNames(List<String> refSetNames) {
        this.refSetNames = refSetNames;
    }
    */

    @XmlElement(required = true, name = "pedible")
    public boolean getRequestable() {
        return requestable;
    }
    public void setRequestable(boolean requestable) {
        this.requestable = requestable;
    }

    @XmlElement(required = true, name = "idEstablecimiento")
    public String getIdStablishment() {
        return idStablishment;
    }
    public void setIdStablishment(String idStablishment) {
        this.idStablishment = idStablishment;
    }
}
