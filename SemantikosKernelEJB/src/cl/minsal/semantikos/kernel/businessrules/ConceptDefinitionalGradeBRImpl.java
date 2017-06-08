package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.kernel.components.RelationshipManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.SnomedCTRelationship;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.util.List;

import static cl.minsal.semantikos.model.relationships.TargetType.SnomedCT;

/**
 * @author Andrés Farías
 */

@Singleton
public class ConceptDefinitionalGradeBRImpl implements ConceptDefinitionalGradeBR {

    @EJB
    private RelationshipManager relationshipManager;

    public void apply(ConceptSMTK conceptSMTK) throws BusinessRuleException {

        if (conceptSMTK.isFullyDefined()) {
            validateIsFullyDefined(conceptSMTK);
        }
    }

    /**
     * Este método tienen como responsabilidad validar que el concepto está completamente definido. Esto significa que
     * no debe existir otro concepto que tenga las mismas relaciones a conceptos Snomed CT.
     *
     * @param conceptSMTK El concepto sobre el cual se quiere hacer la validación.
     *
     * @throws BusinessRuleException Se arroja si viola una o más reglas de negocio.
     */
    protected void validateIsFullyDefined(ConceptSMTK conceptSMTK) throws BusinessRuleException {

        /* Se recuperan todas las relaciones con RelationshipType que tengan destino un concepto SNOMED CT. */
        List<Relationship> sctRelationships = conceptSMTK.getRelationshipsTo(SnomedCT);

        /* Para cada relación a SCT, se verifica si existe un concepto SMTK que tenga las mismas relaciones */
        for (Relationship sctRelationship : sctRelationships) {

            /* Se recuperan todas las relaciones del mismo tipo de relación y que se dirigen al mismo concepto SCT */
            List<Relationship> relationshipsLike = relationshipManager.getRelationshipsLike(sctRelationship.getRelationshipDefinition(), sctRelationship.getTarget());
            for (Relationship relationshipCandidate : relationshipsLike) {
                ConceptSMTK candidateConcept = relationshipCandidate.getSourceConcept();

                /* Si es el mismo concepto, no importa */
                if(candidateConcept.equals(conceptSMTK)){
                    continue;
                }

                /* Se cargan las relaciones del concepto (porque no las debiera traer) */
                List<Relationship> relationshipsBySourceConcept = relationshipManager.getRelationshipsBySourceConcept(candidateConcept);
                candidateConcept.setRelationships(relationshipsBySourceConcept);

                if (haveSameSnomedCTRelationshipsButFromConceptSource(conceptSMTK.getRelationshipsSnomedCT(), candidateConcept.getRelationshipsSnomedCT()) && candidateConcept.isFullyDefined()) {
                    throw new BusinessRuleException("BR-UNK", "El concepto no es completamente definido.");
                }
            }
        }
    }

    /**
     * Este método es responsable de comparar dos conjuntos de relaciones, sin considerar en la comparación el concepto
     * origen de la relación.
     *
     * @param sourceRelationships Las relaciones origen.
     * @param targetRelationships Las relaciones destino.
     *
     * @return <code>true</code> si son los mismos conjuntos de relaciones (salvo por su concepto origen) y
     * <code>false</code> sino.
     */
    private boolean haveSameSnomedCTRelationshipsButFromConceptSource(List<SnomedCTRelationship> sourceRelationships, List<SnomedCTRelationship> targetRelationships) {

        if (sourceRelationships.size() != targetRelationships.size()) return false;

        for (SnomedCTRelationship sourceRelationship : sourceRelationships) {

            boolean isItContained = false;
            for (SnomedCTRelationship targetRelationship : targetRelationships) {
                if (sourceRelationship.equalsButConceptSource(targetRelationship)) {
                    isItContained = true;
                }
            }

            if (!isItContained) {
                return false;
            }
        }

        return true;
    }
}
