package cl.minsal.semantikos.model.snomedct;

import cl.minsal.semantikos.model.users.User;

import java.sql.Timestamp;

/**
 * @author Andrés Farías on 9/26/16.
 */
public class SnomedCTSnapshot {

    /**
     * Versión del snapshot
      */
    private String release;

    /** Rutas de los datafiles del snapshot
     *
     */
    private String conceptSnapshotPath;
    private String descriptionSnapshotPath;
    private String relationshipSnapshotPath;

    /** Fecha */
    private Timestamp date;

    /** Usuario */
    private User user;

    /**
     *
     * @param release: La versión del snapshot
     * @param conceptSnapshot: El datafile de conceptos del snapshot
     * @param descriptionSnapshot: El datafile de descripciones del snapshot
     * @param relationshipSnapshot: El datafile de relaciones del snapshot
     * @param date: El datafile de conceptos del snapshot
     * @param user: El datafile de conceptos del snapshot
     */
    public SnomedCTSnapshot(String release, String conceptSnapshot, String descriptionSnapshot, String relationshipSnapshot, Timestamp date, User user) {
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
}
