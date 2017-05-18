package cl.minsal.semantikos.kernel.factories;

import javax.mail.Session;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Diego Soto
 */
public class EmailFactory {

    private static final EmailFactory instance = new EmailFactory();

    private Session mySession;

    ExecutorService executor = Executors.newFixedThreadPool(5);

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

    /**
     * Method to send the email based upon values entered in the JSF view.  Exception should be handled in a production
     * usage but is not handled in this example.
     * @throws Exception
     */

    public void send(String to, String password, String link, String link2) throws Exception
    {
        Runnable mailer = new Mailer(mySession, to, password, link, link2);
        executor.execute(mailer);
    }
}
