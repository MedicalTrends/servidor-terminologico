package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.factories.AuditableEntityFactory;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.users.Institution;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.model.audit.*;
import cl.minsal.semantikos.model.users.UserFactory;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    InstitutionDAO institutionDAO;

    @EJB
    AuditableEntityFactory auditableEntityFactory;

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    @Override
    public List<ConceptAuditAction> getConceptAuditActions(ConceptSMTK conceptSMTK, boolean changes) {

        String sql = "begin ? := stk.stk_pck_audit.get_concept_audit_actions(?,?); end;";

        List<ConceptAuditAction> auditActions = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se invoca la consulta para recuperar las relaciones */
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, conceptSMTK.getId());
            call.setBoolean(3, changes);
            call.execute();

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
    public List<ConceptAuditAction> getConceptAuditActions(ConceptSMTK conceptSMTK) {

        String sql = "begin ? := stk.stk_pck_audit.get_all_concept_audit_actions(?); end;";

        List<ConceptAuditAction> auditActions = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se invoca la consulta para recuperar las relaciones */
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, conceptSMTK.getId());
            call.execute();

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
    public List<UserAuditAction> getUserAuditActions(User user) {

        String sql = "begin ? := stk.stk_pck_audit.get_user_audit_actions(?); end;";

        List<UserAuditAction> auditActions = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se invoca la consulta para recuperar las relaciones */
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, user.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            auditActions = createAuditActionsFromResultSet(rs, user);

            rs.close();

        } catch (SQLException e) {
            String errorMsg = "No se pudo parsear el JSON a BasicTypeDefinition.";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

        return auditActions;
    }

    @Override
    public List<InstitutionAuditAction> getInstitutionAuditActions(Institution institution) {

        String sql = "begin ? := stk.stk_pck_audit.get_institution_audit_actions(?); end;";

        List<InstitutionAuditAction> auditActions = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se invoca la consulta para recuperar las relaciones */
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, institution.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            auditActions = createAuditActionsFromResultSet(rs, institution);

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

        /*
         * param 1: La fecha en que se realiza (Timestamp).
         * param 2: El usuario que realiza la acción (id_user).
         * param 3: concepto en el que se realiza la acción.
         * param 4: El tipo de acción que realiza
         * param 5: La entidad en la que se realizó la acción..
         */
        String sql = "begin ? := stk.stk_pck_audit.create_concept_audit_actions(?,?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
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

        /*
         * param 1: La fecha en que se realiza (Timestamp).
         * param 2: El usuario que realiza la acción (id_user).
         * param 3: concepto en el que se realiza la acción.
         * param 4: El tipo de acción que realiza
         * param 5: La entidad en la que se realizó la acción..
         */
        //TODO arreglar esto
        String sql = "begin ? := stk.stk_pck_audit.create_refset_audit_actions(?,?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
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

    public void recordAuditAction(UserAuditAction userAuditAction){

        logger.debug("Registrando información de Auditoría: " + userAuditAction);

        /*
         * param 1: La fecha en que se realiza (Timestamp).
         * param 2: El usuario que realiza la acción (id_user).
         * param 3: concepto en el que se realiza la acción.
         * param 4: El tipo de acción que realiza
         * param 5: La entidad en la que se realizó la acción..
         */
        //TODO arreglar esto
        String sql = "begin ? := stk.stk_pck_audit.create_user_audit_actions(?,?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se invoca la consulta para recuperar las relaciones */
            Timestamp actionDate = userAuditAction.getActionDate();
            User user = userAuditAction.getUser();
            AuditActionType auditActionType = userAuditAction.getAuditActionType();
            AuditableEntity auditableEntity = userAuditAction.getAuditableEntity();
            AuditableEntity subjectUser = userAuditAction.getBaseEntity();

            call.registerOutParameter (1, OracleTypes.NUMERIC);
            call.setTimestamp(2, actionDate);
            call.setLong(3, user.getId());
            call.setLong(4, subjectUser.getId());
            call.setLong(5, auditActionType.getId());
            call.setLong(6, auditableEntity.getId());
            call.execute();

            if (call.getLong(1) > 0) {
                call.getLong(1);
            } else {
                String errorMsg = "La información de auditoría del usuario no fue creada por una razón desconocida. Alertar al area de desarrollo" +
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

    public void recordAuditAction(InstitutionAuditAction institutionAuditAction) {

        logger.debug("Registrando información de Auditoría: " + institutionAuditAction);
        /*
         * param 1: La fecha en que se realiza (Timestamp).
         * param 2: El usuario que realiza la acción (id_user).
         * param 3: concepto en el que se realiza la acción.
         * param 4: El tipo de acción que realiza
         * param 5: La entidad en la que se realizó la acción..
         */
        //TODO arreglar esto
        String sql = "begin ? := stk.stk_pck_audit.create_institution_audit_actions(?,?,?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /* Se invoca la consulta para recuperar las relaciones */
            Timestamp actionDate = institutionAuditAction.getActionDate();
            User user = institutionAuditAction.getUser();
            AuditActionType auditActionType = institutionAuditAction.getAuditActionType();
            AuditableEntity auditableEntity = institutionAuditAction.getAuditableEntity();
            AuditableEntity subjectUser = institutionAuditAction.getBaseEntity();

            call.registerOutParameter (1, OracleTypes.NUMERIC);
            call.setTimestamp(2, actionDate);
            call.setLong(3, user.getId());
            call.setLong(4, subjectUser.getId());
            call.setLong(5, auditActionType.getId());
            call.setLong(6, auditableEntity.getId());
            call.setString(7, institutionAuditAction.getDetail());
            call.execute();

            if (call.getLong(1) > 0) {
                call.getLong(1);
            } else {
                String errorMsg = "La información de auditoría del establecimiento no fue creada por una razón desconocida. Alertar al area de desarrollo" +
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

    @Override
    public void recordAuditActionWithDetails(ConceptAuditAction conceptAuditAction) {

        logger.debug("Registrando información de Auditoría: " + conceptAuditAction);

        /*
         * param 1: La fecha en que se realiza (Timestamp).
         * param 2: El usuario que realiza la acción (id_user).
         * param 3: concepto en el que se realiza la acción.
         * param 4: El tipo de acción que realiza
         * param 5: La entidad en la que se realizó la acción..
         */
        String sql = "begin ? := stk.stk_pck_audit.create_concept_audit_actions(?,?,?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
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
            call.setString(7, conceptAuditAction.getDetails().get(0));
            call.execute();

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

                String detail = rs.getString("detail");

                if(detail != null && !detail.isEmpty()) {
                    conceptAuditAction.getDetails().add(detail);
                }

                conceptAuditActions.add(conceptAuditAction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conceptAuditActions;
    }

    /**
     * Este método es responsable de crear un arreglo de objetos de auditoría a partir de una expresión JSON de la
     * forma:
     *
     * @param rs La expresión JSON a partir de la cual se crean los elementos de auditoría.
     *
     * @return Una lista de objetos auditables.
     */
    public List<UserAuditAction> createAuditActionsFromResultSet(ResultSet rs, User user) {

        List<UserAuditAction> userAuditActions = new ArrayList<>();

        try {

            while(rs.next()) {

                if(user == null) {
                    user = UserFactory.getInstance().findUserById(rs.getLong("id_user"));
                }

                AuditActionType auditActionType = AuditActionType.valueOf(rs.getLong("id_action_type"));
                //User user = userDAO.getUserById(rs.getLong("id_user"));
                User _user = UserFactory.getInstance().findUserById(rs.getLong("id_user_"));
                AuditableEntityType auditableEntityType = AuditableEntityType.valueOf(rs.getLong("id_audit_entity_type"));
                AuditableEntity auditableEntityByID = auditableEntityFactory.findAuditableEntityByID(rs.getLong("id_auditable_entity"), auditableEntityType);
                Timestamp date = rs.getTimestamp("date");

                UserAuditAction userAuditAction = new UserAuditAction(user, auditActionType, date, _user, auditableEntityByID);
                userAuditActions.add(userAuditAction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userAuditActions;
    }

    /**
     * Este método es responsable de crear un arreglo de objetos de auditoría a partir de una expresión JSON de la
     * forma:
     *
     * @param rs La expresión JSON a partir de la cual se crean los elementos de auditoría.
     *
     * @return Una lista de objetos auditables.
     */
    public List<InstitutionAuditAction> createAuditActionsFromResultSet(ResultSet rs, Institution institution) {

        List<InstitutionAuditAction> institutionAuditActions = new ArrayList<>();

        try {

            while(rs.next()) {

                if(institution == null) {
                    institution = institutionDAO.getInstitutionById(rs.getLong("id_institution"));
                }

                AuditActionType auditActionType = AuditActionType.valueOf(rs.getLong("id_action_type"));
                //User user = userDAO.getUserById(rs.getLong("id_user"));
                User user = UserFactory.getInstance().findUserById(rs.getLong("id_user"));
                AuditableEntityType auditableEntityType = AuditableEntityType.valueOf(rs.getLong("id_audit_entity_type"));
                AuditableEntity auditableEntityByID = auditableEntityFactory.findAuditableEntityByID(rs.getLong("id_auditable_entity"), auditableEntityType);
                Timestamp date = rs.getTimestamp("date");

                InstitutionAuditAction institutionAuditAction = new InstitutionAuditAction(institution, auditActionType, date, user, auditableEntityByID);
                institutionAuditAction.setDetail(rs.getString("detail"));
                institutionAuditActions.add(institutionAuditAction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return institutionAuditActions;
    }

}
