package cl.minsal.semantikos.ws.response;

import cl.minsal.semantikos.model.Description;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrés Farías on 12/13/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "descripcionPendiente", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "DescripcionPendiente", namespace = "http://service.ws.semantikos.minsal.cl/")
public class PendingDescriptionResponse implements Serializable {

    @XmlElement(name="pendienteCodificacion")
    private Boolean pendingCodification;
    @XmlElement(name="nombreCategoria")
    private String categoryName;
    @XmlElement(name="terminoPreferido")
    private String preferredTerm;
    @XmlElement(name="validez")
    private Boolean valid;
    @XmlElementWrapper(name="descripciones")
    @XmlElement(name="descripcion")
    private List<SimplifiedDescriptionResponse> descriptions;

    public PendingDescriptionResponse() {}

    public PendingDescriptionResponse(@NotNull Description description, @NotNull List<Description> descriptions) {
        this.pendingCodification = true;
        this.valid = description.isValid();
        this.categoryName = description.getConceptSMTK().getCategory().getName();
        this.preferredTerm = description.getConceptSMTK().getDescriptionFavorite().getTerm();
        this.descriptions = new ArrayList<>(descriptions.size());
        for ( Description description1 : descriptions ) {
            this.descriptions.add(new SimplifiedDescriptionResponse(description1));
        }
    }

    public Boolean getPendingCodification() {
        return pendingCodification;
    }

    public void setPendingCodification(Boolean pendingCodification) {
        this.pendingCodification = pendingCodification;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPreferredTerm() {
        return preferredTerm;
    }

    public void setPreferredTerm(String preferredTerm) {
        this.preferredTerm = preferredTerm;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public List<SimplifiedDescriptionResponse> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<SimplifiedDescriptionResponse> descriptions) {
        this.descriptions = descriptions;
    }
}
