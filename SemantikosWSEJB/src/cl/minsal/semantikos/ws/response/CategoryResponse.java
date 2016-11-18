package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Development on 2016-10-11.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "categoria")
public class CategoryResponse implements Serializable {

    @XmlElement(name="nombre")
    private String name;
    @XmlElement(name="nombreAbreviado")
    private String nameAbbreviated;
    @XmlElement(name="restringida")
    private Boolean restriction;
    @XmlElement(name="vigente")
    private Boolean isValid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameAbbreviated() {
        return nameAbbreviated;
    }

    public void setNameAbbreviated(String nameAbbreviated) {
        this.nameAbbreviated = nameAbbreviated;
    }

    public Boolean getRestriction() {
        return restriction;
    }

    public void setRestriction(Boolean restriction) {
        this.restriction = restriction;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }

}
