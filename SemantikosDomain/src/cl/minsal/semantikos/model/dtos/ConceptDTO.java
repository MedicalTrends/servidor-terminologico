package cl.minsal.semantikos.model.dtos;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.audit.AuditableEntity;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.crossmaps.CrossMapType;
import cl.minsal.semantikos.model.crossmaps.Crossmap;
import cl.minsal.semantikos.model.crossmaps.DirectCrossmap;
import cl.minsal.semantikos.model.crossmaps.IndirectCrossmap;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.DescriptionType;
import cl.minsal.semantikos.model.descriptions.DescriptionTypeFactory;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.relationships.*;
import cl.minsal.semantikos.model.tags.Label;
import cl.minsal.semantikos.model.tags.Tag;
import cl.minsal.semantikos.model.tags.TagSMTK;

import javax.ejb.EJBException;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.currentTimeMillis;

/**
 * Esta clase representa al Concepto Semantikos.
 *
 * @author Diego Soto.
 */
public class ConceptDTO extends PersistentEntity implements Serializable {

    /** El valor de negocio del concept_id */
    private String conceptID;

    /** La categoría del concepto */
    private long idCategory;

    /** Si debe ser revisado */
    private boolean toBeReviewed;

    /** Si debe ser consultado? */
    private boolean toBeConsulted;

    /** Si el concepto se encuentra modelado o no */
    private boolean modeled;

    /**
     * Este campo establece si el concepto está completamente definido o si es primitivo. Por defecto, el concepto se
     * considera primitivo
     */
    private Boolean isFullyDefined;

    /** Determina si el concepto está publicado o no */
    private boolean isPublished;

    /** Fecha hasta la cual el concepto se encuentra vigente */
    private Timestamp validUntil;

    /** Otros descriptores */
    private List<DescriptionDTO> descriptionsDTO = new ArrayList<>();

    /** Observación del Concepto */
    private String observation;

    /** Lista de etiquetas */
    private List<Tag> tags = new ArrayList<>();

    /** El Tag Semántikos que tiene asociado el concepto */
    private long idTagSMTK;

    /** Variable que indica si el grado de definición se obtiene heredado * */
    private boolean inherited;

    public ConceptDTO() {
    }

    public List<DescriptionDTO> getDescriptionsDTO() {
        return new ArrayList<>(descriptionsDTO);
    }

    public void setDescriptionsDTO(List<DescriptionDTO> descriptionsDTO) {
        this.descriptionsDTO = descriptionsDTO;
    }

    public String getConceptID() {
        return conceptID;
    }

    public void setConceptID(String conceptID) {
        this.conceptID = conceptID;
    }

    public boolean isToBeReviewed() {
        return toBeReviewed;
    }

    public void setToBeReviewed(boolean toBeReviewed) {
        this.toBeReviewed = toBeReviewed;
    }

    public boolean isToBeConsulted() {
        return toBeConsulted;
    }

    public void setToBeConsulted(boolean toBeConsulted) {
        this.toBeConsulted = toBeConsulted;
    }

    public Boolean isFullyDefined() {
        return isFullyDefined;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getObservation() {
        return observation==null?"":observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Timestamp getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Timestamp validUntil) {
        this.validUntil = validUntil;
    }

    /**
     * Este método es responsable de establecer si un concepto es completamente definido.
     */
    public void setFullyDefined(Boolean fullyDefined) {
        this.isFullyDefined = fullyDefined;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {

        this.isPublished = published;
    }


    @Override
    public String toString() {

        String toString = "CONCEPT_ID=" + this.conceptID;
        if (descriptionsDTO.isEmpty()) {
            return toString;
        }

        DescriptionDTO aDescription = this.descriptionsDTO.get(0);
        return toString + " - " + aDescription.getIdDescriptionType() + ": " + aDescription.getTerm();
    }

    public boolean isModeled() {
        return this.modeled;
    }

    public void setModeled(boolean modeled) {
        this.modeled = modeled;
    }

    public boolean isInherited() {
        return inherited;
    }

    public void setInherited(boolean inherited) {
        this.inherited = inherited;
    }

    /**
     * Este método es responsable de determinar si esta descripción es válida
     *
     * @return <code>true</code> si es válida y <code>false</code> si no lo es.
     */
    public boolean isValid() {
        return (getValidUntil() == null || getValidUntil().after(new Timestamp(currentTimeMillis())));
    }

    /**
     * Sobreescritura de este método para poder usar objetos de esta clase en un HashSet. Andrés por favor no lo borres.
     * @author Alfonso Cornejo
     */
    @Override
    public int hashCode() {
        return getConceptID() != null ? getConceptID().hashCode() : 0;
    }

    public long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(long idCategory) {
        this.idCategory = idCategory;
    }

    public long getIdTagSMTK() {
        return idTagSMTK;
    }

    public void setIdTagSMTK(long idTagSMTK) {
        this.idTagSMTK = idTagSMTK;
    }

    public Boolean getFullyDefined() {
        return isFullyDefined;
    }
}
