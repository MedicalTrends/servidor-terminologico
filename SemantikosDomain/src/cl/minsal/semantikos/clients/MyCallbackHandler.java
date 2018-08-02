package cl.minsal.semantikos.clients;

import javax.security.auth.callback.*;
import java.io.IOException;

/**
 *
 * CallbackHandler which will be invoked by the login module during the login
 * process of the client. This is a simple CallbackHandler which sets the username
 * and password, which will be later used by the Login module for authorizing the
 * subject. This class only handles NameCallback and PasswordCallback. It throws
 * an UnsupportedCallbackException, if the Callback is other than the two mentioned
 * above.
 * The username and password are provided as input to this class through its constructor.
 *
 *
 */
public class MyCallbackHandler implements CallbackHandler {

    /**
     * Username which will be set in the NameCallback, when NameCallback is handled
     */
    private String username;

    /**
     * Password which will be set in the PasswordCallback, when PasswordCallback is handled
     */
    private String password;

    /**
     * Constructor
     * @param username The username
     * @param password The password
     */
    public MyCallbackHandler(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * @param callbacks Instances of Callbacks
     * @throws IOException IOException
     * @throws UnsupportedCallbackException If Callback is other than NameCallback or PasswordCallback
     */
    public void handle(Callback callbacks[]) throws IOException, UnsupportedCallbackException {

        for(int i = 0; i < callbacks.length; i++) {
            if(callbacks[i] instanceof NameCallback) {
                NameCallback nc = (NameCallback)callbacks[i];
                nc.setName(username);
            } else if(callbacks[i] instanceof PasswordCallback) {
                PasswordCallback pc = (PasswordCallback)callbacks[i];
                pc.setPassword(password.toCharArray());
            } else {
                throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
            }

        }
    }
}