package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrés Farías on 8/25/16.
 */
public class ConceptEditionBusinessRuleContainer implements BusinessRulesContainer {

    private static final Logger logger = LoggerFactory.getLogger(ConceptEditionBusinessRuleContainer.class);



    public void preconditionsConceptEditionTag(ConceptSMTK conceptSMTK) {
        brTagSMTK002UpdateTag(conceptSMTK);
    }

    /**
     * Esta regla de negocio (no levantada aun) indica que no es posible modificar el campo CONCEPT_ID.
     *
     * @param conceptSMTK El concepto que se desea modificar.
     * @param user        El usuario que modifica el concepto.
     */
    protected void br101ConceptIDEdition(ConceptSMTK conceptSMTK, User user) {

        /* Nunca se puede modificar, asi que siempre lanza la excepción */
        throw new BusinessRuleException("BR-UNK", "No es posible modificar el CONCEPT_ID del concepto " + conceptSMTK.toString() + ", por el usuario " + user.toString());
    }

    /**
     * Un concepto puede modificar su Tag Semántikos solo si se encuentra en Borrador.
     *
     * @param conceptSMTK El concepto cuyo tag smtk se quiere modificar
     */
    protected void brTagSMTK002UpdateTag(ConceptSMTK conceptSMTK) {

        if (conceptSMTK.isModeled()) {
            String brDesc = "No es posible modificar el Tag Semántiko de un concepto Modelado.";
            String conceptInfo = "Se intentó actualizar el concepto " + conceptSMTK.toString();

            logger.info(conceptInfo + "\n" + brDesc);
            throw new BusinessRuleException("BR-UNK", brDesc + "\n" + conceptInfo);
        }
    }

    /**
     * Se aplican las reglas de pre-condición para dejar no-vigente (eliminar lógicamente) un concepto.
     *
     * @param conceptSMTK El concepto a ser eliminado.
     * @param user        El usuario que realiza la acción.
     */
    public void preconditionsConceptInvalidation(ConceptSMTK conceptSMTK, User user) {
        br101ConceptInvalidation(conceptSMTK);
    }

    /**
     * Este método es responsable de implementar la regla de negocio para invalidar un concepto: No es posible
     * invalidar conceptos que se encuentran modelados.
     *
     * @param conceptSMTK Concepto que se desea invalidar.
     */
    private void br101ConceptInvalidation(ConceptSMTK conceptSMTK) {
        if (conceptSMTK.isModeled()) {
            throw new BusinessRuleException("BR-UNK", "No es posible invalidar un concepto que se encuentra Modelado.");
        }
    }

}
