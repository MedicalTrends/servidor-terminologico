package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.audit.ConceptAuditAction;
import cl.minsal.semantikos.model.audit.RefSetAuditAction;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;

/**
 * @author Andrés Farías on 8/30/16.
 */
public class HistoryRecordBL {

    /**
     * Este método aplica todas las reglas de negocio para validar si se puede realizar el registro.
     *
     * @param conceptAuditAction La acción de Registro de Historial que se desea realizar.
     */
    public void validate(ConceptAuditAction conceptAuditAction) {
        brAud001(conceptAuditAction.getSubjectConcept());
    }

    /**
     * Este método aplica todas las reglas de negocio para validar si se puede realizar el registro.
     *
     * @param refSetAuditAction La acción de Registro de Historial que se desea realizar.
     */
    public void validate(RefSetAuditAction refSetAuditAction) {
        brAud002(refSetAuditAction);

    }

    /**
     * BR-AUD-002: Se guardará en el historial un registro de la creación de un RefSet.
     *
     * @param refSetAuditAction El refSet sobre el cual se realiza el registro en el historial.
     */
    private void brAud002(RefSetAuditAction refSetAuditAction) {
        //TODO FIX isPersistent
        if (refSetAuditAction.getRefSet().getId()  == PersistentEntity.NON_PERSISTED_ID )
            throw new BusinessRuleException("BR-AUD-002", "Solo se pueden registrar acciones relativas a Refsets que se encuentran persistidos");
    }

    /**
     * BR-AUD-001: Cuando se realice una modificación de un Concepto Modelado, se guardará previamente en el sistema un
     * registro de la información actual del Concepto, para que sea mostrado en el listado de modificaciones del
     * Historial.
     *
     * @param conceptSMTK El concepto sobre el cual se realiza el registro en el historial.
     */
    private void brAud001(ConceptSMTK conceptSMTK) {
        if (!conceptSMTK.isModeled())
            throw new BusinessRuleException("BR-AUD-001", "No satisfecha: solo se pueden registrar acciones relativas a Conceptos que se encuentran Modelados");
    }
}
