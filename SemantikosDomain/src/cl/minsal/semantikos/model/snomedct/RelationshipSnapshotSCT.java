package cl.minsal.semantikos.model.snomedct;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by des01c7 on 20-03-17.
 */
public class RelationshipSnapshotSCT implements Serializable {


    private long id;
    private Timestamp effectiveTime;
    private boolean active;
    private long moduleId;
    private long sourceId;
    private long destinationId;
    private long relationshipGroup;
    private long typeId;
    private long characteristicTypeId;
    private long modifierId;


    public RelationshipSnapshotSCT(long id, Timestamp effectiveTime, boolean active, long moduleId, long sourceId, long destinationId, long relationshipGroup, long typeId, long characteristicTypeId, long modifierId) {
        this.id = id;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
