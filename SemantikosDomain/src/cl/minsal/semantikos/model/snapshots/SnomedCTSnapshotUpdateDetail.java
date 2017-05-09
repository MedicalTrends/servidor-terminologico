package cl.minsal.semantikos.model.snapshots;

import cl.minsal.semantikos.model.snomedct.SnomedCTComponent;

/**
 * @author Diego Soto
 */
public class SnomedCTSnapshotUpdateDetail {

    private SnomedCTComponent snomedCTComponent;

    private AuditActionType auditActionType;

    public SnomedCTSnapshotUpdateDetail(SnomedCTComponent snomedCTComponent, AuditActionType auditActionType) {
        this.snomedCTComponent = snomedCTComponent;
        this.auditActionType = auditActionType;
    }

    public SnomedCTComponent getSnomedCTComponent() {
        return snomedCTComponent;
    }

    public void setSnomedCTComponent(SnomedCTComponent snomedCTComponent) {
        this.snomedCTComponent = snomedCTComponent;
    }

    public AuditActionType getAuditActionType() {
        return auditActionType;
    }

    public void setAuditActionType(AuditActionType auditActionType) {
        this.auditActionType = auditActionType;
    }
}
