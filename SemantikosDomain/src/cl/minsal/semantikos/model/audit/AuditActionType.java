package cl.minsal.semantikos.model.audit;

import static cl.minsal.semantikos.model.audit.AuditActionNature.CHANGE;
import static cl.minsal.semantikos.model.audit.AuditActionNature.CREATION;

/**
 * @author Andrés Farías on 8/23/16.
 */
public enum AuditActionType {

    CONCEPT_CREATION(1, "Creación de Concepto", CREATION),
    CONCEPT_PUBLICATION(2, "Publicación de Concepto", AuditActionNature.CHANGE),
    CONCEPT_INVALIDATION(18, "Concepto invalidado.", AuditActionNature.REMOVAL),
    CONCEPT_FAVOURITE_DESCRIPTION_CHANGE(3, "Cambio en descripción preferida de un Concepto", AuditActionNature.CHANGE),
    CONCEPT_DESCRIPTION_BINDING(4, "Descripción agregada a concepto", AuditActionNature.CHANGE),
    CONCEPT_DESCRIPTION_UNBINDING(5, "Descripción eliminada a concepto", AuditActionNature.CHANGE),
    CONCEPT_DESCRIPTION_MOVEMENT(16, "Descripción traslada a otro concepto", AuditActionNature.CHANGE),
    CONCEPT_DESCRIPTION_DELETION(17, "Descripción eliminada de un concepto", AuditActionNature.CHANGE),
    CONCEPT_DESCRIPTION_RECEPTION(25, "Descripción agregada por traslado", AuditActionNature.CHANGE),
    CONCEPT_CATEGORY_CHANGE(6, "Cambio de categoría de Concepto", AuditActionNature.CHANGE),
    CONCEPT_ATTRIBUTE_CHANGE(7, "Cambio de atributo de un concepto", AuditActionNature.CHANGE),
    CONCEPT_RELATIONSHIP_CREATION(8, "Nueva relación de un concepto", AuditActionNature.CHANGE),
    CONCEPT_RELATIONSHIP_REMOVAL(9, "Eliminación de relación de un concepto", AuditActionNature.CHANGE),
    CONCEPT_RELATIONSHIP_CROSSMAP_CREATION(10, "Nuevo CrossMap de un concepto", CREATION),
    CONCEPT_RELATIONSHIP_CROSSMAP_REMOVAL(11, "Eliminación de CrossMap de un concepto", AuditActionNature.REMOVAL),
    CONCEPT_RELATIONSHIP_EXTERNAL_TERMINOLOGY_CREATION(12, "Nueva referencia a terminología externa en Concepto", CREATION),
    CONCEPT_RELATIONSHIP_EXTERNAL_TERMINOLOGY_ELIMINATION(13, "Eliminación de referencia a terminología externa en Concepto", AuditActionNature.REMOVAL),
    CONCEPT_RELATIONSHIP_REFSET_CREATION(14, "Nueva referencia a RefSet en Concepto", CREATION),
    CONCEPT_RELATIONSHIP_REFSET_ELIMINATION(15, "Referencia a RefSet eliminada en Concepto", AuditActionNature.REMOVAL),
    CONCEPT_DESCRIPTION_CREATION(19, "Descripción creada", CREATION),
    REFSET_CREATION(20, "Creación de RefSet", CREATION),
    REFSET_UPDATE(21, "Actualización de RefSet", CHANGE),
    REFSET_BINDING(22, "Actualización de Descripción a RefSet", CHANGE),
    CONCEPT_RELATIONSHIP_DEFINITION_CREATION(23, "Nuevo atributo asociado a un concepto.", CHANGE),
    CONCEPT_RELATIONSHIP_ATTRIBUTE_CREATION(24, "Nueva relación definitoria asociada a un concepto", CHANGE);

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
    CHANGE, CREATION, REMOVAL,
}
