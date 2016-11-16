package cl.minsal.semantikos.model.helpertables;

import cl.minsal.semantikos.model.User;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrés Farías on 11/14/16.
 */
public class HelperTableImportReport {

    private final Timestamp startTime;

    /** El usuario que realizó la carga */
    private User user;

    /** Status de la carga */
    private LoadStatus status;

    /** La tabla destino de la carga */
    private HelperTable helperTable;

    /** Lista de excepciones asociadas al proceso */
    private ArrayList<IOException> exceptions;

    /** Los records que se querían actualizar */
    private List<HelperTableRecord> CSVLoadedRecords;

    private long insertedRecords;

    public HelperTableImportReport(HelperTable helperTable, User user) {
        this.helperTable = helperTable;
        this.startTime = new Timestamp(System.currentTimeMillis());
        this.user = user;
        this.CSVLoadedRecords = new ArrayList<>();
    }

    public void setStatus(LoadStatus status) {
        this.status = status;
    }

    public LoadStatus getStatus() {
        return status;
    }

    public void appendException(IOException e) {
        this.exceptions.add(e);
    }

    public void setCSVLoadedRecords(List<HelperTableRecord> CSVLoadedRecords) {
        this.CSVLoadedRecords = CSVLoadedRecords;
    }

    public List<HelperTableRecord> getCSVLoadedRecords() {
        return CSVLoadedRecords;
    }

    public void setInsertedRecords(long insertedRecords) {
        this.insertedRecords = insertedRecords;
    }

    public long getInsertedRecords() {
        return insertedRecords;
    }

    public void setExceptions(ArrayList<IOException> exceptions) {
        this.exceptions = exceptions;
    }
    public ArrayList<IOException> getExceptions() {
        return exceptions;
    }
}
