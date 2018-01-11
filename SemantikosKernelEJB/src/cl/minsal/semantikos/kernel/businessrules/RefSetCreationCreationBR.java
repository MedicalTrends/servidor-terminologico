package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.Remote;

/**
 * TODO: Eliminar esta interfaz, no es necesaria.
 */

@Remote
public interface RefSetCreationCreationBR {

    public void validatePreConditions(RefSet refSet, User user);

}
