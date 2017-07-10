package cl.minsal.semantikos.kernel.daos;


import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.kernel.util.DataSourceFactory;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.categories.CategoryFactory;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.tags.Tag;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;
import cl.minsal.semantikos.model.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gusatvo Punucura on 13-07-16.
 */

@Stateless
public class ConceptDAOImpl implements ConceptDAO {

    /**
     * El logger de esta clase
     */
    private static final Logger logger = LoggerFactory.getLogger(ConceptDAOImpl.class);

    private final static String PENDING_CONCEPT_FSN_DESCRIPTION = "Pendientes";

    /**
     * Determina si el concepto pendiente ha sido recuperado desde el repositorio
     */
    private static boolean PENDING_CONCEPT_RETRIEVED = false;

    private static ConceptSMTK PENDING_CONCEPT;

    @PersistenceContext(unitName = "SEMANTIKOS_PU")
    private EntityManager em;

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

    @Override
    public void delete(ConceptSMTK conceptSMTK) {

        /* Esto aplica sólo si el concepto no está persistido */
        if (!conceptSMTK.isPersistent()) {
            return;
        }

        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.delete_concept(?)}")) {

            call.setLong(1, conceptSMTK.getId());
            call.execute();
        } catch (SQLException e) {
            String errorMessage = "No se pudo eliminar el concepto: " + conceptSMTK.toString();
            logger.error(errorMessage, e);
            throw new EJBException(errorMessage, e);
        }
    }

    @Override
    public ConceptSMTK getConceptByCONCEPT_ID(String conceptID) {
        ConnectionBD connect = new ConnectionBD();

        String sql = "{call semantikos.get_concept_by_conceptid(?)}";
        ConceptSMTK conceptSMTK;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setString(1, conceptID);
            call.execute();

            ResultSet rs = call.getResultSet();
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

        String sql = "{call semantikos.get_concept_by_id(?)}";
        ConceptSMTK conceptSMTK;
        try (Connection connection = DataSourceFactory.getInstance().getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, id);
            call.execute();

            ResultSet rs = call.getResultSet();

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
        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.find_concepts_by_tag(?)}")) {

            call.setLong(1, tag.getId());
            call.execute();

            ResultSet rs = call.getResultSet();
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

    /**
     * Este método es responsable de crear un concepto SMTK a partir de un resultset.
     *
     * @param resultSet El resultset a partir del cual se obtienen los conceptos.
     * @return La lista de conceptos contenidos en el ResultSet.
     * @throws SQLException Se arroja si hay un problema SQL.
     */
    private ConceptSMTK createConceptSMTKFromResultSet(ResultSet resultSet) throws SQLException {

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
        List<Description> descriptions = descriptionDAO.getDescriptionsByConcept(conceptSMTK);

        conceptSMTK.setDescriptions(descriptions);

        /* Se recuperan sus Etiquetas */
        conceptSMTK.setTags(tagDAO.getTagsByConcept(id));

        return conceptSMTK;
    }

    @Override
    public void persistConceptAttributes(ConceptSMTK conceptSMTK, User user) {

        ConnectionBD connect = new ConnectionBD();
        long id;
        String sql = "{call semantikos.create_concept(?,?,?,?,?,?,?,?,?,?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setString(1, conceptSMTK.getConceptID());
            call.setLong(2, conceptSMTK.getCategory().getId());
            call.setBoolean(3, conceptSMTK.isToBeReviewed());
            call.setBoolean(4, conceptSMTK.isToBeConsulted());
            call.setBoolean(5, conceptSMTK.isModeled());
            call.setBoolean(6, conceptSMTK.isFullyDefined());
            call.setBoolean(7, conceptSMTK.isInherited());
            call.setBoolean(8, conceptSMTK.isPublished());
            call.setString(9, conceptSMTK.getObservation());
            call.setLong(10, conceptSMTK.getTagSMTK().getId());
            call.execute();

            ResultSet rs = call.getResultSet();

            if (rs.next()) {
                /* Se recupera el ID del concepto persistido */
                id = rs.getLong(1);
                conceptSMTK.setId(id);
            } else {
                String errorMsg = "El concepto no fue creado por una razon desconocida. Alertar al area de desarrollo" +
                        " sobre esto";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            rs.close();
        } catch (SQLException e) {
            String errorMsg = "El concepto " + conceptSMTK.toString() + " no fue persistido.";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

    }

    @Override
    public void update(ConceptSMTK conceptSMTK) {

        logger.info("Actualizando información básica de concepto: " + conceptSMTK.toString());
        ConnectionBD connect = new ConnectionBD();
        long updated;
        String sql = "{call semantikos.update_concept(?,?,?,?,?,?,?,?,?,?,?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, conceptSMTK.getId());
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

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                /* Se recupera el ID del concepto persistido */
                updated = rs.getLong(1);
            } else {
                String errorMsg = "El concepto no fue creado por una razón desconocida. Alertar al area de desarrollo" +
                        " sobre esto";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            rs.close();
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
        ConnectionBD connect = new ConnectionBD();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.force_modeled_concept(?)}")) {

            call.setLong(1, idConcept);
            call.execute();
        } catch (SQLException e) {
            logger.error("Error al tratar de modelar un concepto.", e);
        }
    }

    @Override
    public ConceptSMTK getNoValidConcept() {
        // TODO: Parametrizar esto
        return getConceptByID(100); // Desarrollo & Testing
    }

    @Override
    public ConceptSMTK getPendingConcept() {

        /* Se valida si ya fue recuperado */
        if (PENDING_CONCEPT_RETRIEVED) {
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
        List<ConceptSMTK> specialConcepts = this.findPerfectMatchConcept(PENDING_CONCEPT_FSN_DESCRIPTION, new long[]{specialConceptCategory.getId()}, null, true);
        for (ConceptSMTK specialConcept : specialConcepts) {
            if (specialConcept.getDescriptionFavorite().getTerm().equalsIgnoreCase(PENDING_CONCEPT_FSN_DESCRIPTION)) {
                PENDING_CONCEPT = specialConcept;
                PENDING_CONCEPT_RETRIEVED = true;
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

        ConnectionBD connect = new ConnectionBD();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.get_related_concept(?)}")) {

            call.setLong(1, conceptSMTK.getId());
            call.execute();

            ResultSet rs = call.getResultSet();
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
    public List<Long> getAllConceptsId() {
        List<Long> ids = new ArrayList<>();

        ConnectionBD connect = new ConnectionBD();


        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.get_concepts_id()}")) {

            call.execute();
            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                ids.add(rs.getLong(1));
            }
            rs.close();

        } catch (SQLException e) {
            logger.error("Error al buscar conceptos relacionados", e);
        }

        return ids;
    }

    @Override
    public List<ConceptSMTK> findTruncateMatchConcept(String pattern, Long[] categories, Long[] refsets, boolean modeled, int pageSize, int pageNumber) {
        List<ConceptSMTK> concepts;

        ConnectionBD connect = new ConnectionBD();

        String QUERY_TRUNCATE_MATCH = "{call semantikos.find_concept_truncate_match(?,?,?,?,?,?)}";

        try (Connection connection = connect.getConnection(); CallableStatement call =
                connection.prepareCall(QUERY_TRUNCATE_MATCH)) {

            call.setString(1, pattern);
            call.setInt(2, pageNumber);
            call.setInt(3, pageSize);
            call.setBoolean(4, modeled);
            call.execute();

            ResultSet rs = call.getResultSet();
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
    public List<ConceptSMTK> findPerfectMatchConcept(String pattern, long[] categories, Long[] refsets, boolean modeled) {
        List<ConceptSMTK> concepts;

        ConnectionBD connect = new ConnectionBD();

        String QUERY_TRUNCATE_MATCH = "{call semantikos.find_concept_perfect_match(?,?,?,?,?,?)}";

        try (Connection connection = connect.getConnection(); CallableStatement call =
                connection.prepareCall(QUERY_TRUNCATE_MATCH)) {

            call.setString(1, pattern);
            call.setArray(2,  connect.getConnection().createArrayOf("bigint",categories));
            call.setArray(3,  connect.getConnection().createArrayOf("bigint",refsets));
            call.setBoolean(4, modeled);
            call.execute();

            ResultSet rs = call.getResultSet();
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
    public int countPerfectMatchConceptBy(String pattern, Long[] categories, boolean modeled) {
        int concepts=0;

        ConnectionBD connect = new ConnectionBD();

        String QUERY_PERFECT_MATCH_WITH_CATEGORIES = "{call semantikos.count_perfect_match_pattern_and_categories(?,?,?)}";
        String QUERY_PERFECT_MATCH_WITHOUT_CATEGORIES = "{call semantikos.count_perfect_match_pattern(?,?)}";
        String QUERY=(categories.length>0)?QUERY_PERFECT_MATCH_WITH_CATEGORIES:QUERY_PERFECT_MATCH_WITHOUT_CATEGORIES;

        try (Connection connection = connect.getConnection(); CallableStatement call =
                connection.prepareCall(QUERY)) {

            if(categories.length>0){
                Array ArrayCategories = connection.createArrayOf("integer", categories);
                call.setArray(1, ArrayCategories);
                call.setString(2, pattern);
                call.setBoolean(3, modeled);
            }else{
                call.setString(1, pattern);
                call.setBoolean(2, modeled);
            }
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                concepts = Integer.parseInt(rs.getString("count"));
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
    public int countTruncateMatchConceptBy(String pattern, Long[] categories, boolean modeled) {
        int concepts=0;

        ConnectionBD connect = new ConnectionBD();
        String QUERY_TRUNCATE_MATCH_WITH_CATEGORIES = "{call semantikos.count_truncate_match_by_pattern_and_categories(?,?,?)}";
        String QUERY_TRUNCATE_MATCH_WITHOUT_CATEGORIES = "{call semantikos.count_truncate_match_by_pattern(?,?)}";
        String QUERY=(categories.length>0)?QUERY_TRUNCATE_MATCH_WITH_CATEGORIES:QUERY_TRUNCATE_MATCH_WITHOUT_CATEGORIES;

        try (Connection connection = connect.getConnection(); CallableStatement call =
                connection.prepareCall(QUERY)) {

            if(categories.length>0){
                Array ArrayCategories = connection.createArrayOf("integer", categories);
                call.setArray(1, ArrayCategories);
                call.setString(2, pattern);
                call.setBoolean(3, modeled);
            }else{
                call.setString(1, pattern);
                call.setBoolean(2, modeled);
            }
            call.execute();


            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                concepts = Integer.parseInt(rs.getString("count"));
            }
            rs.close();
            call.close();

        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return concepts;
    }
}
