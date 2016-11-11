package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Development on 2016-10-13.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "usuario")
public class UserResponse implements Serializable {

    @XmlElement(name="nombreUsuario")
    private String username;
    @XmlElement(name="nombre")
    private String name;
    @XmlElement(name="apellido")
    private String lastName;
    @XmlElement(name="segundoApellido")
    private String secondLastName;
    @XmlElement(name="email")
    private String email;
    @XmlElement(name="rut")
    private String rut;
    @XmlElement(name="ultimoLogin")
    private Date lastLogin;
    @XmlElement(name="ultimoCambioPassword")
    private Date lastPasswordChange;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Date getLastPasswordChange() {
        return lastPasswordChange;
    }

    public void setLastPasswordChange(Date lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }
}
