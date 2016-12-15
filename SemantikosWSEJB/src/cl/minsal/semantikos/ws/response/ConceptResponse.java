package cl.minsal.semantikos.ws.response;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.Description;
import cl.minsal.semantikos.model.relationships.Relationship;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Alfonso Cornejo on 2016-10-11.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "concepto", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "Concepto", namespace = "http://service.ws.semantikos.minsal.cl/")
public class ConceptResponse implements Serializable {

    @XmlElement(name = "id")
    private String conceptId;
    @XmlElement(name = "aSerRevisado")
    private Boolean toBeReviewed;
    @XmlElement(name = "aSerConsultado")
    private Boolean toBeConsulted;
    @XmlElement(name = "modelado")
    private Boolean modeled;
    @XmlElement(name = "completamenteDefinido")
    private Boolean fullyDefined;

    @XmlElement(name = "publicado")
    private Boolean isPublished;

    @XmlElement(name = "valido")
    private Boolean isValid;

    @XmlElement(name = "validoHasta")
    private Date validUntil;

    @XmlElement(name = "observacion")
    private String observation;

    @XmlElement(name = "tagSMTK")
    private TagSMTKResponse tagSMTK;

    @XmlElement(name = "categoria")
    private CategoryResponse category;

    @XmlElementWrapper(name = "refSets")
    @XmlElement(name = "refSet")
    private List<RefSetResponse> refsets;

    @XmlElementWrapper(name = "descripciones")
    @XmlElement(name = "descripcion")
    private List<DescriptionResponse> descriptions;

    @XmlElementWrapper(name = "atributos")
    @XmlElement(name = "atributo")
    private List<AttributeResponse> attributes;

    @XmlElementWrapper(name = "relaciones")
    @XmlElement(name = "relacion")
    private List<RelationshipResponse> relationships;

    public ConceptResponse() {
        this.relationships = new ArrayList<>();
        this.attributes = new ArrayList<>();
        this.descriptions = new ArrayList<>();
        this.refsets = new ArrayList<>();
    }

    public ConceptResponse(ConceptSMTK conceptSMTK) {
        this();

        this.isPublished = conceptSMTK.isPublished();
        this.modeled = conceptSMTK.isModeled();
        this.conceptId = conceptSMTK.getConceptID();
        this.fullyDefined = conceptSMTK.isFullyDefined();
        this.observation = conceptSMTK.getObservation();
        this.toBeConsulted = conceptSMTK.isToBeConsulted();
        this.toBeReviewed = conceptSMTK.isToBeReviewed();
        this.validUntil = new Date(conceptSMTK.getValidUntil().getTime());
        this.isValid = this.validUntil.after(new Date());

        /* Se cargan las otras propiedades del concepto */
        loadPreferredDescriptions(conceptSMTK);
        loadAttributes(conceptSMTK);
        this.setCategory(new CategoryResponse(conceptSMTK.getCategory()));
    }

    public String getConceptId() {
        return conceptId;
    }

    public void setConceptId(String conceptId) {
        this.conceptId = conceptId;
    }

    public Boolean getToBeReviewed() {
        return toBeReviewed;
    }

    public void setToBeReviewed(Boolean toBeReviewed) {
        this.toBeReviewed = toBeReviewed;
    }

    public Boolean getToBeConsulted() {
        return toBeConsulted;
    }

    public void setToBeConsulted(Boolean toBeConsulted) {
        this.toBeConsulted = toBeConsulted;
    }

    public Boolean getModeled() {
        return modeled;
    }

    public void setModeled(Boolean modeled) {
        this.modeled = modeled;
    }

    public Boolean getFullyDefined() {
        return fullyDefined;
    }

    public void setFullyDefined(Boolean fullyDefined) {
        this.fullyDefined = fullyDefined;
    }

    public Boolean getPublished() {
        return isPublished;
    }

    public void setPublished(Boolean published) {
        isPublished = published;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
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

    /**
     * Este método es responsable de cargar las descripciones de un concepto (<code>toConceptResponse</code>) a otro
     * (<code>sourceConcept</code>).
     *
     * @param sourceConcept El concepto desde el cual se cargan las descripciones
     */
    private void loadPreferredDescriptions(@NotNull ConceptSMTK sourceConcept) {
        for (Description description : sourceConcept.getDescriptions()) {
            this.descriptions.add(new DescriptionResponse(description));
        }
    }

    /**
     * Este método es responsable de caregar en este concepto los atributos de un concepto fuente.
     *
     * @param sourceConcept El concepto desde el cual se cargan los atrubos.
     */
    private void loadAttributes(@NotNull ConceptSMTK sourceConcept) {
        for (Relationship relationship : sourceConcept.getRelationshipsBasicType()) {
            this.attributes.add(new AttributeResponse(relationship));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConceptResponse)) return false;

        ConceptResponse that = (ConceptResponse) o;

        return getConceptId() != null ? getConceptId().equals(that.getConceptId()) : that.getConceptId() == null;
    }

    @Override
    public int hashCode() {
        return getConceptId() != null ? getConceptId().hashCode() : 0;
    }
}
