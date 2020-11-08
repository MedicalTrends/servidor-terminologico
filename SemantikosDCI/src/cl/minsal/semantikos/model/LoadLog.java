package cl.minsal.semantikos.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by root on 09-06-17.
 */
public class LoadLog extends Throwable {

    Timestamp timestamp;
    String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    String type;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final String ERROR = "ERROR";

    public static final String WARNING = "WARNING";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static final String INFO = "INFO";

    public LoadLog(String description, String type) {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.description = description;
        this.type = type;
    }

    @Override
    public String toString() {
        return "["+format.format(timestamp)+"]"+type+": "+description;
    }
}
