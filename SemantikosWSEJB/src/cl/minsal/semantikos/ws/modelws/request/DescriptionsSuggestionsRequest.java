package cl.minsal.semantikos.ws.modelws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Development on 2016-11-23.
 *
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "peticionSugerenciasDeDescripciones", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionSugerenciasDeDescripciones", namespace = "http://service.ws.semantikos.minsal.cl/")
public class DescriptionsSuggestionsRequest extends Request implements Serializable {

    private String term;

    private List<String> categoryNames;
    //@XmlElement(required = false, name = "nombreRefSet")
    //private List<String> refSetNames;

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

    /*
    public List<String> getRefSetNames() {
        return refSetNames;
    }

    public void setRefSetNames(List<String> refSetNames) {
        this.refSetNames = refSetNames;
    }
    */
}
