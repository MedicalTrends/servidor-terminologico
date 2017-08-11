package cl.minsal.semantikos.modelws.response;

import javax.xml.bind.annotation.*;

/**
 * Created by root on 09-05-17.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "Response", namespace = "http://service.ws.semantikos.minsal.cl/")
public class Response {

    @XmlElement(required = true, defaultValue = "0" , name = "code")
    private int code;

    @XmlElement(required = true, defaultValue = "" , name = "description")
    private String description;

    public Response() {
    }

    public Response(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
