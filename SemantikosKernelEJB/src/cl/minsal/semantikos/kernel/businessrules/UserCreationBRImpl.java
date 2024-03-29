package cl.minsal.semantikos.kernel.businessrules;


import cl.minsal.semantikos.kernel.components.AuthenticationManager;
import cl.minsal.semantikos.kernel.components.AuthenticationManagerImpl;
import cl.minsal.semantikos.kernel.components.UserManager;
import cl.minsal.semantikos.model.exceptions.PasswordChangeException;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.kernel.factories.EmailFactory;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.model.users.UserFactory;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Hashtable;

import static cl.minsal.semantikos.model.users.ProfileFactory.MODELER_PROFILE;

/**
 * Este componente es responsable de almacenar las reglas de negocio relacionadas a la persistencia de usuarios.
 *
 * @author Andrés Farías
 */
@Singleton
public class UserCreationBRImpl implements UserCreationBR {

    private static final Logger logger = LoggerFactory.getLogger(UserCreationBRImpl.class);

    @EJB
    private UserManager userManager;

    @EJB
    private AuthenticationManager authenticationManager;

    public void preconditions(User IUser) throws BusinessRuleException {

        /* Reglas que aplican para todas las categorías */
        br301UniqueDocumentNumber(IUser);
        br302UniqueEmail(IUser);
        br303ValidEmail(IUser);
        /* Creación de acuerdo al rol */
        //br001creationRights(conceptSMTK, IUser);
    }

    /**
     * Esta regla de negocio valida que un rut debe ser único en el sistema.
     *
     * @param user El usuario
     */
    public void br301UniqueDocumentNumber(User user) {

        User found = userManager.getUserByDocumentNumber(user.getDocumentNumber());

        if(found != null && found.isValid()) {
            throw new BusinessRuleException("BR-301-UniqueDocumentNumber", "Ya existe un usuario con este número de documento en el sistema.");
        }
    }

    /**
     * Esta regla de negocio valida que un rut debe ser único en el sistema.
     *
     * @param user El usuario
     */
    public void br302UniqueEmail(User user) {

        User found = userManager.getUserByEmail(user.getEmail());

        if(found != null && found.isValid()) {
            throw new BusinessRuleException("BR-302-UniqueEmail", "Ya existe un usuario con este Email en el sistema.");
        }
    }

    /**
     * Esta regla de negocio valida que un rut debe ser único en el sistema.
     *
     * @param user El usuario
     */
    public void br303ValidEmail(User user) {

        Hashtable env = new Hashtable();
        env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        DirContext ictx = null;
        String hostName = user.getEmail().split("@")[1];
        try {
            ictx = new InitialDirContext( env );
            Attributes attrs = ictx.getAttributes( hostName, new String[] { "MX" });
            Attribute attr = attrs.get( "MX" );
            if (( attr == null ) || ( attr.size() == 0 )) {
                attrs = ictx.getAttributes( hostName, new String[] { "A" });
                attr = attrs.get( "A" );
                if( attr == null ) {
                    throw new BusinessRuleException("BR-303-ValidEmail", "No match for name '" + hostName + "'");
                }
            }

        } catch (NamingException e) {
            e.printStackTrace();
            throw new BusinessRuleException("BR-303-ValidEmail", "DNS name not found '" + hostName + "'" );
        }

    }

    /**
     * Esta regla de negocio establece que al momento de crear un usuario, se le debe crear una contraseña por defecto autogenerada
     *
     * @param user El usuario
     */
    public User br304DefaultPassword(User user) {

        try {
            String password = RandomStringUtils.random(8, 0, 20, true, true, "qw32rfHIJk9iQ8Ud7h0X".toCharArray());
            user.setPassword(password);
            user.setPasswordHash(authenticationManager.createUserPassword(user,user.getEmail(),user.getPassword()));
            return user;
        } catch (PasswordChangeException e) {
            e.printStackTrace();
            throw new BusinessRuleException("BR-304-DefaultPassword", "No se pudo generar una contraseña por defecto. Contactar a desarrollo");
        }
    }

    /**
     *  Esta regla de negocio establece que posterior a la creación de un usuario, se le debe crear código de verificación para ser confirmado vía
     *  petición http mediante un link enviado a su correo electrónico.
     *
     * @param user El usuario
     */
    public void br305VerificationCode(User user) {

        /*
        try {
            authenticationManager.createUserVerificationCode(user,user.getEmail(),user.getPassword());
        } catch (PasswordChangeException e) {
            e.printStackTrace();
            throw new BusinessRuleException("BR-305-VerificationCode", "No se pudo generar el código de activación. Contactar a desarrollo");
        }
        */
        //String verificationCode = UUID.randomUUID().toString().substring(0, 20);
        String verificationCode = RandomStringUtils.random(20, 0, 20, true, true, "qw32rfHI5Jk9iQ84Ud7h0X".toCharArray());
        user.setVerificationCode(verificationCode);
    }

    /**
     *  Esta regla de negocio establece que posterior a la creación de un usuario, el usuario debe quedar en estado bloqueado
     *
     * @param user El usuario
     */
    public void br308LockUser(User user) {

        try {
            user.setLocked(true);
            user.setFailedLoginAttempts(0);
            user.setFailedAnswerAttempts(0);
            userManager.updateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessRuleException("BR-308-LockUser", "No se pudo bloquear el usuario. Contactar a desarrollo");
        }
    }

    /**
     * Esta regla de negocio establece que posterior a la creación de un usuario, se le debe enviar un correo electrónico
     * con un mensaje que contenga:
     *  - Link de confirmación de cuenta que contenga el código de verificación
     *  - Contraseña por defecto asignada por el sistema para poder ingresar por primera vez
     *
     * @param user El usuario
     */
    public void br306SendEmail(User user, String baseURL) {

        try {

            //String link = baseURL + "/views/users/activateAccount.xhtml?key="+user.getVerificationCode();
            //String link2 = baseURL + "/views/login.xhtml";
            String link = baseURL + "/views/accounts/activate/" + user.getVerificationCode();
            String link2 = baseURL + "/views/login";
            EmailFactory.getInstance().send(user, user.getPassword(), link, link2);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessRuleException("BR-306-SendEmail", "No se pudo enviar correo a usuario. Contactar a desarrollo");
        }
    }

    /**
     * Esta regla de negocio establece que posterior a la creación de un usuario, se le debe enviar un correo electrónico
     * con un mensaje que contenga:
     *  - Link de confirmación de cuenta que contenga el código de verificación
     *  - Contraseña por defecto asignada por el sistema para poder ingresar por primera vez
     *
     * @param key El código de verificación
     */
    public User br307verificationCodeExists(String key) {

        User found = userManager.getUserByVerificationCode(key);

        return found;
    }

    /**
     * Esta accion consiste en actualizar el cache de usuarios posterior a alguna accion de CRUD sobre el repositorio
     * de usuarios
     *
     * @param user El nuevo usuario
     */
    public void br309RefreshCaches(User user) {

        UserFactory.getInstance().refresh(user);
    }

    public String getURLWithContextPath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }

    @Override
    public void verifyPreConditions(User user) throws BusinessRuleException {
        br301UniqueDocumentNumber(user);
        br303ValidEmail(user);
        br302UniqueEmail(user);
    }

    @Override
    public User preActions(User user) throws BusinessRuleException {
        user = br304DefaultPassword(user);
        return user;
    }

    @Override
    public User postActions(User user, String baseURL) throws BusinessRuleException {
        br305VerificationCode(user);
        br306SendEmail(user, baseURL);
        br308LockUser(user);
        br309RefreshCaches(user);
        return user;
    }

    /**
     * <b>BR-SMTK-001</b>: Conceptos de ciertas categorías pueden sólo ser creados por usuarios con el perfil
     * Modelador.
     *
     * @param conceptSMTK El concepto a crear ser creado.
     * @param user        El usuario que realiza la acción.
     */
    protected void br001creationRights(ConceptSMTK conceptSMTK, User user) {

        /* Categorías restringidas para usuarios con rol diseñador */
        if (conceptSMTK.getCategory().isRestriction()) {
            if (!user.getProfiles().contains(MODELER_PROFILE)) {
                logger.info("Se intenta violar la regla de negocio BR-SMTK-001 por el usuario " + user);
                throw new BusinessRuleException("BR-SMTK-001", "El usuario " + user + " no tiene privilegios para crear conceptos de la categoría " + conceptSMTK.getCategory());
            }
        }
    }


}
