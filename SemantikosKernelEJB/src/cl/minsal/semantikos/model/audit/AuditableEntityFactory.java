package cl.minsal.semantikos.model.audit;

import cl.minsal.semantikos.kernel.daos.*;

import javax.ejb.EJB;
import javax.ejb.Singleton;

/**
 * @author Andrés Farías on 8/24/16.
 */
@Singleton
public class AuditableEntityFactory {

    @EJB
    private ConceptDAO conceptDAO;

    @EJB
    CategoryDAO categoryDAO;

    @EJB
    DescriptionDAO descriptionDAO;

    @EJB
    RelationshipDAO relationshipDAO;

    @EJB
    RefSetDAO refSetDAO;

    /**
     * Este método es responsable de recuperar la entidad adecuada.
     *
     * @param idAuditableEntity   El ID de la base de datos de la entidad.
     * @param auditableEntityType El tipo de entidad a recuperar.
     *
     * @return La entidad completa, recuperada de la base de datos.
     */
    public AuditableEntity findAuditableEntityByID(long idAuditableEntity, AuditableEntityType auditableEntityType) {

        switch (auditableEntityType) {
            case CONCEPT:
                return conceptDAO.getConceptByID(idAuditableEntity);

            case CATEGORY:
                return categoryDAO.getCategoryById(idAuditableEntity);

            case DESCRIPTION:
                return descriptionDAO.getDescriptionBy(idAuditableEntity);

            case RELATIONSHIP:
                return relationshipDAO.getRelationshipByID(idAuditableEntity);

            case REFSET:
                return refSetDAO.getRefsetBy(idAuditableEntity);

            default:
                throw new IllegalArgumentException("El ID del tipo de Entidad Auditable no existe: " + auditableEntityType);
        }
    }
}
