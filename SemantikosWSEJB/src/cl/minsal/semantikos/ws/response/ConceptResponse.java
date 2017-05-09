package cl.minsal.semantikos.ws.response;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.relationships.Relationship;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.sql.Timestamp;
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

    @XmlElement(name = "conceptID")
    private String conceptId;
    @XmlElement(name = "revisar")
    private Boolean toBeReviewed;
    @XmlElement(name = "consultar")
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

    @XmlElement(name="fechaCreacion")
    private Date creationDate;

    @XmlElement(name="fechaPublicacion")
    private Date publishingDate;

    @XmlElement(name = "observacion")
    private String observation;

    @XmlElement(name = "categoria")
    private CategoryResponse category;

    @XmlElementWrapper(name = "descripciones")
    @XmlElement(name = "descripcion")
    private List<DescriptionResponse> descriptions;

    @XmlElementWrapper(name = "refSets")
    @XmlElement(name = "refSet")
    private List<RefSetResponse> refsets;

    @XmlElementWrapper(name = "atributos")
    @XmlElement(name = "atributo")
    private List<AttributeResponse> attributes;

    @XmlElementWrapper(name = "relaciones")
    @XmlElement(name = "relacion")
    private List<RelationshipResponse> relationships;

    @XmlElementWrapper(name = "relacionesSnomedCT")
    @XmlElement(name = "relacionSnomedCT")
    private List<SnomedCTRelationshipResponse> snomedCTRelationshipResponses;

    @XmlElementWrapper(name = "crossmapsIndirectos")
    @XmlElement(name = "crossmapIndirecto")
    private List<IndirectCrossMapResponse> indirectCrossMaps;

    @XmlElementWrapper(name = "crossmapsDirectos")
    @XmlElement(name = "crossmapDirecto")
    private List<CrossmapSetMemberResponse> crossmapSetMember;

    public ConceptResponse() {
        this.relationships = new ArrayList<>();
        this.attributes = new ArrayList<>();
        this.descriptions = new ArrayList<>();
        this.refsets = new ArrayList<>();
        this.snomedCTRelationshipResponses = new ArrayList<>();
        this.indirectCrossMaps = new ArrayList<>();
        this.crossmapSetMember = new ArrayList<>();
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

        Timestamp theValidityUntil = conceptSMTK.getValidUntil();
        this.validUntil = conceptSMTK.getValidUntil(); // new Date(theValidityUntil==null ? System.currentTimeMillis() : theValidityUntil.getTime());
        this.isValid = conceptSMTK.isValid(); //this.validUntil.after(new Date());

        /* Se cargan las otras propiedades del concepto */
        loadDescriptions(conceptSMTK);
        if(conceptSMTK.isRelationshipsLoaded()) {
            loadAttributes(conceptSMTK);
        }
        this.setCategory(new CategoryResponse(conceptSMTK.getCategory()));
    }

    /**
     * Este método es responsable de cargar las descripciones de un concepto (<code>toConceptResponse</code>) a otro
     * (<code>sourceConcept</code>).
     *
     * @param sourceConcept El concepto desde el cual se cargan las descripciones
     */
    private void loadDescriptions(@NotNull ConceptSMTK sourceConcept) {
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

    /**
     * Elimina de la respuesta los campos que no son necesarios retornar en le respuesta del WS-002.
     */
    public void setForREQWS002() {
        this.toBeReviewed = null;
        this.toBeConsulted = null;
        this.modeled = null;
        this.fullyDefined = null;
        this.isPublished = null;
        this.relationships = null;
        this.refsets = null;
        this.indirectCrossMaps = null;
        this.crossmapSetMember = null;

        for ( DescriptionResponse descriptionResponse : this.descriptions ) {
            descriptionResponse.setForREQWS002();
        }
    }

    /**
     * Elimina de la respuesta los campos que no son necesarios retornar en le respuesta del WS-028.
     */
    public void setForREQWS028() {
        //this.toBeReviewed = null;
        //this.toBeConsulted = null;
        //this.modeled = null;
        //this.fullyDefined = null;
        //this.isPublished = null;
        this.relationships = null;

        for ( DescriptionResponse descriptionResponse : this.descriptions ) {
            descriptionResponse.setForREQWS028();
        }
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

    public List<SnomedCTRelationshipResponse> getSnomedCTRelationshipResponses() {
        return snomedCTRelationshipResponses;
    }

    public void setSnomedCTRelationshipResponses(List<SnomedCTRelationshipResponse> snomedCTRelationshipResponses) {
        this.snomedCTRelationshipResponses = snomedCTRelationshipResponses;
    }

    public List<IndirectCrossMapResponse> getIndirectCrossMaps() {
        return indirectCrossMaps;
    }

    public void setIndirectCrossMaps(List<IndirectCrossMapResponse> indirectCrossMaps) {
        this.indirectCrossMaps = indirectCrossMaps;
    }

    public List<CrossmapSetMemberResponse> getCrossmapSetMember() {
        return crossmapSetMember;
    }

    public void setCrossmapSetMember(List<CrossmapSetMemberResponse> crossmapSetMember) {
        this.crossmapSetMember = crossmapSetMember;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(Date publishingDate) {
        this.publishingDate = publishingDate;
    }

}
