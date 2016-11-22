package cl.minsal.semantikos.model.crossmaps;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.crossmaps.Crossmap;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetType;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * @author Andrés Farías on 11/3/16.
 */
public class DirectCrossmap extends Crossmap implements Target {

    public DirectCrossmap(ConceptSMTK sourceConcept, CrossmapSetMember target, RelationshipDefinition relationshipDefinition, Timestamp validityUntil) {
        super(sourceConcept, target, relationshipDefinition, validityUntil);
    }

    public DirectCrossmap(@NotNull long id, @NotNull ConceptSMTK sourceConcept, @NotNull CrossmapSetMember target, @NotNull RelationshipDefinition relationshipDefinition, Timestamp validityUntil) {
        super(id, sourceConcept, target, relationshipDefinition, validityUntil);
    }

    @Override
    public boolean is(CrossMapType indirect) {
        return indirect.equals(CrossMapType.DIRECT);
    }

    @Override
    public TargetType getTargetType() {
        return null;
    }

    @Override
    public Target copy() {
        return null;
    }
}
