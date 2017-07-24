package cl.minsal.semantikos.model.audit;

import javax.ejb.EJBException;

public enum AuditableEntityType {

    CONCEPT(1, "Concepto"),
    RELATIONSHIP(2, "Relacione"),
    DESCRIPTION(3, "Descripción"),
    CATEGORY(4, "Categoría"),
    REFSET(5,"Refset");

    /** El identificador único del tipo de Entidad */
    private long id;

    /* El nombre de la entidad */
    private String entityName;

    AuditableEntityType(long id, String entityName) {
        this.id = id;
        this.entityName = entityName;
    }

    public static AuditableEntityType valueOf(long idAuditEntityType) {
        for (AuditableEntityType auditableEntityType : values()) {
            if (auditableEntityType.id == idAuditEntityType) {
                return auditableEntityType;
            }
        }

        throw new EJBException("No existe un tipo de entidad con ID = " + idAuditEntityType);
    }

    public long getId() {
        return id;
    }

    public String getEntityName() {
        return entityName;
    }
}
