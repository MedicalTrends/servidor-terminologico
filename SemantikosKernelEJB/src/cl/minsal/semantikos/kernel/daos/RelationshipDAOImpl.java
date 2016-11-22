package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.relationships.*;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.snomedct.ConceptSCTFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * @author Diego Soto / Gustavo Punucura
 */
@Stateless
public class RelationshipDAOImpl implements RelationshipDAO {

    /** El logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(RelationshipDAOImpl.class);

    @EJB
    private ConceptSCTFactory conceptSCTFactory;

    @EJB
    private RelationshipFactory relationshipFactory;

    @EJB
    private TargetDAO targetDAO;

    @Override
    public Relationship persist(Relationship relationship) {

        long idTarget= targetDAO.persist(relationship.getTarget(),relationship.getRelationshipDefinition().getTargetDefinition());

        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.create_relationship(?,?,?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, relationship.getSourceConcept().getId());
            call.setLong(2, idTarget);
            call.setLong(3, relationship.getRelationshipDefinition().getId());
            call.execute();

            ResultSet rs = call.getResultSet();

            if (rs.next()) {
                relationship.setId(rs.getLong(1));
            } else {
                String errorMsg = "La relacion no fue creada. Esta es una situación imposible. Contactar a Desarrollo";
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
            rs.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
        return relationship;
    }

    @Override
    public RelationshipDefinition persist(RelationshipDefinition relationshipDefinition){

        /* Primero se persiste el Target Definition */
        long idTargetDefinition = targetDAO.persist(relationshipDefinition.getTargetDefinition());

        ConnectionBD connect = new ConnectionBD();
        /*
         * param 1: nombre
         * param 2: descripción
         * param 3: lower boundary
         * param 4: upper boundary
         * param 5: idTargetDefinition
         */
        String sql = "{call semantikos.create_relationship_definition(?,?,?,?,?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setString(1, relationshipDefinition.getName());
            call.setString(2, relationshipDefinition.getDescription());
            call.setInt(3, relationshipDefinition.getMultiplicity().getLowerBoundary());
            call.setInt(4, relationshipDefinition.getMultiplicity().getUpperBoundary());
            call.setLong(5, idTargetDefinition);
            call.execute();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return relationshipDefinition;
    }

    @Override
    public void delete(Relationship relationship) {

        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.invalidate_relationship(?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, relationship.getId());
            call.execute();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
    }

    @Override
    public void update(Relationship relationship) {
        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.update_relation(?,?,?,?,?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, relationship.getId());
            call.setLong(2, relationship.getSourceConcept().getId());
            call.setLong(3, getTargetByRelationship(relationship));
            call.setLong(4, relationship.getRelationshipDefinition().getId());
            call.setTimestamp(5, relationship.getValidityUntil());
            call.execute();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
    }


    @Override
    public void invalidate(Relationship relationship) {
        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.invalidate_relationship(?,?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, relationship.getId());
            call.setTimestamp(2, relationship.getValidityUntil());
            call.execute();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
    }

    @Override
    public List<Relationship> getRelationshipsToCSTConcept(ConceptSCT destinyConceptSCT) {

        long idDestinyConceptSCT = destinyConceptSCT.getId();

        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.get_relationships_to_concept_sct(?)}";
        String resultJSON;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, idDestinyConceptSCT);
            call.execute();

            ResultSet rs = call.getResultSet();

            if (rs.next()) {
                resultJSON = rs.getString(1);
            } else {
                String errorMsg = "La relacion no fue creada. Esta es una situación imposible. Contactar a Desarrollo";
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
            rs.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return relationshipFactory.createRelationshipsFromJSON(resultJSON);
    }

    @Override
    public Relationship getRelationshipByID(long idRelationship) {

        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.get_relationships_by_id(?)}";
        String resultJSON;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, idRelationship);
            call.execute();

            ResultSet rs = call.getResultSet();

            if (rs.next()) {
                resultJSON = rs.getString(1);
            } else {
                String errorMsg = "La relacion no fue creada. Esta es una situación imposible. Contactar a Desarrollo";
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
            rs.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return relationshipFactory.createFromJSON(resultJSON);
    }

    @Override
    public List<Relationship> getRelationshipsLike(RelationshipDefinition relationshipDefinition, Target target) {

        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.get_snomedct_relationship(?, ?)}";
        String resultJSON;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, relationshipDefinition.getId());
            call.setLong(2, target.getId());
            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                resultJSON = rs.getString(1);
            } else {
                String errorMsg = "La relación no fue creada. Esta es una situación imposible. Contactar a Desarrollo";
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
            rs.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return relationshipFactory.createRelationshipsFromJSON(resultJSON);
    }

    @Override
    public List<Relationship> getRelationshipsByRelationshipDefinition(RelationshipDefinition relationshipDefinition) {

        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.get_relationships_by_definition(?)}";
        String resultJSON;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, relationshipDefinition.getId());
            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                resultJSON = rs.getString(1);
                if (resultJSON == null) {
                    return Collections.emptyList();
                }
            } else {
                String errorMsg = "La relación no fue creada. Esta es una situación imposible. Contactar a Desarrollo";
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
            rs.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return relationshipFactory.createRelationshipsFromJSON(resultJSON);
    }

    @Override
    public List<Relationship> getRelationshipsBySourceConcept(long idConcept) {

        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.get_relationships_by_source_concept_id(?)}";
        String resultJSON;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, idConcept);
            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                resultJSON = rs.getString(1);
            } else {
                String errorMsg = "No se obtuvo respuesta desde la base de datos.";
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
            rs.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return relationshipFactory.createRelationshipsFromJSON(resultJSON);
    }

    @Override
    public Long getTargetByRelationship(Relationship relationship) {

        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.get_id_target_by_id_relationship(?)}";
        Long result;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, relationship.getId());
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

