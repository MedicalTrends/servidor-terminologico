package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.SnomedCTRelationship;
import cl.minsal.semantikos.model.users.User;

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
    public void applyRules(ConceptSMTK conceptSMTK, Relationship relationship, User user) throws Exception {

        /* Siempre debe haber una relación SNOMED definitoria en conceptos modelados */
        brRelationshipRemoval001(conceptSMTK,relationship);
    }

    /**
     * BR-REL-001: Se puede borrar una relación de un concepto modelado, siempre y cuando éste posea al menos una
     * relación Snomed de tipo “ES UN” o “ES UN MAPEO DE”.
     */
    private void brRelationshipRemoval001(ConceptSMTK conceptSMTK, Relationship relationship) throws Exception {
        ConceptSMTK sourceConcept = conceptSMTK;

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
