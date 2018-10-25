package cl.minsal.semantikos.jaas;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.AuthenticationManager;
import cl.minsal.semantikos.kernel.components.UserManager;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.User;
import org.jboss.security.SecurityAssociation;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

public class CustomEJBLoginModule implements LoginModule {

    private CallbackHandler handler;
    private Subject subject;
    private User userPrincipal;
    private Profile rolePrincipal;
    private User login;
    private List<Profile> userGroups;

    private static Properties props;
    private static Context context;

    private static String APP_NAME = "SemantikosCentral/";
    private static String MODULE_NAME = "SemantikosKernelEJB/";

    @Override
    public void initialize(Subject subject,
                           CallbackHandler callbackHandler,
                           Map<String, ?> sharedState,
                           Map<String, ?> options) {

        handler = callbackHandler;
        this.subject = subject;
    }

    @Override
    public boolean login() throws LoginException {

        Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback("login");
        callbacks[1] = new PasswordCallback("password", true);

        try {
            handler.handle(callbacks);
            String name = ((NameCallback) callbacks[0]).getName();
            String password = String.valueOf(((PasswordCallback) callbacks[1]).getPassword());

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
            props.put("remote.connection.default.username", name);
            props.put("remote.connection.default.password", password);

            context = new InitialContext(props);
            AuthenticationManager authenticationManager = (AuthenticationManager) lookupRemoteStatelessEJB(AuthenticationManager.class);

            // Here we validate the credentials against some
            // authentication/authorization provider.
            // It can be a Database, an external LDAP,
            // a Web Service, etc.
            // For this tutorial we are just checking if
            // user is "user123" and password is "pass123"
            login = authenticationManager.login();

            if(login != null) {
                // We store the username and roles
                // fetched from the credentials provider
                // to be used later in commit() method.
                // For this tutorial we hard coded the
                // "admin" role
                userGroups = new ArrayList<Profile>();
                userGroups.addAll(login.getProfiles());
                return true;
            }

            else {
                // If credentials are NOT OK we throw a LoginException
                throw new LoginException("Authentication failed");
            }

        } catch (IOException e) {
            throw new LoginException(e.getMessage());
        } catch (UnsupportedCallbackException e) {
            throw new LoginException(e.getMessage());
        } catch (NamingException e) {
            throw new LoginException(e.getMessage());
        }
    }

    @Override
    public boolean commit() throws LoginException {

        userPrincipal = login;
        subject.getPrincipals().add(userPrincipal);

        if (userGroups != null && userGroups.size() > 0) {
            for (Profile groupName : userGroups) {
                rolePrincipal = new Profile(groupName);
                subject.getPrincipals().add(rolePrincipal);
            }
        }

        //Registrar contexto en ServiceLocator para usuario autenticado
        ServiceLocator.getInstance().registerContext(userPrincipal.getEmail(), context);

        return true;
    }

    @Override
    public boolean abort() throws LoginException {
        return false;
    }

    @Override
    public boolean logout() throws LoginException {
        subject.getPrincipals().remove(userPrincipal);
        subject.getPrincipals().remove(rolePrincipal);

        //Desregistrar contexto de ServiceLocator
        ServiceLocator.getInstance().unregisterContext(userPrincipal.getEmail());

        return true;
    }

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

        return remoteEjb;
    }

    private static String getServiceName(Type type) {
        String[] tokens = type.toString().split(" ")[1].split("\\.");
        return tokens[tokens.length-1]+"Impl";
    }

    private static String getViewClassName(Type type) {
        return type.toString().split(" ")[1];
    }

}

