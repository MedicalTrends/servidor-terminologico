package cl.minsal.semantikos.clients; /**
 * Created by root on 15-05-17.
 */

import cl.minsal.semantikos.kernel.components.AuthenticationManager;
import cl.minsal.semantikos.model.users.User;
import org.jboss.ejb.client.ContextSelector;
import org.jboss.ejb.client.EJBClientConfiguration;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.PropertiesBasedEJBClientConfiguration;
import org.jboss.ejb.client.remoting.ConfigBasedEJBClientContextSelector;
import org.jboss.security.SecurityAssociation;

import javax.ejb.SessionContext;
import javax.naming.*;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import java.lang.reflect.Type;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class ServiceLocator {

    private static final ServiceLocator instance = new ServiceLocator();
    private static Context context;
    private static LoginContext loginContext;
    private static Properties props;

    /** Mapa de interfaces por su nombre. */
    private static Map<String, Object> servicesByName;

    private static Map<Principal, Context> contextMap;

    private static Map<Principal, Map<String, Object>> serviceMap;

    private static String APP_NAME = "SemantikosCentral/";
    private static String MODULE_NAME = "SemantikosKernelEJB/";

    public static Object lookupRemoteStatelessEJB(Type type) throws NamingException {


        //final String version =  getClass().getPackage().getImplementationVersion();
        // The app name is the application name of the deployed EJBs. This is typically the ear name
        // without the .ear suffix. However, the application name could be overridden in the application.xml of the
        // EJB deployment on the server.
        // Since we haven't deployed the application as a .ear, the app name for us will be an empty string
        final String appName = APP_NAME;
        // This is the module name of the deployed EJBs on the server. This is typically the jar name of the
        // EJB deployment, without the .jar suffix, but can be overridden via the ejb-jar.xml
        // In this example, we have deployed the EJBs in a jboss-as-ejb-remote-app.jar, so the module name is
        // jboss-as-ejb-remote-app
        final String moduleName = MODULE_NAME;
        // AS7 allows each deployment to have an (optional) distinct name. We haven't specified a distinct name for
        // our EJB deployment, so this is an empty string
        final String distinctName = "";
        // The EJB name which by default is the simple class name of the bean implementation class
        final String beanName = getServiceName(type);
        // the remote view fully qualified class name
        final String viewClassName = getViewClassName(type);
        // let's do the lookup (notice the ?stateful string as the last part of the jndi name for stateful bean lookup)
        String jndiname = "ejb:" + appName + moduleName + beanName + "!" + viewClassName;
        //String jndiname = appName + moduleName + beanName + "!" + viewClassName;

        Object remoteEjb = context.lookup(jndiname);

        servicesByName.put(getServiceName(type), remoteEjb);

        return remoteEjb;
    }


    /**
     * Constructor privado para el Singleton del Factory.
     * EJBClientContext using EJBClientAPI
     */

    private ServiceLocator() {

        Configuration.setConfiguration(new DefaultJaasConfiguration());
        this.servicesByName = new ConcurrentHashMap<>();
        this.contextMap = new ConcurrentHashMap<>();
        this.serviceMap = new ConcurrentHashMap<>();
    }


    public static ServiceLocator getInstance() {
        return instance;
    }

    /**
     * Constructor privado para el Singleton del Factory.
     * EJBClientContext using RemoteNamingProject
     */
    /*
    private ServiceLocator() {

        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        props.put(Context.PROVIDER_URL, "remote://192.168.0.194:4447");

        //props.put("jboss.naming.client.ejb.context", true);
        props.put("jboss.naming.client.ejb.context", false);
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");

        try {
            context = new InitialContext(props);
            //context = new InitialContext(properties);
        } catch (NamingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        this.managersByName = new ConcurrentHashMap<>();
    }
    */

    /**
     * Este método es responsable de retornar el manager correspondiente.
     *
     * @return Retorna una instancia de FSN.
     */
    public Object getService(Type type) {

        Principal principal = SecurityAssociation.getPrincipal();

        if (!servicesByName.containsKey(getServiceName(type))) {
            try {
                lookupRemoteStatelessEJB(type);
            } catch (NamingException e) {
                e.printStackTrace();
            }
        }

        return this.servicesByName.get(getServiceName(type));
    }

    private static String getServiceName(Type type) {
        String[] tokens = type.toString().split(" ")[1].split("\\.");
        return tokens[tokens.length-1]+"Impl";
    }

    private static String getViewClassName(Type type) {
        return type.toString().split(" ")[1];
    }

    public void closeContext() {
        try {
            context.close();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public static Principal login(String userName, String password) throws LoginException {

        /*
       * During the login process(i.e. when the login() method on the LoginContext is called),
       * the control will be transferred to a CallbackHandler. The CallbackHandler will be
       * responsible for populating the Callback object with the username and password, which
       * will be later on used by the login process
       *
       * The "MyCallbackHandler" is your own class and you can give any name to it. MyCallbackHandler
       * expects the username and password to be passed through its constructor, but this is NOT
       * mandatory when you are writing your own callback handler.
       *
       *
       */
        MyCallbackHandler handler = new MyCallbackHandler(userName, password);

       /*
        * Create a login context. Here, as the first parameter, you will specify which
        * configuration(mentioned in the "authFile" above) will be used. Here we are specifying
        * "someXYZLogin" as the configuration to be used. Note: This has to match the configuration
        * specified in the someFilename.someExtension authFile above.
        * The login context expects a CallbackHandler as the second parameter. Here we are specifying
        * the instance of MyCallbackHandler created earlier. The "handle()" method of this handler
        * will be called during the login process.
        */
        loginContext = new LoginContext(Configuration.getConfiguration().getClass().getName(), handler);
       /*
        * Do the login
        */
        loginContext.login();

        props = new Properties();
        props.put("endpoint.name", "client-endpoint");
        props.put("remote.connections", "default");
        props.put("remote.connection.default.port", "4447");  // the default remoting port, replace if necessary
        props.put("remote.connection.default.host", "192.168.0.194");  // the host, replace if necessary
        props.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false"); // the server defaults to SSL_ENABLED=false
        props.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "false");
        props.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
        props.put("remote.connection.default.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS", "false");
        props.put("remote.connection.default.connect.options.org.jboss.remoting3.RemotingOptions.HEARTBEAT_INTERVAL", "60000");
        props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        props.put("org.jboss.ejb.client.scoped.context", "true"); // enable scoping here

        //these 2 lines below are not necessary, if security-realm is removed from remoting-connector
        props.put("remote.connection.default.username", userName);
        props.put("remote.connection.default.password", password);

        //these 2 lines below are not necessary, if security-realm is removed from remoting-connector
        //props.put("Context.SECURITY_PRINCIPAL", userName);
        //props.put("Context.SECURITY_CREDENTIALS", password);

        props.put("Context.SECURITY_PROTOCOL", "org.jboss.security.ClientLoginModule");

        try {
            context = new InitialContext(props);

            Object object = loginContext.getSubject().getPrincipals().toArray()[0];
            Principal principal = (Principal) object;

            contextMap.put(principal, context);

            return principal;

        } catch (NamingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void logout() {

        try {
            context.close();
            servicesByName.clear();
        } catch (NamingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}