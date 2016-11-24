package cl.minsal.semantikos.model.businessrules;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.User;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.SnomedCTRelationship;

import java.util.List;

/**
 * @author Andrés Farías on 8/30/16.
 */
public class RelationshipRemovalBR {

    /**
     * Se verifican las reglas de negocio relativas a la eliminación de una relación.
     *
     * @param relationship La relación que se desea dejar novigente.
     * @param user         El usuaario que realiza la operación.
     */
    public void applyRules(Relationship relationship, User user) {

        /* Siempre debe haber una relación SNOMED definitoria en conceptos modelados */
        brRelationshipRemoval001(relationship);
    }

    /**
     * BR-REL-001: Se puede borrar una relación de un concepto modelado, siempre y cuando éste posea al menos una
     * relación Snomed de tipo “ES UN” o “ES UN MAPEO DE”.
     */
    private void brRelationshipRemoval001(Relationship relationship) {
        ConceptSMTK sourceConcept = relationship.getSourceConcept();

        /* Esta regla sólo aplica a conceptos modelados */
        if (!sourceConcept.isModeled()) {
            return;
        }

        /*
         * Se itera sobre todas las relaciones a Snomed. Si se encuentra una que no sea la que se desea eliminar,
         * que esté vigente, y que sea definitoria, se cumple la regla de negocio.
         */
        List<SnomedCTRelationship> relationshipsSnomedCT = sourceConcept.getRelationshipsSnomedCT();
        for (SnomedCTRelationship snomedCTRelationship : relationshipsSnomedCT) {
            if (!snomedCTRelationship.equals(relationship) && snomedCTRelationship.isDefinitional()) {
                return;
            }
        }

        /* Se se recorrieron todas las relaciones y ninguna satisfizo las condiciones es que no se debe borrar la relación */
        throw new BusinessRuleException("BR-REL-001", "Se puede borrar una relación de un concepto modelado, siempre y " +
                "cuando éste posea al menos una relación Snomed de tipo “ES UN” o “ES UN MAPEO DE”.");

    }
}
