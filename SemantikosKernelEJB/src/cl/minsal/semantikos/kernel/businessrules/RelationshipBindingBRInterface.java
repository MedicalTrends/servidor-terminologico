package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.Local;
import javax.ejb.Remote;

/**
 * TODO: Eliminar esta interfaz, no es necesaria.
 */

@Remote
public interface RelationshipBindingBRInterface {

    public void verifyPreConditions(ConceptSMTK concept, Relationship relationship, User user);

    public void postActions(Relationship relationship, User user);

    public void brSCT005(ConceptSMTK concept);

    public void brSCT001(ConceptSMTK concept, Relationship relationship);

    public void brSTK001(ConceptSMTK concept, Relationship relationship);

    public void brSTK002(ConceptSMTK concept, Relationship relationship);

    public void brSTK003(ConceptSMTK concept, Relationship relationship);

    public void brSTK004(ConceptSMTK concept, Relationship relationship);

}
