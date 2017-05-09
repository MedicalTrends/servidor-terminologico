package cl.minsal.semantikos.model.businessrules;

import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.User;

import static cl.minsal.semantikos.model.ProfileFactory.ADMINISTRATOR_PROFILE;


/**
 * @author Andrés Farías on 9/20/16.
 */
public class RefSetCreationBR {

    public void validatePreConditions(Institution refSetInstitution, User user) {
        brRefSet001(user);
        brRefSet005(refSetInstitution, user);
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
        if (!user.getProfiles().contains(ADMINISTRATOR_PROFILE)) {
            throw new BusinessRuleException("BR-RefSet-001", "El RefSet puede ser creado por un usuario con el perfil " + ADMINISTRATOR_PROFILE);
        }
    }
}
