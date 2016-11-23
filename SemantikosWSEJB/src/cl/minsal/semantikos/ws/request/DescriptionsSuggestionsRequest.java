package cl.minsal.semantikos.ws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Development on 2016-11-23.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "peticionSugerenciasDeDescripciones", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionSugerenciasDeDescripciones", namespace = "http://service.ws.semantikos.minsal.cl/")
public class DescriptionsSuggestionsRequest implements Serializable {

    @XmlElement(required = true, name = "termino")
    private String term;
    @XmlElement(required = false, name = "nombreCategoria")
    private List<String> categoryNames;
    @XmlElement(required = false, name = "nombreRefSet")
    private List<String> refSetNames;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

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
}
