package cl.minsal.semantikos.kernel.factories;

import cl.minsal.semantikos.model.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Diego Soto
 */
public class Mailer implements Runnable {

    private Session mySession;

    private User user;

    private static final String from = "sistemas@minsal.cl";//"semantikos.minsal@gmail.com";

    private static final String subject = "Confirmación Cuenta Semantikos";

    //private static String body = "<b>Bienvenido a Semantikos</b><br><br>Una cuenta asociada a este correo ha sido creada. <ul><li>Para activar su cuenta, por favor pinche el siguiente link: <br>%link%</li><li>Su contraseña inicial es: %password%</li><li>Cambie su contraseña inicial</li><li>Configure sus preguntas de seguridad</li></ul>El Equipo Semantikos";

    private static String body; //= "<table style='border-collapse:collapse;table-layout:fixed;min-width:320px;width:100%;background-color:#f5f7fa;' cellpadding='0' cellspacing='0'><tr><td><table style='background:white;border-collapse:collapse;table-layout:fixed;max-width:600px;min-width:320px;width:100%;font-family:Impact, Charcoal, sans-serif;color:#283593' align='center' cellpadding='0' cellspacing='0'><tr><td align='center' style='width:100%' colspan='3'><img src=\"cid:image2\" style='width:230px'></td></tr><tr><td colspan=3 style='padding-left:2em;padding-right:2em'><hr style='padding: 2px; background: #283593;' /><br/><br/>Estimado(a) %username%, una cuenta asociada a este correo ha sido creada en el Sistema.</p><ul><li>Su contraseña inicial es: %password%</li><li>Cambie su contraseña inicial</li><li>Configure sus preguntas de seguridad</li></ul><p>El equipo Semantikos</p><br/></td></tr><tr><td style='width:30%'></td><td style='background: #283593;width:40%;text-align:center;font-family:arial;font-size:13px;border-radius: 15px;-moz-border-radius: 15px;' align='center' height=31><a style='color: white;text-decoration:none;text-align:center' href='%link%'><strong>Activar Cuenta</strong></a></td><td></td></tr><tr><td colspan='3' align='center'><br/><div>©2016 Ministerio de Salud</div></td></tr></table></td></tr></table>";

    BufferedReader reader;

    private static final Logger logger = LoggerFactory.getLogger(Mailer.class);

    protected Mailer(Session session, User user, String password, String link, String link2) throws IOException {
        mySession = session;
        this.user = user;
        this.body = "";
        loadMailBody();
        this.body = this.body.replace("%username%", user.getFullName());
        this.body = this.body.replace("%password%", password);
        this.body = this.body.replace("%link%", link);
        this.body = this.body.replace("%email%", user.getEmail());
    }

    @Override
    public void run() {

        logger.info("Enviando correo a destinatario "+user.getEmail());

        int count = 0;
        int maxTries = 5;

        while(true) {

            try {

                Message message = new MimeMessage(mySession);
                message.setFrom(new InternetAddress(from));
                Address toAddress= new InternetAddress(user.getEmail());
                message.addRecipient(Message.RecipientType.TO, toAddress);
                message.setSubject(subject);
                message.setContent(body, "text/html; charset=utf-8");
                //Transport.send(message);

                //Transport transport = mySession.getTransport();

                //
                // This HTML mail have to 2 part, the BODY and the embedded image
                //
                MimeMultipart multipart = new MimeMultipart("related");

                // first part  (the html)
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(body, "text/html; charset=utf-8");

                // add it
                multipart.addBodyPart(messageBodyPart);

                // attach images
                attachImage(multipart, "<image1>", "/img/image-1.png");
                attachImage(multipart, "<image2>", "/img/image-2.png");
                attachImage(multipart, "<image3>", "/img/image-3.png");
                attachImage(multipart, "<image4>", "/img/image-4.png");
                attachImage(multipart, "<image5>", "/img/image-5.png");

                // put everything together
                message.setContent(multipart);

                //transport.connect();
                Transport.send(message, message.getRecipients(Message.RecipientType.TO));
                //transport.close();

                break;

            } catch (Exception e) {
                // handle exception
                logger.info((count+1)+"° intento enviando correo a destinatario "+user.getEmail()+" :"+e.getMessage());
                if (++count == maxTries) try {
                    logger.error("Error al enviar correo a destinatario "+user.getEmail()+": "+e.getMessage());
                    throw e;
                } catch (MessagingException e1) {
                    logger.error("Error: "+e1.getMessage());
                    e1.printStackTrace();
                }
            }
        }
    }

    private void attachImage(MimeMultipart multipart, String cid, String fileName) {

        try {

            // second part (the image)
            BodyPart messageBodyPart = new MimeBodyPart();

            InputStream stream = this.getClass().getResourceAsStream(fileName);

            if (stream == null) {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                if (classLoader == null) {
                    classLoader = this.getClass().getClassLoader();
                }

                stream = classLoader.getResourceAsStream(fileName);
            }

            DataSource ds = new ByteArrayDataSource(stream, "image/*");

            messageBodyPart.setDataHandler(new DataHandler(ds));
            messageBodyPart.setHeader("Content-ID", cid);

            // add it
            multipart.addBodyPart(messageBodyPart);
        }
        catch (MessagingException e1) {
            logger.error("Error: "+e1.getMessage());
            e1.printStackTrace();
        }
        catch (IOException e1) {
            logger.error("Error: "+e1.getMessage());
            e1.printStackTrace();
        }
    }

    private void loadMailBody() throws IOException {
        reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/mail/body.html")));
        String line = "";
        while ((line = reader.readLine()) != null) {
            if(line.trim().isEmpty()) {
                continue;
            }
            body = body + line;
        }
        reader.close();
    }
}
