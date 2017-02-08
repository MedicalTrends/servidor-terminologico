package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cl.minsal.semantikos.kernel.daos.DAO.NON_PERSISTED_ID;
import static cl.minsal.semantikos.kernel.util.StringUtils.underScoreToCamelCaseJSON;
import static java.lang.System.currentTimeMillis;
import static java.sql.Types.TIMESTAMP;

/**
 * @author Andres Farias.
 */
@Singleton
@Startup
public class DescriptionDAOImpl implements DescriptionDAO {

    /** El logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(DescriptionDAOImpl.class);

    @EJB
    private ConceptDAO conceptDAO;

    @PostConstruct
    private void init() {
        this.refreshDescriptionTypes();
    }

    @EJB
    private AuthDAO authDAO;

    @Override
    public List<DescriptionType> getDescriptionTypes() {

        ConnectionBD connect = new ConnectionBD();
        ObjectMapper mapper = new ObjectMapper();
        DescriptionType[] descriptionTypes = new DescriptionType[0];
        String sql = "{call semantikos.get_all_description_types()}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.execute();
            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                String resultJSON = rs.getString(1);
                descriptionTypes = mapper.readValue(underScoreToCamelCaseJSON(resultJSON), DescriptionType[].class);
            }
        } catch (SQLException e) {
            String errorMsg = "Error al recuperar descripciones de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        } catch (IOException e) {
            String errorMsg = "Error al parsear los objetos a JSON.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return Arrays.asList(descriptionTypes);
    }

    @Override
    public NoValidDescription getNoValidDescriptionByID(long id) {
        ConnectionBD connect = new ConnectionBD();

        NoValidDescription noValidDescription = null;

        String sql = "{call semantikos.get_description_by_id(?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, id);
            call.execute();

            logger.debug("Descripciones recuperadas con ID=" + id);
            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                noValidDescription = createNoValidDescriptionFromResultSet(rs, null);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar la descripción de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return noValidDescription;
    }

    @Override
    public Description getDescriptionBy(long id) {
        ConnectionBD connect = new ConnectionBD();
        Description description = null;

        String sql = "{call semantikos.get_description_by_id(?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, id);
            call.execute();

            logger.debug("Descripciones recuperadas con ID=" + id);
            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                description = createDescriptionFromResultSet(rs, null);

            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar la descripción de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return description;
    }

    @Override
    public Description getDescriptionByDescriptionID(String descriptionId) {
        ConnectionBD connect = new ConnectionBD();
        Description description= null;

        String sql = "{call semantikos.get_description_by_business_id(?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setString(1, descriptionId);
            call.execute();

            logger.debug("Descripciones recuperadas con Business ID=" + descriptionId);
            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                description = createDescriptionFromResultSet(rs, null);
            } else {
                throw new IllegalArgumentException("No existe una descripción con DESCRIPTION_ID = " + descriptionId);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar la descripción de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return description;
    }

    @Override
    public List<Description> getDescriptionsByConcept(ConceptSMTK conceptSMTK) {

        ConnectionBD connect = new ConnectionBD();
        List<Description> descriptions = new ArrayList<>();

        String sql = "{call semantikos.get_descriptions_by_idconcept(?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            long idConcept = conceptSMTK.getId();
            call.setLong(1, idConcept);
            call.execute();

            logger.debug("Descripciones recuperadas para concepto con ID=" + idConcept);
            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                Description description = createDescriptionFromResultSet(rs, conceptSMTK);
                descriptions.add(description);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar descripciones de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return descriptions;
    }

    @Override
    public DescriptionTypeFactory refreshDescriptionTypes() {

        ConnectionBD connect = new ConnectionBD();
        ObjectMapper mapper = new ObjectMapper();

        List<DescriptionType> descriptionTypes = new ArrayList<>();

        String sql = "{call semantikos.get_description_types()}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.execute();
            ResultSet rs = call.getResultSet();

            /* Se recuperan los description types */
            DescriptionTypeDTO[] theDescriptionTypes = new DescriptionTypeDTO[0];
            if (rs.next()) {
                String resultJSON = rs.getString(1);
                theDescriptionTypes = mapper.readValue(underScoreToCamelCaseJSON(resultJSON), DescriptionTypeDTO[].class);
            }

            if (theDescriptionTypes.length > 0) {
                for (DescriptionTypeDTO aDescriptionType : theDescriptionTypes) {
                    DescriptionType descriptionType = aDescriptionType.getDescriptionType();
                    descriptionTypes.add(descriptionType);
                }
            }

            /* Se setea la lista de Tipos de descripción */
            DescriptionTypeFactory.getInstance().setDescriptionTypes(descriptionTypes);
        } catch (SQLException e) {
            String errorMsg = "Error al intentar recuperar Description Types de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        } catch (IOException e) {
            String errorMsg = "Error al intentar parsear Description Types en JSON.";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return DescriptionTypeFactory.getInstance();
    }

    @Override
    public Description persist(Description description, User user) {

        ConnectionBD connect = new ConnectionBD();
        /*
         * param1: ID
         * param 2: DesType ID
         * param 3: Term
         * param 4: case
         * param 5: auto-generado
         * param 6: validity until
         * param 7: published
         * param 8: estado
         * param 9: id user
         * param 10: id concepto
         */
        String sql = "{call semantikos.create_description(?,?,?,?,?,?,?,?,?,?,?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setString(1, description.getDescriptionId());
            call.setLong(2, description.getDescriptionType().getId());
            call.setString(3, description.getTerm());
            call.setBoolean(4, description.isCaseSensitive());
            call.setBoolean(5, description.isAutogeneratedName());
            call.setBoolean(7, description.isPublished());
            call.setBoolean(8, description.isModeled());
            call.setLong(9, user.getIdUser());
            call.setLong(10, description.getConceptSMTK().getId());
            call.setTimestamp(11, description.getCreationDate());

            /* Y el caso especial "nulo" del validity until */
            Timestamp validityUntil = description.getValidityUntil();
            if (validityUntil == null) {
                call.setNull(6, TIMESTAMP);
            } else {
                call.setTimestamp(6, validityUntil);
            }

            call.execute();

            ResultSet rs = call.getResultSet();

            if (rs.next()) {
                description.setId(rs.getLong(1));
            } else {
                String errorMsg = "La descripción no fue creada. Contacte a Desarrollo";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            rs.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return description;
    }

    @Override
    public void invalidate(Description description) {
        description.setValidityUntil(new Timestamp(currentTimeMillis()));
        this.update(description);
    }

    @Override
    public void deleteDescription(Description description) {
        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.delete_description(?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, description.getId());
            call.execute();
        } catch (SQLException e) {
            logger.error("La descripción con DESCRIPTION_ID=" + description.getDescriptionId() + " no fue eliminada.", e);
            throw new EJBException("La descripción con DESCRIPTION_ID=" + description.getDescriptionId() + " no fue eliminada.", e);
        }
    }

    @Override
    public void update(Description description) {

        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.update_description(?,?,?,?,?,?,?,?,?,?,?,?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, description.getId());
            call.setString(2, description.getDescriptionId());
            call.setLong(3, description.getDescriptionType().getId());
            call.setString(4, description.getTerm());
            call.setBoolean(5, description.isCaseSensitive());
            call.setBoolean(6, description.isAutogeneratedName());
            call.setBoolean(7, description.isPublished());
            call.setBoolean(8, description.isModeled());
            call.setLong(9, description.getUses());
            call.setTimestamp(10, description.getValidityUntil());
            call.setLong(11, description.getConceptSMTK().getId());
            call.setTimestamp(12, description.getCreationDate());
            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                if (!rs.getBoolean(1)) {
                    throw new EJBException("La descripción con DESCRIPTION_ID=" + description.getDescriptionId() + " no fue actualizada.");
                }
            } else {
                String errorMsg = "La descripción con DESCRIPTION_ID=" + description.getDescriptionId() + " no fue actualizada.";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("La descripción con DESCRIPTION_ID=" + description.getDescriptionId() + " no fue actualizada.", e);
            throw new EJBException("La descripción con DESCRIPTION_ID=" + description.getDescriptionId() + " no fue actualizada.", e);
        }
    }

    @Override
    public List<ObservationNoValid> getObservationsNoValid() {
        ConnectionBD connect = new ConnectionBD();
        List<ObservationNoValid> observationNoValids= new ArrayList<>();
        String sql = "{call semantikos.get_all_not_valid_observation()}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {
            call.execute();
            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                Long id = rs.getLong("id");
                String description = rs.getString("description");
                observationNoValids.add(new ObservationNoValid(id,description));
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar descripciones de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return observationNoValids;
    }

    @Override
    public ObservationNoValid getObservationNoValidBy(Description description) {
        ConnectionBD connect = new ConnectionBD();
        ObservationNoValid observationNoValid= null;
        String sql = "{call semantikos.get_observation_no_valid_by_description(?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {
            call.setLong(1, description.getId());
            call.execute();
            ResultSet rs = call.getResultSet();

            while (rs.next()) {
                Long id = rs.getLong("id");
                String obsDescription = rs.getString("description");
                observationNoValid = new ObservationNoValid(id,obsDescription);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar descripciones de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return observationNoValid;
    }

    @Override
    public List<ConceptSMTK> getSuggestedConceptsBy(Description description) {
        ConnectionBD connect = new ConnectionBD();
        List<ConceptSMTK> suggestedConcepts = new ArrayList<>();
        String sql = "{call semantikos.get_concept_suggested(?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {
            call.setLong(1, description.getId());
            call.execute();
            ResultSet rs = call.getResultSet();

            while (rs.next()) {
                ConceptSMTK recoveredConcept = conceptDAO.getConceptByID(rs.getLong("id"));
                suggestedConcepts.add(recoveredConcept);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar descripciones de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return suggestedConcepts;
    }

    @Override
    public void setInvalidDescription(NoValidDescription noValidDescription) {
        ConnectionBD connect = new ConnectionBD();

        String sql1 = "{call semantikos.persist_observation_to_description_no_valid(?,?)}";
        String sql2 = "{call semantikos.persist_concept_suggested_to_description_no_valid(?,?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call1 = connection.prepareCall(sql1);
             CallableStatement call2 = connection.prepareCall(sql2)) {

            /* Se registra la observación primero */
            call1.setLong(1, noValidDescription.getNoValidDescription().getId());
            call1.setLong(2, noValidDescription.getObservation());
            call1.execute();


            /* Se guardan los conceptos sugeridos para dicha descripción */
            List<ConceptSMTK> suggestedConcepts = noValidDescription.getSuggestedConcepts();
            call2.setLong(1, noValidDescription.getNoValidDescription().getId());
            for (ConceptSMTK suggestedConcept : suggestedConcepts) {
                call2.setLong(2, suggestedConcept.getId());
                call2.execute();
            }
        } catch (SQLException e) {
            String errorMsg = "Error al recuperar descripciones de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }
    }

    @Override
    public List<Description> searchDescriptionsByTerm(String term, List<Category> categories) {
        ConnectionBD connect = new ConnectionBD();
        List<Description> descriptions = new ArrayList<>();

        String sql = "{call semantikos.search_descriptions_by_term_and_categories(?,?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setString(1, term.toLowerCase());
            Category[] entities = categories.toArray(new Category[categories.size()]);
            call.setArray(2, connection.createArrayOf("bigint", convertListPersistentToListID(entities)));
            call.execute();

            logger.debug("Búsqueda exacta descripciones con término =" + term);
            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                Description description = createDescriptionFromResultSet(rs, null);
                descriptions.add(description);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar descripciones de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return descriptions;
    }

    @Override
    public List<Description> searchDescriptionsByTerm(String term, List<Category> categories, List<RefSet> refSets) {
        /* Se registra el tiempo de inicio */
        long init = currentTimeMillis();

        ConnectionBD connect = new ConnectionBD();
        List<Description> descriptions = new ArrayList<>();

        String sql = "{call semantikos.search_descriptions_by_term_and_categories_and_refsets(?,?,?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setString(1, term.toLowerCase());
            Category[] entities = categories.toArray(new Category[categories.size()]);
            RefSet[] refsetEntities = refSets.toArray(new RefSet[refSets.size()]);
            call.setArray(2, connection.createArrayOf("bigint", convertListPersistentToListID(entities)));
            call.setArray(3, connection.createArrayOf("bigint", convertListPersistentToListID(refsetEntities)));
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                Description description = createDescriptionFromResultSet(rs, null);
                descriptions.add(description);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar descripciones de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        logger.info("searchDescriptionsByTerm(" + term + ", " + categories + ", " + refSets + "): " + descriptions);
        logger.info("searchDescriptionsByTerm(" + term + ", " + categories + ", " + refSets + "): {}s", String.format("%.2f", (currentTimeMillis() - init)/1000.0));
        return descriptions;
    }

    private Long[] convertListPersistentToListID(PersistentEntity[] entities) {

        List<Long> listIDs = new ArrayList<>();

        for (PersistentEntity entity : entities) {
            listIDs.add(entity.getId());
        }

        return listIDs.toArray(new Long[entities.length]);
    }

    private Description createDescriptionFromResultSet(ResultSet resultSet, ConceptSMTK conceptSMTK) throws SQLException {

        long id = resultSet.getLong("id");

        /*
         * Try y catch ignored porque no todas las funciones de la BD que recuperan Descriptions de la BD traen esta columna.
         * Ej: Usar la funcion semantikos.get_descriptions_by_idconcept para recueprar conceptos se cae con la excepcion:
         * org.postgresql.util.PSQLException: The column name uses was not found in this ResultSet.
         */
        String descriptionID = resultSet.getString("description_id");
        long idDescriptionType = resultSet.getLong("id_description_type");
        String term = resultSet.getString("term");
        boolean isCaseSensitive = resultSet.getBoolean("case_sensitive");
        boolean isAutoGenerated = resultSet.getBoolean("autogenerated");
        boolean isPublished = resultSet.getBoolean("is_published");
        boolean isModeled = resultSet.getBoolean("is_modeled");
        Timestamp validityUntil = resultSet.getTimestamp("validity_until");
        Timestamp creationDate = resultSet.getTimestamp("creation_date");
        long uses = resultSet.getLong("uses");

        User user = authDAO.getUserById(resultSet.getLong("id_user"));

        long idConcept = resultSet.getLong("id_concept");

        ConceptSMTK conceptByID;
        if (conceptSMTK == null) {
            conceptByID = conceptDAO.getConceptByID(idConcept);
        } else {
            conceptByID = conceptSMTK;
        }
        DescriptionType descriptionType = DescriptionTypeFactory.getInstance().getDescriptionTypeByID(idDescriptionType);
        Description description = new Description(id, conceptByID, descriptionID, descriptionType, term, uses,
                isCaseSensitive, isAutoGenerated, isPublished,
                validityUntil, creationDate, user, isModeled);

        return description;
    }

    private NoValidDescription createNoValidDescriptionFromResultSet(ResultSet resultSet, ConceptSMTK conceptSMTK) throws SQLException {

        long id = resultSet.getLong("id");
        String descriptionID = resultSet.getString("description_id");
        long idDescriptionType = resultSet.getLong("id_description_type");
        String term = resultSet.getString("term");
        boolean isCaseSensitive = resultSet.getBoolean("case_sensitive");
        boolean isAutoGenerated = resultSet.getBoolean("autogenerated");
        boolean isPublished = resultSet.getBoolean("is_published");
        boolean isModeled = resultSet.getBoolean("is_modeled");
        Timestamp validityUntil = resultSet.getTimestamp("validity_until");
        Timestamp creationDate = resultSet.getTimestamp("creation_date");
        long uses = resultSet.getLong("uses");

        User user = authDAO.getUserById(resultSet.getLong("id_user"));

        long idConcept = resultSet.getLong("id_concept");

        ConceptSMTK conceptByID;
        if (conceptSMTK == null) {
            conceptByID = conceptDAO.getConceptByID(idConcept);
        } else {
            conceptByID = conceptSMTK;
        }

        DescriptionType descriptionType = DescriptionTypeFactory.getInstance().getDescriptionTypeByID(idDescriptionType);
        Description description = new Description(id, conceptByID, descriptionID, descriptionType, term, 0, isCaseSensitive, isAutoGenerated, isPublished, validityUntil, creationDate, user, isModeled);
        ObservationNoValid observationNoValid = getObservationNoValidBy(description);
        List<ConceptSMTK> suggestedConcepts = getSuggestedConceptsBy(description);
        return new NoValidDescription(description, observationNoValid, suggestedConcepts);
    }

    @Override
    public List<Description> persistNonPersistent(List<Description> descriptions, ConceptSMTK conceptSMTK, User user) {
        List<Description> persistedDescriptions = new ArrayList<>();

        for (Description description : descriptions) {
            if (!isPersistent(description)) {
                this.persist(description, user);
                persistedDescriptions.add(description);
            }
        }

        return persistedDescriptions;
    }

    /**
     * Este método es responsable de indicar si la descripción es persistente o no.
     *
     * @param description La descripción de la que se desea determinar si es persistente.
     *
     * @return <code>true</code> si es persistente y <code>false</code> sino.
     */
    private boolean isPersistent(Description description) {
        return description.getId() != NON_PERSISTED_ID;
    }
}

class DescriptionTypeDTO {

    private long id;
    private String name;
    private String description;

    public DescriptionTypeDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DescriptionType getDescriptionType() {
        return new DescriptionType(this.id, this.name, this.description);
    }

    @Override
    public String toString() {
        return "DescriptionTypeDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
