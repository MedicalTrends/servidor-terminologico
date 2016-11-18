package cl.minsal.semantikos.ws.fault;

/**
 * Created by Development on 2016-10-13.
 *
 */
public class NotFoundFault extends Exception {
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
