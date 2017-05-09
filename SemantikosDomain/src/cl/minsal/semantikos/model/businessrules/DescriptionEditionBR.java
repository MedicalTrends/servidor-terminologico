package cl.minsal.semantikos.model.businessrules;

import cl.minsal.semantikos.model.Description;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;

import static cl.minsal.semantikos.model.DescriptionType.FSN;
import static cl.minsal.semantikos.model.DescriptionType.PREFERIDA;

/**
 * @author Andrés Farías on 8/25/16.
 */
public class DescriptionEditionBR implements BusinessRulesContainer {


    /**
     * Este método es responsable de aplicar todas las validaciones de pre-condiciones.
     *
     * @param original La descripción original.
     * @param changed  La descripción cambiada.
     */
    public void validatePreConditions(Description original, Description changed) {
        brDescriptionEdition001(original, changed);
        brDescriptionEdition002(original, changed);
    }


    /**
     * Este método implementa la regla de negocio REQ-DES-004.
     * <p>
     * ﻿REQ-DES-004: La descripcion FSN no se pueden modificar si se encuentran en un concepto Modelado y la Preferida
     * no puede cambiar su termino.
     * </p>
     */
    private void brDescriptionEdition002(Description original, Description changed) {

        /* Si la descripción es FSN y está modelado es OK */
        if (original.getDescriptionType().equals(FSN) & original.getConceptSMTK().isModeled()) {
            if (!original.getTerm().equals(changed.getTerm()) && !original.getDescriptionType().equals(changed.getDescriptionType())) {
                throw new BusinessRuleException("REQ-DES-004", "No se puede modificar la descripción FSN de un concepto modelado");
            }

        }

        /* Si la descripción es Preferida y está modelado es OK también */
        if (original.getDescriptionType().equals(PREFERIDA) && original.getConceptSMTK().isModeled()) {
            if (!original.getTerm().equals(changed.getTerm())) {
                throw new BusinessRuleException("REQ-DES-004", "No se puede modificar la descripción Preferida de un concepto modelado");
            }

        }
    }

    /**
     * Este método implementa una regla de negocio...
     *
     * @param original La descripción original.
     * @param changed  La descripción cambiada.
     */
    private void brDescriptionEdition001(Description original, Description changed) {
        if (!original.getDescriptionId().equalsIgnoreCase(changed.getDescriptionId())) {
            throw new BusinessRuleException("BR-UNK", "Los CONCEPT_ID de las descripciones no coinciden.");
        }
    }


}
