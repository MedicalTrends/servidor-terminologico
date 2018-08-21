package cl.minsal.semantikos.clients;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by des01c7 on 23-05-18.
 */
public class DefaultJaasConfiguration extends Configuration {
    public AppConfigurationEntry[] getAppConfigurationEntry(String arg0) {
        Map options = new HashMap();
        AppConfigurationEntry[] entries =
                {
                        /*new AppConfigurationEntry(
                                "org.jboss.as.security.remoting.RemotingLoginModule",
                                AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
                                options),*/
                        new AppConfigurationEntry(
                                "org.jboss.security.ClientLoginModule",
                                AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
                                options)
                };
        return entries;
    }
    public void refresh() {}
}
