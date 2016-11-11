package cl.minsal.semantikos.ws.fault;

/**
 * Created by Development on 2016-11-02.
 *
 */
public class IllegalInputFault extends Exception {
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
