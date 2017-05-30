package cl.minsal.semantikos.ws.modelws.response;

import cl.minsal.semantikos.model.ConceptSMTK;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Development on 2016-11-04.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "respuestaConceptosRelacionados", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "RespuestaConceptosRelacionados", namespace = "http://service.ws.semantikos.minsal.cl/")
public class RelatedConceptsResponse implements Serializable {

    @XmlElement(name="conceptID")
    private String conceptId;
    @XmlElement(name="descriptionIDPreferida")
    private String descriptionId;
    @XmlElement(name="descriptionPreferida")
    private String description;
    @XmlElement(name="nombreCategoria")
    private String category;

    @XmlElementWrapper(name="conceptosRelacionados")
    @XmlElement(name="concepto")
    private List<ConceptResponse> relatedConcepts;
    @XmlElement(name = "cantidadRegistros")
    private int quantity;

    public RelatedConceptsResponse() {
    }

    public RelatedConceptsResponse(ConceptSMTK conceptSMTK) {
        this.conceptId = conceptSMTK.getConceptID();
        this.descriptionId = conceptSMTK.getDescriptionFavorite().getDescriptionId();
        this.description = conceptSMTK.getDescriptionFavorite().getTerm();
        this.category = conceptSMTK.getCategory().getName();
    }

    public String getConceptId() {
        return conceptId;
    }

    public void setConceptId(String conceptId) {
        this.conceptId = conceptId;
    }

    public String getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(String descriptionId) {
        this.descriptionId = descriptionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<ConceptResponse> getRelatedConcepts() {
        return relatedConcepts;
    }

    public void setRelatedConcepts(List<ConceptResponse> relatedConcepts) {
        this.relatedConcepts = relatedConcepts;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
