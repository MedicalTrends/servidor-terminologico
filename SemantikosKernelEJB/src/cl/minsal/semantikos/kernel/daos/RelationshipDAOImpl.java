package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.factories.CacheFactory;
import cl.minsal.semantikos.kernel.factories.DataSourceFactory;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;
import cl.minsal.semantikos.model.crossmaps.DirectCrossmap;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.*;

import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.concurrent.Future;

import static java.sql.Types.VARCHAR;

/**
 * @author Diego Soto / Gustavo Punucura
 */
@Stateless
@org.jboss.ejb3.annotation.Pool(value="heavy-load-pool")
public class RelationshipDAOImpl implements RelationshipDAO {

    /** El logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(RelationshipDAOImpl.class);

    @EJB
    private TargetDAO targetDAO;

    @EJB
    private ConceptDAO conceptDAO;

    @EJB
    private RelationshipAttributeDAO relationshipAttributeDAO;

    @EJB
    private BasicTypeDAO basicTypeDAO;

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    @Override
    public Relationship persist(Relationship relationship) {

        long idTarget= targetDAO.persist(relationship.getTarget(),relationship.getRelationshipDefinition().getTargetDefinition());

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship.create_relationship(?,?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
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

        //ConnectionBD connect = new ConnectionBD();
        /*
         * param 1: nombre
         * param 2: descripción
         * param 3: lower boundary
         * param 4: upper boundary
         * param 5: idTargetDefinition
         */

        String sql = "begin ? := stk.stk_pck_relationship.create_relationship_definition(?,?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
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

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship.invalidate_relationship(?); end;";

        try (Connection connection = dataSource.getConnection();
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
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship.update_relation(?,?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
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
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship.invalidate_relationship(?,?); end;";

        try (Connection connection = dataSource.getConnection();
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

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship.get_relationships_by_id(?); end;";

        Relationship relationship = null;

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, idRelationship);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                relationship = createRelationshipFromResultSet(rs, null);
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

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship.get_snomedct_relationship(?,?); end;";

        List<Relationship> relationships = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, relationshipDefinition.getId());
            call.setLong(3, target.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                relationships.add(createRelationshipFromResultSet(rs, null));
            }

            rs.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return relationships;
    }

    @Override
    public List<Relationship> findRelationshipsLike(RelationshipDefinition relationshipDefinition, Target target) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship.find_relationships_like(?,?,?); end;";

        List<Relationship> relationships = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);

            call.setLong(2, relationshipDefinition.getId());

            if(relationshipDefinition.getTargetDefinition().isSMTKType()){
                call.setLong(3, relationshipDefinition.getTargetDefinition().getId());
                call.setString(4, String.valueOf(target.getId()));
            }

            if(relationshipDefinition.getTargetDefinition().isHelperTable()){
                call.setLong(3, relationshipDefinition.getTargetDefinition().getId());
                HelperTableRow helperTableRow = (HelperTableRow) target;
                call.setString(4, String.valueOf(helperTableRow.getId()));
            }

            if(relationshipDefinition.getTargetDefinition().isBasicType()){
                call.setLong(3, relationshipDefinition.getId());
                BasicTypeValue basicTypeValue = (BasicTypeValue) target;
                call.setString(4, basicTypeValue.toString());
            }

            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                relationships.add(createRelationshipFromResultSet(rs, null));
            }

            rs.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return relationships;
    }

    @Override
    public List<Relationship> getRelationshipsBySourceConcept(ConceptSMTK conceptSMTK) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship.get_relationships_by_source_concept_id(?); end;";

        List<Relationship> relationships = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, conceptSMTK.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                relationships.add(createRelationshipFromResultSet(rs, conceptSMTK));
            }

            rs.close();
            call.close();
            connection.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return relationships;
    }

    @Override
    @Asynchronous
    public Future<List<Relationship>> getRelationshipsBySourceConceptAsync(ConceptSMTK conceptSMTK) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship.get_relationships_by_source_concept_id(?); end;";

        List<Relationship> relationships = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, conceptSMTK.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                relationships.add(createRelationshipFromResultSet(rs, conceptSMTK));
            }

            rs.close();
            call.close();
            connection.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return new AsyncResult<>(relationships);
    }

    @Override
    public List<Relationship> getRelationshipsBySourceConcept(ConceptSMTK conceptSMTK, TargetType targetType) {

        //ConnectionBD connect = new ConnectionBD();
        /*
        if(CacheFactory.getInstance().getCache().containsKey(conceptSMTK.getId())) {
            return CacheFactory.getInstance().getCache().get(conceptSMTK.getId()).getRelationships();
        }
        */

        String sql = "begin ? := stk.stk_pck_relationship.get_relationships_by_source_concept_and_target_type(?,?); end;";

        List<Relationship> relationships = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, conceptSMTK.getId());
            call.setLong(3, targetType.getIdTargetType());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                relationships.add(createRelationshipFromResultSet(rs, conceptSMTK));
            }

            rs.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        //CacheFactory.getInstance().getCache().put(conceptSMTK.getId(), conceptSMTK);;

        return relationships;
    }

    @Override
    public Long getTargetByRelationship(Relationship relationship) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_relationship.get_id_target_by_id_relationship(?); end;";

        Long result;
        try (Connection connection = dataSource.getConnection();
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

    public Relationship createRelationshipFromResultSet(ResultSet rs, ConceptSMTK conceptSMTK) {

        try {

            long id = rs.getLong("id");
            long idConcept = rs.getLong("id_source_concept");
            long idRelationshipDefinition = rs.getLong("id_relationship_definition");
            Timestamp validityUntil = rs.getTimestamp("validity_until");
            long idTarget = rs.getLong("id_target");
            Timestamp creationDate = rs.getTimestamp("creation_date");

            if(conceptSMTK == null) {
                conceptSMTK = conceptDAO.getConceptByID(idConcept);
            }

            /* Definición de la relación y sus atributos */
            RelationshipDefinition relationshipDefinition = conceptSMTK.getCategory().findRelationshipDefinitionsById(idRelationshipDefinition).get(0);

            /* El target que puede ser básico, smtk, tablas, crossmaps o snomed-ct */
            Relationship relationship = createRelationshipByTargetType(idTarget, conceptSMTK, relationshipDefinition, id, validityUntil);
            relationship.setValidityUntil(validityUntil);
            relationship.setCreationDate(creationDate);

            List<RelationshipAttribute> relationshipAttributes = relationshipAttributeDAO.getRelationshipAttribute(relationship);
            relationship.setRelationshipAttributes(relationshipAttributes);

            return relationship;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Este método es reponsable de crear una instancia del tipo correcto de relación en función del target de un tipo
     * en particular.
     *
     * @param relationshipDefinition La relación que lo define.
     *
     * @return Una relación del tipo correcta que define el Target.
     */
    private Relationship createRelationshipByTargetType(long idTarget, ConceptSMTK conceptSMTK, RelationshipDefinition relationshipDefinition, long id, Timestamp validityUntil) {

        Target target;

        /* El target puede ser Tipo Básico */
        if (relationshipDefinition.getTargetDefinition().isBasicType()) {
            BasicTypeValue basicTypeValueByID = basicTypeDAO.getBasicTypeValueByID(idTarget);
            return new Relationship(id, conceptSMTK, basicTypeValueByID, relationshipDefinition, validityUntil, new ArrayList<RelationshipAttribute>());
        }

        /* El target puede ser a un registro de una tabla auxiliar */
        if (relationshipDefinition.getTargetDefinition().isHelperTable()) {
            //target = helperTableManager.getRecord(idTarget);
            target = targetDAO.getTargetByID(idTarget);
            //target = new HelperTableRow();
            /**
             * Se setea el id desde el fields para ser utilizado por el custom converter
             */
            HelperTableRow helperTableRow = (HelperTableRow) target;

            return new Relationship(id, conceptSMTK, helperTableRow, relationshipDefinition, validityUntil, new ArrayList<RelationshipAttribute>());
        }

        /* El target puede ser un concepto SMTK */
        if (relationshipDefinition.getTargetDefinition().isSMTKType()) {

            ConceptSMTK conceptByID = (ConceptSMTK) targetDAO.getTargetByID(idTarget);
            return new Relationship(id, conceptSMTK, conceptByID, relationshipDefinition, validityUntil, new ArrayList<RelationshipAttribute>());
        }

        /* El target puede ser un concepto Snomed CT */
        if (relationshipDefinition.getTargetDefinition().isSnomedCTType()) {
            ConceptSCT conceptCSTByID = (ConceptSCT) targetDAO.getTargetByID(idTarget);
            return new SnomedCTRelationship(id, conceptSMTK, conceptCSTByID, relationshipDefinition, new ArrayList<RelationshipAttribute>(), validityUntil);
        }

        /* Y sino, puede ser crossmap */
        if (relationshipDefinition.getTargetDefinition().isCrossMapType()) {
            target = targetDAO.getTargetByID(idTarget);
            //CrossmapSetMember crossmapSetMemberById = crossmapDAO.getCrossmapSetMemberById(idTarget);
            return new DirectCrossmap(id, conceptSMTK, (CrossmapSetMember)target, relationshipDefinition, validityUntil);
        }

        /* Sino, hay un nuevo tipo de target que no está siendo gestionado */
        String msg = "Un tipo no manejado de Target se ha recibido.";
        logger.error(msg);
        throw new EJBException(msg);
    }
}

