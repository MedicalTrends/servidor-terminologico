package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.RefSetManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrés Farías on 8/25/16.
 */
@Singleton
public class ConceptEditionBusinessRuleContainerImpl implements BusinessRulesContainer, ConceptEditionBusinessRuleContainer {

    private static final Logger logger = LoggerFactory.getLogger(ConceptEditionBusinessRuleContainerImpl.class);

    @EJB
    private ConceptManager conceptManager;

    @EJB
    private RefSetManager refSetManager;

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
        //br101ConceptInvalidation(conceptSMTK);
        br102ConceptInvalidation(conceptSMTK);
        br103ConceptInvalidation(conceptSMTK);
        br104ConceptInvalidation(conceptSMTK);
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

    /**
     * Este método es responsable de implementar la regla de negocio para invalidar un concepto: No es posible
     * invalidar conceptos que se encuentran invalidados.
     *
     * @param conceptSMTK Concepto que se desea invalidar.
     */
    private void br102ConceptInvalidation(ConceptSMTK conceptSMTK) {
        if (conceptSMTK.getValidUntil() != null) {
            throw new BusinessRuleException("BR-UNK", "No es posible invalidar un concepto que ya se encuentra invalidado.");
        }
    }

    /**
     * Este método es responsable de implementar la regla de negocio para invalidar un concepto: No es posible
     * invalidar conceptos que se encuentran modelados.
     *
     * @param conceptSMTK Concepto que se desea invalidar.
     */
    private void br103ConceptInvalidation(ConceptSMTK conceptSMTK) {

        List<ConceptSMTK> relatedConcepts = conceptManager.getRelatedConcepts(conceptSMTK);

        if(!relatedConcepts.isEmpty()) {
            throw new BusinessRuleException("BR-UNK", "No es posible invalidar el concepto, ya que tiene los siguientes conceptos relacionados: " + relatedConcepts);
        }

    }

    /**
     * Este método es responsable de implementar la regla de negocio para invalidar un concepto: No es posible
     * invalidar conceptos que se encuentran modelados.
     *
     * @param conceptSMTK Concepto que se desea invalidar.
     */
    private void br104ConceptInvalidation(ConceptSMTK conceptSMTK) {

        List<RefSet> refSets = refSetManager.getRefsetsBy(conceptSMTK);
        List<RefSet> validRefSets = new ArrayList<>();

        for (RefSet refSet : refSets) {
            if(refSet.isValid()) {
                validRefSets.add(refSet);

            }
        }

        if(!validRefSets.isEmpty()) {
            throw new BusinessRuleException("BR-UNK", "No es posible invalidar el concepto, ya que pertenece a los siguientes RefSet vigentes: " + validRefSets);
        }

    }


}
