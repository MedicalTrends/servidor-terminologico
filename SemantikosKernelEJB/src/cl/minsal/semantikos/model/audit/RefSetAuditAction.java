package cl.minsal.semantikos.model.audit;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.users.User;

import java.sql.Timestamp;

/**
 * @author Andrés Farías on 9/20/16.
 */
public class RefSetAuditAction extends AuditAction{

    private final RefSet refSet;

    public RefSetAuditAction(RefSet refSet, AuditActionType refsetCreation, Timestamp now, User user, ConceptSMTK concept) {
        super(refsetCreation, now, user, refSet,concept);
        this.refSet = refSet;
    }
    public RefSetAuditAction(RefSet refSet, AuditActionType refsetCreation, Timestamp now, User user) {
        super(refsetCreation, now, user, refSet);
        this.refSet = refSet;
    }

    public RefSet getRefSet() {
        return refSet;
    }
}
