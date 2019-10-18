package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;
import cl.minsal.semantikos.model.relationships.RelationshipAttributeDefinition;
import cl.minsal.semantikos.model.relationships.Target;
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
 * @author Gustavo Punucura
 */
@Stateless
public class RelationshipAttributeDAOImpl implements RelationshipAttributeDAO {

    /**
     * El logger para esta clase
     */
    private static final Logger logger = LoggerFactory.getLogger(RelationshipDAOImpl.class);

    @EJB
    private TargetDAO targetDAO;

    @EJB
    private RelationshipDAO relationshipDAO;

    @EJB
    private RelationshipDefinitionDAO relationshipDefinitionDAO;

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    @Override
    public RelationshipAttribute createRelationshipAttribute(RelationshipAttribute relationshipAttribute) {
        long idRelationShipAttribute;
        long idTarget = targetDAO.persist(relationshipAttribute.getTarget(), relationshipAttribute.getRelationAttributeDefinition().getTargetDefinition());

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship_attribute.create_relationship_attribute(?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.NUMERIC);
            call.setLong(2, relationshipAttribute.getRelationAttributeDefinition().getId());
            call.setLong(3, relationshipAttribute.getRelationship().getId());
            call.setLong(4, idTarget);
            call.execute();

            //ResultSet rs = (ResultSet) call.getObject(1);

            if (call.getLong(1) > 0) {
                idRelationShipAttribute = call.getLong(1);
                if (idRelationShipAttribute == -1) {
                    String errorMsg = "La relacion no fue creada";
                    logger.error(errorMsg);
                    throw new EJBException(errorMsg);
                }
                relationshipAttribute.setIdRelationshipAttribute(idRelationShipAttribute);
            } else {
                String errorMsg = "La relacion no fue creada";
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
            //rs.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return relationshipAttribute;
    }

    @Override
    public List<RelationshipAttribute> getRelationshipAttribute(Relationship relationship) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship_attribute.get_relationship_attribute_by_relationship(?); end;";

        ResultSet rs;
        List<RelationshipAttribute> relationshipAttributeList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, relationship.getId());
            call.execute();

            rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                relationshipAttributeList.add(createRelationshipAttributeFromResultSet(rs, relationship));
            }
            rs.close();
            call.close();
            connection.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return relationshipAttributeList;
    }

    private RelationshipAttribute createRelationshipAttributeFromResultSet(ResultSet rs, Relationship relationship) throws SQLException {

        Target target = targetDAO.getTargetByID(relationship.getRelationshipDefinition().getTargetDefinition(), rs.getLong("id_destiny"));
        //Relationship relationship = relationshipDAO.getRelationshipByID(rs.getLong("id_relationship"));

        //RelationshipAttributeDefinition relationshipAttributeDefinition = relationshipDefinitionDAO.getRelationshipAttributeDefinitionBy(rs.getLong("id_relation_attribute_definition")) ;
        long relationshipAttributeDefinitionId = rs.getLong("id_rel_att_def");

        RelationshipAttributeDefinition relationshipAttributeDefinition = relationship.getRelationshipDefinition().findRelationshipAttributeDefinitionsById(relationshipAttributeDefinitionId).get(0);

        RelationshipAttribute relationshipAttribute = new RelationshipAttribute(relationshipAttributeDefinition, relationship, target);
        relationshipAttribute.setIdRelationshipAttribute(rs.getLong("id"));
        return relationshipAttribute;
    }

    @Override
    public void update(RelationshipAttribute relationshipAttribute) {

        //ConnectionBD connect = new ConnectionBD();
        String sql = "begin ? := stk.stk_pck_relationship_attribute.update_relation_attribute(?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.NUMERIC);
            call.setLong(2, relationshipAttribute.getIdRelationshipAttribute());
            call.setLong(3, relationshipAttribute.getRelationship().getId());
            call.setLong(4, getTargetByRelationshipAttribute(relationshipAttribute));
            call.setLong(5, relationshipAttribute.getRelationAttributeDefinition().getId());
            //call.setTimestamp(5, relationship.getValidityUntil());
            call.execute();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

    }

    @Override
    public Long getTargetByRelationshipAttribute(RelationshipAttribute relationshipAttribute) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship_attribute.get_id_target_by_id_relationship_attribute(?); end;";

        Long result;
        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, relationshipAttribute.getIdRelationshipAttribute());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                result = rs.getLong(1);
            } else {
                String errorMsg = "No se obtuvo respuesta desde la base de datos.";
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
            rs.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return result;
    }
}
