package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.snapshots.SnapshotProcessingResult;
import cl.minsal.semantikos.model.snapshots.SnomedCTSnapshotUpdate;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 * @author Andrés Farías on 9/26/16.
 */
@Remote
public interface SnomedCTSnapshotManager {

    /**
     * Este método es responsable de procesar un snapshot de Snomed CT.
     *
     * @param snomedCTSnapshotUpdate El Snapshot que será procesado.
     *
     * @return El resultado del proceso.
     */
    public void updateSnapshot(SnomedCTSnapshotUpdate snomedCTSnapshotUpdate);

}
