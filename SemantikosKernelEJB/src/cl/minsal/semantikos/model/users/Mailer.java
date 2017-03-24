package cl.minsal.semantikos.model.users;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author Diego Soto
 */
public class Mailer implements Runnable {


    private Session mySession;

    private String to;

    private String from = "semantikos.minsal@gmail.com";

    private String subject = "Confirmación Cuenta Semantikos";

    private String body = "<b>Bienvenido a Semantikos!</b><br><br>Una cuenta asociada a este correo ha sido creada. <ul><li>Para activar su cuenta, por favor pinche el siguiente link: <br>%link%</li><li>Una vez activada su cuenta ingrese al sitio web:<br>%link2%<br>con la siguiente contraseña: %password%</li><li>Cambie su contraseña en la opción Perfil de la pestaña Usuario</li></ul>El Equipo Semantikos";


    protected Mailer(Session session, String to, String password, String link, String link2) {
        mySession = session;
        this.to = to;
        this.body = this.body.replace("%password%", password);
        this.body = this.body.replace("%link%", link);
        this.body = this.body.replace("%link2%", link2);
    }

    @Override
    public void run() {
        int count = 0;
        int maxTries = 5;

        while(true) {

            try {
                Message message = new MimeMessage(mySession);
                message.setFrom(new InternetAddress(from));
                Address toAddress= new InternetAddress(to);
                message.addRecipient(Message.RecipientType.TO, toAddress);
                message.setSubject(subject);
                message.setContent(body, "text/html; charset=utf-8");
                Transport.send(message);
                break;
            } catch (Exception e) {
                // handle exception
                if (++count == maxTries) try {
                    throw e;
                } catch (MessagingException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
