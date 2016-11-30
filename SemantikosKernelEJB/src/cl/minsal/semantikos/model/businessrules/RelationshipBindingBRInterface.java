package cl.minsal.semantikos.model.businessrules;

import cl.minsal.semantikos.kernel.daos.ConceptDAO;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.User;
import cl.minsal.semantikos.model.relationships.Relationship;

import javax.ejb.Local;
import javax.validation.constraints.NotNull;

/**
 * Created by des01c7 on 17-11-16.
 */

@Local
public interface RelationshipBindingBRInterface {

    public void verifyPreConditions(ConceptSMTK concept, Relationship relationship, User user);

    public void postActions(Relationship relationship, @NotNull ConceptDAO conceptDAO, User user);
}
