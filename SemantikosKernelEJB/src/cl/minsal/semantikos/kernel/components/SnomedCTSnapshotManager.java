package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.snapshots.SnapshotProcessingResult;
import cl.minsal.semantikos.model.snapshots.SnomedCTSnapshotUpdate;

import javax.ejb.Local;

/**
 * @author Andrés Farías on 9/26/16.
 */
@Local
public interface SnomedCTSnapshotManager {

    /**
     * Este método es responsable de procesar un snapshot de Snomed CT.
     *
     * @param snomedCTSnapshotUpdate El Snapshot que será procesado.
     *
     * @return El resultado del proceso.
     */
    public SnapshotProcessingResult processSnapshot(SnomedCTSnapshotUpdate snomedCTSnapshotUpdate);

}
