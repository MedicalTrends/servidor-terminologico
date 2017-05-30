package cl.minsal.semantikos.ws.modelws.fault;

import java.io.Serializable;

/**
 * Created by Development on 2016-10-13.
 *
 */
public class NotFoundFault extends Exception implements Serializable {
    public NotFoundFault() {
    }

    public NotFoundFault(String message) {
        super(message);
    }

    public NotFoundFault(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundFault(Throwable cause) {
        super(cause);
    }
}
