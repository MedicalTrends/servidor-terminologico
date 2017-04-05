package cl.minsal.semantikos.model.snapshots;

import static cl.minsal.semantikos.model.snapshots.AuditActionNature.CHANGE;
import static cl.minsal.semantikos.model.snapshots.AuditActionNature.CREATION;

/**
 * @author Andrés Farías on 8/23/16.
 */
public enum AuditActionType {

    SNOMED_CT_CREATION(1, "Creación Registro Componente SnomedCT", AuditActionNature.CREATION),
    SNOMED_CT_REMOVAL(2, "Eliminación Registro Componente SnomedCT", AuditActionNature.REMOVAL),
    SNOMED_CT_INVALIDATION(3, "Cambio vigente a no vigente Componente SnomedCT", AuditActionNature.CHANGE),
    SNOMED_CT_RESTORYING(4, "Cambio no vigente a vigente Componente SnomedCT", AuditActionNature.CHANGE),
    SNOMED_CT_UNMODIFYING(5, "Sin Modificación", AuditActionNature.CHANGE),
    SNOMED_CT_ERROR(6, "Error", AuditActionNature.ERROR),
    SNOMED_CT_UNDEFINED(7, "Indefinido", AuditActionNature.UNDEFINED);

    /** Identificador único de la base de datos */
    private long id;

    /** Nombre o descripción del cambio */
    private String name;

    /** Indica si la naturaleza del cambio es de edición o adición */
    private AuditActionNature change;

    AuditActionType(long id, String name, AuditActionNature nature) {
        this.id = id;
        this.name = name;
        this.change = nature;
    }

    public long getId() {
        return id;
    }

    public boolean isChange() {
        return change.equals(AuditActionNature.CHANGE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Este método es responsable de retornar el AuditActionType asociado al ID <code>idAuditActionType</code>.
     *
     * @param idAuditActionType El identificador del AuditActionType.
     *
     * @return El objeto que representa la acción de auditoría.
     */
    public static AuditActionType valueOf(long idAuditActionType) {
        for (AuditActionType auditActionType : values()) {
            if (auditActionType.getId() == idAuditActionType) {
                return auditActionType;
            }
        }
        throw new IllegalArgumentException("No hay un tipo de acción con ID=" + idAuditActionType);
    }

}

enum AuditActionNature {
    CHANGE, CREATION, REMOVAL, ERROR, UNDEFINED
}
