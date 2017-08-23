package cl.minsal.semantikos.modelws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by root on 09-05-17.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "Response", namespace = "http://service.ws.semantikos.minsal.cl/")
public class Response implements Serializable{

    @XmlElement(required = true, defaultValue = "0" , name = "code")
    private int code;

    @XmlElement(required = true, defaultValue = "" , name = "message")
    private String message;

    public Response() {
    }

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean withErrors() {
        return code==1;
    }
}
