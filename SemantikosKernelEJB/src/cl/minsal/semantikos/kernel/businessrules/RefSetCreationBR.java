package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.kernel.components.RefSetManager;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import static cl.minsal.semantikos.model.users.ProfileFactory.ADMINISTRATOR_PROFILE;
import static cl.minsal.semantikos.model.users.ProfileFactory.REFSET_ADMIN_PROFILE;


/**
 * @author Andrés Farías on 9/20/16.
 */
@Singleton
public class RefSetCreationBR {

    @EJB
    RefSetManager refSetManager;

    public void validatePreConditions(RefSet refSet, User user) {
        brRefSet001(user);
        brRefSet002(refSet);
        brRefSet005(refSet.getInstitution(), user);
    }

    /**
     * BR-RefSet-005: Puede crear (y modificar) los RefSets de cualquiera de las Instituciones a las que pertenece el
     * usuario.
     *
     * @param refSetInstitution La institución a la que está asociado el futuro RefSet.
     * @param user              El usuario que crea el RefSet.
     */
    private void brRefSet005(Institution refSetInstitution, User user) {
        if (!user.getInstitutions().contains(refSetInstitution)) {
            throw new BusinessRuleException("BR-RefSet-005", "Un Administrador sólo puede crear RefSets asociados a los establecimientos a los que pertenece el usuario");
        }
    }

    /**
     * BR-RefSet-001: Sólo usuarios con el perfil Administrador de RefSets pueden crear RefSets.
     *
     * @param user El usuario que realiza la creación.
     */
    private void brRefSet001(User user) {
        if (!user.getProfiles().contains(ADMINISTRATOR_PROFILE) && user.getProfiles().contains(REFSET_ADMIN_PROFILE)) {
            throw new BusinessRuleException("BR-RefSet-001", "El RefSet puede ser creado por un usuario con el perfil " + ADMINISTRATOR_PROFILE + " o " + REFSET_ADMIN_PROFILE);
        }
    }

    /**
     * BR-RefSet-002: El nombre del RefSet debe ser único en el sistema
     *
     * @param refSet El usuario que realiza la creación.
     */
    private void brRefSet002(RefSet refSet) {
        RefSet other = refSetManager.getRefsetByName(refSet.getName());

        if(other != null && other.getValidityUntil() == null) {
            throw new BusinessRuleException("BR-RefSet-002", "Ya existe un RefSet vigente con este nombre. RefSet " + other);
        }
    }
}
