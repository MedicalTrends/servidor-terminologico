package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;
import cl.minsal.semantikos.model.relationships.RelationshipAttributeDefinition;
import cl.minsal.semantikos.model.relationships.Target;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    @Override
    public RelationshipAttribute createRelationshipAttribute(RelationshipAttribute relationshipAttribute) {
        long idRelationShipAttribute;
        long idTarget = targetDAO.persist(relationshipAttribute.getTarget(), relationshipAttribute.getRelationAttributeDefinition().getTargetDefinition());

        ConnectionBD connect = new ConnectionBD();

        String sql = "{call semantikos.create_relationship_attribute(?,?,?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, relationshipAttribute.getRelationAttributeDefinition().getId());
            call.setLong(2, relationshipAttribute.getRelationship().getId());
            call.setLong(3, idTarget);
            call.execute();

            ResultSet rs = call.getResultSet();

            if (rs.next()) {
                idRelationShipAttribute = rs.getLong(1);
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
            rs.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return relationshipAttribute;
    }

    @Override
    public List<RelationshipAttribute> getRelationshipAttribute(Relationship relationship) {

        ConnectionBD connect = new ConnectionBD();

        String sql = "{call semantikos.get_relationship_attribute_by_relationship(?)}";
        ResultSet rs;
        List<RelationshipAttribute> relationshipAttributeList = new ArrayList<>();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, relationship.getId());
            call.execute();

            rs = call.getResultSet();

            while (rs.next()) {
                relationshipAttributeList.add(createRelationshipAttributeFromResultSet(rs, relationship));
            }
            rs.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return relationshipAttributeList;
    }

    private RelationshipAttribute createRelationshipAttributeFromResultSet(ResultSet rs, Relationship relationship) throws SQLException {

        Target target = targetDAO.getTargetByID(rs.getLong("id_destiny"));
        //Relationship relationship = relationshipDAO.getRelationshipByID(rs.getLong("id_relationship"));

        //RelationshipAttributeDefinition relationshipAttributeDefinition = relationshipDefinitionDAO.getRelationshipAttributeDefinitionBy(rs.getLong("id_relation_attribute_definition")) ;
        String relationshipDefinitionName = relationship.getRelationshipDefinition().getName();
        RelationshipAttributeDefinition relationshipAttributeDefinition = relationship.getRelationshipDefinition().findRelationshipAttributeDefinitionsByName(relationshipDefinitionName).get(0);

        RelationshipAttribute relationshipAttribute = new RelationshipAttribute(relationshipAttributeDefinition, relationship, target);
        relationshipAttribute.setIdRelationshipAttribute(rs.getLong("id"));
        return relationshipAttribute;
    }

    @Override
    public void update(RelationshipAttribute relationshipAttribute) {

        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.update_relation_attribute(?,?,?,?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, relationshipAttribute.getIdRelationshipAttribute());
            call.setLong(2, relationshipAttribute.getRelationship().getId());
            call.setLong(3, getTargetByRelationshipAttribute(relationshipAttribute));
            call.setLong(4, relationshipAttribute.getRelationAttributeDefinition().getId());
            //call.setTimestamp(5, relationship.getValidityUntil());
            call.execute();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

    }



    @Override
    public Long getTargetByRelationshipAttribute(RelationshipAttribute relationshipAttribute) {

        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.get_id_target_by_id_relationship_attribute(?)}";
        Long result;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, relationshipAttribute.getIdRelationshipAttribute());
            call.execute();

            ResultSet rs = call.getResultSet();
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
