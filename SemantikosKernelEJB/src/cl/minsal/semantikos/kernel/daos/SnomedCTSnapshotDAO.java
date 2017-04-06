package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.snapshots.SnapshotPreprocessingRequest;
import cl.minsal.semantikos.model.snapshots.SnapshotProcessingRequest;
import cl.minsal.semantikos.model.snapshots.SnomedCTSnapshotUpdate;
import cl.minsal.semantikos.model.snapshots.SnomedCTSnapshotUpdateDetail;
import cl.minsal.semantikos.model.snomedct.*;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;

/**
 * @author Diego Soto
 */
@Local
public interface SnomedCTSnapshotDAO {

    void persistSnomedCTSnapshotUpdate(SnomedCTSnapshotUpdate snomedCTSnapshotUpdate);

    void updateSnomedCTSnapshotUpdate(SnomedCTSnapshotUpdate snomedCTSnapshotUpdate);

    SnomedCTSnapshotUpdate getSnomedCTSnapshotUpdateById(String id);

    List<SnomedCTSnapshotUpdate> getAllSnomedCTSnapshotUpdates();

    SnapshotProcessingRequest preprocessRequest(SnapshotPreprocessingRequest snapshotPreprocessingRequest);

    List<SnomedCTSnapshotUpdateDetail> processRequest(SnapshotProcessingRequest snapshotProcessingRequest);

    List<SnomedCTSnapshotUpdateDetail> postProcessRequest(SnomedCTSnapshotUpdate snomedCTSnapshotUpdate);

}
