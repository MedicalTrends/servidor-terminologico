package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.kernel.components.CategoryManager;
import cl.minsal.semantikos.kernel.components.DescriptionManager;
import cl.minsal.semantikos.kernel.daos.ConceptDAO;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.PendingTerm;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.util.Arrays;
import java.util.List;

/**
 * @author Andrés Farías on 11/23/16.
 */
@Singleton
public class PendingTermAddingBR {

    @EJB
    ConceptDAO conceptDAO;

    @EJB
    private DescriptionManager descriptionManager;

    @EJB
    private CategoryManager categoryManager;

    /**
     * Este método valida las post-condiciones asociadas a la agregación del c
     *
     * @param pendingTerm El término pendiente que se ha agregado.
     */
    public void validatePreConditions(PendingTerm pendingTerm) {

        /* El término pendiente no debe existir dentro de los términos pendientes */
        preCondition001(pendingTerm);
    }

    /**
     * Este método valida las post-condiciones asociadas a la agregación del c
     *
     * @param pendingTerm El término pendiente que se ha agregado.
     */
    public void validatePostConditions(PendingTerm pendingTerm) {

        /* El concepto fue asociado al concepto especial 'Pendientes' */
        postCondition001(pendingTerm);

        /* El término pendiente debe tener una descripción asociada */
        postCondition002(pendingTerm);
    }

    /**
     * BR-PEND-002: El sistema deberá guarda sólo un formulario por Término Pendiente.
     *
     * @param pendingTerm El término que se desea agregar
     */
    private void preCondition001(PendingTerm pendingTerm) {
        /* La búsqueda de térimnos se realiza en la categoría del concepto especial */
        Category specialConceptCategory = conceptDAO.getPendingConcept().getCategory();

        /* Se obtienen descripciones similares (no hay busqueda exacta por el momento) */
        String termToAdd = pendingTerm.getTerm();
        List<Description> descriptions = descriptionManager.searchDescriptionsByTerm(termToAdd, Arrays.asList(specialConceptCategory));
        for (Description description : descriptions) {

            /* Y se compara el término con el que se desea agregar */
            if (description.getTerm().equals(termToAdd)) {
                throw new BusinessRuleException("BR-PEND-002", "El sistema deberá guarda sólo un formulario por Término Pendiente.");
            }
        }
    }

    /**
     * BR-PEND-001: El valor del campo Término del Formulario de Solicitud quedará asociado a una descripción
     * perteneciente a un concepto cuyo “Pendientes”  de la Categoría Concepto Especial.
     *
     * @param pendingTerm El término agregado.
     */
    private void postCondition001(PendingTerm pendingTerm) {

        /* Se recupera el concepto pendiente */
        ConceptSMTK pendingConcept = conceptDAO.getPendingConcept();

        List<Description> descriptions = pendingConcept.getDescriptions();
        for (Description description : descriptions) {
            if (description.getTerm().equals(pendingTerm.getTerm())) {
                return;
            }
        }

        /* En este punto, no se encontró el término en las descripcioens del concepto */
        throw new BusinessRuleException("BR-PEND-001", "El término pendiente " + pendingTerm + " no fue agregado como descripción al concepto 'Pendientes'.");
    }

    /**
     * BR-PEND-003: El sistema registra la relación del Formulario de Solicitud con la Descripción que contiene el
     * Término Pendiente.
     *
     * @param pendingTerm El término pendiente sobre el cual se verifican las reglas de negocio.
     */
    private void postCondition002(PendingTerm pendingTerm) {

        Description relatedDescription = pendingTerm.getRelatedDescription();
        if (relatedDescription == null || !relatedDescription.isPersistent()) {
            throw new BusinessRuleException("BR-PEND-003", "El sistema registra la relación del Formulario de Solicitud con la Descripción que contiene el Término Pendiente.");
        }
    }
}
