package cl.minsal.semantikos.ws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Development on 2016-11-23.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "peticionObtenerTerminosPedibles", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionObtenerTerminosPedibles", namespace = "http://service.ws.semantikos.minsal.cl/")
public class GetRequestableTermsRequest implements Serializable {

    @XmlElement(required = false, name = "nombreCategoria")
    private List<String> categoryNames;

    @XmlElement(required = false, name = "nombreRefSet")
    private List<String> refSetNames;

    @XmlElement(required = true, name = "pedible")
    private Boolean requestable;

    public List<String> getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(List<String> categoryNames) {
        this.categoryNames = categoryNames;
    }

    public List<String> getRefSetNames() {
        return refSetNames;
    }

    public void setRefSetNames(List<String> refSetNames) {
        this.refSetNames = refSetNames;
    }

    public Boolean getRequestable() {
        return requestable;
    }

    public void setRequestable(Boolean requestable) {
        this.requestable = requestable;
    }
}
