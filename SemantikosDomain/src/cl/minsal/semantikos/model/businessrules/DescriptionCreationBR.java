package cl.minsal.semantikos.model.businessrules;

import cl.minsal.semantikos.kernel.components.CategoryManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.DescriptionType;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.users.User;

import static cl.minsal.semantikos.model.descriptions.DescriptionType.*;

/**
 * @author Andrés Farías on 8/29/16.
 */
public class DescriptionCreationBR {

    /**
     * Este método es responsable de aplicar las reglas de negocio asociadas a la creación de una Descripción.
     *
     * @param concept         El concepto al cual se asocia la descripción.
     * @param term            El término que contiene la descripción a crear.
     * @param type            El tipo de la descripción.
     * @param user            El usuario que quiere crear la descripción.
     * @param categoryManager El componente de negocio que encapsula las operaciones referentes a las categorías.
     */
    public void applyRules(ConceptSMTK concept, String term, DescriptionType type, User user, CategoryManager categoryManager) {

        /* Se verifican las invariantes */
        Description description = new Description(concept, term, type);
        new DescriptionInvariantsBR().invariants(description);
    }

    /**
     * Este método aplica las pre-condiciones a la creación.
     *
     * @param concept         El concepto al cual se agrega la descripción.
     * @param description     La descripción que se desea agregar.
     * @param categoryManager El Manager.
     */
    public void validatePreConditions(ConceptSMTK concept, Description description, CategoryManager categoryManager, boolean edition) {

        brDescriptionCreation001(concept, description.getTerm(), categoryManager);
        brDescriptionCreation003(concept, description.getDescriptionType());

        /* Reglas para modo edición */
        if(edition){
            brDescriptionEdition003(concept, description.getDescriptionType());
        }
    }

    /**
     * Esta invariante implementa la regla de negocio BR-DES-009. Esta regla consiste en que un término es único en una
     * categoría.
     *
     * @param concept         El concepto al cual se asocia la descripción.
     * @param term            El término que contiene la descripción a crear.
     * @param categoryManager El Manager para realizar la verificación a nivel de la BDD.
     */
    private void brDescriptionCreation001(ConceptSMTK concept, String term, CategoryManager categoryManager) {
        Category category = concept.getCategory();
        ConceptSMTK aConcept = categoryManager.categoryContains(category, term);

        if (aConcept != null) {
            throw new BusinessRuleException("BR-UNK", "Un término sólo puede existir una vez en una categoría. Descripción perteneciente a concepto: "+aConcept);
        }
    }

    /**
     * ﻿BR-DES-010: Cuando se edita un concepto solo se le pueden agregar Descripciones de tipo: Abreviado, Sinónimo,
     * Ambiguo, General o Mal Escrito.
     *
     * <p>
     * Para distinguir si se está <em>editando</em> el concepto, se valida si este está persistido. Si lo está quiere
     * decir que se está editando, si no lo está, significa que se está creando.
     * </p>
     *
     * @param concept El concepto al cual se agrega la descripción.
     * @param type    El tipo de la descripción que se desea crear.
     */
    private void brDescriptionEdition003(ConceptSMTK concept, DescriptionType type) {

        if (concept.isPersistent()) {
            if (type.equals(ABREVIADA) || type.equals(SYNONYMOUS) || type.equals(AMBIGUA) || type.equals(GENERAL) || type.equals(BAD_WRITTEN) || type.equals(PREFERIDA)) {
                return;
            }

            if(!concept.isModeled() && type.equals(FSN) ){
                return;
            }

            throw new BusinessRuleException("BR-DES-010", "Cuando se edita un concepto solo se le pueden agregar Descripciones de tipo: Abreviado, Sinónimo,\n" +
                    "     * Ambiguo, General o Mal Escrito.");
        }
    }

    /**
     * ﻿BR-DES-012: Un concepto ya existente puede tener hasta 1 descripción abreviada.
     *
     * @param concept El concepto al cual se agrega la descripción.
     * @param type    El tipo de la descripción que se desea crear.
     */
    private void brDescriptionCreation003(ConceptSMTK concept, DescriptionType type) {

        for (Description description: concept.getDescriptions()) {
            if(description.getDescriptionType().equals(ABREVIADA) && description.isPersistent()){
                /* Si se está editando y es una abreviada.... */
                if (concept.isPersistent() && type.equals(ABREVIADA)) {
                    throw new BusinessRuleException("BR-DES-012", "Cuando se edita un concepto no es posible agregarle una descripción de tipo 'Abreviada'.");
                }
            }
        }
    }
}
