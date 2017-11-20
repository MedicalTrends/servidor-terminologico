package cl.minsal.semantikos.kernel.daos.ws;

import cl.minsal.semantikos.kernel.daos.*;
import cl.minsal.semantikos.kernel.util.StringUtils;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.basictypes.BasicTypeDefinition;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.crossmaps.CrossmapSet;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;
import cl.minsal.semantikos.model.crossmaps.DirectCrossmap;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.DescriptionType;
import cl.minsal.semantikos.model.descriptions.DescriptionTypeFactory;
import cl.minsal.semantikos.model.dtos.*;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.*;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.model.users.UserFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import static java.sql.Types.VARCHAR;

/**
 * @author Diego Soto / Gustavo Punucura
 */
@Stateless
public class RelationshipWSDAOImpl implements RelationshipWSDAO {

    /** El logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(RelationshipWSDAOImpl.class);

    @EJB
    private HelperTableWSDAO helperTableDAO;

    @EJB
    private ConceptWSDAO conceptDAO;

    @EJB
    private RelationshipAttributeDAO relationshipAttributeDAO;

    @EJB
    private BasicTypeWSDAO basicTypeDAO;

    @EJB
    private ConceptSCTWSDAO conceptSCTDAO;

    @EJB
    private CrossmapsWSDAO crossmapDAO;

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<Relationship> getRelationshipsBySourceConcept(ConceptSMTK conceptSMTK) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_ws.get_relationships_by_source_concept_json(?); end;";

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

        } catch (SQLException e) {
            throw new EJBException(e);
        } catch (IOException e) {
            throw new EJBException(e);
        }

        return relationships;
    }

    @Override
    @Asynchronous
    public Future<List<Relationship>> getRelationshipsBySourceConceptAsync(ConceptSMTK conceptSMTK) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_ws.get_relationships_by_source_concept_json(?); end;";

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
        } catch (IOException e) {
            throw new EJBException(e);
        }

        return new AsyncResult<>(relationships);
    }

    @Override
    public List<Relationship> findRelationshipsLike(RelationshipDefinition relationshipDefinition, Target target) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_ws.find_relationships_like_json(?,?,?); end;";

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
                if(basicTypeValue.isBoolean()) {
                    call.setString(4, (Boolean)basicTypeValue.getValue()?"1":"0");
                }
                else {
                    call.setString(4, String.valueOf(basicTypeValue.getValue()));
                }
            }

            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                relationships.add(createRelationshipFromResultSet(rs, null));
            }

            rs.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        } catch (IOException e) {
            throw new EJBException(e);
        }

        return relationships;
    }


    public Relationship createRelationshipFromResultSet(ResultSet rs, ConceptSMTK conceptSMTK) throws IOException {

        try {

            String jsonObject = rs.getString("json_object");

            RelationshipDTO relationshipDTO = mapper.readValue(StringUtils.cleanJSON(jsonObject), RelationshipDTO.class);

            long id = relationshipDTO.getId();
            long idConcept = relationshipDTO.getIdSourceConcept();
            long idRelationshipDefinition = relationshipDTO.getIdRelationshipDefinition();
            Timestamp validityUntil = relationshipDTO.getValidityUntil();
            TargetDTO targetDTO = relationshipDTO.getTargetDTO();
            Timestamp creationDate = relationshipDTO.getCreationDate();

            if(conceptSMTK == null) {
                conceptSMTK = conceptDAO.getConceptByID(idConcept);
            }

            /* Definición de la relación y sus atributos */
            RelationshipDefinition relationshipDefinition = conceptSMTK.getCategory().findRelationshipDefinitionsById(idRelationshipDefinition).get(0);

            /* El target que puede ser básico, smtk, tablas, crossmaps o snomed-ct */
            Relationship relationship = createRelationshipByTargetType(targetDTO, conceptSMTK, relationshipDefinition, id, validityUntil);
            relationship.setValidityUntil(validityUntil);
            relationship.setCreationDate(creationDate);

            List<RelationshipAttributeDTO> relationshipAttributesDTO = relationshipDTO.getRelationshipAttributeDTOs();
            relationship.setRelationshipAttributes(createRelationshipAttributesFromDTO(relationshipAttributesDTO, relationship));

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
    private Relationship createRelationshipByTargetType(TargetDTO targetDTO, ConceptSMTK conceptSMTK, RelationshipDefinition relationshipDefinition, long id, Timestamp validityUntil) throws SQLException {

        Target target = null;

        /* El target puede ser Tipo Básico */
        if (relationshipDefinition.getTargetDefinition().isBasicType()) {
            target = basicTypeDAO.createBasicTypeFromDTO((BasicTypeValueDTO) targetDTO, (BasicTypeDefinition)relationshipDefinition.getTargetDefinition());
        }

        /* El target puede ser a un registro de una tabla auxiliar */
        if (relationshipDefinition.getTargetDefinition().isHelperTable()) {
            target = helperTableDAO.createHelperTableRowFromDTO((HelperTableRowDTO) targetDTO);
        }

        /* El target puede ser un concepto SMTK */
        if (relationshipDefinition.getTargetDefinition().isSMTKType()) {
            target = conceptDAO.createConceptFromDTO((ConceptDTO) targetDTO);
        }

        /* El target puede ser un concepto Snomed CT */
        if (relationshipDefinition.getTargetDefinition().isSnomedCTType()) {
            target =  conceptSCTDAO.createConceptSCTFromDTO((ConceptSCTDTO) targetDTO);
        }

        /* Y sino, puede ser crossmap */
        if (relationshipDefinition.getTargetDefinition().isCrossMapType()) {
            target = crossmapDAO.createCrossmapSetMemberFromDTO((CrossmapSetMemberDTO) targetDTO);
        }

        if(target == null) {
            /* Sino, hay un nuevo tipo de target que no está siendo gestionado */
            String msg = "Un tipo no manejado de Target se ha recibido.";
            logger.error(msg);
            throw new EJBException(msg);
        }

        return new Relationship(id, conceptSMTK, target, relationshipDefinition, validityUntil, new ArrayList<RelationshipAttribute>());
    }

    public List<RelationshipAttribute> createRelationshipAttributesFromDTO(List<RelationshipAttributeDTO> attributesDTO, Relationship relationship) throws SQLException {

        List<RelationshipAttribute> relationshipAttributes = new ArrayList<>();

        if(attributesDTO != null) {

            for (RelationshipAttributeDTO relationshipAttributeDTO : attributesDTO) {

                long id = relationshipAttributeDTO.getId();

                TargetDTO targetDTO = relationshipAttributeDTO.getTargetDTO();

                long relationshipAttributeDefinitionId = relationshipAttributeDTO.getRelationAttributeDefinitionId();

                RelationshipAttributeDefinition relationshipAttributeDefinition = relationship.getRelationshipDefinition().findRelationshipAttributeDefinitionsById(relationshipAttributeDefinitionId).get(0);

                RelationshipAttribute relationshipAttribute = createRelationshipAttributeByTargetType(targetDTO, relationship, relationshipAttributeDefinition, id);

                relationshipAttributes.add(relationshipAttribute);

            }

        }

        return relationshipAttributes;
    }

    /**
     * Este método es reponsable de crear una instancia del tipo correcto de relación en función del target de un tipo
     * en particular.
     *
     * @param relationshipAttributeDefinition La relación que lo define.
     *
     * @return Una relación del tipo correcta que define el Target.
     */
    private RelationshipAttribute createRelationshipAttributeByTargetType(TargetDTO targetDTO, Relationship relationship, RelationshipAttributeDefinition relationshipAttributeDefinition, long id) throws SQLException {

        Target target = null;

        /* El target puede ser Tipo Básico */
        if (relationshipAttributeDefinition.getTargetDefinition().isBasicType()) {
            target = basicTypeDAO.createBasicTypeFromDTO((BasicTypeValueDTO) targetDTO, (BasicTypeDefinition)relationshipAttributeDefinition.getTargetDefinition());
        }

        /* El target puede ser a un registro de una tabla auxiliar */
        if (relationshipAttributeDefinition.getTargetDefinition().isHelperTable()) {
            target = helperTableDAO.createHelperTableRowFromDTO((HelperTableRowDTO) targetDTO);
        }

        /* El target puede ser un concepto SMTK */
        if (relationshipAttributeDefinition.getTargetDefinition().isSMTKType()) {
            target = conceptDAO.createConceptFromDTO((ConceptDTO) targetDTO);
        }

        /* El target puede ser un concepto Snomed CT */
        if (relationshipAttributeDefinition.getTargetDefinition().isSnomedCTType()) {
            target =  conceptSCTDAO.createConceptSCTFromDTO((ConceptSCTDTO) targetDTO);
        }

        /* Y sino, puede ser crossmap */
        if (relationshipAttributeDefinition.getTargetDefinition().isCrossMapType()) {
            target = crossmapDAO.createCrossmapSetMemberFromDTO((CrossmapSetMemberDTO) targetDTO);
        }

        if(target == null) {
            /* Sino, hay un nuevo tipo de target que no está siendo gestionado */
            String msg = "Un tipo no manejado de Target se ha recibido.";
            logger.error(msg);
            throw new EJBException(msg);
        }

        return new RelationshipAttribute(id, relationshipAttributeDefinition, relationship, target);
    }
}

