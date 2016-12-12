package cl.minsal.semantikos.model.businessrules;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.User;
import cl.minsal.semantikos.model.relationships.Relationship;

import javax.ejb.Local;

/**
 * TODO: Eliminar esta interfaz, no es necesaria.
 */

@Local
public interface RelationshipBindingBRInterface {

    public void verifyPreConditions(ConceptSMTK concept, Relationship relationship, User user);

    public void postActions(Relationship relationship, User user);

    public void brSCT005(ConceptSMTK concept);
}
