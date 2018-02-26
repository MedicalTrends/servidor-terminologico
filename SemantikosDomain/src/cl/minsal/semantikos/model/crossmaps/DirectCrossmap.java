package cl.minsal.semantikos.model.crossmaps;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Andrés Farías on 11/3/16.
 */
public class DirectCrossmap extends Crossmap implements Target, Serializable {


    public DirectCrossmap(ConceptSMTK sourceConcept, CrossmapSetRecord target, RelationshipDefinition relationshipDefinition, Timestamp validityUntil) {
        super(sourceConcept, target, relationshipDefinition, validityUntil);
    }
    /**
     * Este constructor sobre-escribe el constructor de la clase Target para asignar los objetos del tipo correcto, en
     * particular el del Target.
     *
     * @param id                     El ID del crossmap directo.
     * @param sourceConcept          El concepto origen de la relación.
     * @param target                 El crossmapSetMember de la relación.
     * @param relationshipDefinition La definición de la relación.
     * @param validityUntil          La fecha de vigencia.
     */
    public DirectCrossmap(@NotNull long id, @NotNull ConceptSMTK sourceConcept, @NotNull CrossmapSetRecord target, @NotNull RelationshipDefinition relationshipDefinition, Timestamp validityUntil) {
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
    public String getRepresentation() {
        return toString();
    }

    @Override
    public Target copy() {
        return null;
    }

    @Override
    public CrossmapSetRecord getTarget() {
        return (CrossmapSetRecord) super.getTarget();

    }
}
