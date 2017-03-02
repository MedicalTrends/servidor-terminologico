package cl.minsal.semantikos.model.businessrules;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.User;
import cl.minsal.semantikos.model.relationships.Relationship;

import javax.ejb.Local;

/**
 * TODO: Eliminar esta interfaz, no es necesaria.
 */

@Local
public interface UserCreationBRInterface {

    public void br301UniqueRut(User user);

    public void br302UniqueUsername(User user);

}
