package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.relationships.TargetType;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.util.List;

/**
 * @author Andrés Farías on 11/24/16.
 */
@Singleton
public class ConceptTransferBR {

    @EJB
    private ConceptManager conceptManager;

    /**
     * Este método es responsable de verificar el cumplimiento de todas las pre-condiciones asociadas a la
     * transferencia
     * de conceptos.
     *
     * @param conceptSMTK El concepto que se desea trasladar y sobre el cual se validan las pre-condiciones.
     */
    public void validatePreConditions(ConceptSMTK conceptSMTK) throws Exception {

        /* Antes de validar las pre-condiciones se actualizan las relaciones del concepto en caso que no las tenga actualizadas */
        conceptManager.loadRelationships(conceptSMTK);

        /* Sólo se pueden trasladar Conceptos Modelados y que no tengan Relaciones a otros Conceptos Semantikos */
        preCondition001(conceptSMTK);

        /* No se pueden trasladar el concepto "Pendientes" ni el concepto No Validos de la categoría Concepto Especial */
        preCondition002(conceptSMTK);
    }

    /**
     * BR-TRANS-001: Sólo se pueden trasladar Conceptos Modelados y que no tengan Relaciones a otros Conceptos
     * Semantikos
     *
     * @param conceptSMTK El concepto que se desea trasladar y sobre el cual se validan las pre-condiciones.
     */
    private void preCondition001(ConceptSMTK conceptSMTK) {

        if (!conceptSMTK.isModeled() || !conceptSMTK.getRelationshipsTo(TargetType.SMTK).isEmpty()) {
            throw new BusinessRuleException("BR-TRANS-001", "Sólo se pueden trasladar Conceptos Modelados y que no tengan Relaciones a otros Conceptos Semantikos");
        }
        List<ConceptSMTK> conceptSMTKList = conceptManager.getRelatedConcepts(conceptSMTK);
        if(!conceptSMTKList.isEmpty()){
            throw new BusinessRuleException("BR-TRANS-001", "Sólo se pueden trasladar Conceptos Modelados y que no tengan Relaciones a otros Conceptos Semantikos");

        }
    }

    /**
     * BR-TRANS-002: El Concepto “Pendientes” y el Concepto “No Válidos” Modelados de categoría Concepto Especial no se
     * pueden Trasladar.
     *
     * @param conceptSMTK El concepto que se quiere trasladar.
     */
    private void preCondition002(ConceptSMTK conceptSMTK) {

        ConceptSMTK noValidConcept = conceptManager.getNoValidConcept();
        ConceptSMTK pendingConcept = conceptManager.getPendingConcept();

        /* Se valida que el concepto en cuestión no sea ni el no valido ni el pendientes */
        if (conceptSMTK.equals(noValidConcept) || conceptSMTK.equals(pendingConcept)){
            throw new BusinessRuleException("BR-TRANS-002", "El Concepto “Pendientes” y el Concepto “No Válidos” " +
                    "Modelados de categoría Concepto Especial no se pueden Trasladar.");
        }
    }
}
