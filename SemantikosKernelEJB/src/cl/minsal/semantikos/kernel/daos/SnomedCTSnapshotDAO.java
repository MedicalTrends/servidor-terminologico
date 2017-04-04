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

    public void persistSnomedCTSnapshotUpdate(SnomedCTSnapshotUpdate snomedCTSnapshotUpdate);

    public void updateSnomedCTSnapshotUpdate(SnomedCTSnapshotUpdate snomedCTSnapshotUpdate);

    public void replaceSnomedCTSnapshotUpdate(SnomedCTSnapshotUpdate snomedCTSnapshotUpdate);

    public SnomedCTSnapshotUpdate getSnomedCTSnapshotUpdateById(String id);

    public List<SnomedCTSnapshotUpdate> getAllSnomedCTSnapshotUpdates();

    public SnapshotProcessingRequest preprocessRequest(SnapshotPreprocessingRequest snapshotPreprocessingRequest);

    public List<SnomedCTSnapshotUpdateDetail> processRequest(SnapshotProcessingRequest snapshotProcessingRequest);

}
