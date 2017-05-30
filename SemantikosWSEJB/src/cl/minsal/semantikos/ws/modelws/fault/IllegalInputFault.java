package cl.minsal.semantikos.ws.modelws.fault;

import java.io.Serializable;

/**
 * Created by Development on 2016-11-02.
 *
 */
public class IllegalInputFault extends Exception implements Serializable {
    public IllegalInputFault() {
    }

    public IllegalInputFault(String message) {
        super(message);
    }

    public IllegalInputFault(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalInputFault(Throwable cause) {
        super(cause);
    }
}
