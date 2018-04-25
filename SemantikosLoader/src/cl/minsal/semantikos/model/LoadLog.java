package cl.minsal.semantikos.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by root on 09-06-17.
 */
public class LoadLog extends Throwable {

    Timestamp timestamp;
    String loadMessage;
    String type;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final String ERROR = "ERROR";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static final String INFO = "INFO";

    public LoadLog(String message, String type) {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.loadMessage = message;
        this.type = type;
    }

    @Override
    public String toString() {
        return "["+format.format(timestamp)+"]"+type+": "+loadMessage;
    }

    public String getLoadMessage() {
        return loadMessage;
    }

    public void setLoadMessage(String loadMessage) {
        this.loadMessage = loadMessage;
    }
}
