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
public interface RelationshipBindingBR {

    public void verifyPreConditions(ConceptSMTK concept, Relationship relationship, User user) throws Exception;

    public void postActions(Relationship relationship, User user) throws Exception;

    public void brSCT005(ConceptSMTK concept) throws Exception;

    public void brSCT001(ConceptSMTK concept, Relationship relationship) throws Exception;

    public void brISP001(ConceptSMTK concept, Relationship relationship) throws Exception;

    public void brISP002(ConceptSMTK concept, Relationship relationship);

    public void brISP003(ConceptSMTK concept, Relationship relationship) throws Exception;

    public void brISP004(ConceptSMTK concept, Relationship relationship);

}
