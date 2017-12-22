package cl.minsal.semantikos.model;

/**
 * Created by root on 09-06-17.
 */
public class LoadException extends LoadLog {

    String dataFilePath;
    Long idConcept;
    boolean severe = false;

    public LoadException(String dataFilePath, Long idConcept, String errorMessage, String type) {
        super(errorMessage, type);
        this.dataFilePath = dataFilePath;
        this.idConcept = idConcept;
    }

    public LoadException(String dataFilePath, Long idConcept, String errorMessage, String type, boolean severe) {
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

    public Long getIdConcept() {
        return idConcept;
    }

    public void setIdConcept(Long idConcept) {
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
