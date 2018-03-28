package cl.minsal.semantikos.model.snomedct;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.snapshots.AuditActionType;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Andrés Farías on 9/26/16.
 */
public class RelationshipSCT extends PersistentEntity implements SnomedCTComponent, Serializable {

    /** Identificador único y numérico (de negocio) de la relación */
    private long idRelationship;

    /**
     * Definition: Specifies the inclusive date at which the component version's state became the then current valid
     * state of the component
     */
    private Timestamp effectiveTime;

    /**
     * <p></p>Si la descripción Snomed CT está vigente
     *
     * <p>Specifies whether the description's state was active or inactive from the nominal release date specified by
     * the
     * effectiveTime</p>
     */
    private boolean active;

    /**
     * <p>Identifies the description version's module. Set to a descendant of |Module| within the metadata
     * hierarchy.</p>
     */
    private long moduleId;

    /** Concepto origen de la relación */
    private long sourceId;

    /** Concepto destino de la relación */
    private long destinationId;

    private long typeId;

    /** Concepto origen de la relación */
    private ConceptSCT sourceConcept;

    /** Concepto destino de la relación */
    private ConceptSCT destinationConcept;

    private long relationshipGroup;

    private ConceptSCT typeConcept;

    private long characteristicTypeId;

    private long modifierId;

    public RelationshipSCT(long idRelationship, Timestamp effectiveTime, boolean active, long moduleId, long sourceId, long destinationId, long typeId, long characteristicTypeId, long modifierId, long relationshipGroup) {
        this.idRelationship = idRelationship;
        this.effectiveTime = effectiveTime;
        this.active = active;
        this.moduleId = moduleId;
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.typeId = typeId;
        this.characteristicTypeId = characteristicTypeId;
        this.modifierId = modifierId;
        this.relationshipGroup = relationshipGroup;
    }

    public RelationshipSCT(long idRelationship, Timestamp effectiveTime, boolean active, long moduleId, ConceptSCT sourceConcept, ConceptSCT destinationConcept, long relationshipGroup, ConceptSCT typeConcept, long characteristicTypeId, long modifierId) {
        this.idRelationship = idRelationship;
        this.effectiveTime = effectiveTime;
        this.active = active;
        this.moduleId = moduleId;
        this.sourceConcept = sourceConcept;
        this.destinationConcept = destinationConcept;
        this.relationshipGroup = relationshipGroup;
        this.typeConcept = typeConcept;
        this.characteristicTypeId = characteristicTypeId;
        this.modifierId = modifierId;
    }

    /**
     * Este es el constructor completo para la clase relationshipSCT
     */



    @Override
    public boolean isPersistent() {
        return false;
    }


    public long getIdRelationship() {
        return idRelationship;
    }

    public void setIdRelationship(long idRelationship) {
        this.idRelationship = idRelationship;
    }

    public Timestamp getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Timestamp effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getModuleId() {
        return moduleId;
    }

    public void setModuleId(long moduleId) {
        this.moduleId = moduleId;
    }

    public long getRelationshipGroup() {
        return relationshipGroup;
    }

    public void setRelationshipGroup(long relationshipGroup) {
        this.relationshipGroup = relationshipGroup;
    }

    public long getCharacteristicTypeId() {
        return characteristicTypeId;
    }

    public void setCharacteristicTypeId(long characteristicTypeId) {
        this.characteristicTypeId = characteristicTypeId;
    }

    public long getModifierId() {
        return modifierId;
    }

    public void setModifierId(long modifierId) {
        this.modifierId = modifierId;
    }

    public ConceptSCT getSourceConcept() {
        return sourceConcept;
    }

    public void setSourceConcept(ConceptSCT sourceConcept) {
        this.sourceConcept = sourceConcept;
    }

    public ConceptSCT getDestinationConcept() {
        return destinationConcept;
    }

    public void setDestinationConcept(ConceptSCT destinationConcept) {
        this.destinationConcept = destinationConcept;
    }

    public ConceptSCT getTypeConcept() {
        return typeConcept;
    }

    public void setTypeConcept(ConceptSCT typeConcept) {
        this.typeConcept = typeConcept;
    }

    @Override
    public AuditActionType evaluateChange(SnomedCTComponent snomedCTComponent) {

        RelationshipSCT that = (RelationshipSCT) snomedCTComponent;

        if(this.equals(that))
            return AuditActionType.SNOMED_CT_UNMODIFYING;

        if(this.isActive() && !that.isActive())
            return AuditActionType.SNOMED_CT_INVALIDATION;

        if(!this.isActive() && that.isActive())
            return AuditActionType.SNOMED_CT_RESTORYING;

        return AuditActionType.SNOMED_CT_UNDEFINED;
    }
}
