package cl.minsal.semantikos.model.audit;

import cl.minsal.semantikos.model.User;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * @author Andrés Farías on 9/20/16.
 */
public abstract class AuditAction {

    /** El tipo de acción que se registra */
    private AuditActionType auditActionType;

    /** La fecha en que tomo lugar la acción auditable */
    private Timestamp actionDate;

    /** El usuario que realizó la acción */
    private User user;

    /** La entidad que fue el sujeto mismo de la acción: concepto, relación (atributo o SCT), descripción o categoría */
    private AuditableEntity auditableEntity;

    /* La entidad que alberga el cambio: si se cambia una descripción a un concepto, el auditableEntity es la descripción y la entidad base es el concepto. */
    private AuditableEntity baseEntity;

    public AuditAction(AuditActionType auditActionType, Timestamp actionDate, User user, AuditableEntity auditableEntity, AuditableEntity baseEntity) {
        this.auditActionType = auditActionType;
        this.actionDate = actionDate;
        this.user = user;
        this.auditableEntity = auditableEntity;
        this.baseEntity = baseEntity;
    }

    public AuditAction(AuditActionType auditActionType, Timestamp actionDate, User user, AuditableEntity auditableEntity) {
        this.auditActionType = auditActionType;
        this.actionDate = actionDate;
        this.user = user;
        this.auditableEntity = auditableEntity;
        this.baseEntity = auditableEntity;
    }

    public AuditActionType getAuditActionType() {
        return auditActionType;
    }

    public void setAuditActionType(AuditActionType auditActionType) {
        this.auditActionType = auditActionType;
    }

    public Timestamp getActionDate() {
        return actionDate;
    }

    public String getActionDateFormat() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(actionDate);
    }

    public void setActionDate(Timestamp actionDate) {
        this.actionDate = actionDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AuditableEntity getAuditableEntity() {
        return auditableEntity;
    }

    public void setAuditableEntity(AuditableEntity auditableEntity) {
        this.auditableEntity = auditableEntity;
    }

    public AuditableEntity getBaseEntity() {
        return baseEntity;
    }


}
