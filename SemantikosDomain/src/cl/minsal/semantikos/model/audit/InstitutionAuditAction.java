package cl.minsal.semantikos.model.audit;

import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.User;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Andrés Farías on 8/23/16.
 */
public class InstitutionAuditAction extends AuditAction implements Serializable {

    String detail;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public InstitutionAuditAction(Institution subjectInstitution, AuditActionType auditActionType, Timestamp actionDate, User user, AuditableEntity auditableEntity) {
        super(auditActionType, actionDate, user, auditableEntity, subjectInstitution);
    }

    public Institution getSubjectInstitution() {
        return (Institution) getBaseEntity();
    }


    public String detailAuditAction() {

        String detail = "";

        if (this.getAuditableEntity().getClass().equals(Institution.class)) {
            Institution institution = (Institution) this.getAuditableEntity();
            detail = getDetail();
        }

        return detail;
    }

}