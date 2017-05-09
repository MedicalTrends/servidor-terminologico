package cl.minsal.semantikos.model.snomedct;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.snapshots.AuditActionType;

import java.sql.Timestamp;

/**
 * @author Andrés Farías on 9/26/16.
 */
public class RelationshipSCT extends PersistentEntity implements SnomedCTComponent {

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

    private long relationshipGroup;

    private long typeId;

    private long characteristicTypeId;

    private long modifierId;

    /**
     * Este es el constructor completo para la clase relationshipSCT
     */
    public RelationshipSCT(long idRelationship, Timestamp effectiveTime, boolean active, long moduleId, long sourceId, long destinationId, long relationshipGroup, long typeId, long characteristicTypeId, long modifierId) {
        super(idRelationship);
        this.idRelationship = idRelationship;
        this.effectiveTime = effectiveTime;
        this.active = active;
        this.moduleId = moduleId;
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.relationshipGroup = relationshipGroup;
        this.typeId = typeId;
        this.characteristicTypeId = characteristicTypeId;
        this.modifierId = modifierId;
    }

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

    public long getSourceId() {
        return sourceId;
    }

    public void setSourceId(long sourceId) {
        this.sourceId = sourceId;
    }

    public long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(long destinationId) {
        this.destinationId = destinationId;
    }

    public long getRelationshipGroup() {
        return relationshipGroup;
    }

    public void setRelationshipGroup(long relationshipGroup) {
        this.relationshipGroup = relationshipGroup;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
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
