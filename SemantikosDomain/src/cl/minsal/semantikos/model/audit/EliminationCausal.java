package cl.minsal.semantikos.model.audit;

import java.io.Serializable;

import static cl.minsal.semantikos.model.audit.AuditActionNature.*;

/**
 * @author Andrés Farías on 8/23/16.
 */
public enum EliminationCausal implements Serializable {

    CONCEPT_CREATION(1, "1° Causal"),
    CONCEPT_PUBLICATION(2, "2° Causal"),
    CONCEPT_FAVOURITE_DESCRIPTION_CHANGE(3, "3° Causal")
    ;

    /** Identificador único de la base de datos */
    private long id;

    /** Nombre o descripción del cambio */
    private String name;


    EliminationCausal(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
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
     * @param idEliminationCausal El identificador del AuditActionType.
     *
     * @return El objeto que representa la acción de auditoría.
     */
    public static EliminationCausal valueOf(long idEliminationCausal) {
        for (EliminationCausal auditActionType : values()) {
            if (auditActionType.getId() == idEliminationCausal) {
                return auditActionType;
            }
        }

        throw new IllegalArgumentException("No hay una causal de eliminación con ID=" + idEliminationCausal);
    }

}

