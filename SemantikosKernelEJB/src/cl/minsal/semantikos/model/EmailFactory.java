package cl.minsal.semantikos.model;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.InitialContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Diego Soto
 */
public class EmailFactory {

    private static final EmailFactory instance = new EmailFactory();

    private Session mySession;

    private String to;

    private String from = "semantikos.minsal@gmail.com";

    private String subject = "Confirmación Cuenta Semantikos";

    private String body = "<b>Bienvenido a Semantikos!</b><br><br>Una cuenta asociada a este correo ha sido creada. <ul><li>Para activar su cuenta, por favor pinche el siguiente link: <br>%link%</li><li>Una vez activada su cuenta ingrese al sitio web:<br>%link2%<br>con la siguiente contraseña: %password%</li><li>Cambie su contraseña en la opción Perfil de la pestaña Usuario</li></ul>El Equipo Semantikos";

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private EmailFactory() {

    }

    public static EmailFactory getInstance() {
        return instance;
    }

    public Session getMySession() {
        return mySession;
    }

    public void setMySession(Session mySession) {
        this.mySession = mySession;
    }

    public void prepareMail(String to, String password, String link, String link2) {
        this.to = to;
        this.body = this.body.replace("%password%", password);
        this.body = this.body.replace("%link%", link);
        this.body = this.body.replace("%link2%", link2);
    }

    /**
     * Method to send the email based upon values entered in the JSF view.  Exception should be handled in a production
     * usage but is not handled in this example.
     * @throws Exception
     */
    public void send() throws Exception
    {
        try {
            Message message = new MimeMessage(mySession);
            message.setFrom(new InternetAddress(from));
            Address toAddress= new InternetAddress(to);
            message.addRecipient(Message.RecipientType.TO, toAddress);
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=utf-8");
            Transport.send(message);
        } catch (Exception e) {
            throw e;
        }
    }
}
