package cl.minsal.semantikos.model.businessrules;

import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;

import static cl.minsal.semantikos.model.users.ProfileFactory.ADMINISTRATOR_PROFILE;

/**
 * @author Andrés Farías on 9/20/16.
 */
public class RefSetUpdateBR {

    /**
     * Este método realiza la validación de las pre-condiciones necesarias para poder modificar un RefSet.
     *
     * @param refSet El RefSet que se desea modificar.
     * @param user   El usuario que realiza la modificación.
     */
    public void validatePreConditions(RefSet refSet, User user) {
        brRefSet001(user);
        brRefSet002(refSet, user);
    }

    /**
     * BR-RefSet-001: Los RefSets son administrados por usuarios que tienen perfil de Administrador de RefSets.
     *
     * @param user El usuario que realiza la operación.
     */
    protected void brRefSet001(User user) {
        if (user.getProfiles().equals(ADMINISTRATOR_PROFILE)) {
            throw new BusinessRuleException("BR-RefSet-001", "Los RefSets puede ser actualizados sólo por usuario con el perfil " + ADMINISTRATOR_PROFILE);
        }
    }

    /**
     * BR-RefSet-002: sólo pueden modificarlos los Administradores que están asociados a dicha Institución.
     *
     * @param refSet El RefSet que se desea modificar.
     * @param user   El usuario que realiza la operación.
     */
    protected void brRefSet002(RefSet refSet, User user) {
        for (Institution institution: user.getInstitutions() ) {
            if(institution.getId()==refSet.getInstitution().getId())return;
        }throw new BusinessRuleException("BR-RefSet-002", "Un usuario puede editar RefSets de instituciones a las que se encuentra asociado.");

    }
}
