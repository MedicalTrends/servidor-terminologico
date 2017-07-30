package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.daos.mappers.RelationshipMapper;
import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.*;

import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import java.sql.*;
import java.util.*;

import static java.sql.Types.VARCHAR;

/**
 * @author Diego Soto / Gustavo Punucura
 */
@Stateless
public class RelationshipDAOImpl implements RelationshipDAO {

    /** El logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(RelationshipDAOImpl.class);

    @EJB
    private RelationshipMapper relationshipMapper;

    @EJB
    private TargetDAO targetDAO;

    @Override
    public Relationship persist(Relationship relationship) {

        long idTarget= targetDAO.persist(relationship.getTarget(),relationship.getRelationshipDefinition().getTargetDefinition());

        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship.create_relationship(?,?,?,?,?); end;";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.NUMERIC);

            if(relationship.getIdRelationship()!=null) {
                call.setString(2,relationship.getIdRelationship());
            }else {
               call.setNull(2,VARCHAR);
            }
            call.setLong(3, relationship.getSourceConcept().getId());
            call.setLong(4, idTarget);
            call.setLong(5, relationship.getRelationshipDefinition().getId());
            call.setTimestamp(6, relationship.getCreationDate());
            call.execute();

            //ResultSet rs = (ResultSet) call.getObject(1);

            if (call.getLong(1) > 0) {
                relationship.setId(call.getLong(1));
            } else {
                String errorMsg = "La relacion no fue creada. Esta es una situación imposible. Contactar a Desarrollo";
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
            //rs.close();
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

        String sql = "begin ? := stk.stk_pck_relationship.create_relationship_definition(?,?,?,?,?); end;";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.NUMERIC);
            call.setString(2, relationshipDefinition.getName());
            call.setString(3, relationshipDefinition.getDescription());
            call.setInt(4, relationshipDefinition.getMultiplicity().getLowerBoundary());
            call.setInt(5, relationshipDefinition.getMultiplicity().getUpperBoundary());
            call.setLong(6, idTargetDefinition);
            call.execute();

        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return relationshipDefinition;
    }

    @Override
    public void delete(Relationship relationship) {

        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship.invalidate_relationship(?); end;";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.NUMERIC);
            call.setLong(2, relationship.getId());
            call.execute();

        } catch (SQLException e) {
            throw new EJBException(e);
        }
    }

    @Override
    public void update(Relationship relationship) {
        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship.update_relation(?,?,?,?,?); end;";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.NUMERIC);
            call.setLong(2, relationship.getId());
            call.setLong(3, relationship.getSourceConcept().getId());
            call.setLong(4, getTargetByRelationship(relationship));
            call.setLong(5, relationship.getRelationshipDefinition().getId());
            call.setTimestamp(6, relationship.getValidityUntil());
            call.execute();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
    }

    @Override
    public void invalidate(Relationship relationship) {
        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship.invalidate_relationship(?,?); end;";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.NUMERIC);
            call.setLong(2, relationship.getId());
            call.setTimestamp(3, relationship.getValidityUntil());
            call.execute();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
    }

    @Override
    public Relationship getRelationshipByID(long idRelationship) {

        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship.get_relationships_by_id(?); end;";

        Relationship relationship = null;

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, idRelationship);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                relationship = relationshipMapper.createRelationshipFromResultSet(rs, null);
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
    public List<Relationship> getRelationshipsLike(RelationshipDefinition relationshipDefinition, Target target) {

        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship.get_snomedct_relationship(?,?); end;";

        List<Relationship> relationships = new ArrayList<>();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, relationshipDefinition.getId());
            call.setLong(3, target.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                relationships.add(relationshipMapper.createRelationshipFromResultSet(rs, null));
            }

            rs.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return relationships;
    }

    @Override
    public List<Relationship> findRelationshipsLike(RelationshipDefinition relationshipDefinition, Target target) {

        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship.find_relationships_like(?,?); end;";

        List<Relationship> relationships = new ArrayList<>();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);

            if(relationshipDefinition.getTargetDefinition().isSMTKType()){
                call.setLong(2, relationshipDefinition.getTargetDefinition().getId());
                call.setString(3, String.valueOf(target.getId()));
            }

            if(relationshipDefinition.getTargetDefinition().isHelperTable()){
                call.setLong(2, relationshipDefinition.getTargetDefinition().getId());
                HelperTableRow helperTableRow = (HelperTableRow) target;
                call.setString(3, String.valueOf(helperTableRow.getId()));
            }

            if(relationshipDefinition.getTargetDefinition().isBasicType()){
                call.setLong(2, relationshipDefinition.getId());
                BasicTypeValue basicTypeValue = (BasicTypeValue) target;
                call.setString(3, basicTypeValue.toString());
            }

            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                relationships.add(relationshipMapper.createRelationshipFromResultSet(rs, null));
            }

            rs.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return relationships;
    }

    @Override
    public List<Relationship> getRelationshipsBySourceConcept(ConceptSMTK conceptSMTK) {

        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship.get_relationships_by_source_concept_id(?); end;";

        List<Relationship> relationships = new ArrayList<>();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, conceptSMTK.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                relationships.add(relationshipMapper.createRelationshipFromResultSet(rs, conceptSMTK));
            }

            rs.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return relationships;
    }

    @Override
    public Long getTargetByRelationship(Relationship relationship) {

        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship.get_id_target_by_id_relationship(?); end;";

        Long result;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, relationship.getId());
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

