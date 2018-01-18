package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.model.users.Institution;

import javax.ejb.Remote;

/**
 * TODO: Eliminar esta interfaz, no es necesaria.
 */

@Remote
public interface InstitutionDeletionBR {

    public void verifyPreConditions(Institution institution);

}
