package cl.minsal.semantikos.model;

/**
 * Created by root on 09-06-17.
 */
public class LoadException extends LoadLog {

    String dataFilePath;
    Long idConcept;

    public LoadException(String dataFilePath, Long idConcept, String errorMessage) {
        super(errorMessage);
        this.dataFilePath = dataFilePath;
        this.idConcept = idConcept;
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

    @Override
    public String toString() {
        return "LoadException{" +
                "dataFilePath='" + dataFilePath + '\'' +
                ", idConcept=" + idConcept +
                '}';
    }
}
