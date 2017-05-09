package cl.minsal.semantikos.model.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private String body = "<b>Bienvenido a Semantikos!</b><br><br>Una cuenta asociada a este correo ha sido creada. <ul><li>Para activar su cuenta, por favor pinche el siguiente link: <br>%link%</li><li>Su contraseña inicial es: %password%</li><li>Cambie su contraseña inicial</li><li>Configure sus preguntas de seguridad</li></ul>El Equipo Semantikos";

    private static final Logger logger = LoggerFactory.getLogger(Mailer.class);


    protected Mailer(Session session, String to, String password, String link, String link2) {
        mySession = session;
        this.to = to;
        this.body = this.body.replace("%password%", password);
        this.body = this.body.replace("%link%", link);
        this.body = this.body.replace("%link2%", link2);
    }

    @Override
    public void run() {

        logger.info("Enviando correo a destinatario "+to);

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
                logger.info((count+1)+"° intento enviando correo a destinatario "+to+" :"+e.getMessage());
                if (++count == maxTries) try {
                    logger.error("Error al enviar correo a destinatario "+to+": "+e.getMessage());
                    throw e;
                } catch (MessagingException e1) {
                    logger.error("Error: "+e1.getMessage());
                    e1.printStackTrace();
                }
            }
        }
    }
}
