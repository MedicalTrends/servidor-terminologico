package cl.minsal.semantikos.model;

import java.sql.Timestamp;

/**
 * Created by root on 09-06-17.
 */
public class LoadLog extends Throwable {

    Timestamp timestamp;
    String message;

    public LoadLog(String message) {
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.message = message;
    }

    @Override
    public String toString() {
        return "LoadLog{" +
                "timestamp=" + timestamp +
                ", message='" + message + '\'' +
                '}';
    }
}
