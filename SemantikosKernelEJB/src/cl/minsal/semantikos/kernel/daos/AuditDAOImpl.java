package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.factories.AuditableEntityFactory;
import cl.minsal.semantikos.kernel.factories.DataSourceFactory;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.model.audit.*;
import cl.minsal.semantikos.model.users.UserFactory;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

/**
 * @author Andrés Farías on 8/23/16.
 */
@Stateless
public class AuditDAOImpl implements AuditDAO {

    /** Logger para la clase */
    private static final Logger logger = LoggerFactory.getLogger(AuditDAOImpl.class);

    @EJB
    ConceptDAO conceptDAO;

    @EJB
    AuthDAO userDAO;

    @EJB
    AuditableEntityFactory auditableEntityFactory;

    @Override
    public List<ConceptAuditAction> getConceptAuditActions(ConceptSMTK conceptSMTK, boolean changes) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_audit.get_concept_audit_actions(?,?); end;";

        List<ConceptAuditAction> auditActions = new ArrayList<>();

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se invoca la consulta para recuperar las relaciones */
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, conceptSMTK.getId());
            call.setBoolean(3, changes);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            auditActions = createAuditActionsFromResultSet(rs, conceptSMTK);

            rs.close();

        } catch (SQLException e) {
            String errorMsg = "No se pudo parsear el JSON a BasicTypeDefinition.";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

        return auditActions;
    }

    @Override
    public void recordAuditAction(ConceptAuditAction conceptAuditAction) {

        logger.debug("Registrando información de Auditoría: " + conceptAuditAction);
        //ConnectionBD connect = new ConnectionBD();
        /*
         * param 1: La fecha en que se realiza (Timestamp).
         * param 2: El usuario que realiza la acción (id_user).
         * param 3: concepto en el que se realiza la acción.
         * param 4: El tipo de acción que realiza
         * param 5: La entidad en la que se realizó la acción..
         */
        String sql = "begin ? := stk.stk_pck_audit.create_concept_audit_actions(?,?,?,?,?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se invoca la consulta para recuperar las relaciones */
            Timestamp actionDate = conceptAuditAction.getActionDate();
            User user = conceptAuditAction.getUser();
            AuditableEntity subjectConcept = conceptAuditAction.getBaseEntity();
            AuditActionType auditActionType = conceptAuditAction.getAuditActionType();
            AuditableEntity auditableEntity = conceptAuditAction.getAuditableEntity();

            call.registerOutParameter (1, Types.NUMERIC);
            call.setTimestamp(2, actionDate);
            call.setLong(3, user.getId());
            call.setLong(4, subjectConcept.getId());
            call.setLong(5, auditActionType.getId());
            call.setLong(6, auditableEntity.getId());
            call.execute();

            //ResultSet rs = (ResultSet) call.getObject(1);

            if (call.getLong(1) > 0) {
                call.getLong(1);
            } else {
                String errorMsg = "La información de auditoría del concepto no fue creada por una razón desconocida. Alertar al area de desarrollo" +
                        " sobre esto";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al registrar en el log.";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }
    }

    public void recordAuditAction(RefSetAuditAction refSetAuditAction){
        logger.debug("Registrando información de Auditoría: " + refSetAuditAction);
        //ConnectionBD connect = new ConnectionBD();
        /*
         * param 1: La fecha en que se realiza (Timestamp).
         * param 2: El usuario que realiza la acción (id_user).
         * param 3: concepto en el que se realiza la acción.
         * param 4: El tipo de acción que realiza
         * param 5: La entidad en la que se realizó la acción..
         */
        //TODO arreglar esto
        String sql = "begin ? := stk.stk_pck_audit.create_refset_audit_actions(?,?,?,?,?); end;";

        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se invoca la consulta para recuperar las relaciones */
            Timestamp actionDate = refSetAuditAction.getActionDate();
            User user = refSetAuditAction.getUser();
            AuditActionType auditActionType = refSetAuditAction.getAuditActionType();
            AuditableEntity auditableEntity = refSetAuditAction.getAuditableEntity();
            AuditableEntity subjectConcept = refSetAuditAction.getBaseEntity();

            call.registerOutParameter (1, OracleTypes.NUMERIC);
            call.setTimestamp(2, actionDate);
            call.setLong(3, user.getId());
            call.setLong(4, subjectConcept.getId());
            call.setLong(5, auditActionType.getId());
            call.setLong(6, auditableEntity.getId());
            call.execute();

            //ResultSet rs = (ResultSet) call.getObject(1);

            if (call.getLong(1) > 0) {
                call.getLong(1);
            } else {
                String errorMsg = "La información de auditoría del refset no fue creada por una razón desconocida. Alertar al area de desarrollo" +
                        " sobre esto";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al registrar en el log.";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

    }

    /**
     * Este método es responsable de crear un arreglo de objetos de auditoría a partir de una expresión JSON de la
     * forma:
     *
     * @param rs La expresión JSON a partir de la cual se crean los elementos de auditoría.
     *
     * @return Una lista de objetos auditables.
     */
    public List<ConceptAuditAction> createAuditActionsFromResultSet(ResultSet rs, ConceptSMTK conceptSMTK) {

        List<ConceptAuditAction> conceptAuditActions = new ArrayList<>();

        try {

            while(rs.next()) {

                if(conceptSMTK == null) {
                    conceptSMTK = conceptDAO.getConceptByID(rs.getLong("id_concept"));
                }

                AuditActionType auditActionType = AuditActionType.valueOf(rs.getLong("id_action_type"));
                //User user = userDAO.getUserById(rs.getLong("id_user"));
                User user = UserFactory.getInstance().findUserById(rs.getLong("id_user"));
                AuditableEntityType auditableEntityType = AuditableEntityType.valueOf(rs.getLong("id_audit_entity_type"));
                AuditableEntity auditableEntityByID = auditableEntityFactory.findAuditableEntityByID(rs.getLong("id_auditable_entity"), auditableEntityType);
                Timestamp date = rs.getTimestamp("date");

                ConceptAuditAction conceptAuditAction = new ConceptAuditAction(conceptSMTK, auditActionType, date, user, auditableEntityByID);
                conceptAuditActions.add(conceptAuditAction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conceptAuditActions;
    }

}
