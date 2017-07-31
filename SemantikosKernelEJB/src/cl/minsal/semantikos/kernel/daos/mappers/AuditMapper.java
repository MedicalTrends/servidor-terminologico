package cl.minsal.semantikos.kernel.daos.mappers;

import cl.minsal.semantikos.kernel.daos.AuthDAO;
import cl.minsal.semantikos.kernel.daos.ConceptDAO;
import cl.minsal.semantikos.kernel.factories.AuditableEntityFactory;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.audit.*;
import cl.minsal.semantikos.model.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static cl.minsal.semantikos.kernel.util.StringUtils.underScoreToCamelCaseJSON;

/**
 * Created by root on 28-06-17.
 */
@Singleton
public class AuditMapper {

    /** El logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(AuditMapper.class);

    @EJB
    ConceptDAO conceptDAO;

    @EJB
    AuthDAO userDAO;

    @EJB
    AuditableEntityFactory auditableEntityFactory;

    /**
     * Este método es responsable de crear un arreglo de objetos de auditoría a partir de una expresión JSON de la
     * forma:
     *
     * @param rs La expresión JSON a partir de la cual se crean los elementos de auditoría.
     *
     * @return Una lista de objetos auditables.
     */
    public List<ConceptAuditAction> createAuditActionsFromResultSet(ResultSet rs) {

        List<ConceptAuditAction> conceptAuditActions = new ArrayList<>();

        try {

            while(rs.next()) {

                ConceptSMTK concept = conceptDAO.getConceptByID(rs.getLong("id_concept"));
                AuditActionType auditActionType = AuditActionType.valueOf(rs.getLong("id_action_type"));
                User user = userDAO.getUserById(rs.getLong("id_user"));
                AuditableEntityType auditableEntityType = AuditableEntityType.valueOf(rs.getLong("id_audit_entity_type"));
                AuditableEntity auditableEntityByID = auditableEntityFactory.findAuditableEntityByID(rs.getLong("id_auditable_entity"), auditableEntityType);
                Timestamp date = rs.getTimestamp("date");

                ConceptAuditAction conceptAuditAction = new ConceptAuditAction(concept, auditActionType, date, user, auditableEntityByID);
                conceptAuditActions.add(conceptAuditAction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conceptAuditActions;
    }
}
