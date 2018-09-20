package cl.minsal.semantikos.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by root on 09-06-17.
 */
public class LoadLog extends Throwable {

    public Timestamp timestamp;
    public String message;
    public String type;
    public SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final String ERROR = "ERROR";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static final String INFO = "INFO";

    public static final String WARNING = "WARNING";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoadLog(String message, String type) {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.message = message;

        this.type = type;
    }

    @Override
    public String toString() {
        return "["+format.format(timestamp)+"]"+type+": "+ message;
    }
}
