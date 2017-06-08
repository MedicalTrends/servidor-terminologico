package cl.minsal.semantikos.model;

import cl.minsal.semantikos.model.users.User;

import java.sql.Timestamp;

/**
 * Created by root on 08-06-17.
 */
public class SMTKLoader {

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
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

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
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

    public String getBasicConceptPath() {
        return basicConceptPath;
    }

    public void setBasicConceptPath(String basicConceptPath) {
        this.basicConceptPath = basicConceptPath;
    }

    public String getBasicDescriptionPath() {
        return basicDescriptionPath;
    }

    public void setBasicDescriptionPath(String basicDescriptionPath) {
        this.basicDescriptionPath = basicDescriptionPath;
    }

    public String getBasicRelationshipPath() {
        return basicRelationshipPath;
    }

    public void setBasicRelationshipPath(String basicRelationshipPath) {
        this.basicRelationshipPath = basicRelationshipPath;
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
    }

    public int getDescriptionsTotal() {
        return descriptionsTotal;
    }

    public void setDescriptionsTotal(int descriptionsTotal) {
        this.descriptionsTotal = descriptionsTotal;
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

    public int getRemoved() {
        return removed;
    }

    public void setRemoved(int removed) {
        this.removed = removed;
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

    /**
     * Versión del snapshot (moduleId??)

     */
    private String release;

    /** Fecha */
    private Timestamp date;

    /** Usuario */
    private User user;

    private boolean started = false;

    private boolean finished = false;

    private boolean withErrors = false;

    private boolean userRequestedHalt = false;


    /** Rutas de los datafiles (componentes) del snapshot
     *
     */
    private String basicConceptPath;
    private String basicDescriptionPath;
    private String basicRelationshipPath;


    /**
     * Datos de control del proceso de actualización
     */
    private int conceptsTotal;
    private int conceptsProcessed;
    private int descriptionsTotal;

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
}
