package cl.minsal.semantikos.model.snomedct;

import cl.minsal.semantikos.model.IPersistentEntity;
import cl.minsal.semantikos.model.snapshots.AuditActionType;

/**
 * Created by root on 29-03-17.
 */
public interface SnomedCTComponent extends IPersistentEntity {

    AuditActionType evaluateChange(SnomedCTComponent snomedCTComponent);
}
