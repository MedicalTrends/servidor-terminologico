package cl.minsal.semantikos.ws.response;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.Description;
import cl.minsal.semantikos.model.relationships.Relationship;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Alfonso Cornejo on 2016-10-11.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "conceptoLight", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "ConceptoLight", namespace = "http://service.ws.semantikos.minsal.cl/")
public class ConceptLightResponse implements Serializable {

    /** Identificador de negocio del concepto retornado */
    @XmlElement(name = "conceptID")
    private String conceptId;

    /** <em>DESCRIPTION_ID</em> de la descripción preferida */
    @XmlElement(name = "descriptionIDPreferida")
    private String favouriteDescriptionID;

    @XmlElement(name = "descriptionPreferida")
    private String favouriteDescription;

    @XmlElement(name = "nombreCategoria")
    private String categoryName;

    /*
    @XmlElement(name = "esValido")
    private Boolean valid;
    */

    public ConceptLightResponse() {
    }

    public ConceptLightResponse(ConceptSMTK conceptSMTK) {
        this();

        this.conceptId = conceptSMTK.getConceptID();
        Description descriptionFavorite = conceptSMTK.getDescriptionFavorite();
        this.favouriteDescriptionID = descriptionFavorite.getDescriptionId();
        this.favouriteDescription = descriptionFavorite.getTerm();
        this.categoryName = conceptSMTK.getCategory().getName();
    }

    public String getConceptId() {
        return conceptId;
    }

    public void setConceptId(String conceptId) {
        this.conceptId = conceptId;
    }

    public String getFavouriteDescriptionID() {
        return favouriteDescriptionID;
    }

    public void setFavouriteDescriptionID(String favouriteDescriptionID) {
        this.favouriteDescriptionID = favouriteDescriptionID;
    }

    public String getFavouriteDescription() {
        return favouriteDescription;
    }

    public void setFavouriteDescription(String favouriteDescription) {
        this.favouriteDescription = favouriteDescription;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    /*
    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }
    */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConceptLightResponse)) return false;

        ConceptLightResponse that = (ConceptLightResponse) o;

        return getConceptId() != null ? getConceptId().equals(that.getConceptId()) : that.getConceptId() == null;
    }

    @Override
    public int hashCode() {
        return getConceptId() != null ? getConceptId().hashCode() : 0;
    }
}
