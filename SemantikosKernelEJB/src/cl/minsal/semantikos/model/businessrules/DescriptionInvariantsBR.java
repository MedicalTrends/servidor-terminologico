package cl.minsal.semantikos.model.businessrules;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;

/**
 * @author Andrés Farías on 9/13/16.
 */
public class DescriptionInvariantsBR {
    /**
     * Este método es responsable de aplicar todas las invariantes sobre la entidad de Categorías.
     *
     * @param description La categoría sobre la cual se validan las invariantes.
     */
    public void invariants(Description description) {
        brDescriptionInvariant001(description);
    }

    /**
     * ﻿BR-DES-001: La descripción de tipo FSN siempre terminar con el Tag Semántikos asociado al concepto de la
     * descripción.
     *
     * @param description La descripción cuya invariante se verifica.
     */
    private void brDescriptionInvariant001(Description description) {
        ConceptSMTK conceptSMTK = description.getConceptSMTK();
        TagSMTK tagSMTK = conceptSMTK.getTagSMTK();
        Description descriptionFSN = conceptSMTK.getDescriptionFSN();

        /* La validación */
        String tagSMTKParentisis = "(" + tagSMTK.getName().toLowerCase() + ")";
        String descTerm = descriptionFSN.getTerm().toLowerCase();
        if (!descTerm.endsWith(tagSMTKParentisis)) {
            throw new BusinessRuleException("BR-DES-001", "La descripción FSN debe terminar con el Tag Semántikos entre paréntesis.");
        }
    }
}
