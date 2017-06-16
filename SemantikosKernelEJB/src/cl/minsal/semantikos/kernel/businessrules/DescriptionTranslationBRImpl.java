package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.kernel.components.CategoryManager;
import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import java.util.List;

import static cl.minsal.semantikos.model.descriptions.DescriptionType.*;

/**
 * @author Andrés Farías on 8/26/16.
 */
@Stateless
public class DescriptionTranslationBRImpl implements DescriptionTranslationBR {

    private static final Logger logger = LoggerFactory.getLogger(DescriptionTranslationBRImpl.class);

    public void apply(ConceptSMTK sourceConcept,ConceptSMTK targetConcept, Description description,
                      ConceptManager conceptManager, CategoryManager categoryManager) {

        /* Se validan las pre-condiciones para realizar el movimiento de descripciones */
        validatePreConditions(sourceConcept,description, targetConcept, conceptManager, categoryManager);

        /* Traslado de Descripciones abreviadas */
        brDescriptionTranslate001(sourceConcept, targetConcept, description);
    }

    /**
     * Este método es responsable de realizar la validación de las pre-condiciones.
     *
     * @param description   La descripción que se desea validar.
     * @param targetConcept El concepto al cual se desea mover la descripción.
     */
    public void validatePreConditions(ConceptSMTK sourceConcept, Description description, ConceptSMTK targetConcept,
                                      ConceptManager conceptManager, CategoryManager categoryManager) {

        /* Descripciones que no se pueden trasladar */
        pcDescriptionTranslate001(description);

        /* Estados posibles para trasladar descripciones */
        brDescriptionTranslate011(sourceConcept, targetConcept, conceptManager);

        /* Condiciones en concepto destino */
        brDescriptionTranslate012(targetConcept, description, conceptManager, categoryManager);
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
            throw new BusinessRuleException("BR-DES-002", "Las descripciones a trasladar no pueden ser de tipo FSN (Full Specified Name)”.");
        }

        /* Validación de tipo Preferida */
        if (description.getDescriptionType().equals(PREFERIDA)) {
            throw new BusinessRuleException("BR-DES-011", "Las descripciones a trasladar no pueden ser de tipo Preferida.");
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
    private void brDescriptionTranslate011(ConceptSMTK sourceConcept, ConceptSMTK targetConcept,
                                           ConceptManager conceptManager) {

        /* Desde conceptos modelados a conceptos en borrador */
        if (!sourceConcept.isModeled() && targetConcept.isModeled() || sourceConcept.isModeled() && targetConcept.isModeled()  ) {
            return;
        }

        if(conceptManager.getPendingConcept().getId()==sourceConcept.getId()) {
            return;
        }

        throw new BusinessRuleException("BR-UNK", "No se puede trasladar una descripción a un concepto Borrador");
    }

    /**
     * ﻿BR-DES-004: Al trasladar una descripción, ésta no debe existir dentro de la categoría del concepto destino
     * Los tipos de traslado pueden ser:
     * <ul>
     * <li> Trasladar una descripción</li>
     * </ul>
     *
     * @param targetConcept El concepto al cual se traslada la descripción.
     * @param description la descripción.
     */
    private void brDescriptionTranslate012(ConceptSMTK targetConcept, Description description,
                                           ConceptManager conceptManager, CategoryManager categoryManager) {

        ConceptSMTK aConcept = categoryManager.categoryContains(targetConcept.getCategory(), description.getTerm(), description.isCaseSensitive());
        if (aConcept != null) {
            if(aConcept.getId() == conceptManager.getNoValidConcept().getId()){
                return;
            }
            throw new BusinessRuleException("BR-UNK", "Un término sólo puede existir una vez en una categoría. Descripción perteneciente a concepto: "+aConcept);
        }

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
                    logger.info("BR-UNK", "Aplicando regla de negocio de Movimiento de traslados de descripciones Abreviadas");
                    description.setDescriptionType(GENERAL);
                }
            }
        }

    }
}
