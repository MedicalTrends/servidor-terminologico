package cl.minsal.semantikos.model.crossmaps;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Un CrossMap es una relación atributo de un Concepto.
 *
 * @author Andrés Farías
 */
public abstract class Crossmap extends Relationship implements Serializable {

    public Crossmap(ConceptSMTK sourceConcept, CrossmapSetMember target, RelationshipDefinition relationshipDefinition, Timestamp validityUntil) {
        super(sourceConcept, target, relationshipDefinition, new ArrayList<RelationshipAttribute>(), validityUntil);
    }

    public Crossmap(@NotNull long id, @NotNull ConceptSMTK sourceConcept, @NotNull CrossmapSetMember target, @NotNull RelationshipDefinition relationshipDefinition, Timestamp validityUntil) {
        super(id, sourceConcept, target, relationshipDefinition, validityUntil,new ArrayList<RelationshipAttribute>());
    }

    public abstract boolean is(CrossMapType indirect);
}
