package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.Description;
import cl.minsal.semantikos.model.snomedct.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Funciones de base de dato para acceder a los datos de Snomed.
 *
 * @author Andrés Farías on 10/25/16.
 */
@Stateless
public class SnomedCTDAOImpl implements SnomedCTDAO {

    private static final Logger logger = LoggerFactory.getLogger(SnomedCTDAOImpl.class);

    @Override
    public List<ConceptSCT> findConceptsBy(String pattern, Integer group) {
        List<ConceptSCT> concepts = new ArrayList<>();

        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.find_sct_by_pattern(?,?)}")) {

            call.setString(1, pattern);
            if (group == null) {
                call.setNull(2, Types.INTEGER);
            } else {
                call.setInt(2, group);
            }
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                ConceptSCT recoveredConcept = createConceptSCTFromResultSet(rs);
                concepts.add(recoveredConcept);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al buscar Snomed CT";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

        return concepts;
    }

    @Override
    public List<ConceptSCT> findPerfectMatch(String pattern, Integer group) {
        List<ConceptSCT> concepts = new ArrayList<>();

        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.find_sct_perfect_match(?,?)}")) {

            call.setString(1, pattern);
            if (group == null) {
                call.setNull(2, Types.INTEGER);
            } else {
                call.setInt(2, group);
            }
            call.execute();

            ResultSet rs = call.getResultSet();

            while (rs.next()) {
                ConceptSCT recoveredConcept = createConceptSCTFromResultSet(rs);
                concepts.add(recoveredConcept);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al buscar Snomed CT";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

        return concepts;
    }

    @Override
    public List<ConceptSCT> findTruncateMatch(String pattern, Integer group) {
        List<ConceptSCT> concepts = new ArrayList<>();

        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.find_sct_truncate_match(?,?)}")) {

            call.setString(1, pattern);
            if (group == null) {
                call.setNull(2, Types.INTEGER);
            } else {
                call.setInt(2, group);
            }
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                ConceptSCT recoveredConcept = createConceptSCTFromResultSet(rs);
                concepts.add(recoveredConcept);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al buscar Snomed CT";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

        return concepts;
    }

    @Override
    public long countPerfectMatch(String pattern, Integer group) {
        long concepts = 0;

        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.count_sct_perfect_match(?,?)}")) {

            call.setString(1, pattern);
            if (group == null) {
                call.setNull(2, Types.INTEGER);
            } else {
                call.setInt(2, group);
            }
            call.execute();

            ResultSet rs = call.getResultSet();

            while (rs.next()) {
                concepts = rs.getLong(1);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al buscar Snomed CT";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

        return concepts;
    }

    @Override
    public long countTruncateMatch(String pattern, Integer group) {
        long concepts = 0;

        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.count_sct_truncate_match(?,?)}")) {

            call.setString(1, pattern);
            if (group == null) {
                call.setNull(2, Types.INTEGER);
            } else {
                call.setInt(2, group);
            }
            call.execute();

            ResultSet rs = call.getResultSet();

            while (rs.next()) {
                concepts = rs.getLong(1);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al buscar Snomed CT";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

        return concepts;
    }

    @Override
    public void persistSnapshotConceptSCT(List<ConceptSCT> conceptSCTs) {

        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.create_concept_sct(?,?,?,?,?)}")) {
            int i=0;
            for (ConceptSCT conceptSCT : conceptSCTs) {
                call.setLong(1,conceptSCT.getIdSnomedCT());
                call.setTimestamp(2,conceptSCT.getEffectiveTime());
                call.setBoolean(3,conceptSCT.isActive());
                call.setLong(4,conceptSCT.getModuleId());
                call.setLong(5,conceptSCT.getDefinitionStatusId());
                call.addBatch();
                i++;
            }

            call.executeBatch();

        } catch (SQLException e) {
            String errorMsg = "Error al persistir Concept Snomed CT";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }
    }

    @Override
    public void persistSnapshotDescriptionSCT(List<DescriptionSCT> descriptionSCTs) {
        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.create_description_sct(?,?,?,?,?,?,?,?,?)}")) {

            for (DescriptionSCT descriptionSCT : descriptionSCTs) {
                call.setLong(1,descriptionSCT.getId());
                call.setTimestamp(2,descriptionSCT.getEffectiveTime());
                call.setBoolean(3,descriptionSCT.isActive());
                call.setLong(4,descriptionSCT.getModuleId());
                call.setLong(5,descriptionSCT.getConceptId());
                call.setString(6,descriptionSCT.getLanguageCode());
                call.setLong(7,descriptionSCT.getDescriptionType().getTypeId());
                call.setString(8,descriptionSCT.getTerm());
                call.setLong(9,descriptionSCT.getCaseSignificanceId());
                call.addBatch();
            }

            call.executeBatch();


        } catch (SQLException e) {
            String errorMsg = "Error al persistir Description Snomed CT";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }
    }

    @Override
    public void persistSnapshotRelationshipSCT(List<RelationshipSnapshotSCT> relationshipSnapshotSCTs) {
        long idRelationshipSCT = 0;

        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.create_relationship_sct(?,?,?,?,?,?,?,?,?,?)}")) {

            for (RelationshipSnapshotSCT relationshipSnapshotSCT : relationshipSnapshotSCTs) {
                call.setLong(1,relationshipSnapshotSCT.getId());
                call.setTimestamp(2,relationshipSnapshotSCT.getEffectiveTime());
                call.setBoolean(3,relationshipSnapshotSCT.isActive());
                call.setLong(4,relationshipSnapshotSCT.getModuleId());
                call.setLong(5,relationshipSnapshotSCT.getSourceId());
                call.setLong(6,relationshipSnapshotSCT.getDestinationId());
                call.setLong(7,relationshipSnapshotSCT.getRelationshipGroup());
                call.setLong(8,relationshipSnapshotSCT.getTypeId());
                call.setLong(9,relationshipSnapshotSCT.getCharacteristicTypeId());
                call.setLong(10,relationshipSnapshotSCT.getModifierId());
                call.addBatch();

            }

            call.executeBatch();

        } catch (SQLException e) {
            String errorMsg = "Error al persistir Relationship Snomed CT";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }
    }

    @Override
    public void persistSnapshotTransitiveSCT(TransitiveSCT transitiveSCT) {
        long idTransitiveSCT = 0;

        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.create_transitive_sct(?,?)}")) {

            call.setLong(1,transitiveSCT.getIdPartent());
            call.setLong(2,transitiveSCT.getIdChild());
            call.execute();

            ResultSet rs = call.getResultSet();

            while (rs.next()) {
                idTransitiveSCT = rs.getLong(1);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al persistir Transitivo Snomed CT";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }
    }

    @Override
    public void persistSnapshotLanguageRefSetSCT(LanguageRefsetSCT languageRefsetSCT) {
        long idTransitiveSCT = 0;

        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.create_language_ref_set_sct(?,?,?,?,?,?,?)}")) {

            call.setString(1,languageRefsetSCT.getId());
            call.setTimestamp(2,languageRefsetSCT.getEffectiveTime());
            call.setBoolean(3,languageRefsetSCT.isActive());
            call.setLong(4,languageRefsetSCT.getModuleId());
            call.setLong(5,languageRefsetSCT.getRefsetId());
            call.setLong(6,languageRefsetSCT.getReferencedComponentId());
            call.setLong(7,languageRefsetSCT.getAcceptabilityId());
            call.execute();


        } catch (SQLException e) {
            String errorMsg = "Error al persistir Lenguaje RefSet Snomed CT";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }
    }

    @Override
    public void updateSnapshotConceptSCT(ConceptSCT conceptSCT) {
        long idConceptSCT = 0;

        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.update_concept_sct(?,?,?,?,?)}")) {

            call.setLong(1,conceptSCT.getIdSnomedCT());
            call.setTimestamp(2,conceptSCT.getEffectiveTime());
            call.setBoolean(3,conceptSCT.isActive());
            call.setLong(4,conceptSCT.getModuleId());
            call.setLong(5,conceptSCT.getDefinitionStatusId());
            call.execute();

            ResultSet rs = call.getResultSet();

            while (rs.next()) {
                idConceptSCT = rs.getLong(1);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al actualizar Concept Snomed CT";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }
    }

    @Override
    public void updateSnapshotDescriptionSCT(DescriptionSCT descriptionSCT) {
        long idDescriptionSCT = 0;

        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.update_description_sct(?,?,?,?,?,?,?,?,?)}")) {

            call.setLong(1,descriptionSCT.getId());
            call.setTimestamp(2,descriptionSCT.getEffectiveTime());
            call.setBoolean(3,descriptionSCT.isActive());
            call.setLong(4,descriptionSCT.getModuleId());
            call.setLong(5,descriptionSCT.getConceptId());
            call.setString(6,descriptionSCT.getLanguageCode());
            call.setLong(7,descriptionSCT.getDescriptionType().getTypeId());
            call.setString(8,descriptionSCT.getTerm());
            call.setLong(9,descriptionSCT.getCaseSignificanceId());
            call.execute();


        } catch (SQLException e) {
            String errorMsg = "Error al actualizar Description Snomed CT";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }
    }

    @Override
    public void updateSnapshotRelationshipSCT(RelationshipSnapshotSCT relationshipSnapshotSCT) {
        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.update_relationship_sct(?,?,?,?,?,?,?,?,?,?)}")) {

            call.setLong(1,relationshipSnapshotSCT.getId());
            call.setTimestamp(2,relationshipSnapshotSCT.getEffectiveTime());
            call.setBoolean(3,relationshipSnapshotSCT.isActive());
            call.setLong(4,relationshipSnapshotSCT.getModuleId());
            call.setLong(5,relationshipSnapshotSCT.getSourceId());
            call.setLong(6,relationshipSnapshotSCT.getDestinationId());
            call.setLong(7,relationshipSnapshotSCT.getRelationshipGroup());
            call.setLong(8,relationshipSnapshotSCT.getTypeId());
            call.setLong(9,relationshipSnapshotSCT.getCharacteristicTypeId());
            call.setLong(10,relationshipSnapshotSCT.getModifierId());
            call.execute();

        } catch (SQLException e) {
            String errorMsg = "Error al actualizar Relationship Snomed CT";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }
    }



    @Override
    public void updateSnapshotLanguageRefSetSCT(LanguageRefsetSCT languageRefsetSCT) {

        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.update_language_ref_set_sct(?,?,?,?,?,?,?)}")) {

            call.setString(1,languageRefsetSCT.getId());
            call.setTimestamp(2,languageRefsetSCT.getEffectiveTime());
            call.setBoolean(3,languageRefsetSCT.isActive());
            call.setLong(4,languageRefsetSCT.getModuleId());
            call.setLong(5,languageRefsetSCT.getRefsetId());
            call.setLong(6,languageRefsetSCT.getReferencedComponentId());
            call.setLong(7,languageRefsetSCT.getAcceptabilityId());
            call.execute();


        } catch (SQLException e) {
            String errorMsg = "Error al actualizar lenguaje RefSet Snomed CT";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }
    }

    @Override
    public void deleteSnapshotTransitiveSCT(TransitiveSCT transitiveSCT) {
        long idTransitiveSCT = 0;

        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.create_transitive_sct(?,?)}")) {

            call.setLong(1,transitiveSCT.getIdPartent());
            call.setLong(2,transitiveSCT.getIdChild());
            call.execute();

            ResultSet rs = call.getResultSet();

            while (rs.next()) {
                idTransitiveSCT = rs.getLong(1);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al eliminar transitivo Snomed CT";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }
    }

    @Override
    public boolean existConceptSCT(ConceptSCT conceptSCT) {
        long idConceptSCT = 0;

        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.get_concepts_sct_by_id(?)}")) {

            call.setLong(1,conceptSCT.getIdSnomedCT());
            call.execute();

            ResultSet rs = call.getResultSet();

            while (rs.next()) {
                idConceptSCT = rs.getLong(1);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al buscar Concept Snomed CT";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }
        if(idConceptSCT!=0){
            return true;
        }

        return false;
    }

    @Override
    public boolean existDescriptionSCT(DescriptionSCT descriptionSCT) {
        long idDescriptionSCT = 0;

        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.get_descriptions_sct_by_id(?)}")) {

            call.setLong(1,descriptionSCT.getId());
            call.execute();

            ResultSet rs = call.getResultSet();

            while (rs.next()) {
                idDescriptionSCT = rs.getLong(1);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al buscar Concept Snomed CT";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }
        if(idDescriptionSCT!=0){
            return true;
        }

        return false;
    }

    @Override
    public DescriptionSCT getDescriptionSCTBy(long idDescriptionSCT) {
        ConnectionBD connect = new ConnectionBD();
        DescriptionSCT descriptionSCT = null;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.get_description_sct_by_id(?)}")) {

            call.setLong(1,idDescriptionSCT);
            call.execute();

            ResultSet rs = call.getResultSet();

            while (rs.next()) {
                descriptionSCT= createDescriptionSCTFromResultSet(rs);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al buscar Concept Snomed CT";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }
        return descriptionSCT;
    }

    @Override
    public ConceptSCT getConceptByID(long conceptID) {

        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.get_sct_by_concept_id(?)}")) {

            call.setLong(1, conceptID);
            call.execute();

            ResultSet rs = call.getResultSet();
            ConceptSCT conceptSCTFromResultSet;
            if (rs.next()) {
                conceptSCTFromResultSet = createConceptSCTFromResultSet(rs);
            } else {
                throw new EJBException("No se encontró un concepto con ID=" + conceptID);
            }
            rs.close();

            return conceptSCTFromResultSet;
        } catch (SQLException e) {
            String errorMsg = "Error al buscar Snomed CT por CONCEPT_ID: " + conceptID;
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }
    }

    @Override
    public List<ConceptSCT> findConceptsByConceptID(long conceptIdPattern, Integer group) {

        List<ConceptSCT> conceptSCTs = new ArrayList<>();
        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.get_concepts_sct_by_id(?,?)}")) {

            call.setLong(1, conceptIdPattern);
            if (group == null) {
                call.setNull(2, Types.INTEGER);
            } else {
                call.setInt(2, group);
            }
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                ConceptSCT conceptSCT = createConceptSCTFromResultSet(rs);
                conceptSCTs.add(conceptSCT);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al buscar conceptos SCT por Patrón de ID.";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

        return conceptSCTs;
    }

    @Override
    public Map<DescriptionSCT, ConceptSCT> findDescriptionsByPattern(String pattern) {

        Map<DescriptionSCT, ConceptSCT> result = new HashMap<>();
        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.find_descriptions_sct_by_pattern(?)}")) {

            call.setString(1, pattern);
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                DescriptionSCT descriptionSCT = createDescriptionSCTFromResultSet(rs);
                ConceptSCT conceptByID = this.getConceptByID(rs.getLong("conceptId"));

                result.put(descriptionSCT, conceptByID);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al buscar conceptos SCT por Patrón de ID.";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

        return result;

    }

    private ConceptSCT createConceptSCTFromResultSet(ResultSet resultSet) throws SQLException {

        long id = resultSet.getLong("id");
        Timestamp effectiveTime = resultSet.getTimestamp("effectiveTime");
        boolean active = resultSet.getBoolean("active");
        long moduleID = resultSet.getLong("moduleId");
        long definitionStatusID = resultSet.getLong("definitionStatusId");

        ConceptSCT conceptSCT = new ConceptSCT(id, effectiveTime, active, moduleID, definitionStatusID);

        conceptSCT.setId(id);

        /* Se recuperan las descripciones del concepto */
        List<DescriptionSCT> descriptions = getDescriptionsSCTByConcept(id);
        conceptSCT.setDescriptions(descriptions);

        return conceptSCT;
    }

    private List<DescriptionSCT> getDescriptionsSCTByConcept(long id) {
        List<DescriptionSCT> descriptionSCTs = new ArrayList<>();
        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.get_descriptions_sct_by_id(?)}")) {

            call.setLong(1, id);
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                DescriptionSCT recoveredConcept = createDescriptionSCTFromResultSet(rs);
                descriptionSCTs.add(recoveredConcept);
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return descriptionSCTs;
    }

    private DescriptionSCT createDescriptionSCTFromResultSet(ResultSet resultSet) throws SQLException {

        long id = resultSet.getLong("id");
        Timestamp effectiveTime = resultSet.getTimestamp("effectivetime");
        boolean active = resultSet.getBoolean("active");
        long moduleID = resultSet.getLong("moduleId");
        long conceptID = resultSet.getLong("conceptId");
        String languageCode = resultSet.getString("languageCode");
        long typeID = resultSet.getLong("typeId");
        String term = resultSet.getString("term");
        long caseSignificanceID = resultSet.getLong("caseSignificanceId");
        long acceptabilityID = resultSet.getLong("acceptabilityId");

        /**
         * Identifies whether the description is an FSN, Synonym or other description type.
         * This field is set to a child of 900000000000446008 | Description type | in the Metadata hierarchy.
         */
        try {
            DescriptionSCT descriptionSCT = new DescriptionSCT(id, DescriptionSCTType.valueOf(typeID), effectiveTime, active, moduleID, conceptID, languageCode, term, caseSignificanceID);

            descriptionSCT.setFavourite(DescriptionSCTType.valueOf(acceptabilityID).equals(DescriptionSCTType.PREFERRED));

            return descriptionSCT;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
