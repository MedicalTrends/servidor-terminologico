package cl.minsal.semantikos.model.system;

import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.DescriptionType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;


/**
 * @author Andrés Farías
 */
public class SystemFactory implements Serializable {

    private static final SystemFactory instance = new SystemFactory();

    private static int timeout;

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private SystemFactory() {
    }

    public static SystemFactory getInstance() {
        return instance;
    }

    public static int getTimeout() {
        return timeout;
    }

    public static void setTimeout(int timeout) {
        SystemFactory.timeout = timeout;
    }
}
