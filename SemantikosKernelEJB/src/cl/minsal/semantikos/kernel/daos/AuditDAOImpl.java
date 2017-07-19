package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.daos.mappers.AuditMapper;
import cl.minsal.semantikos.kernel.daos.mappers.BasicTypeMapper;
import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.model.audit.*;
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
    AuditMapper auditMapper;

    @Override
    public List<ConceptAuditAction> getConceptAuditActions(long idConcept, boolean changes) {

        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_audit.get_concept_audit_actions(?,?); end;";

        List<ConceptAuditAction> auditActions = new ArrayList<>();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se invoca la consulta para recuperar las relaciones */
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, idConcept);
            call.setBoolean(3, changes);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            auditActions = auditMapper.createAuditActionsFromResultSet(rs);

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
        ConnectionBD connect = new ConnectionBD();
        /*
         * param 1: La fecha en que se realiza (Timestamp).
         * param 2: El usuario que realiza la acción (id_user).
         * param 3: concepto en el que se realiza la acción.
         * param 4: El tipo de acción que realiza
         * param 5: La entidad en la que se realizó la acción..
         */
        String sql = "begin ? := stk.stk_pck_audit.create_concept_audit_actions(?,?,?,?,?); end;";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se invoca la consulta para recuperar las relaciones */
            Timestamp actionDate = conceptAuditAction.getActionDate();
            User user = conceptAuditAction.getUser();
            AuditableEntity subjectConcept = conceptAuditAction.getBaseEntity();
            AuditActionType auditActionType = conceptAuditAction.getAuditActionType();
            AuditableEntity auditableEntity = conceptAuditAction.getAuditableEntity();

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setTimestamp(2, actionDate);
            call.setLong(3, user.getId());
            call.setLong(4, subjectConcept.getId());
            call.setLong(5, auditActionType.getId());
            call.setLong(6, auditableEntity.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                rs.getLong(1);
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
        ConnectionBD connect = new ConnectionBD();
        /*
         * param 1: La fecha en que se realiza (Timestamp).
         * param 2: El usuario que realiza la acción (id_user).
         * param 3: concepto en el que se realiza la acción.
         * param 4: El tipo de acción que realiza
         * param 5: La entidad en la que se realizó la acción..
         */
        //TODO arreglar esto
        String sql = "begin ? := stk.stk_pck_audit.create_refset_audit_actions(?,?,?,?,?); end;";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se invoca la consulta para recuperar las relaciones */
            Timestamp actionDate = refSetAuditAction.getActionDate();
            User user = refSetAuditAction.getUser();
            AuditActionType auditActionType = refSetAuditAction.getAuditActionType();
            AuditableEntity auditableEntity = refSetAuditAction.getAuditableEntity();
            AuditableEntity subjectConcept = refSetAuditAction.getBaseEntity();

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setTimestamp(2, actionDate);
            call.setLong(3, user.getId());
            call.setLong(4, subjectConcept.getId());
            call.setLong(5, auditActionType.getId());
            call.setLong(6, auditableEntity.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                rs.getLong(1);
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

}
