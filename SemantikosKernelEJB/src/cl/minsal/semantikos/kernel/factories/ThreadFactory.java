package cl.minsal.semantikos.kernel.factories;

import javax.mail.Session;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Diego Soto
 */
public class ThreadFactory {

    private static final ThreadFactory instance = new ThreadFactory();

    ExecutorService executor;

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private ThreadFactory() {

    }

    public static ThreadFactory getInstance() {
        return instance;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }
}
