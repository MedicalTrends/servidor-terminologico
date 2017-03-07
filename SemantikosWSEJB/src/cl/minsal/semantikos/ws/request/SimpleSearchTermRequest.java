package cl.minsal.semantikos.ws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Development on 2016-11-23.
 *
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "peticionBuscarTerminoSimple", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionBuscarTerminoSimple", namespace = "http://service.ws.semantikos.minsal.cl/")
public class SimpleSearchTermRequest implements Serializable {

    private String term;

    private List<String> categoryNames;

    private List<String> refSetNames;

    private String idStablishment;

    @XmlElement(required = true, name = "termino")
    public String getTerm() {
        return term;
    }
    public void setTerm(String term) {
        this.term = term;
    }

    @XmlElement(required = false, name = "nombreCategoria")
    public List<String> getCategoryNames() {
        return categoryNames;
    }
    public void setCategoryNames(List<String> categoryNames) {
        this.categoryNames = categoryNames;
    }

    @XmlElement(required = false, name = "nombreRefSet")
    public List<String> getRefSetNames() {
        return refSetNames;
    }
    public void setRefSetNames(List<String> refSetNames) {
        this.refSetNames = refSetNames;
    }

    @XmlElement(required = true, name="idEstablecimiento")
    public String getIdStablishment() {
        return idStablishment;
    }
    public void setIdStablishment(String idStablishment) {
        this.idStablishment = idStablishment;
    }

}
