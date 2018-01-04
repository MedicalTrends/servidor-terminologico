package cl.minsal.semantikos.model.audit;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.Profile;
import cl.minsal.semantikos.model.users.User;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Andrés Farías on 8/23/16.
 */
public class UserAuditAction extends AuditAction implements Serializable {

    public UserAuditAction(User subjectUser, AuditActionType auditActionType, Timestamp actionDate, User user, AuditableEntity auditableEntity) {
        super(auditActionType, actionDate, user, auditableEntity, subjectUser);
    }

    public User getSubjectUser() {
        return (User) getBaseEntity();
    }


    public String detailAuditAction() {

        String detail = "";

        if (this.getAuditableEntity().getClass().equals(User.class)) {
            User user = (User) this.getAuditableEntity();
            detail = "Usuario: " + user.getEmail();
        }
        if (this.getAuditableEntity().getClass().equals(Profile.class)) {
            Profile profile = (Profile) this.getAuditableEntity();
            detail = this.getAuditActionType().getName() + " : Perfil: " + profile.getName();
        }
        if (this.getAuditableEntity().getClass().equals(Institution.class)) {
            Institution institution = (Institution) this.getAuditableEntity();
            detail = this.getAuditActionType().getName() + " : Establecimiento: " + institution.getName();
        }

        return detail;
    }

}