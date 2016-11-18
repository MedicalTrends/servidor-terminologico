package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Development on 2016-10-11.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "concepto")
public class ConceptResponse implements Serializable {

    @XmlElement(name="id")
    private String conceptId;
    @XmlElement(name="aSerRevisado")
    private Boolean isToBeReviewed;
    @XmlElement(name="aSerConsultado")
    private Boolean isToBeConsulted;
    @XmlElement(name="modelado")
    private Boolean modeled;
    @XmlElement(name="completamenteDefinido")
    private Boolean isFullyDefined;
    @XmlElement(name="publicado")
    private Boolean isPublished;
    @XmlElement(name="validoHasta")
    private Date validUntil;
    @XmlElement(name="observacion")
    private String observation;
    @XmlElement(name="tagSMTK")
    private TagSMTKResponse tagSMTK;
    @XmlElement(name="categoria")
    private CategoryResponse category;
    @XmlElementWrapper(name = "refSets")
    @XmlElement(name="refSet")
    private List<RefSetResponse> refsets;
    @XmlElementWrapper(name = "descripciones")
    @XmlElement(name="descripcion")
    private List<DescriptionResponse> descriptions;
    @XmlElementWrapper(name = "atributos")
    @XmlElement(name="atributo")
    private List<AttributeResponse> attributes;
    @XmlElementWrapper(name = "relaciones")
    @XmlElement(name="relacion")
    private List<RelationshipResponse> relationships;

    public String getConceptId() {
        return conceptId;
    }

    public void setConceptId(String conceptId) {
        this.conceptId = conceptId;
    }

    public Boolean getToBeReviewed() {
        return isToBeReviewed;
    }

    public void setToBeReviewed(Boolean toBeReviewed) {
        isToBeReviewed = toBeReviewed;
    }

    public Boolean getToBeConsulted() {
        return isToBeConsulted;
    }

    public void setToBeConsulted(Boolean toBeConsulted) {
        isToBeConsulted = toBeConsulted;
    }

    public Boolean getModeled() {
        return modeled;
    }

    public void setModeled(Boolean modeled) {
        this.modeled = modeled;
    }

    public Boolean getFullyDefined() {
        return isFullyDefined;
    }

    public void setFullyDefined(Boolean fullyDefined) {
        isFullyDefined = fullyDefined;
    }

    public Boolean getPublished() {
        return isPublished;
    }

    public void setPublished(Boolean published) {
        isPublished = published;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public TagSMTKResponse getTagSMTK() {
        return tagSMTK;
    }

    public void setTagSMTK(TagSMTKResponse tagSMTK) {
        this.tagSMTK = tagSMTK;
    }

    public List<RefSetResponse> getRefsets() {
        return refsets;
    }

    public void setRefsets(List<RefSetResponse> refsets) {
        this.refsets = refsets;
    }

    public CategoryResponse getCategory() {
        return category;
    }

    public void setCategory(CategoryResponse category) {
        this.category = category;
    }

    public List<DescriptionResponse> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<DescriptionResponse> descriptions) {
        this.descriptions = descriptions;
    }

    public List<AttributeResponse> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeResponse> attributes) {
        this.attributes = attributes;
    }

    public List<RelationshipResponse> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<RelationshipResponse> relationships) {
        this.relationships = relationships;
    }
}
