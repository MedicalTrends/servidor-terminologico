package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.factories.DataSourceFactory;
import cl.minsal.semantikos.kernel.singletons.DescriptionTypeSingleton;
import cl.minsal.semantikos.kernel.singletons.UserSingleton;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.categories.CategoryFactory;
import cl.minsal.semantikos.model.descriptions.*;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.model.users.UserFactory;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import org.apache.commons.collections4.iterators.SingletonListIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Collections.singletonList;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptors;
import javax.interceptor.InvocationContext;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.Future;

import static cl.minsal.semantikos.kernel.util.StringUtils.underScoreToCamelCaseJSON;
import static cl.minsal.semantikos.model.DAO.NON_PERSISTED_ID;
import static java.lang.System.currentTimeMillis;

import static java.sql.Types.TIMESTAMP;

/**
 * @author Andres Farias.
 */
@Stateless
public class DescriptionDAOImpl implements DescriptionDAO {

    /** El logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(DescriptionDAOImpl.class);

    @EJB
    private ConceptDAO conceptDAO;

    @EJB
    private AuthDAO authDAO;

    @EJB
    private TagDAO tagDAO;

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    Map<Long, ConceptSMTK> conceptSMTKMap;

    public static List<String> NO_VALID_TERMS = Arrays.asList(new String[] {"Concepto no válido", "Concepto no válido (concepto especial)"});

    @Override
    public NoValidDescription getNoValidDescriptionByID(long id) {
        //ConnectionBD connect = new ConnectionBD();

        NoValidDescription noValidDescription = null;

        String sql = "begin ? := stk.stk_pck_description.get_description_by_id(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, id);
            call.execute();

            logger.debug("Descripciones recuperadas con ID=" + id);
            ResultSet rs = (ResultSet) call.getObject(1);

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

        //ConnectionBD connect = new ConnectionBD();
        Description description = null;

        String sql = "begin ? := stk.stk_pck_description.get_description_by_id(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, id);
            call.execute();

            logger.debug("Descripciones recuperadas con ID=" + id);
            ResultSet rs = (ResultSet) call.getObject(1);

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
        //ConnectionBD connect = new ConnectionBD();
        Description description= null;

        //conceptSMTKMap = new HashMap<>();

        String sql = "begin ? := stk.stk_pck_description.get_description_by_business_id(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, descriptionId);
            call.execute();

            logger.debug("Descripciones recuperadas con Business ID=" + descriptionId);
            ResultSet rs = (ResultSet) call.getObject(1);

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

        //ConnectionBD connect = new ConnectionBD();
        List<Description> descriptions = new ArrayList<>();

        //conceptSMTKMap = new HashMap<>();

        String sql = "begin ? := stk.stk_pck_description.get_descriptions_by_idconcept(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            long idConcept = conceptSMTK.getId();

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, idConcept);
            call.execute();

            logger.debug("Descripciones recuperadas para concepto con ID=" + idConcept);
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                Description description = createDescriptionFromResultSet(rs, conceptSMTK);
                descriptions.add(description);
            }

            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar descripciones de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return descriptions;
    }

    @Override
    public Description persist(Description description, User user) {

        //ConnectionBD connect = new ConnectionBD();
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
        String sql = "begin ? := stk.stk_pck_description.create_description(?,?,?,?,?,?,?,?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.NUMERIC);
            call.setString(2, description.getDescriptionId());
            call.setLong(3, description.getDescriptionType().getId());
            call.setString(4, description.getTerm());
            call.setBoolean(5, description.isCaseSensitive());
            call.setBoolean(6, description.isAutogeneratedName());
            call.setBoolean(8, description.isPublished());
            call.setBoolean(9, description.isModeled());
            call.setLong(10, user.getId());
            call.setLong(11, description.getConceptSMTK().getId());
            call.setTimestamp(12, description.getCreationDate());

            /* Y el caso especial "nulo" del validity until */
            Timestamp validityUntil = description.getValidityUntil();
            if (validityUntil == null) {
                call.setNull(7, TIMESTAMP);
            } else {
                call.setTimestamp(7, validityUntil);
            }

            call.execute();

            //ResultSet rs = (ResultSet) call.getObject(1);

            if (call.getLong(1) > 0) {
                description.setId(call.getLong(1));
            } else {
                String errorMsg = "La descripción no fue creada. Contacte a Desarrollo";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            //rs.close();
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
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_description.delete_description(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.NUMERIC);
            call.setLong(2, description.getId());
            call.execute();

        } catch (SQLException e) {
            logger.error("La descripción con DESCRIPTION_ID=" + description.getDescriptionId() + " no fue eliminada.", e);
            throw new EJBException("La descripción con DESCRIPTION_ID=" + description.getDescriptionId() + " no fue eliminada.", e);
        }
    }

    @Override
    public void update(Description description) {

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_description.update_description(?,?,?,?,?,?,?,?,?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.NUMERIC);
            call.setLong(2, description.getId());
            call.setString(3, description.getDescriptionId());
            call.setLong(4, description.getDescriptionType().getId());
            call.setString(5, description.getTerm());
            call.setBoolean(6, description.isCaseSensitive());
            call.setBoolean(7, description.isAutogeneratedName());
            call.setBoolean(8, description.isPublished());
            call.setBoolean(9, description.isModeled());
            call.setLong(10, description.getUses());
            call.setTimestamp(11, description.getValidityUntil());
            call.setLong(12, description.getConceptSMTK().getId());
            call.setTimestamp(13, description.getCreationDate());
            call.execute();

            //ResultSet rs = (ResultSet) call.getObject(1);

            if (call.getLong(1) == 0) {
                String errorMsg = "La descripción con DESCRIPTION_ID=" + description.getDescriptionId() + " no fue actualizada.";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            //rs.close();
        } catch (SQLException e) {
            logger.error("La descripción con DESCRIPTION_ID=" + description.getDescriptionId() + " no fue actualizada.", e);
            throw new EJBException("La descripción con DESCRIPTION_ID=" + description.getDescriptionId() + " no fue actualizada.", e);
        }
    }

    @Override
    public List<ObservationNoValid> getObservationsNoValid() {
        //ConnectionBD connect = new ConnectionBD();
        List<ObservationNoValid> observationNoValids= new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_description.get_all_not_valid_observation; end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                Long id = rs.getLong("id");
                String description = rs.getString("observation");
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
        //ConnectionBD connect = new ConnectionBD();
        ObservationNoValid observationNoValid= null;

        String sql = "begin ? := stk.stk_pck_description.get_observation_no_valid_by_description(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, description.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                Long id = rs.getLong("id");
                String obsDescription = rs.getString("observation");
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
        //ConnectionBD connect = new ConnectionBD();
        List<ConceptSMTK> suggestedConcepts = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_description.get_concept_suggested(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, description.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                ConceptSMTK recoveredConcept = conceptDAO.getConceptByID(rs.getLong("id_concept"));
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
    public List<DescriptionType> getDescriptionTypes() {
        List<DescriptionType> descriptionTypes = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_description.get_description_types; end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            /* Se recuperan los description types */
            while (rs.next()) {
                descriptionTypes.add(createDescriptionTypeFromResultSet(rs));
            }

            /* Se setea la lista de Tipos de descripción */
            DescriptionTypeFactory.getInstance().setDescriptionTypes(descriptionTypes);

        } catch (SQLException e) {
            String errorMsg = "Error al intentar recuperar Description Types de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return descriptionTypes;
    }

    @Override
    public void setInvalidDescription(NoValidDescription noValidDescription) {
        //ConnectionBD connect = new ConnectionBD();

        String sql1 = "begin ? := stk.stk_pck_description.persist_observation_to_description_no_valid(?,?); end;";
        String sql2 = "begin ? := stk.stk_pck_description.persist_concept_suggested_to_description_no_valid(?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call1 = connection.prepareCall(sql1);
             CallableStatement call2 = connection.prepareCall(sql2)) {

            /* Se registra la observación primero */
            call1.registerOutParameter (1, OracleTypes.NUMERIC);
            call1.setLong(2, noValidDescription.getNoValidDescription().getId());
            call1.setLong(3, noValidDescription.getObservation());
            call1.execute();

            /* Se guardan los conceptos sugeridos para dicha descripción */
            List<ConceptSMTK> suggestedConcepts = noValidDescription.getSuggestedConcepts();

            call2.registerOutParameter (1, OracleTypes.NUMERIC);
            call2.setLong(2, noValidDescription.getNoValidDescription().getId());

            for (ConceptSMTK suggestedConcept : suggestedConcepts) {
                call2.setLong(3, suggestedConcept.getId());
                call2.execute();
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar descripciones de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }
    }

    @Override
    public List<Description> searchDescriptionsByTerm(String term, Long[] categories, Long[] refsets) {
        /* Se registra el tiempo de inicio */
        long init = currentTimeMillis();

        //ConnectionBD connect = new ConnectionBD();
        List<Description> descriptions = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_description.search_descriptions(?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, term);

            if(categories == null) {
                call.setNull(3, Types.ARRAY, "STK.NUMBER_ARRAY");
            }
            else {
                call.setArray(3, connection.unwrap(OracleConnection.class).createARRAY("STK.NUMBER_ARRAY", categories));
            }
            if(refsets == null) {
                call.setNull(4, Types.ARRAY, "STK.NUMBER_ARRAY");
            }
            else {
                call.setArray(4, connection.unwrap(OracleConnection.class).createARRAY("STK.NUMBER_ARRAY", refsets));
            }

            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                Description description = createDescriptionFromResultSet(rs, null);
                descriptions.add(description);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar descripciones de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        logger.info("searchDescriptionsByTerm(" + term + ", " + categories + ", " + refsets + "): " + descriptions);
        logger.info("searchDescriptionsByTerm(" + term + ", " + categories + ", " + refsets + "): {}s", String.format("%.2f", (currentTimeMillis() - init)/1000.0));
        return descriptions;
    }

    @Override
    public List<Description> searchDescriptionsPerfectMatch(String term, Long[] categories, Long[] refsets, int page, int pageSize) {

        /* Se registra el tiempo de inicio */
        //long init = currentTimeMillis();

        //ConnectionBD connect = new ConnectionBD();

        conceptSMTKMap = new HashMap<>();

        List<Description> descriptions = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_description.search_descriptions_perfect_match(?,?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            //connection.setReadOnly(true);

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, term.toLowerCase());

            if(categories == null) {
                call.setNull(3, Types.ARRAY, "STK.NUMBER_ARRAY");
            }
            else {
                call.setArray(3, connection.unwrap(OracleConnection.class).createARRAY("STK.NUMBER_ARRAY", categories));
            }
            if(refsets == null) {
                call.setNull(4, Types.ARRAY, "STK.NUMBER_ARRAY");
            }
            else {
                call.setArray(4, connection.unwrap(OracleConnection.class).createARRAY("STK.NUMBER_ARRAY", refsets));
            }

            call.setInt(5, page);

            call.setInt(6, pageSize);

            call.setFetchSize(100);

            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                Description description = createDescriptionFromResultSet(rs, null);
                //Description description = getDescriptionById(rs.getLong("id"));
                descriptions.add(description);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar descripciones de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        conceptSMTKMap.clear();

        //float time = (float) (currentTimeMillis() - init);

        //logger.info("ws-req-001: {}s", String.format("%.2f", time));

        //logger.info("searchDescriptionsByTerm(" + term + ", " + categories + ", " + refsets + "): " + descriptions);
        //logger.info("searchDescriptionsByTerm(" + term + ", " + categories + ", " + refsets + "): {}s", String.format("%.2f", (currentTimeMillis() - init)/1000.0));
        return descriptions;
    }

    @Override
    @Asynchronous
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Future<List<Description>> searchDescriptionsPerfectMatchAsync(String term, Long[] categories, Long[] refsets, int page, int pageSize) {

        /* Se registra el tiempo de inicio */
        long init = currentTimeMillis();

        //ConnectionBD connect = new ConnectionBD();

        conceptSMTKMap = new HashMap<>();

        List<Description> descriptions = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_description.search_descriptions_perfect_match(?,?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            //connection.setReadOnly(true);

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, term.toLowerCase());

            if(categories == null) {
                call.setNull(3, Types.ARRAY, "STK.NUMBER_ARRAY");
            }
            else {
                call.setArray(3, connection.unwrap(OracleConnection.class).createARRAY("STK.NUMBER_ARRAY", categories));
            }
            if(refsets == null) {
                call.setNull(4, Types.ARRAY, "STK.NUMBER_ARRAY");
            }
            else {
                call.setArray(4, connection.unwrap(OracleConnection.class).createARRAY("STK.NUMBER_ARRAY", refsets));
            }

            call.setInt(5, page);

            call.setInt(6, pageSize);

            //call.setFetchSize(100);

            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                Description description = createDescriptionFromResultSet(rs, null);
                //Description description = getDescriptionById(rs.getLong("id"));
                descriptions.add(description);
            }

            //rs.close();
            //call.close();
            //connection.close();

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar descripciones de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        conceptSMTKMap.clear();

        //logger.info("searchDescriptionsByTerm(" + term + ", " + categories + ", " + refsets + "): " + descriptions);
        //logger.info("searchDescriptionsByTerm(" + term + ", " + categories + ", " + refsets + "): {}s", String.format("%.2f", (currentTimeMillis() - init)/1000.0));
        return new AsyncResult<>(descriptions);
    }

    @Override
    public List<Description> searchDescriptionsTruncateMatch(String term, Long[] categories, Long[] refsets, int page, int pageSize) {
        /* Se registra el tiempo de inicio */
        long init = currentTimeMillis();

        conceptSMTKMap = new HashMap<>();

        //ConnectionBD connect = new ConnectionBD();
        List<Description> descriptions = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_description.search_descriptions_truncate_match(?,?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, term.toLowerCase());

            if(categories == null) {
                call.setNull(3, Types.ARRAY, "STK.NUMBER_ARRAY");
            }
            else {
                call.setArray(3, connection.unwrap(OracleConnection.class).createARRAY("STK.NUMBER_ARRAY", categories));
            }
            if(refsets == null) {
                call.setNull(4, Types.ARRAY, "STK.NUMBER_ARRAY");
            }
            else {
                call.setArray(4, connection.unwrap(OracleConnection.class).createARRAY("STK.NUMBER_ARRAY", refsets));
            }

            call.setInt(5, page);

            call.setInt(6, pageSize);

            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                Description description = createDescriptionFromResultSet(rs, null);
                descriptions.add(description);
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar descripciones de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        conceptSMTKMap.clear();

        //logger.info("searchDescriptionsByTerm(" + term + ", " + categories + ", " + refsets + "): " + descriptions);
        //logger.info("searchDescriptionsByTerm(" + term + ", " + categories + ", " + refsets + "): {}s", String.format("%.2f", (currentTimeMillis() - init)/1000.0));
        return descriptions;
    }

    @Override
    public int countDescriptionsPerfectMatch(String term, Long[] categories, Long[] refsets) {
        /* Se registra el tiempo de inicio */
        long init = currentTimeMillis();

        //ConnectionBD connect = new ConnectionBD();
        List<Description> descriptions = new ArrayList<>();
        int count = 0;

        String sql = "begin ? := stk.stk_pck_description.count_descriptions_perfect_match(?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.NUMERIC);
            call.setString(2, term.toLowerCase());

            if(categories == null) {
                call.setNull(3, Types.ARRAY, "STK.NUMBER_ARRAY");
            }
            else {
                call.setArray(3, connection.unwrap(OracleConnection.class).createARRAY("STK.NUMBER_ARRAY", categories));
            }
            if(refsets == null) {
                call.setNull(4, Types.ARRAY, "STK.NUMBER_ARRAY");
            }
            else {
                call.setArray(4, connection.unwrap(OracleConnection.class).createARRAY("STK.NUMBER_ARRAY", refsets));
            }

            call.execute();

            //ResultSet rs = (ResultSet) call.getObject(1);

            count = (int) call.getLong(1);

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar descripciones de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        logger.info("searchDescriptionsByTerm(" + term + ", " + categories + ", " + refsets + "): " + descriptions);
        logger.info("searchDescriptionsByTerm(" + term + ", " + categories + ", " + refsets + "): {}s", String.format("%.2f", (currentTimeMillis() - init)/1000.0));
        return count;
    }

    @Override
    public int countDescriptionsTruncateMatch(String term, Long[] categories, Long[] refsets) {
        /* Se registra el tiempo de inicio */
        long init = currentTimeMillis();

        //ConnectionBD connect = new ConnectionBD();
        List<Description> descriptions = new ArrayList<>();
        int count = 0;

        String sql = "begin ? := stk.stk_pck_description.count_descriptions_truncate_match(?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.NUMERIC);
            call.setString(2, term.toLowerCase());

            if(categories == null) {
                call.setNull(3, Types.ARRAY, "STK.NUMBER_ARRAY");
            }
            else {
                call.setArray(3, connection.unwrap(OracleConnection.class).createARRAY("STK.NUMBER_ARRAY", categories));
            }
            if(refsets == null) {
                call.setNull(4, Types.ARRAY, "STK.NUMBER_ARRAY");
            }
            else {
                call.setArray(4, connection.unwrap(OracleConnection.class).createARRAY("STK.NUMBER_ARRAY", refsets));
            }

            call.execute();

            //ResultSet rs = (ResultSet) call.getObject(1);

            count = (int) call.getLong(1);

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar descripciones de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        logger.info("searchDescriptionsByTerm(" + term + ", " + categories + ", " + refsets + "): " + descriptions);
        logger.info("searchDescriptionsByTerm(" + term + ", " + categories + ", " + refsets + "): {}s", String.format("%.2f", (currentTimeMillis() - init)/1000.0));
        return count;
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

    public Description createDescriptionFromResultSet(ResultSet resultSet, ConceptSMTK conceptSMTK) throws SQLException {

        long id = resultSet.getLong("id");

        /*
         * Try y catch ignored porque no todas las funciones de la BD que recuperan Descriptions de la BD traen esta columna.
         * Ej: Usar la funcion semantikos.get_descriptions_by_idconcept para recueprar conceptos se cae con la excepcion:
         * org.postgresql.util.PSQLException: The column name uses was not found in this ResultSet.
         */
        String descriptionID = resultSet.getString("descid");
        long idDescriptionType = resultSet.getLong("id_description_type");
        String term = resultSet.getString("term");
        boolean isCaseSensitive = resultSet.getBoolean("case_sensitive");
        boolean isAutoGenerated = resultSet.getBoolean("autogenerated_name");
        boolean isPublished = resultSet.getBoolean("is_published");
        boolean isModeled = resultSet.getBoolean("is_modeled");
        Timestamp validityUntil = resultSet.getTimestamp("validity_until");
        Timestamp creationDate = resultSet.getTimestamp("creation_date");
        long uses = resultSet.getLong("uses");

        long idUser = resultSet.getLong("id_user");

        User user = UserFactory.getInstance().findUserById(idUser);//authDAO.getUserById();

        long idConcept = resultSet.getLong("id_concept");

        if (conceptSMTK == null) {
            conceptSMTK = conceptDAO.getConceptByID(idConcept);
            /*
            if(conceptSMTKMap.containsKey(idConcept)) {
                conceptSMTK = conceptSMTKMap.get(idConcept);
            }
            else {
                conceptSMTK = conceptDAO.getConceptByID(idConcept);
                conceptSMTKMap.put(idConcept, conceptSMTK);
            }
            */
        }

        DescriptionType descriptionType = DescriptionTypeFactory.getInstance().getDescriptionTypeByID(idDescriptionType);

        Description description = new Description(id, conceptSMTK, descriptionID, descriptionType, term, uses,
                isCaseSensitive, isAutoGenerated, isPublished,
                validityUntil, creationDate, user, isModeled);

        return description;
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

    public NoValidDescription createNoValidDescriptionFromResultSet(ResultSet resultSet, ConceptSMTK conceptSMTK) throws SQLException {

        long id = resultSet.getLong("id");
        String descriptionID = resultSet.getString("descid");
        long idDescriptionType = resultSet.getLong("id_description_type");
        String term = resultSet.getString("term");
        boolean isCaseSensitive = resultSet.getBoolean("case_sensitive");
        boolean isAutoGenerated = resultSet.getBoolean("autogenerated_name");
        boolean isPublished = resultSet.getBoolean("is_published");
        boolean isModeled = resultSet.getBoolean("is_modeled");
        Timestamp validityUntil = resultSet.getTimestamp("validity_until");
        Timestamp creationDate = resultSet.getTimestamp("creation_date");
        long uses = resultSet.getLong("uses");

        //User user = authDAO.getUserById(resultSet.getLong("id_user"));
        User user = UserFactory.getInstance().findUserById(resultSet.getLong("id_user"));

        long idConcept = resultSet.getLong("id_concept");

        if (conceptSMTK == null) {
            conceptSMTK = conceptDAO.getConceptByID(idConcept);
        }

        DescriptionType descriptionType = DescriptionTypeFactory.getInstance().getDescriptionTypeByID(idDescriptionType);
        Description description = new Description(id, conceptSMTK, descriptionID, descriptionType, term, 0, isCaseSensitive, isAutoGenerated, isPublished, validityUntil, creationDate, user, isModeled);
        description.setUses(uses);
        ObservationNoValid observationNoValid = getObservationNoValidBy(description);
        List<ConceptSMTK> suggestedConcepts = getSuggestedConceptsBy(description);

        return new NoValidDescription(description, observationNoValid, suggestedConcepts);
    }

    public DescriptionType createDescriptionTypeFromResultSet(ResultSet rs) {

        DescriptionType descriptionType = new DescriptionType();

        try {
            descriptionType.setId(rs.getLong("id"));
            descriptionType.setName(rs.getString("name"));
            descriptionType.setDescription(rs.getString("description"));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return descriptionType;

    }


}
