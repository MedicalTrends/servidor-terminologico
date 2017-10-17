package cl.minsal.semantikos.modelws.fault;

import javax.xml.bind.annotation.XmlType;
import javax.xml.ws.WebFault;
import java.io.Serializable;

/**
 * Created by Development on 2016-10-13.
 *
 */
@WebFault(name = "NotFoundFault", targetNamespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "NotFoundFault", namespace = "http://service.ws.semantikos.minsal.cl/")
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
