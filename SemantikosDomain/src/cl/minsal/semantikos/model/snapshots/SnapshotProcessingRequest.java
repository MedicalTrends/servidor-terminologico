package cl.minsal.semantikos.model.snapshots;

import cl.minsal.semantikos.model.snomedct.SnomedCTComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 31-03-17.
 */

/**
 * Esta clase representa una petición de procesamiento de un bloque de registros de un componente del snapshot
 * SnomedCT
 */
public class SnapshotProcessingRequest {

    /**
     * Los registros que violan restricciones de llave foranea
     */
    List<SnomedCTComponent> errors = new ArrayList<>();

    /**
     * Los registros a ser insertados
     */
    List<SnomedCTComponent> inserts = new ArrayList<>();;

    /**
     * los registros a ser actualizados
     */
    List<SnomedCTComponent> updates = new ArrayList<>();;


    public List<SnomedCTComponent> getUpdates() {
        return updates;
    }

    public void setUpdates(List<SnomedCTComponent> updates) {
        this.updates = updates;
    }

    public List<SnomedCTComponent> getErrors() {
        return errors;
    }

    public void setErrors(List<SnomedCTComponent> errors) {
        this.errors = errors;
    }

    public List<SnomedCTComponent> getInserts() {
        return inserts;
    }

    public void setInserts(List<SnomedCTComponent> inserts) {
        this.inserts = inserts;
    }

    public void clear() {
        errors.clear();
        inserts.clear();
        updates.clear();
    }
}
