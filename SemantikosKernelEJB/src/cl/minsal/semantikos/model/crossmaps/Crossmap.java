package cl.minsal.semantikos.model.crossmaps;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.relationships.*;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Un CrossMap es una relación atributo de un Concepto.
 *
 * @author Andrés Farías
 */
public class Crossmap extends Relationship implements Target {

    public Crossmap(ConceptSMTK sourceConcept, Target target, RelationshipDefinition relationshipDefinition) {
        super(sourceConcept, target, relationshipDefinition,new ArrayList<RelationshipAttribute>());
    }

    public Crossmap(@NotNull long id, @NotNull ConceptSMTK sourceConcept, @NotNull Target target, @NotNull RelationshipDefinition relationshipDefinition, Timestamp validityUntil) {
        super(id, sourceConcept, target, relationshipDefinition, validityUntil,new ArrayList<RelationshipAttribute>());
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public TargetType getTargetType() {
        return TargetType.CrossMap;
    }

    @Override
    public Target copy() {
        return new Crossmap(this.getSourceConcept(), this.getTarget(), this.getRelationshipDefinition());
    }


}
