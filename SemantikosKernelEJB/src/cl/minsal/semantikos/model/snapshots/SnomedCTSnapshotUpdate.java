package cl.minsal.semantikos.model.snapshots;

import cl.minsal.semantikos.model.users.User;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Andrés Farías on 9/26/16.
 */
public class SnomedCTSnapshotUpdate {

    /**
     * Versión del snapshot (moduleId??)
      */
    private String release;

    /** Fecha */
    private Timestamp date;

    /** Usuario */
    private User user;

    private boolean finished = false;

    private boolean withErrors = false;

    private boolean userRequestedHalt = false;


    /** Rutas de los datafiles (componentes) del snapshot
     *
     */
    private String conceptSnapshotPath;
    private String descriptionSnapshotPath;
    private String relationshipSnapshotPath;
    private String refsetSnapshotPath;
    private String transitiveSnapshotPath;

    /**
     * Datos de control del proceso de actualización
     */
    private long conceptsTotal;
    private long conceptsProcessed;
    private long descriptionsTotal;
    private long descriptionsProcessed;
    private long relationshipsTotal;
    private long relationshipsProcessed;
    private long refsetsTotal;
    private long refsetsProcessed;
    private long transitiveTotal;
    private long transitiveProcessed;

    /**
     * Estadísticas de la actualización
     */
    private long total;
    private long created;
    private long removed;
    private long unmodified;
    private long invalidated;
    private long restored;
    private long failed;

    /** Detalle de la actualización */
    List<SnomedCTSnapshotUpdateDetail> snomedCTSnapshotUpdateDetails;

    /**
     *
     * @param release: La versión del snapshot
     * @param conceptSnapshot: El datafile de conceptos del snapshot
     * @param descriptionSnapshot: El datafile de descripciones del snapshot
     * @param relationshipSnapshot: El datafile de relaciones del snapshot
     * @param date: El datafile de conceptos del snapshot
     * @param user: El datafile de conceptos del snapshot
     */
    public SnomedCTSnapshotUpdate(String release, String conceptSnapshot, String descriptionSnapshot, String relationshipSnapshot, Timestamp date, User user) {
        this.release = release;
        this.conceptSnapshotPath = conceptSnapshot;
        this.descriptionSnapshotPath = descriptionSnapshot;
        this.relationshipSnapshotPath = relationshipSnapshot;
        this.date = date;
        this.user = user;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getConceptSnapshotPath() {
        return conceptSnapshotPath;
    }

    public void setConceptSnapshotPath(String conceptSnapshotPath) {
        this.conceptSnapshotPath = conceptSnapshotPath;
    }

    public String getDescriptionSnapshotPath() {
        return descriptionSnapshotPath;
    }

    public void setDescriptionSnapshotPath(String descriptionSnapshotPath) {
        this.descriptionSnapshotPath = descriptionSnapshotPath;
    }

    public String getRelationshipSnapshotPath() {
        return relationshipSnapshotPath;
    }

    public void setRelationshipSnapshotPath(String relationshipSnapshotPath) {
        this.relationshipSnapshotPath = relationshipSnapshotPath;
    }

    public String getRefsetSnapshotPath() {
        return refsetSnapshotPath;
    }

    public void setRefsetSnapshotPath(String refsetSnapshotPath) {
        this.refsetSnapshotPath = refsetSnapshotPath;
    }

    public String getTransitiveSnapshotPath() {
        return transitiveSnapshotPath;
    }

    public void setTransitiveSnapshotPath(String transitiveSnapshotPath) {
        this.transitiveSnapshotPath = transitiveSnapshotPath;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<SnomedCTSnapshotUpdateDetail> getSnomedCTSnapshotUpdateDetails() {
        return snomedCTSnapshotUpdateDetails;
    }

    public void setSnomedCTSnapshotUpdateDetails(List<SnomedCTSnapshotUpdateDetail> snomedCTSnapshotUpdateDetails) {
        this.snomedCTSnapshotUpdateDetails = snomedCTSnapshotUpdateDetails;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getUnmodified() {
        return unmodified;
    }

    public void setUnmodified(long unmodified) {
        this.unmodified = unmodified;
    }

    public long getInvalidated() {
        return invalidated;
    }

    public void setInvalidated(long invalidated) {
        this.invalidated = invalidated;
    }

    public long getRestored() {
        return restored;
    }

    public void setRestored(long restored) {
        this.restored = restored;
    }

    public long getFailed() {
        return failed;
    }

    public void setFailed(long failed) {
        this.failed = failed;
    }

    public long getRemoved() {
        return removed;
    }

    public void setRemoved(long removed) {
        this.removed = removed;
    }

    public long getConceptsTotal() {
        return conceptsTotal;
    }

    public void setConceptsTotal(long conceptsTotal) {
        this.conceptsTotal = conceptsTotal;
    }

    public long getConceptsProcessed() {
        return conceptsProcessed;
    }

    public void setConceptsProcessed(long conceptsProcessed) {
        this.conceptsProcessed = conceptsProcessed;
    }

    public long getDescriptionsTotal() {
        return descriptionsTotal;
    }

    public void setDescriptionsTotal(long descriptionsTotal) {
        this.descriptionsTotal = descriptionsTotal;
    }

    public long getDescriptionsProcessed() {
        return descriptionsProcessed;
    }

    public void setDescriptionsProcessed(long descriptionsProcessed) {
        this.descriptionsProcessed = descriptionsProcessed;
    }

    public long getRelationshipsTotal() {
        return relationshipsTotal;
    }

    public void setRelationshipsTotal(long relationshipsTotal) {
        this.relationshipsTotal = relationshipsTotal;
    }

    public long getRelationshipsProcessed() {
        return relationshipsProcessed;
    }

    public void setRelationshipsProcessed(long relationshipsProcessed) {
        this.relationshipsProcessed = relationshipsProcessed;
    }

    public long getRefsetsTotal() {
        return refsetsTotal;
    }

    public void setRefsetsTotal(long refsetsTotal) {
        this.refsetsTotal = refsetsTotal;
    }

    public long getRefsetsProcessed() {
        return refsetsProcessed;
    }

    public void setRefsetsProcessed(long refsetsProcessed) {
        this.refsetsProcessed = refsetsProcessed;
    }

    public long getTransitiveTotal() {
        return transitiveTotal;
    }

    public void setTransitiveTotal(long transitiveTotal) {
        this.transitiveTotal = transitiveTotal;
    }

    public long getTransitiveProcessed() {
        return transitiveProcessed;
    }

    public void setTransitiveProcessed(long transitiveProcessed) {
        this.transitiveProcessed = transitiveProcessed;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isWithErrors() {
        return withErrors;
    }

    public void setWithErrors(boolean withErrors) {
        this.withErrors = withErrors;
    }

    public boolean isUserRequestedHalt() {
        return userRequestedHalt;
    }

    public void setUserRequestedHalt(boolean userRequestedHalt) {
        this.userRequestedHalt = userRequestedHalt;
    }

    public void updateStats() {

        total+=snomedCTSnapshotUpdateDetails.size();

        for (SnomedCTSnapshotUpdateDetail snomedCTSnapshotUpdateDetail : snomedCTSnapshotUpdateDetails) {
            switch (snomedCTSnapshotUpdateDetail.getAuditActionType()) {
                case SNOMED_CT_CREATION:
                    created++;
                    break;
                case SNOMED_CT_REMOVAL:
                    removed++;
                    break;
                case SNOMED_CT_INVALIDATION:
                    invalidated++;
                    break;
                case SNOMED_CT_RESTORYING:
                    restored++;
                    break;
                case SNOMED_CT_UNMODIFYING:
                    unmodified++;
                    break;
                case SNOMED_CT_ERROR:
                    failed++;
                    withErrors = true;
                    break;
                case SNOMED_CT_UNDEFINED:
                    break;
            }
        }
    }
}
