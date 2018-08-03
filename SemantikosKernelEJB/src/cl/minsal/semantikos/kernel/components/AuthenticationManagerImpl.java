package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.auth.Base64Encoder;

import cl.minsal.semantikos.kernel.daos.AuthDAO;
import cl.minsal.semantikos.model.exceptions.PasswordChangeException;
import cl.minsal.semantikos.model.users.*;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.DeclareRoles;
import javax.naming.AuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.Date;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import static cl.minsal.semantikos.model.users.ProfileFactory.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * @author Francisco Méndez on 19-05-2016.
 */
@Stateless
@SecurityDomain("SemantikosDomain")
@DeclareRoles({Roles.ADMINISTRATOR_ROLE, Roles.DESIGNER_ROLE, Roles.MODELER_ROLE, Roles.WS_CONSUMER_ROLE, Roles.REFSET_ADMIN_ROLE, Roles.QUERY_ROLE})
public class AuthenticationManagerImpl implements AuthenticationManager{

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationManagerImpl.class);

    public static final String BASE64_ENCODING = "BASE64";
    public static final String BASE16_ENCODING = "HEX";
    public static final int MAX_FAILED_LOGIN_ATTEMPTS = 5;

    @EJB
    private InstitutionManager institutionManager;

    @EJB
    private AuthDAO authDAO;

    long MAX_DURATION = MILLISECONDS.convert(5, MINUTES);

    @PermitAll
    public boolean authenticate(String email, String password, HttpServletRequest request) throws AuthenticationException {

        //return getAuthenticationMethod().authenticate(email, password, request);
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
            throw (AuthenticationException) new AuthenticationException("Error de autenticacion: e-mail o contraseña no son correctos");
        }

        /**
         * Validar perfiles
         */
        if( user.getProfiles().contains(ADMINISTRATOR_PROFILE) ||
                user.getProfiles().contains(DESIGNER_PROFILE) ||
                user.getProfiles().contains(MODELER_PROFILE) ||
                user.getProfiles().contains(REFSET_ADMIN_PROFILE) ) {
            authDAO.markLogin(email);
            return true;
        }
        else {
            throw new AuthenticationException("No posee los perfiles suficientes para realizar esta acción");
        }
    }

    @PermitAll
    @Override
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
        if( user.getProfiles().contains(ADMINISTRATOR_PROFILE) ||
                user.getProfiles().contains(DESIGNER_PROFILE) ||
                user.getProfiles().contains(MODELER_PROFILE) ||
                user.getProfiles().contains(REFSET_ADMIN_PROFILE) ) {
            authDAO.markLogin(username);
            return true;
        }
        else {
            throw new AuthenticationException("No posee los perfiles suficientes para realizar esta acción");
        }
    }

    @PermitAll
    public User authenticateWS(String username, String password) throws AuthenticationException {
        User user = authDAO.getUserByEmail(username);
        if (user == null)
            throw new AuthenticationException("Usuario no existe");

        if (user.isLocked())
            throw new AuthenticationException("Usuario bloqueado. Contacte al administrador");

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
                /*
                Para evitar contención en BD al actualizar concurrentemente el mismo registro, se actualiza
                el ultimo login solo si se ha sobrepasado una ventana de tiempo
                 */
                if( (System.currentTimeMillis() - user.getLastLogin().getTime()) > MAX_DURATION ) {
                    authDAO.markLogin(username);
                }
                return user;
            }
        }

        throw new AuthenticationException("No posee los perfiles suficientes para realizar esta acción");
    }

    @PermitAll
    public Institution validateInstitution(String idInstitution) throws Exception {

        Institution institution = null;

        if(idInstitution.isEmpty()) {
            throw new Exception("No se ha especificado idEstablecimiento como parámetro de esta operación");
        }

        try {
            institution = institutionManager.getInstitutionById(Long.parseLong(idInstitution));
        }
        catch (Exception e) {
            throw new Exception("El parámetro idEstablecimiento debe ser un valor numérico");
        }

        if(institution == null) {
            throw new Exception("No existe un establecimiento con el idEstablecimiento proporcionado");
        }

        return institution;
    }

    @PermitAll
    public User getUserDetails(String email) {
        return this.getUser(email);
    }




    //@RolesAllowed("Administrador")
    @PermitAll()
    public void setUserPassword(String username, String password) throws PasswordChangeException {
        User user = authDAO.getUserByEmail(username);

        String passwordHash = createPasswordHash("MD5", BASE64_ENCODING, null, null, password);

        if ((user.getPasswordHash() != null && user.getPasswordHash().equals(passwordHash))
                || (user.getLastPasswordHash1() != null && user.getLastPasswordHash1().equals(passwordHash))
                || (user.getLastPasswordHash2() != null && user.getLastPasswordHash2().equals(passwordHash))
                || (user.getLastPasswordHash3() != null && user.getLastPasswordHash3().equals(passwordHash))
                || (user.getLastPasswordHash4() != null && user.getLastPasswordHash4().equals(passwordHash))
                ) {
            throw new PasswordChangeException("El password no puede ser igual a uno de los ultimos 5 passwords usados");
        }

        user.setLastPasswordHash4(user.getLastPasswordHash3());
        user.setLastPasswordHash3(user.getLastPasswordHash2());
        user.setLastPasswordHash2(user.getLastPasswordHash1());
        user.setLastPasswordHash1(user.getPasswordHash());
        user.setPasswordHash(passwordHash);

        user.setLastPasswordChange(new Date());


        authDAO.updateUserPasswords(user);
    }

    @PermitAll()
    //@RolesAllowed("Administrador")
    public String createUserPassword(User user, String username, String password) throws PasswordChangeException {
        user.setPasswordHash(this.createUserPassword(username, password));
        return user.getPasswordHash();
    }

    @PermitAll()
    //@RolesAllowed("Administrador")
    public void createUserVerificationCode(User user, String username, String password) throws PasswordChangeException {
        user.setVerificationCode(this.createUserPassword(username, username+"."+password));
    }

    @PermitAll()
    public boolean checkPassword(User user, String username, String password) {
        //mromero: createUserPassword puede retornar null, por eso se controla este caso
        String userPass = this.createUserPassword(username, password);
        if(userPass!=null)
            return userPass.equals(user.getPasswordHash());
        else
            return false;
    }

    private void failLogin(User user) {


        authDAO.markLoginFail(user.getEmail());

        if (user.getFailedLoginAttempts() + 1 == MAX_FAILED_LOGIN_ATTEMPTS)
            authDAO.lockUser(user.getEmail());

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

    private String createUserPassword(String username, String password) {
        return createPasswordHash("MD5", BASE64_ENCODING, null, null, password);
    }

    private User getUser(String email) {
        return authDAO.getUserByEmail(email);
    }

}
