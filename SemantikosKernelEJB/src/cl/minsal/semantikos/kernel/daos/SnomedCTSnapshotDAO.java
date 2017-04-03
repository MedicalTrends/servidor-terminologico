package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.snapshots.SnapshotPreprocessingRequest;
import cl.minsal.semantikos.model.snapshots.SnapshotProcessingRequest;
import cl.minsal.semantikos.model.snomedct.*;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;

/**
 * @author Andrés Farías on 10/25/16.
 */
@Local
public interface SnomedCTSnapshotDAO {


    public SnapshotProcessingRequest preprocessRequest(SnapshotPreprocessingRequest snapshotPreprocessingRequest);

    public void processRequest(SnapshotProcessingRequest snapshotProcessingRequest);

}
