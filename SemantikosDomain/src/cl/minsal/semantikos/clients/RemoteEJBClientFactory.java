package cl.minsal.semantikos.clients; /**
 * Created by root on 15-05-17.
 */


import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.categories.CategoryFactory;

import javax.naming.*;

import java.lang.reflect.Type;
import java.util.*;


public class RemoteEJBClientFactory {

    private static final RemoteEJBClientFactory instance = new RemoteEJBClientFactory();
    private static Context context;

    /** Mapa de interfaces por su nombre. */
    private Map<String, Object> managersByName;

    private void lookupRemoteStatelessEJB(Type type) throws NamingException {

        // The app name is the application name of the deployed EJBs. This is typically the ear name
        // without the .ear suffix. However, the application name could be overridden in the application.xml of the
        // EJB deployment on the server.
        // Since we haven't deployed the application as a .ear, the app name for us will be an empty string
        final String appName = "SemantikosBusinessEAR-4.0.0/";
        // This is the module name of the deployed EJBs on the server. This is typically the jar name of the
        // EJB deployment, without the .jar suffix, but can be overridden via the ejb-jar.xml
        // In this example, we have deployed the EJBs in a jboss-as-ejb-remote-app.jar, so the module name is
        // jboss-as-ejb-remote-app
        final String moduleName = "SemantikosKernelEJB-4.0.0/";
        // AS7 allows each deployment to have an (optional) distinct name. We haven't specified a distinct name for
        // our EJB deployment, so this is an empty string
        final String distinctName = "";
        // The EJB name which by default is the simple class name of the bean implementation class
        final String beanName = getBeanName(type);
        // the remote view fully qualified class name
        final String viewClassName = getViewClassName(type);
        // let's do the lookup (notice the ?stateful string as the last part of the jndi name for stateful bean lookup)
        this.managersByName.put(getBeanName(type), context.lookup("ejb:" + appName + moduleName + beanName + "!" + viewClassName));
    }

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private RemoteEJBClientFactory() {
        final Hashtable<String, String> jndiProperties = new Hashtable<>();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        try {
            context = new InitialContext(jndiProperties);
        } catch (NamingException e) {
            e.printStackTrace();
        }
        this.managersByName = new HashMap<>();
    }

    public static RemoteEJBClientFactory getInstance() {
        return instance;
    }

    public Map<String, Object> getManagersByName() {
        return managersByName;
    }

    public void setManagersByName(Map<String, Object> managersByName) {
        this.managersByName = managersByName;
    }

    /**
     * Este método es responsable de retornar el manager correspondiente.
     *
     * @return Retorna una instancia de FSN.
     */
    public Object getManager(Type type) {

        if (!managersByName.containsKey(getBeanName(type))) {
            try {
                lookupRemoteStatelessEJB(type);
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }
        return this.managersByName.get(getBeanName(type));
    }

    private String getBeanName(Type type) {
        String[] tokens = type.toString().split(" ")[1].split("\\.");
        return tokens[tokens.length-1]+"Impl";
    }

    private String getViewClassName(Type type) {
        return type.toString().split(" ")[1];
    }

}