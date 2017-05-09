package cl.minsal.semantikos.model.snapshots;

import cl.minsal.semantikos.model.snomedct.SnomedCTComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 31-03-17.
 */

/**
 * Esta clase representa una petición de preprocesamiento de un bloque de registros de un componente del snapshot
 * SnomedCT
 */
public class SnapshotPreprocessingRequest {

    /**
     * El bloque de registros a pre-procesar
     */
    private Map<Object, SnomedCTComponent> registers = new HashMap<>();

    /**
     * Las referencias desde un componente SnomedCT del registro indicado por la llave
     */
    private Map<Long, Long> referencesFrom = new HashMap<>();;

    /**
     * Las referencias hacia un componente SnomedCT del registro indicado por la llave
     */
    private Map<Long, Long> referencesTo = new HashMap<>();;

    public Map<Object, SnomedCTComponent> getRegisters() {
        return registers;
    }

    public void setRegisters(Map<Object, SnomedCTComponent> registers) {
        this.registers = registers;
    }

    public Map<Long, Long> getReferencesFrom() {
        return referencesFrom;
    }

    public void setReferencesFrom(Map<Long, Long> referencesFrom) {
        this.referencesFrom = referencesFrom;
    }

    public Map<Long, Long> getReferencesTo() {
        return referencesTo;
    }

    public void setReferencesTo(Map<Long, Long> referencesTo) {
        this.referencesTo = referencesTo;
    }

    public boolean isEmpty() {
        return registers.isEmpty();
    }

    public void clear() {
        registers.clear();
        referencesFrom.clear();
        referencesTo.clear();
    }
}
