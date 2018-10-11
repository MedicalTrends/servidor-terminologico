package cl.minsal.semantikos.modelws.request;

import cl.minsal.semantikos.modelws.fault.IllegalInputFault;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Development on 2016-11-23.
 *
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "peticionSugerenciasDeDescripciones2", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionSugerenciasDeDescripciones2", namespace = "http://service.ws.semantikos.minsal.cl/")
public class DescriptionsSuggestionsRequest2 extends Request implements Serializable {

    private String term;

    private List<String> categoryNames;

    private List<String> refSetNames;

    private List<String> descriptionTypeNames;

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

    @XmlElement(required = false, name = "nombreRefSet")
    public List<String> getRefSetNames() {
        return refSetNames;
    }

    public void setRefSetNames(List<String> refSetNames) {
        this.refSetNames = refSetNames;
    }

    @XmlElement(required = false, name = "nombreTipoDescripcion")
    public List<String> getDescriptionTypeNames() {
        return descriptionTypeNames;
    }

    public void setDescriptionTypeNames(List<String> descriptionTypeNames) {
        this.descriptionTypeNames = descriptionTypeNames;
    }

    public void setCategoryNames(List<String> categoryNames) {
        this.categoryNames = categoryNames;
    }

    public void validate() throws IllegalInputFault {
        super.validate();
        if (isEmpty(getCategoryNames())) {
            throw new IllegalInputFault("Debe ingresar por lo menos una Categoría.");
        }
        if ( getTerm().length() < 3 ) {
            throw new IllegalInputFault("El termino a buscar debe tener minimo 3 caracteres de largo");
        }
    }
}
