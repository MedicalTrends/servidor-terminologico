package cl.minsal.semantikos.ws.response;

import cl.minsal.semantikos.model.Category;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-10-11.
 *
 */
//TODO: Quitar el namespace y ver qué pasa!
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "categoria", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "Categoria", namespace = "http://service.ws.semantikos.minsal.cl/")
public class CategoryResponse implements Serializable {

    private String name;

    private String nameAbbreviated;

    private Boolean restriction;

    public CategoryResponse() {
    }

    public CategoryResponse(Category category) {
        this();

        this.name = category.getName();
        this.nameAbbreviated = category.getNameAbbreviated();
        this.restriction = category.isRestriction();
    }

    @XmlElement(name="nombre")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name="nombreAbreviado")
    public String getNameAbbreviated() {
        return nameAbbreviated;
    }
    public void setNameAbbreviated(String nameAbbreviated) {
        this.nameAbbreviated = nameAbbreviated;
    }

    @XmlElement(name="restringida")
    public Boolean getRestriction() {
        return restriction;
    }
    public void setRestriction(Boolean restriction) {
        this.restriction = restriction;
    }
}
