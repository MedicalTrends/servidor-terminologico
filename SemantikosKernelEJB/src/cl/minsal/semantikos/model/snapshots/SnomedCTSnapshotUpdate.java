package cl.minsal.semantikos.model.snapshots;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.snomedct.*;
import cl.minsal.semantikos.model.users.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Diego Soto
 */
public class SnomedCTSnapshotUpdate extends PersistentEntity {

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
    private int conceptsTotal;
    private int conceptsProcessed;
    private int descriptionsTotal;
    private int descriptionsProcessed;
    private int relationshipsTotal;
    private int relationshipsProcessed;
    private int refsetsTotal;
    private int refsetsProcessed;
    private int transitiveTotal;
    private int transitiveProcessed;

    /**
     * Estadísticas de la actualización
     */
    private int total;
    private int created;
    private int removed;
    private int unmodified;
    private int invalidated;
    private int restored;
    private int failed;

    SnomedCTSnapshotUpdateState snomedCTSnapshotUpdateState = new SnomedCTSnapshotUpdateState();

    /** Detalle de la actualización */
    List<SnomedCTSnapshotUpdateDetail> snomedCTSnapshotUpdateDetails = new ArrayList<>();

    /**
     *
     * @param release: La versión del snapshot
     * @param conceptSnapshot: El datafile de conceptos del snapshot
     * @param descriptionSnapshot: El datafile de descripciones del snapshot
     * @param relationshipSnapshot: El datafile de relaciones del snapshot
     * @param date: El datafile de conceptos del snapshot
     * @param user: El datafile de conceptos del snapshot
     */
    public SnomedCTSnapshotUpdate(String release, String conceptSnapshot, String descriptionSnapshot, String relationshipSnapshot, String refsetSnapshot, String transitiveSnapshot, Timestamp date, User user) {
        this.release = release;
        this.conceptSnapshotPath = conceptSnapshot;
        this.descriptionSnapshotPath = descriptionSnapshot;
        this.relationshipSnapshotPath = relationshipSnapshot;
        this.refsetSnapshotPath = refsetSnapshot;
        this.transitiveSnapshotPath = transitiveSnapshot;
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

    public SnomedCTSnapshotUpdateState getSnomedCTSnapshotUpdateState() {
        return snomedCTSnapshotUpdateState;
    }

    public void setSnomedCTSnapshotUpdateState(SnomedCTSnapshotUpdateState snomedCTSnapshotUpdateState) {
        this.snomedCTSnapshotUpdateState = snomedCTSnapshotUpdateState;
    }

    public List<SnomedCTSnapshotUpdateDetail> getSnomedCTSnapshotUpdateDetails() {
        return snomedCTSnapshotUpdateDetails;
    }

    public void setSnomedCTSnapshotUpdateDetails(List<SnomedCTSnapshotUpdateDetail> snomedCTSnapshotUpdateDetails) {
        this.snomedCTSnapshotUpdateDetails = snomedCTSnapshotUpdateDetails;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getUnmodified() {
        return unmodified;
    }

    public void setUnmodified(int unmodified) {
        this.unmodified = unmodified;
    }

    public int getInvalidated() {
        return invalidated;
    }

    public void setInvalidated(int invalidated) {
        this.invalidated = invalidated;
    }

    public int getRestored() {
        return restored;
    }

    public void setRestored(int restored) {
        this.restored = restored;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public int getRemoved() {
        return removed;
    }

    public void setRemoved(int removed) {
        this.removed = removed;
    }

    public int getConceptsTotal() {
        return conceptsTotal;
    }

    public void setConceptsTotal(int conceptsTotal) {
        this.conceptsTotal = conceptsTotal;
    }

    public int getConceptsProcessed() {
        return conceptsProcessed;
    }

    public void setConceptsProcessed(int conceptsProcessed) {
        this.conceptsProcessed = conceptsProcessed;
        this.snomedCTSnapshotUpdateState.setConceptsFileLine(conceptsProcessed);
        if(getConceptsProcessed()>=getConceptsTotal()) {
            this.snomedCTSnapshotUpdateState.setConceptsProcessed(true);
        }
    }

    public int getDescriptionsTotal() {
        return descriptionsTotal;
    }

    public void setDescriptionsTotal(int descriptionsTotal) {
        this.descriptionsTotal = descriptionsTotal;
    }

    public int getDescriptionsProcessed() {
        return descriptionsProcessed;
    }

    public void setDescriptionsProcessed(int descriptionsProcessed) {
        this.descriptionsProcessed = descriptionsProcessed;
        this.snomedCTSnapshotUpdateState.setDescriptionsFileLine(descriptionsProcessed);
        if(getDescriptionsProcessed()>=getDescriptionsTotal()) {
            this.snomedCTSnapshotUpdateState.setDescriptionsProcessed(true);
        }
    }

    public int getRelationshipsTotal() {
        return relationshipsTotal;
    }

    public void setRelationshipsTotal(int relationshipsTotal) {
        this.relationshipsTotal = relationshipsTotal;
    }

    public int getRelationshipsProcessed() {
        return relationshipsProcessed;
    }

    public void setRelationshipsProcessed(int relationshipsProcessed) {
        this.relationshipsProcessed = relationshipsProcessed;
        this.snomedCTSnapshotUpdateState.setRelationshipsFileLine(relationshipsProcessed);
        if(getRelationshipsProcessed()>=getRelationshipsTotal()) {
            this.snomedCTSnapshotUpdateState.setRelationshipsProcessed(true);
        }
    }

    public int getRefsetsTotal() {
        return refsetsTotal;
    }

    public void setRefsetsTotal(int refsetsTotal) {
        this.refsetsTotal = refsetsTotal;
    }

    public int getRefsetsProcessed() {
        return refsetsProcessed;
    }

    public void setRefsetsProcessed(int refsetsProcessed) {
        this.refsetsProcessed = refsetsProcessed;
        this.snomedCTSnapshotUpdateState.setRefsetsFileLine(refsetsProcessed);
        if(getRefsetsProcessed()>=getRefsetsTotal()) {
            this.snomedCTSnapshotUpdateState.setRefsetsProcessed(true);
        }
    }

    public int getTransitiveTotal() {
        return transitiveTotal;
    }

    public void setTransitiveTotal(int transitiveTotal) {
        this.transitiveTotal = transitiveTotal;
    }

    public int getTransitiveProcessed() {
        return transitiveProcessed;
    }

    public void setTransitiveProcessed(int transitiveProcessed) {
        this.transitiveProcessed = transitiveProcessed;
        this.snomedCTSnapshotUpdateState.setTransitivesFileLine(transitiveProcessed);
        if(getTransitiveProcessed()>=getTransitiveTotal()) {
            this.snomedCTSnapshotUpdateState.setTransitivesProcessed(true);
        }
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

        if(snomedCTSnapshotUpdateDetails.get(0).getSnomedCTComponent() instanceof ConceptSCT) {
            setConceptsProcessed(getConceptsProcessed()+snomedCTSnapshotUpdateDetails.size());
        }
        if(snomedCTSnapshotUpdateDetails.get(0).getSnomedCTComponent() instanceof DescriptionSCT) {
            setDescriptionsProcessed(getDescriptionsProcessed()+snomedCTSnapshotUpdateDetails.size());
        }
        if(snomedCTSnapshotUpdateDetails.get(0).getSnomedCTComponent() instanceof RelationshipSCT) {
            setRelationshipsProcessed(getRelationshipsProcessed()+snomedCTSnapshotUpdateDetails.size());
        }
        if(snomedCTSnapshotUpdateDetails.get(0).getSnomedCTComponent() instanceof LanguageRefsetSCT) {
            setRefsetsProcessed(getRefsetsProcessed()+snomedCTSnapshotUpdateDetails.size());
        }
        if(snomedCTSnapshotUpdateDetails.get(0).getSnomedCTComponent() instanceof TransitiveSCT) {
            setTransitiveProcessed(getTransitiveProcessed()+snomedCTSnapshotUpdateDetails.size());
        }

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
