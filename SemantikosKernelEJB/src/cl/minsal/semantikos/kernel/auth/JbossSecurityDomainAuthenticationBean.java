package cl.minsal.semantikos.kernel.auth;

import cl.minsal.semantikos.kernel.daos.AuthDAO;
import cl.minsal.semantikos.model.exceptions.PasswordChangeException;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.ProfileFactory;
import cl.minsal.semantikos.model.users.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.AuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;

import static cl.minsal.semantikos.model.users.ProfileFactory.ADMINISTRATOR_PROFILE;
import static cl.minsal.semantikos.model.users.ProfileFactory.DESIGNER_PROFILE;
import static cl.minsal.semantikos.model.users.ProfileFactory.MODELER_PROFILE;


/**
 * @author Francisco Méndez on 18-05-2016.
 */
@Stateless(name = "JbossSecurityDomainAuthenticationEJB")
public class JbossSecurityDomainAuthenticationBean extends AuthenticationMethod {

    static private final Logger logger = LoggerFactory.getLogger(JbossSecurityDomainAuthenticationBean.class);
    public static final String BASE64_ENCODING = "BASE64";
    public static final String BASE16_ENCODING = "HEX";

    public static final int MAX_FAILED_LOGIN_ATTEMPTS = 5;

    @EJB
    AuthDAO authDAO;

    /**
     * Autenticación utilizada al logearse desde la aplicación WEB.
     * @param email
     * @param password
     * @param request
     * @return
     * @throws AuthenticationException
     */
    public boolean authenticate(String email, String password, HttpServletRequest request) throws AuthenticationException {

        User user = authDAO.getUserByEmail(email);

        if (user == null)
            throw new AuthenticationException("Usuario no existe");

        if (user.isLocked())
            throw new AuthenticationException("Usuario bloqueado. Contacte al administrador");

        try {

            //si ya esta logueado debo desbloguearlo para evitar exception
            if (request.getUserPrincipal() != null) {
                request.logout();
            }

            //login programático que resuelve jboss
            request.login(email, password);

        } catch (ServletException e) {

            //aumenta en 1 los intentos fallidos y si son mas que el maximo bloquea a usuario
            failLogin(user);

            logger.debug("Error de login", e);
            //throw (AuthenticationException) new AuthenticationException("Error de autenticacion: " + e.getMessage()).initCause(e);
            throw (AuthenticationException) new AuthenticationException("Error de autenticacion: e-mail o contraseña no son correctos");
        }

        /**
         * Validar perfiles
         */
        if( user.getProfiles().contains(ADMINISTRATOR_PROFILE) ||
            user.getProfiles().contains(DESIGNER_PROFILE) ||
            user.getProfiles().contains(MODELER_PROFILE) ) {
            authDAO.markLogin(email);
            return true;
        }
        else {
            throw new AuthenticationException("No posee los perfiles suficientes para realizar esta acción");
        }

    }

    /**
     * Autenticación utilizada al invocar un WS. La autenticación básica únicamente provee un username y un password
     * @param username
     * @param password
     * @return
     * @throws AuthenticationException
     */
    public boolean authenticate(String username, String password) throws AuthenticationException {

        User user = authDAO.getUserByEmail(username);

        if (user == null) {
            throw new AuthenticationException("Usuario no existe");
        }

        if (user.isLocked()) {
            throw new AuthenticationException("Usuario bloqueado. Contacte al administrador");
        }

        String passwordHash = createPasswordHash("MD5", BASE64_ENCODING, null, null, password);

        if (!user.getPasswordHash().equals(passwordHash)) {
            //aumenta en 1 los intentos fallidos y si son mas que el maximo bloquea a usuario
            failLogin(user);
            throw new AuthenticationException("Nombre de usuario o contraseña no es correcta");
        }

        /**
         * Validar perfiles
         */
        for (Profile profile : user.getProfiles()) {
            if(profile.equals(ProfileFactory.WS_CONSUMER_PROFILE)) {
                authDAO.markLogin(username);
                return true;
            }
        }

        throw new AuthenticationException("No posee los perfiles suficientes para realizar esta acción");

    }

    private void failLogin(User user) {


        authDAO.markLoginFail(user.getEmail());

        if (user.getFailedLoginAttempts() + 1 == MAX_FAILED_LOGIN_ATTEMPTS)
            authDAO.lockUser(user.getEmail());

    }

    public boolean hasRole(String username, String role) {
        return false;
    }

    public boolean isInGroup(String username, String group) {
        return false;
    }

    public List<String> getUsersInGroup(String group) {
        return null;
    }



    /*
    Actualiza el password del usuario
    recibe el usuario y el pass en texto plano
    Lo encripta y lo guarda manteniendo los ultimos 5 passwords
     */
    @Override
    public void setUserPassword(String username, String password) throws PasswordChangeException {
        User user = authDAO.getUserByEmail(username);

        String passwordHash = createPasswordHash("MD5", BASE64_ENCODING, null, null, password);

        if ((user.getPasswordHash() != null && user.getPasswordHash().equals(passwordHash))
                || (user.getLastPasswordHash1() != null && user.getLastPasswordHash1().equals(passwordHash))
                || (user.getLastPasswordHash2() != null && user.getLastPasswordHash2().equals(passwordHash))
                || (user.getLastPasswordHash3() != null && user.getLastPasswordHash3().equals(passwordHash))
                || (user.getLastPasswordHash4() != null && user.getLastPasswordHash4().equals(passwordHash))
                )
            throw new PasswordChangeException("El password no puede ser igual a uno de los ultimos 5 passwords usados");

        user.setLastPasswordHash4(user.getLastPasswordHash3());
        user.setLastPasswordHash3(user.getLastPasswordHash2());
        user.setLastPasswordHash2(user.getLastPasswordHash1());
        user.setLastPasswordHash1(user.getPasswordHash());
        user.setPasswordHash(passwordHash);

        user.setLastPasswordChange(new Date());


        authDAO.updateUserPasswords(user);
    }

    @Override
    public String createUserPassword(String username, String password) {
        return createPasswordHash("MD5", BASE64_ENCODING, null, null, password);
    }

    @Override
    public User getUser(String email) {
        return authDAO.getUserByEmail(email);
    }


    /**
     * Calculate a password hash using a MessageDigest.
     *
     * @param hashAlgorithm - the MessageDigest algorithm name
     * @param hashEncoding  - either base64 or hex to specify the type of
     *                      encoding the MessageDigest as a string.
     * @param hashCharset   - the charset used to create the byte[] passed to the
     *                      MessageDigestfrom the password String. If null the platform default is
     *                      used.
     * @param username      - ignored in default version
     * @param password      - the password string to be hashed
     *
     * @return the hashed string if successful, null if there is a digest exception
     */
    private String createPasswordHash(String hashAlgorithm, String hashEncoding,
                                      String hashCharset, String username, String password) {
        byte[] passBytes;
        String passwordHash = null;

        // convert password to byte data
        try {
            if (hashCharset == null)
                passBytes = password.getBytes();
            else
                passBytes = password.getBytes(hashCharset);
        } catch (UnsupportedEncodingException uee) {
            logger.error("charset {} not found. Using platform default.", hashCharset, uee);
            passBytes = password.getBytes();
        }

        // calculate the hash and apply the encoding.
        try {
            MessageDigest md = MessageDigest.getInstance(hashAlgorithm);

            md.update(passBytes);

            byte[] hash = md.digest();
            if (hashEncoding.equalsIgnoreCase(BASE64_ENCODING)) {
                passwordHash = encodeBase64(hash);
            } else if (hashEncoding.equalsIgnoreCase(BASE16_ENCODING)) {
                passwordHash = encodeBase16(hash);
            } else {
                logger.error("Unsupported hash encoding format {}", hashEncoding);
            }
        } catch (Exception e) {
            logger.error("Password hash calculation failed ", e);
        }
        return passwordHash;
    }


    /**
     * Hex encoding of hashes, as used by Catalina. Each byte is converted to
     * the corresponding two hex characters.
     */
    private String encodeBase16(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            // top 4 bits
            char c = (char) ((b >> 4) & 0xf);
            if (c > 9)
                c = (char) ((c - 10) + 'a');
            else
                c = (char) (c + '0');
            sb.append(c);
            // bottom 4 bits
            c = (char) (b & 0xf);
            if (c > 9)
                c = (char) ((c - 10) + 'a');
            else
                c = (char) (c + '0');
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * BASE64 encoder implementation.
     * Provides encoding methods, using the BASE64 encoding rules, as defined
     * in the MIME specification, <a href="http://ietf.org/rfc/rfc1521.txt">rfc1521</a>.
     */
    private String encodeBase64(byte[] bytes) {
        String base64 = null;
        try {
            base64 = Base64Encoder.encode(bytes);

        } catch (Exception e) {
        }
        return base64;
    }

}
