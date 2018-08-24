package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.model.relationships.Relationship;

import javax.ejb.Remote;

/**
 * TODO: Eliminar esta interfaz, no es necesaria.
 */

@Remote
public interface RelationshipCreationBR {

    public void verifyPreConditions(Relationship relationship);

    public void brCrossmap001(Relationship relationship);
}
