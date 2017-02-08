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
    private ArrayList<Exception> exceptions;

    /** Los records que se querían actualizar */
    private List<HelperTableRow> CSVLoadedRecords;

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

    public void appendException(Exception e) {
        this.exceptions.add(e);
    }

    public void setCSVLoadedRecords(List<HelperTableRow> CSVLoadedRecords) {
        this.CSVLoadedRecords = CSVLoadedRecords;
    }

    public List<HelperTableRow> getCSVLoadedRecords() {
        return CSVLoadedRecords;
    }

    public void setInsertedRecords(long insertedRecords) {
        this.insertedRecords = insertedRecords;
    }

    public long getInsertedRecords() {
        return insertedRecords;
    }

    public void setExceptions(ArrayList<Exception> exceptions) {
        this.exceptions = exceptions;
    }
    public ArrayList<Exception> getExceptions() {
        return exceptions;
    }
}
