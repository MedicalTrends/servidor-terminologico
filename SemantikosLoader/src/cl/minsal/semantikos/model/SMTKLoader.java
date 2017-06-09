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

    /** Fecha */
    private Timestamp date;

    /** Usuario */
    private User user;


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
    private List<LoadLog> logs = new ArrayList<>();


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

    public List<LoadLog> getLogs() {
        return logs;
    }

    public void setLogs(List<LoadLog> logs) {
        this.logs = logs;
    }


    public void log(LoadLog e) {
        logger.error(e.toString());
    }
}
