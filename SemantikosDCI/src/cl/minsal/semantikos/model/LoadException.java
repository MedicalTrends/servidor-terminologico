package cl.minsal.semantikos.model;

/**
 * Created by root on 09-06-17.
 */
public class LoadException extends LoadLog {

    String dataFilePath;
    String idConcept;
    boolean severe = false;
    String action;

    public LoadException(String dataFilePath, String idConcept, String errorMessage, String type) {
        super(errorMessage, type);
        this.dataFilePath = dataFilePath;
        this.idConcept = idConcept;
    }

    public LoadException(String dataFilePath, String idConcept, String errorMessage, String severe, String  action) {
        super(errorMessage, severe);
        this.dataFilePath = dataFilePath;
        this.idConcept = idConcept;
        this.idConcept = idConcept;
        this.action = action;
    }

    public LoadException(String dataFilePath, String idConcept, String errorMessage, String type, boolean severe) {
        super(errorMessage, type);
        this.dataFilePath = dataFilePath;
        this.idConcept = idConcept;
        this.severe = severe;

    }

    public String getDataFilePath() {
        return dataFilePath;
    }

    public void setDataFilePath(String dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    public String getIdConcept() {
        return idConcept;
    }

    public void setIdConcept(String idConcept) {
        this.idConcept = idConcept;
    }

    public boolean isSevere() {
        return severe;
    }

    public void setSevere(boolean severe) {
        this.severe = severe;
    }

    @Override
    public String toString() {
        return super.toString()+" ("+dataFilePath.split("\\/")[dataFilePath.split("\\/").length-1]+":"+idConcept+")";
    }
}
