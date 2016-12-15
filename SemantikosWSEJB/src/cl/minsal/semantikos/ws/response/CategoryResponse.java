package cl.minsal.semantikos.ws.response;

import cl.minsal.semantikos.model.Category;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-10-11.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "categoria", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "Categoria", namespace = "http://service.ws.semantikos.minsal.cl/")
public class CategoryResponse implements Serializable {

    @XmlElement(name="nombre")
    private String name;
    @XmlElement(name="nombreAbreviado")
    private String nameAbbreviated;
    @XmlElement(name="restringida")
    private Boolean restriction;

    public CategoryResponse() {
    }

    public CategoryResponse(Category category) {
        this();

        this.name = category.getName();
        this.nameAbbreviated = category.getNameAbbreviated();
        this.restriction = category.isRestriction();
    }

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
}
