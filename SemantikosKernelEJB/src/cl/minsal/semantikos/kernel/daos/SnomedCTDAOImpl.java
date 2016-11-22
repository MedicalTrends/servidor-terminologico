package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.snomedct.DescriptionSCT;
import cl.minsal.semantikos.model.snomedct.DescriptionSCTType;
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

        /**
         * Identifies whether the description is an FSN, Synonym or other description type.
         * This field is set to a child of 900000000000446008 | Description type | in the Metadata hierarchy.
         */
        try {
            return new DescriptionSCT(id, DescriptionSCTType.valueOf(typeID), effectiveTime, active, moduleID, conceptID, languageCode, term, caseSignificanceID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
