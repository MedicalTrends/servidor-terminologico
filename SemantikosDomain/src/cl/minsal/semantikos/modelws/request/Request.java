package cl.minsal.semantikos.modelws.request;

import cl.minsal.semantikos.modelws.fault.IllegalInputFault;

import javax.xml.bind.annotation.*;
import java.util.List;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;

/**
 * Created by root on 09-05-17.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "Request", namespace = "http://service.ws.semantikos.minsal.cl/")
public class Request {

    @XmlElement(required = true/*, defaultValue = "1"*/ , name = "idEstablecimiento")
    private String idStablishment;

    public String getIdStablishment() {
        return idStablishment;
    }

    public void setIdStablishment(String idStablishment) {
        this.idStablishment = idStablishment;
    }

    public void validate() throws IllegalInputFault {

    };

    public static boolean isEmpty(List<String> list) {
        return list.isEmpty() || (list.size() == 1 && list.contains(EMPTY_STRING));
    }
}
