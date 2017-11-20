package cl.minsal.semantikos.kernel.daos.ws;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetType;

import javax.ejb.Local;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author Andrés Farías
 */
@Local
public interface RelationshipWSDAO {

    /**
     * Este método es responsable de recuperar las relaciones donde el concepto de origen coincide con el
     * <code>idConcept</code> dado como argumento.
     *
     * @param conceptSMTK El id del concepto cuyas relaciones se quiere recuperar.
     *
     * @return Una lista con las relaciones del concepto.
     */
    List<Relationship> getRelationshipsBySourceConcept(ConceptSMTK conceptSMTK);

    /**
     * Este método es responsable de recuperar las relaciones donde el concepto de origen coincide con el
     * <code>idConcept</code> dado como argumento.
     *
     * @param conceptSMTK El id del concepto cuyas relaciones se quiere recuperar.
     *
     * @return Una lista con las relaciones del concepto.
     */
    Future<List<Relationship>> getRelationshipsBySourceConceptAsync(ConceptSMTK conceptSMTK);

    public List<Relationship> findRelationshipsLike(RelationshipDefinition relationshipDefinition, Target target);

}
