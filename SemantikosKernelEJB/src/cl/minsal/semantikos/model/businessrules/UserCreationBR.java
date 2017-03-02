package cl.minsal.semantikos.model.businessrules;

import cl.minsal.semantikos.kernel.auth.UserManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.Description;
import cl.minsal.semantikos.model.TagSMTK;
import cl.minsal.semantikos.model.User;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.validation.constraints.NotNull;

import static cl.minsal.semantikos.model.ProfileFactory.MODELER_PROFILE;

/**
 * Este componente es responsable de almacenar las reglas de negocio relacionadas a la persistencia de usuarios.
 *
 * @author Andrés Farías
 */
@Singleton
public class UserCreationBR implements UserCreationBRInterface {

    private static final Logger logger = LoggerFactory.getLogger(UserCreationBR.class);

    @EJB
    private UserManager userManager;

    public void preconditions(User IUser) throws BusinessRuleException {

        /* Reglas que aplican para todas las categorías */
        br301UniqueRut(IUser);

        br302UniqueUsername(IUser);

        /* Creación de acuerdo al rol */
        //br001creationRights(conceptSMTK, IUser);
    }

    /**
     * Esta regla de negocio valida que un rut debe ser único en el sistema.
     *
     * @param user El usuario
     */
    public void br301UniqueRut(User user) {

        User found = userManager.getUserByRut(user.getRut());

        if(found != null) {
            throw new BusinessRuleException("BR-301-UniqueRut", "Ya existe un usuario con este RUT en el sistema.");
        }
    }

    /**
     * Esta regla de negocio valida que un rut debe ser único en el sistema.
     *
     * @param user El usuario
     */
    public void br302UniqueUsername(User user) {

        User found = userManager.getUserByUsername(user.getUsername());

        if(found != null) {
            throw new BusinessRuleException("BR-302-UniqueUsername", "Ya existe un usuario con este Username en el sistema.");
        }
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
