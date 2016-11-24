package cl.minsal.semantikos.model.businessrules;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.Description;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;

/**
 * Esta clase implementa todas las <em>invariantes</em> de un concepto.
 *
 * @author Andrés Farías on 9/12/16.
 */
public class ConceptInvariantsBR {

    public void invariants(ConceptSMTK conceptSMTK) {
        brConceptInvariant001(conceptSMTK);
        brConceptInvariant002(conceptSMTK);
        brConceptInvariant003(conceptSMTK);
    }

    /**
     * Esta regla de negocio valida que todas las descripciones satisfacen las invariantes.
     */
    private void brConceptInvariant003(ConceptSMTK conceptSMTK) {
        DescriptionInvariantsBR descriptionInvariantsBR = new DescriptionInvariantsBR();
        for (Description description : conceptSMTK.getDescriptions()) {
            descriptionInvariantsBR.invariants(description);
        }
    }

    /**
     * <p>
     * Este método implementa la regla de negocio BR-CON-001.
     * Esta regla de negocio es una <b>invariante</b>.
     * </p>
     * <p>BR-CON-001: En el diseño de un nuevo concepto, el término de tipo de descriptor preferido, es
     * obligatorio.</p>
     *
     * @param conceptSMTK El concepto cuya invariante se verifica.
     */
    private void brConceptInvariant001(ConceptSMTK conceptSMTK) {
        if (conceptSMTK.getDescriptionFavorite() == null) {
            throw new BusinessRuleException("BR-CON-001", "Un concepto siempre debe tener una descripción preferida.");
        }
    }

    /**
     * <p>
     * Este método implementa la regla de negocio BR-CON-002.
     * Esta regla de negocio es una <b>invariante</b>.
     * </p>
     * <p>BR-CON-001: En el diseño de un nuevo concepto, el término de tipo de descriptor FSN, es
     * obligatorio.</p>
     *
     * @param conceptSMTK El concepto cuya invariante se verifica.
     */
    private void brConceptInvariant002(ConceptSMTK conceptSMTK) {
        if (conceptSMTK.getDescriptionFSN() == null) {
            throw new BusinessRuleException("BR-CON-001", "Un concepto siempre debe tener una descripción FSN.");
        }
    }
}
