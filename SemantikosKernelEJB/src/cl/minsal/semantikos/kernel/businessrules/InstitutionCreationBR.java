package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.Remote;

/**
 * TODO: Eliminar esta interfaz, no es necesaria.
 */

@Remote
public interface InstitutionCreationBR {

    public void verifyPreConditions(Institution institution);

    public void br401UniqueInstitutionCode(Institution institution);

}
