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
@XmlRootElement(name = "peticionSugerenciasSnomed", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionSugerenciasSnomed", namespace = "http://service.ws.semantikos.minsal.cl/")
public class SnomedSuggestionsRequest extends Request implements Serializable {

    private String term;

    @XmlElement(required = true, name = "termino")
    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void validate() throws IllegalInputFault {
        super.validate();
        if(getTerm() == null || getTerm().isEmpty() || getTerm().length() < 3) {
            throw new IllegalInputFault("El termino a buscar debe tener minimo 3 caracteres de largo");
        }
    }

}
