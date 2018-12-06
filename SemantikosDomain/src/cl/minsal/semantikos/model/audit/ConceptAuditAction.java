package cl.minsal.semantikos.model.audit;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.users.User;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Andrés Farías on 8/23/16.
 */
public class ConceptAuditAction extends AuditAction implements Serializable {

    List<String> details;

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

    public ConceptAuditAction(ConceptSMTK subjectConcept, AuditActionType auditActionType, Timestamp actionDate, User user, AuditableEntity auditableEntity) {
        super(auditActionType, actionDate, user, auditableEntity, subjectConcept);
    }

    public ConceptSMTK getSubjectConcept() {
        return (ConceptSMTK) getBaseEntity();
    }

    public String detailAuditAction() {

        String detail = "";

        if (this.getAuditableEntity().getClass().equals(ConceptSMTK.class)) {
            ConceptSMTK conceptSMTK = (ConceptSMTK) this.getAuditableEntity();
            detail = "Concepto: " + conceptSMTK.getDescriptionFavorite();
        }
        if (this.getAuditableEntity().getClass().equals(Description.class)) {
            Description description = (Description) this.getAuditableEntity();
            if(this.getAuditActionType().equals(AuditActionType.CONCEPT_FAVOURITE_DESCRIPTION_CHANGE)){
                detail = "Término preferido anterior: " + description.getTerm() + " - DESCID: " + description.getDescriptionId();
            }else{
                detail = this.getAuditActionType().getName() + " - Término: " + description.getTerm() + " - DESCID: " + description.getDescriptionId();
            }
        }
        if (this.getAuditableEntity().getClass().equals(Relationship.class)) {
            Relationship relationship = (Relationship) this.getAuditableEntity();

            if (relationship.getRelationshipDefinition().isSNOMEDCT()) {
                ConceptSCT conceptSCT = (ConceptSCT) relationship.getTarget();
                detail = "Relación SNOMED CT: " + conceptSCT.getRepresentation();
            } else {
                if(relationship.getTarget().getClass().equals(ConceptSMTK.class)){
                    ConceptSMTK conceptSMTK = (ConceptSMTK) relationship.getTarget();
                    detail = "Relación " + relationship.getRelationshipDefinition().getName() + ": " + conceptSMTK.getDescriptionFSN().getTerm() +" Concept ID: "+conceptSMTK.getConceptID();

                }else{
                    detail = "Relación " + relationship.getRelationshipDefinition().getName() + ": " + relationship.getTarget().getRepresentation();

                }
            }

        }
        if(this.getAuditableEntity().getClass().equals(Category.class)){
            Category category = (Category) this.getAuditableEntity();
            detail = "Categoría Origen: " + category.getName();

        }

        return detail;
    }
}