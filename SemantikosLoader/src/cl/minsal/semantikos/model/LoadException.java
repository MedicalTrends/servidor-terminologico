package cl.minsal.semantikos.model;

/**
 * Created by root on 09-06-17.
 */
public class LoadException extends Throwable {

    String dataFilePath;
    Long idConcept;
    String errorMessage;

    public LoadException(String dataFilePath, Long idConcept, String errorMessage) {

        this.dataFilePath = dataFilePath;
        this.idConcept = idConcept;
        this.errorMessage = errorMessage;
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "LoadError{" +
                "dataFilePath='" + dataFilePath + '\'' +
                ", idConcept=" + idConcept +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
