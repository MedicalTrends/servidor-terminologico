package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.factories.DataSourceFactory;
import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.snomedct.*;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * Funciones de base de dato para acceder a los datos de Snomed.
 *
 * @author Andrés Farías on 10/25/16.
 */
@Stateless
public class SnomedCTDAOImpl implements SnomedCTDAO {

    private static final Logger logger = LoggerFactory.getLogger(SnomedCTDAOImpl.class);

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    @Override
    public List<ConceptSCT> findConceptsBy(String pattern, Integer group) {
        List<ConceptSCT> concepts = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_snomed.find_sct_by_pattern(?,?); end;";

        //ConnectionBD connect = new ConnectionBD();
        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, pattern);
            if (group == null) {
                call.setNull(3, Types.INTEGER);
            } else {
                call.setInt(3, group);
            }
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

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
    public List<ConceptSCT> findPerfectMatch(String pattern, Integer group, int page, int pageSize) {
        List<ConceptSCT> concepts = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_snomed.find_sct_perfect_match(?,?,?,?); end;";

        //ConnectionBD connect = new ConnectionBD();
        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, pattern);
            if (group == null) {
                call.setNull(3, Types.INTEGER);
            } else {
                call.setInt(3, group);
            }
            call.setInt(4, page);
            call.setInt(5, pageSize);

            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

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
    public List<ConceptSCT> findTruncateMatch(String pattern, Integer group, int page, int pageSize) {
        List<ConceptSCT> concepts = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_snomed.find_sct_truncate_match(?,?,?,?); end;";

        //ConnectionBD connect = new ConnectionBD();
        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, pattern);
            if (group == null) {
                call.setNull(3, Types.INTEGER);
            } else {
                call.setInt(3, group);
            }
            call.setInt(4, page);
            call.setInt(5, pageSize);

            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

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

        String sql = "begin ? := stk.stk_pck_snomed.count_sct_perfect_match(?,?); end;";

        //ConnectionBD connect = new ConnectionBD();
        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.NUMERIC);
            call.setString(2, pattern);
            if (group == null) {
                call.setNull(3, Types.INTEGER);
            } else {
                call.setInt(3, group);
            }
            call.execute();

            concepts = call.getLong(1);

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

        String sql = "begin ? := stk.stk_pck_snomed.count_sct_truncate_match(?,?); end;";

        //ConnectionBD connect = new ConnectionBD();
        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.NUMERIC);
            call.setString(2, pattern);
            if (group == null) {
                call.setNull(3, Types.INTEGER);
            } else {
                call.setInt(3, group);
            }
            call.execute();

            concepts = call.getLong(1);

        } catch (SQLException e) {
            String errorMsg = "Error al buscar Snomed CT";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

        return concepts;
    }

    @Override
    public ConceptSCT getConceptByID(long conceptID) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_snomed.get_sct_by_concept_id(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, conceptID);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            ConceptSCT conceptSCTFromResultSet;
            if (rs.next()) {
                conceptSCTFromResultSet = createConceptSCTFromResultSet(rs);
            } else {
                throw new EJBException("No se encontró un concepto con ID=" + conceptID);
            }
            rs.close();
            call.close();
            connection.close();
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

        String sql = "begin ? := stk.stk_pck_snomed.get_concepts_sct_by_id(?,?); end;";

        //ConnectionBD connect = new ConnectionBD();
        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, conceptIdPattern);
            if (group == null) {
                call.setNull(3, Types.INTEGER);
            } else {
                call.setInt(3, group);
            }
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

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
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_snomed.find_descriptions_sct_by_pattern(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, pattern);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

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
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_snomed.get_descriptions_sct_by_id(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, id);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

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

    @Override
    public DescriptionSCT getDescriptionSCTBy(long idDescriptionSCT) {
        //ConnectionBD connect = new ConnectionBD();
        DescriptionSCT descriptionSCT = null;

        String sql = "begin ? := stk.stk_pck_snomed.get_description_sct_by_id(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, idDescriptionSCT);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                descriptionSCT = createDescriptionSCTFromResultSet(rs);
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
    public List<RelationshipSCT> getRelationshipsBySourceConcept(ConceptSCT conceptSCT) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_snomed.get_relationships_sct_by_id(?); end;";

        List<RelationshipSCT> relationships = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, conceptSCT.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                try {
                    relationships.add(createRelationshipSCTFromResultSet(rs, conceptSCT));
                }
                catch (EJBException e) {
                    logger.warn(e.getMessage());
                }
            }

            rs.close();
            call.close();
            connection.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return relationships;
    }

    private RelationshipSCT createRelationshipSCTFromResultSet(ResultSet resultSet, ConceptSCT sourceConcept) throws SQLException {

        long id = resultSet.getLong("id");
        Timestamp effectiveTime = resultSet.getTimestamp("effectivetime");
        boolean active = resultSet.getBoolean("active");
        long moduleID = resultSet.getLong("moduleId");
        long relationshipGroup = resultSet.getLong("relationshipGroup");
        long characteristicTypeId = resultSet.getLong("characteristicTypeId");
        long modifierId = resultSet.getLong("modifierId");

        ConceptSCT destinationConcept = getConceptByID(resultSet.getLong("destinationId"));
        ConceptSCT typeConcept = getConceptByID(resultSet.getLong("typeId"));

        return new RelationshipSCT(id, effectiveTime, active, moduleID, sourceConcept, destinationConcept, relationshipGroup, typeConcept, characteristicTypeId, modifierId);
    }
}
