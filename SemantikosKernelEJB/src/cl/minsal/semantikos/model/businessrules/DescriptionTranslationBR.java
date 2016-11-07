package cl.minsal.semantikos.model.businessrules;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.Description;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static cl.minsal.semantikos.model.DescriptionType.*;

/**
 * @author Andrés Farías on 8/26/16.
 */
public class DescriptionTranslationBR {

    private static final Logger logger = LoggerFactory.getLogger(DescriptionTranslationBR.class);

    public void apply(ConceptSMTK targetConcept, Description description) {

        ConceptSMTK sourceConcept = description.getConceptSMTK();

        /* Se validan las pre-condiciones para realizar el movimiento de descripciones */
        validatePreConditions(description, targetConcept);

        /* Traslado de Descripciones abreviadas */
        brDescriptionTranslate001(sourceConcept, targetConcept, description);
    }

    /**
     * Este método es responsable de realizar la validación de las pre-condiciones.
     *
     * @param description   La descripción que se desea validar.
     * @param targetConcept El concepto al cual se desea mover la descripción.
     */
    public void validatePreConditions(Description description, ConceptSMTK targetConcept) {

        /* Descripciones que no se pueden trasladar */
        pcDescriptionTranslate001(description);

        /* Estados posibles para trasladar descripciones */
        brDescriptionTranslate011(description.getConceptSMTK(), targetConcept);
    }

    /**
     * BR-DES-002: Las descripciones a trasladar no pueden ser de tipo “FSN”, ni “Preferida”.
     * ﻿BR-DES-011: Las descripciones a trasladar de un concepto modelado cuando se edita no pueden ser del tipo FSN ni
     * preferida.
     *
     * @param description La descripción que se desea trasladar.
     */
    private void pcDescriptionTranslate001(Description description) {

        /* Validación de tipo FSN */
        if (description.getDescriptionType().equals(FSN)) {
            throw new BusinessRuleException("Las descripciones a trasladar no pueden ser de tipo FSN (Full Specified Name)”.");
        }

        /* Validación de tipo Preferida */
        if (description.getDescriptionType().equals(PREFERIDA)) {
            throw new BusinessRuleException("Las descripciones a trasladar no pueden ser de tipo Preferida.");
        }
    }

    /**
     * ﻿BR-DES-003: Sólo se pueden trasladar descripciones desde conceptos en borrador o modelados únicamente a
     * conceptos modelados
     * Los tipos de traslado pueden ser:
     * <ul>
     * <li>Trasladar una descripción desde un Concepto en Borrador a un Concepto Modelado</li>
     * </ul>
     *
     * @param targetConcept El concepto al cual se traslada la descripción.
     */
    private void brDescriptionTranslate011(ConceptSMTK sourceConcept, ConceptSMTK targetConcept) {

        /* Desde conceptos modelados a conceptos en borrador */
        if (!sourceConcept.isModeled() && targetConcept.isModeled() || sourceConcept.isModeled() && targetConcept.isModeled()  ) {
            return;
        }

        throw new BusinessRuleException("No se puede trasladar una descripción a un concepto Borrador");
    }

    /**
     * En el proceso de trasladar una Descripción de Tipo Descriptor “Abreviada”, si el concepto destino ya tiene
     * definida una descripción Abreviada, entonces la descripción a ser trasladada pasará como tipo descriptor
     * “General”.
     *
     * @param sourceConcept El concepto en donde se encuentra la descripción inicialmente.
     * @param targetConcept El concepto al cual se quiere mover la descripción.
     * @param description   La descripción que se desea trasladar.
     */
    private void brDescriptionTranslate001(ConceptSMTK sourceConcept, ConceptSMTK targetConcept, Description description) {

        /* Aplica si el tipo de la descripción es Abreviada */
        if (description.getDescriptionType().equals(ABREVIADA)) {

            /* Se busca en el concepto destino si posee alguna descripción del tipo Abreviada */
            List<Description> descriptions = targetConcept.getDescriptions();
            for (Description aTargetDescription : descriptions) {

                /* Si tiene una del tipo abreviada, la descripción a trasladar cambia de tipo a General */
                if (aTargetDescription.getDescriptionType().equals(ABREVIADA)) {
                    logger.info("Aplicando regla de negocio de Movimiento de traslados de descripciones Abreviadas");
                    description.setDescriptionType(GENERAL);
                }
            }
        }

    }
}
