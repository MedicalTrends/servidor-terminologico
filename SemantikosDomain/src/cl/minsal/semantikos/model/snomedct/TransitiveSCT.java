package cl.minsal.semantikos.model.snomedct;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.snapshots.AuditActionType;

import java.io.Serializable;

/**
 * Created by des01c7 on 20-03-17.
 */
public class TransitiveSCT extends PersistentEntity implements SnomedCTComponent, Serializable {

    private long idPartent;
    private long idChild;

    public TransitiveSCT(long idPartent, long idChild) {
        this.idPartent = idPartent;
        this.idChild = idChild;
    }

    public long getIdPartent() {
        return idPartent;
    }

    public void setIdPartent(long idPartent) {
        this.idPartent = idPartent;
    }

    public long getIdChild() {
        return idChild;
    }

    public void setIdChild(long idChild) {
        this.idChild = idChild;
    }

    @Override
    public long getId() {

        return Long.parseLong(String.valueOf(this.getIdPartent())+String.valueOf(this.getIdChild()));

    }

    @Override
    public AuditActionType evaluateChange(SnomedCTComponent snomedCTComponent) {

        DescriptionSCT that = (DescriptionSCT) snomedCTComponent;

        if(this.equals(that))
            return AuditActionType.SNOMED_CT_UNMODIFYING;

        return AuditActionType.SNOMED_CT_UNDEFINED;
    }
}
