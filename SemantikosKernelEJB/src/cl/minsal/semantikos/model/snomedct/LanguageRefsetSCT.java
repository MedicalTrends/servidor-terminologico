package cl.minsal.semantikos.model.snomedct;

import java.sql.Timestamp;

/**
 * Created by des01c7 on 20-03-17.
 */
public class LanguageRefsetSCT {


    private String id;
    private Timestamp effectiveTime;
    private boolean active;
    private long moduleId;
    private long refsetId;
    private long referencedComponentId;
    private long acceptabilityId;

    public LanguageRefsetSCT(String id, Timestamp effectiveTime, boolean active, long moduleId, long refsetId, long referencedComponentId, long acceptabilityId) {
        this.id = id;
        this.effectiveTime = effectiveTime;
        this.active = active;
        this.moduleId = moduleId;
        this.refsetId = refsetId;
        this.referencedComponentId = referencedComponentId;
        this.acceptabilityId = acceptabilityId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public long getRefsetId() {
        return refsetId;
    }

    public void setRefsetId(long refsetId) {
        this.refsetId = refsetId;
    }

    public long getReferencedComponentId() {
        return referencedComponentId;
    }

    public void setReferencedComponentId(long referencedComponentId) {
        this.referencedComponentId = referencedComponentId;
    }

    public long getAcceptabilityId() {
        return acceptabilityId;
    }

    public void setAcceptabilityId(long acceptabilityId) {
        this.acceptabilityId = acceptabilityId;
    }
}

