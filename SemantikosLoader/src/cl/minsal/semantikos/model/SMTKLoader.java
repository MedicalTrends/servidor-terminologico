package cl.minsal.semantikos.model;

import cl.minsal.semantikos.loaders.BasicConceptLoader;
import cl.minsal.semantikos.model.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 08-06-17.
 */
public class SMTKLoader {

    private static final Logger logger = LoggerFactory.getLogger(SMTKLoader.class);

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
     * Datos de control del proceso de carga
     */
    private int conceptsTotal;
    private int conceptsProcessed;

    /**
     *
     * Errores
     */
    private List<LoadException> errors = new ArrayList<>();

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

    public List<LoadException> getErrors() {
        return errors;
    }

    public void setErrors(List<LoadException> errors) {
        this.errors = errors;
    }

    public void logError(LoadException e) {
        logger.error(e.toString());
        getErrors().add(e);
    }
}
