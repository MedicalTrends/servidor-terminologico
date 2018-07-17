package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.factories.DataSourceFactory;
import cl.minsal.semantikos.kernel.singletons.CategorySingleton;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.categories.CategoryFactory;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.tags.Tag;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;
import cl.minsal.semantikos.model.users.User;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.currentTimeMillis;
import static java.util.Collections.EMPTY_LIST;
import static org.apache.commons.lang.ArrayUtils.EMPTY_LONG_OBJECT_ARRAY;

/**
 * @author Gusatvo Punucura on 13-07-16.
 */

@Stateless
public class ConceptDAOImpl implements ConceptDAO {

    /**
     * El logger de esta clase
     */
    private static final Logger logger = LoggerFactory.getLogger(ConceptDAOImpl.class);

    private final static String PENDING_CONCEPT_PREFERRED_TERM = "Pendientes";

    private final static String NO_VALID_CONCEPT_PREFERRED_TERM = "Concepto no válido";

    public static ConceptSMTK NO_VALID_CONCEPT;

    public static ConceptSMTK PENDING_CONCEPT;

    /*
    @PersistenceContext(unitName = "SEMANTIKOS_PU")
    private EntityManager em;
    */

    @EJB
    private CategoryDAO categoryDAO;

    @EJB
    private DescriptionDAO descriptionDAO;

    /**
     * El DAO para manejar relaciones del concepto
     */
    @EJB
    private RelationshipDAO relationshipDAO;

    @EJB
    private TargetDAO targetDAO;

    @EJB
    private TagSMTKDAO tagSMTKDAO;

    @EJB
    TagDAO tagDAO;

    @EJB
    RefSetDAO refSetDAO;

    @EJB
    CategorySingleton categorySingleton;

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    @Override
    public void delete(ConceptSMTK conceptSMTK) {

        /* Esto aplica sólo si el concepto no está persistido */
        if (!conceptSMTK.isPersistent()) {
            return;
        }

        String sql = "begin ? := stk.stk_pck_concept.delete_concept(?); end;";

        //ConnectionBD connect = new ConnectionBD();
        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.NUMERIC);
            call.setLong(2, conceptSMTK.getId());
            call.execute();

        } catch (SQLException e) {
            String errorMessage = "No se pudo eliminar el concepto: " + conceptSMTK.toString();
            logger.error(errorMessage, e);
            throw new EJBException(errorMessage, e);
        }
    }

    @Override
    public List<ConceptSMTK> findConcepts(Long[] categories, Long[] refsets, Boolean modeled) {
        List<ConceptSMTK> concepts = new ArrayList<>();
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_concept.find_concept(?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setArray(2, connection.unwrap(OracleConnection.class).createARRAY("STK.NUMBER_ARRAY", categories));
            call.setArray(3, connection.unwrap(OracleConnection.class).createARRAY("STK.NUMBER_ARRAY", refsets));
            if(modeled == null) {
                call.setNull(4, Types.NUMERIC);
            }
            else {
                call.setBoolean(4, modeled);
            }

            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                ConceptSMTK e = createConceptSMTKFromResultSet(rs);
                concepts.add(e);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return concepts;
    }

    @Override
    public ConceptSMTK getConceptByCONCEPT_ID(String conceptID) {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_concept.get_concept_by_conceptid(?); end;";

        ConceptSMTK conceptSMTK;
        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, conceptID);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                conceptSMTK = createConceptSMTKFromResultSet(rs);
            } else {
                String errorMsg = "No existe un concepto con CONCEPT_ID=" + conceptID;
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return conceptSMTK;
    }

    @Override
    public ConceptSMTK getConceptByID(long id) {
        //ConnectionBD connect = new ConnectionBD();
        /* Se registra el tiempo de inicio */
        long init = currentTimeMillis();

        String sql = "begin ? := stk.stk_pck_concept.get_concept_by_id(?); end;";

        ConceptSMTK conceptSMTK;
        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            //connection.setReadOnly(true);

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, id);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                conceptSMTK = createConceptSMTKFromResultSet(rs);
            } else {
                String errorMsg = "No existe un concepto con CONCEPT_ID=" + id;
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }

            rs.close();

        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        float time = (float) (currentTimeMillis() - init);

        //logger.info("ws-req-001: {}s", String.format("%.2f", time));

        return conceptSMTK;
    }

    @Override
    public ConceptSMTK getConceptByID(long id, boolean chargeDescriptions) {
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_concept.get_concept_by_id(?); end;";

        ConceptSMTK conceptSMTK;
        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, id);
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                conceptSMTK = createConceptSMTKFromResultSet(rs);
            } else {
                String errorMsg = "No existe un concepto con CONCEPT_ID=" + id;
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return conceptSMTK;
    }

    @Override
    public List<ConceptSMTK> findConceptsByTag(Tag tag) {

        List<ConceptSMTK> concepts = new ArrayList<>();
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_concept.find_concepts_by_tag(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(1, tag.getId());
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                ConceptSMTK conceptSMTKFromResultSet = createConceptSMTKFromResultSet(rs);
                concepts.add(conceptSMTKFromResultSet);
            }
            rs.close();
        } catch (SQLException e) {
            String errorMgs = "Error al buscar conceptos por Tag[" + tag + "]";
            logger.error(errorMgs, e);
            throw new EJBException(errorMgs, e);
        }

        return concepts;
    }

    @Override
    public List<ConceptSMTK> findConceptsByRefSet(RefSet refSet) {

        List<ConceptSMTK> concepts = new ArrayList<>();
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_concept.find_concept_by_refset(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, refSet.getId());
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                ConceptSMTK conceptSMTKFromResultSet = createConceptSMTKFromResultSet(rs);
                concepts.add(conceptSMTKFromResultSet);
            }
            rs.close();
        } catch (SQLException e) {
            String errorMgs = "Error al buscar conceptos por RefSet[" + refSet + "]";
            logger.error(errorMgs, e);
            throw new EJBException(errorMgs, e);
        }

        return concepts;
    }

    @Override
    public void persistConceptAttributes(ConceptSMTK conceptSMTK, User user) {

        //ConnectionBD connect = new ConnectionBD();
        long id;

        String sql = "begin ? := stk.stk_pck_concept.create_concept(?,?,?,?,?,?,?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.NUMERIC);
            call.setString(2, conceptSMTK.getConceptID());
            call.setLong(3, conceptSMTK.getCategory().getId());
            call.setBoolean(4, conceptSMTK.isToBeReviewed());
            call.setBoolean(5, conceptSMTK.isToBeConsulted());
            call.setBoolean(6, conceptSMTK.isModeled());
            call.setBoolean(7, conceptSMTK.isFullyDefined());
            call.setBoolean(8, conceptSMTK.isInherited());
            call.setBoolean(9, conceptSMTK.isPublished());
            call.setString(10, conceptSMTK.getObservation());
            call.setLong(11, conceptSMTK.getTagSMTK().getId());
            call.execute();

            //ResultSet rs = call.getResultSet();
            //ResultSet rs = (ResultSet) call.getObject(1);

            if (call.getLong(1) > 0) {
                /* Se recupera el ID del concepto persistido */
                id = call.getLong(1);
                conceptSMTK.setId(id);
            } else {
                String errorMsg = "El concepto no fue creado por una razon desconocida. Alertar al area de desarrollo" +
                        " sobre esto";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            //rs.close();
        } catch (SQLException e) {
            String errorMsg = "El concepto " + conceptSMTK.toString() + " no fue persistido.";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

    }

    @Override
    public void update(ConceptSMTK conceptSMTK) {

        logger.info("Actualizando información básica de concepto: " + conceptSMTK.toString());
        //ConnectionBD connect = new ConnectionBD();
        long updated;

        String sql = "begin ? := stk.stk_pck_concept.update_concept(?,?,?,?,?,?,?,?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.NUMERIC);
            call.setLong(2, conceptSMTK.getId());
            call.setString(3, conceptSMTK.getConceptID());
            call.setLong(4, conceptSMTK.getCategory().getId());
            call.setBoolean(5, conceptSMTK.isToBeReviewed());
            call.setBoolean(6, conceptSMTK.isToBeConsulted());
            call.setBoolean(7, conceptSMTK.isModeled());
            call.setBoolean(8, conceptSMTK.isFullyDefined());
            call.setBoolean(9, conceptSMTK.isInherited());
            call.setBoolean(10, conceptSMTK.isPublished());
            call.setString(11, conceptSMTK.getObservation());
            call.setLong(12, conceptSMTK.getTagSMTK().getId());
            call.execute();

            //ResultSet rs = call.getResultSet();
            //ResultSet rs = (ResultSet) call.getObject(1);

            if (call.getLong(1) > 0) {
                /* Se recupera el ID del concepto persistido */
                updated = call.getLong(1);
            } else {
                String errorMsg = "El concepto no fue creado por una razón desconocida. Alertar al area de desarrollo" +
                        " sobre esto";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            //rs.close();
        } catch (SQLException e) {
            String errorMsg = "El concepto " + conceptSMTK.toString() + " no fue actualizado.";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        if (updated != 0) {
            logger.info("Información de concepto (CONCEPT_ID=" + conceptSMTK.getConceptID() + ") actualizada " +
                    "exitosamente.");
        } else {
            String errorMsg = "Información de concepto (CONCEPT_ID=" + conceptSMTK.getConceptID() + ") no fue " +
                    "actualizada.";
            logger.error(errorMsg);
            throw new EJBException(errorMsg);
        }
    }

    @Override
    public void forcedModeledConcept(Long idConcept) {

        logger.info("Pasando a modelado el concepto de ID=" + idConcept);
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_concept.force_modeled_concept(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, idConcept);
            call.execute();
        } catch (SQLException e) {
            logger.error("Error al tratar de modelar un concepto.", e);
        }
    }

    @Override
    public ConceptSMTK getNoValidConcept() {

        if(NO_VALID_CONCEPT != null) {
            return NO_VALID_CONCEPT;
        }

        /* De otro modo, se recupera desde la base de datos. Primero se busca su categoría por nombre */
        Category specialConceptCategory;
        try {
            //specialConceptCategory = categoryDAO.getCategoryByName("Concepto Especial");
            specialConceptCategory = CategoryFactory.getInstance().findCategoryByName("Concepto Especial");
        } catch (IllegalArgumentException iae) {
            String errorMsg = "No se encontró la categoría Especial!";
            logger.error(errorMsg, iae);
            throw new EJBException(errorMsg, iae);
        }

        /* Luego se recuperan los conceptos de la categoría y se busca por el que tenga el FSN adecuado */
        List<ConceptSMTK> specialConcepts = findPerfectMatch(NO_VALID_CONCEPT_PREFERRED_TERM, new Long[]{specialConceptCategory.getId()}, null, true);
        for (ConceptSMTK specialConcept : specialConcepts) {
            if (specialConcept.getDescriptionFavorite().getTerm().equalsIgnoreCase(NO_VALID_CONCEPT_PREFERRED_TERM)) {
                NO_VALID_CONCEPT = specialConcept;
                return specialConcept;
            }
        }

        /* Saliendo del for significa que no se creo */
        String errorMsg = "No se encontró el concepto especial!";
        logger.error(errorMsg);
        throw new EJBException(errorMsg);
    }

    @Override
    public ConceptSMTK getPendingConcept() {

        if(PENDING_CONCEPT != null) {
            return PENDING_CONCEPT;
        }

        /* De otro modo, se recupera desde la base de datos. Primero se busca su categoría por nombre */
        Category specialConceptCategory;
        try {
            //specialConceptCategory = categoryDAO.getCategoryByName("Concepto Especial");
            specialConceptCategory = CategoryFactory.getInstance().findCategoryByName("Concepto Especial");
        } catch (IllegalArgumentException iae) {
            String errorMsg = "No se encontró la categoría Especial!";
            logger.error(errorMsg, iae);
            throw new EJBException(errorMsg, iae);
        }

        /* Luego se recuperan los conceptos de la categoría y se busca por el que tenga el FSN adecuado */
        List<ConceptSMTK> specialConcepts = findPerfectMatch(PENDING_CONCEPT_PREFERRED_TERM, new Long[]{specialConceptCategory.getId()}, null, true);
        for (ConceptSMTK specialConcept : specialConcepts) {
            if (specialConcept.getDescriptionFavorite().getTerm().equalsIgnoreCase(PENDING_CONCEPT_PREFERRED_TERM)) {
                PENDING_CONCEPT = specialConcept;
                return specialConcept;
            }
        }

        /* Saliendo del for significa que no se creo */
        String errorMsg = "No se encontró el concepto especial!";
        logger.error(errorMsg);
        throw new EJBException(errorMsg);
    }

    @Override
    public List<ConceptSMTK> getRelatedConcepts(ConceptSMTK conceptSMTK) {

        List<ConceptSMTK> concepts = new ArrayList<>();

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_concept.get_related_concept(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, conceptSMTK.getId());
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                concepts.add(createConceptSMTKFromResultSet(rs));
            }
            rs.close();

        } catch (SQLException e) {
            logger.error("Error al buscar conceptos relacionados", e);
        }

        return concepts;
    }

    @Override
    public List<ConceptSMTK> findConceptsWithTarget(Relationship relationship) {

        String sql = "begin ? := stk.stk_pck_concept.find_concepts_with_target(?,?,?,?); end;";

        List<ConceptSMTK> conceptSMTKs = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);

            call.setLong(2, relationship.getSourceConcept().getId());

            call.setLong(3, relationship.getRelationshipDefinition().getId());

            if(relationship.getRelationshipDefinition().getTargetDefinition().isSMTKType()){
                call.setLong(4, relationship.getRelationshipDefinition().getTargetDefinition().getId());
                call.setString(5, String.valueOf(relationship.getTarget().getId()));
            }

            if(relationship.getRelationshipDefinition().getTargetDefinition().isHelperTable()){
                call.setLong(4, relationship.getRelationshipDefinition().getTargetDefinition().getId());
                HelperTableRow helperTableRow = (HelperTableRow) relationship.getTarget();
                call.setString(5, String.valueOf(helperTableRow.getId()));
            }

            if(relationship.getRelationshipDefinition().getTargetDefinition().isBasicType()){
                call.setLong(4, relationship.getRelationshipDefinition().getTargetDefinition().getId());
                BasicTypeValue basicTypeValue = (BasicTypeValue) relationship.getTarget();
                call.setString(5, basicTypeValue.toString());
            }

            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while(rs.next()) {
                conceptSMTKs.add(createConceptSMTKFromResultSet(rs));
            }

            rs.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return conceptSMTKs;
    }

    @Override
    public List<ConceptSMTK> findTruncateMatch(String pattern, Long[] categories, Long[] refsets, Boolean modeled) {
        List<ConceptSMTK> concepts;

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_concept.find_concept_truncate_match(?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection(); CallableStatement call =
                connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, pattern);

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

            if(modeled == null) {
                call.setNull(5, Types.NUMERIC);
            }
            else {
                call.setBoolean(5, modeled);
            }
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            concepts = new ArrayList<>();

            while (rs.next()) {
                concepts.add(createConceptSMTKFromResultSet(rs));
            }
            rs.close();
            call.close();

        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return concepts;
    }

    @Override
    public List<ConceptSMTK> findPerfectMatch(String pattern, Long[] categories, Long[] refsets, Boolean modeled) {
        List<ConceptSMTK> concepts;

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_concept.find_concept_perfect_match(?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection(); CallableStatement call =
                connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, pattern);

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

            if(modeled == null) {
                call.setNull(5, Types.NUMERIC);
            }
            else {
                call.setBoolean(5, modeled);
            }
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            concepts = new ArrayList<>();
            while (rs.next()) {
                concepts.add(createConceptSMTKFromResultSet(rs));
            }
            rs.close();
            call.close();

        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return concepts;
    }


    @Override
    public int countPerfectMatch(String pattern, Long[] categories, Long[] refsets, Boolean modeled) {
        int concepts=0;

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_concept.count_concept_perfect_match(?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection(); CallableStatement call =
            connection.prepareCall(sql)) {

            call.registerOutParameter (1, Types.NUMERIC);
            call.setString(2, pattern);

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

            if(modeled == null) {
                call.setNull(5, OracleTypes.NUMBER);
            }
            else {
                call.setBoolean(5, modeled);
            }

            call.execute();

            //ResultSet rs = call.getResultSet();
            concepts =  call.getInt(1);

            call.close();

        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return concepts;
    }

    @Override
    public int countTruncateMatch(String pattern, Long[] categories, Long[] refsets, Boolean modeled) {
        int concepts=0;

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_concept.count_concept_truncate_match(?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection(); CallableStatement call =
                connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, pattern);

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

            if(modeled == null) {
                call.setNull(5, OracleTypes.NUMBER);
            }
            else {
                call.setBoolean(5, modeled);
            }

            call.execute();

            //ResultSet rs = call.getResultSet();
            concepts =  call.getInt(1);

            call.close();

        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return concepts;
    }

    @Override
    public List<ConceptSMTK> getConceptsPaginated(Long categoryId, int pageSize, int pageNumber, Boolean modeled) {
        List<ConceptSMTK> concepts = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_concept.find_concept_by_categories_paginated(?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setArray(2, connection.unwrap(OracleConnection.class).createARRAY("STK.NUMBER_ARRAY", new Long[]{categoryId}));
            call.setInt(3, pageNumber);
            call.setInt(4, pageSize);

            if(modeled == null) {
                call.setNull(5, OracleTypes.NUMBER);
            }
            else {
                call.setBoolean(5, modeled);
            }

            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                ConceptSMTK e = createConceptSMTKFromResultSet(rs);
                concepts.add(e);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return concepts;
    }

    /**
     * Este método es responsable de crear un concepto SMTK a partir de un resultset.
     *
     * @param resultSet El resultset a partir del cual se obtienen los conceptos.
     * @return La lista de conceptos contenidos en el ResultSet.
     * @throws SQLException Se arroja si hay un problema SQL.
     */
    public ConceptSMTK createConceptSMTKFromResultSet(ResultSet resultSet) throws SQLException {

        long id;
        long idCategory;
        Category objectCategory;
        boolean check;
        boolean consult;
        boolean modeled;
        boolean completelyDefined;
        boolean published;
        String conceptId;
        boolean heritable = false;

        id = Long.valueOf(resultSet.getString("id"));
        conceptId = resultSet.getString("conceptid");

        /* Se recupera la categoría como objeto de negocio */
        idCategory = Long.valueOf(resultSet.getString("id_category"));
        //objectCategory = categoryDAO.getCategoryById(idCategory);
        objectCategory = CategoryFactory.getInstance().findCategoryById(idCategory);

        check = resultSet.getBoolean("is_to_be_reviewed");
        consult = resultSet.getBoolean("is_to_be_consultated");
        modeled = resultSet.getBoolean("is_modeled");
        completelyDefined = resultSet.getBoolean("is_fully_defined");
        published = resultSet.getBoolean("is_published");
        conceptId = resultSet.getString("conceptid");
        String observation = resultSet.getString("observation");
        long idTagSMTK = resultSet.getLong("id_tag_smtk");

        /**
         * Try y catch ignored porque no todas las funciones de la BD que recuperan Concepts de la BD traen esta
         * columna.
         * Ej: Usar la funcion semantikos.find_concepts_by_refset_paginated para recueprar conceptos se cae con la
         * excepcion:
         * org.postgresql.util.PSQLException: The column name is_inherited was not found in this ResultSet.
         */
        try {
            heritable = resultSet.getBoolean("is_inherited");
        } catch (Exception ignored) {
        }

        /* Se recupera su Tag Semántikos */
        //TagSMTK tagSMTKByID = tagSMTKDAO.findTagSMTKByID(idTagSMTK);
        TagSMTK tagSMTKByID = TagSMTKFactory.getInstance().findTagSMTKById(idTagSMTK);

        ConceptSMTK conceptSMTK = new ConceptSMTK(id, conceptId, objectCategory, check, consult, modeled,
                completelyDefined, heritable, published, observation, tagSMTKByID);

        /* Se recuperan las descripciones del concepto */
        conceptSMTK.setDescriptions(descriptionDAO.getDescriptionsByConcept(conceptSMTK));

        /* Se recuperan sus Etiquetas, solo si posee */
        /*
        if(resultSet.getLong("id_concept") != 0) {
            conceptSMTK.setTags(tagDAO.getTagsByConcept(conceptSMTK));
        }
        */
        conceptSMTK.setTags(tagDAO.getTagsByConcept(conceptSMTK));

        return conceptSMTK;
    }
}
