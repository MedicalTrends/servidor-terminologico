package cl.minsal.semantikos.kernel.daos;


import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Gusatvo Punucura on 13-07-16.
 */

@Stateless
public class ConceptDAOImpl implements ConceptDAO {

    /** El logger de esta clase */
    private static final Logger logger = LoggerFactory.getLogger(ConceptDAOImpl.class);

    private final static String PENDING_CONCEPT_FSN_DESCRIPTION = "Pendientes";

    /** Determina si el concepto pendiente ha sido recuperado desde el repositorio */
    private static boolean PENDING_CONCEPT_RETRIEVED = false;

    private static ConceptSMTK PENDING_CONCEPT;

    @PersistenceContext(unitName = "SEMANTIKOS_PU")
    private EntityManager em;

    @EJB
    private CategoryDAO categoryDAO;

    @EJB
    private DescriptionDAO descriptionDAO;

    /** El DAO para manejar relaciones del concepto */
    @EJB
    private RelationshipDAO relationshipDAO;

    @EJB
    private TargetDAO targetDAO;

    @EJB
    private TagSMTKDAO tagSMTKDAO;

    @EJB
    TagDAO tagDAO;

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
    public List<ConceptSMTK> getConceptsBy(boolean modeled, int pageSize, int pageNumber) {

        List<ConceptSMTK> concepts = new ArrayList<>();
        ConnectionBD connect = new ConnectionBD();
        CallableStatement call;

        try (Connection connection = connect.getConnection();) {

            call = connection.prepareCall("{call semantikos.get_all_concepts(?,?,?)}");
            call.setBoolean(1, modeled);
            call.setInt(2, pageNumber);
            call.setInt(3, pageSize);
            call.execute();

            ResultSet rs = call.getResultSet();

            while (rs.next()) {
                ConceptSMTK recoveredConcept = createConceptSMTKFromResultSet(rs);
                concepts.add(recoveredConcept);
            }
            rs.close();

        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return concepts;
    }

    @Override
    public List<ConceptSMTK> getModeledConceptBy(Long categoryId, int pageSize, int pageNumber) {
        return this.getConceptBy(new Long[]{categoryId}, true, pageSize, pageNumber);
    }

    @Override
    public List<ConceptSMTK> getConceptBy(Long[] categories, boolean modeled, int pageSize, int pageNumber) {

        List<ConceptSMTK> concepts = new ArrayList<>();
        ConnectionBD connect = new ConnectionBD();
        CallableStatement call;

        try (Connection connection = connect.getConnection();) {

            call = connection.prepareCall("{call semantikos.find_concept_by_categories(?,?,?,?)}");

            call.setArray(1, connect.getConnection().createArrayOf("integer", categories));
            call.setInt(2, pageNumber);
            call.setInt(3, pageSize);
            call.setBoolean(4, modeled);

            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                ConceptSMTK e = this.createConceptSMTKFromResultSet(rs);
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
    public List<ConceptSMTK> getConceptBy(Category category, int pageSize, int pageNumber) {

        List<ConceptSMTK> concepts = new ArrayList<>();
        ConnectionBD connect = new ConnectionBD();
        CallableStatement call;

        try (Connection connection = connect.getConnection();) {

            call = connection.prepareCall("{call semantikos.find_concept_by_category(?,?,?)}");

            call.setLong(1, category.getId());

            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                ConceptSMTK e = this.getConceptByID(rs.getLong(1));
                concepts.add(e);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return concepts;
    }

    @Override
    public List<ConceptSMTK> getConceptBy(Category category) {

        List<ConceptSMTK> concepts = new ArrayList<>();
        ConnectionBD connect = new ConnectionBD();
        CallableStatement call;

        try (Connection connection = connect.getConnection();) {

            call = connection.prepareCall("{call semantikos.find_concept_by_category(?)}");

            call.setLong(1, category.getId());

            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                ConceptSMTK e = this.getConceptByID(rs.getLong(1));
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
    public List<ConceptSMTK> getConceptBy(String[] pattern, boolean isModeled, int pageSize, int pageNumber) {
        List<ConceptSMTK> concepts = new ArrayList<ConceptSMTK>();
        ConnectionBD connect = new ConnectionBD();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.find_concept_by_pattern(?,?,?,?)}")) {

            call.setArray(1, connection.createArrayOf("text", pattern));
            call.setInt(2, pageNumber);
            call.setInt(3, pageSize);
            call.setBoolean(4, isModeled);
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                ConceptSMTK conceptSMTK = createConceptSMTKFromResultSet(rs);
                concepts.add(conceptSMTK);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return concepts;
    }

    @Override
    public List<ConceptSMTK> getConceptBy(String[] pattern, Long[] categories, boolean modeled, int pageSize, int pageNumber) {

        List<ConceptSMTK> concepts = new ArrayList<ConceptSMTK>();
        ConnectionBD connect = new ConnectionBD();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.find_concept_by_pattern_and_categories(?,?,?,?,?)}")) {

            Array ArrayCategories = connection.createArrayOf("integer", categories);

            call.setArray(1, ArrayCategories);
            call.setArray(2, connection.createArrayOf("text", pattern));
            call.setInt(3, pageNumber);
            call.setInt(4, pageSize);
            call.setBoolean(5, modeled);
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                ConceptSMTK conceptSMTK = createConceptSMTKFromResultSet(rs);
                concepts.add(conceptSMTK);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return concepts;
    }

    @Override
    public List<ConceptSMTK> getConceptBy(String[] pattern, Long[] categories, Long[] refsets, boolean modeled, int pageSize, int pageNumber) {
        List<ConceptSMTK> concepts = new ArrayList<ConceptSMTK>();
        ConnectionBD connect = new ConnectionBD();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.find_concept_by_pattern_and_categories_and_refsets(?,?,?,?,?,?)}")) {

            Array ArrayCategories = connection.createArrayOf("integer", categories);
            Array ArrayRefsets = connection.createArrayOf("integer", refsets);

            call.setArray(1, ArrayCategories);
            call.setArray(2, ArrayRefsets);
            call.setArray(3, connection.createArrayOf("text", pattern));
            call.setInt(4, pageNumber);
            call.setInt(5, pageSize);
            call.setBoolean(6, modeled);
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                ConceptSMTK conceptSMTK = createConceptSMTKFromResultSet(rs);
                concepts.add(conceptSMTK);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return concepts;
    }

    @Override
    public List<ConceptSMTK> getConceptBy(String PatternOrConceptId, Long[] Category, int pageNumber, int pageSize, boolean isModeled) {

        List<ConceptSMTK> concepts = new ArrayList<ConceptSMTK>();

        ConnectionBD connect = new ConnectionBD();


        CallableStatement call;

        // TODO: TryWithResources
        try (Connection connection = connect.getConnection();) {
            if (Category.length > 0) {
                call = connection.prepareCall("{call semantikos.find_concept_by_conceptid_categories(?,?,?,?,?)}");
                Array ArrayCategories = connection.createArrayOf("integer", Category);

                call.setString(1, PatternOrConceptId);
                call.setArray(2, ArrayCategories);
                call.setInt(3, pageNumber);
                call.setInt(4, pageSize);
                call.setBoolean(5, isModeled);
            } else {
                call = connection.prepareCall("{call semantikos.find_concept_by_concept_id(?,?,?,?)}");
                call.setString(1, PatternOrConceptId);
                call.setInt(2, pageNumber);
                call.setInt(3, pageSize);
                call.setBoolean(4, isModeled);
            }
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
        } finally {

        }

        return concepts;
    }

    @Override
    public int countConceptBy(String[] Pattern, Long[] category, boolean isModeled) {


        ConnectionBD connect = new ConnectionBD();
        CallableStatement call;
        int count = 0;

        try (Connection connection = connect.getConnection();) {

            if (Pattern != null) {

                Array ArrayPattern = connection.createArrayOf("text", Pattern);

                if (category.length > 0) {
                    call = connection.prepareCall("{call semantikos.count_concept_by_pattern_and_categories(?,?,?)}");
                    Array ArrayCategories = connection.createArrayOf("integer", category);

                    call.setArray(1, ArrayCategories);
                    call.setArray(2, ArrayPattern);
                    call.setBoolean(3, isModeled);

                } else {
                    call = connection.prepareCall("{call semantikos.count_concept_by_pattern(?,?)}");
                    call.setArray(1, ArrayPattern);
                    call.setBoolean(2, isModeled);
                }

            } else {

                if (category.length > 0) {
                    call = connection.prepareCall("{call semantikos.count_concept_count_by_categories(?,?)}");
                    Array ArrayCategories = connect.getConnection().createArrayOf("integer", category);
                    call.setArray(1, ArrayCategories);
                    call.setBoolean(2, isModeled);

                } else {
                    call = connection.prepareCall("{call semantikos.count_concept_by_page_size(?)}");
                    call.setBoolean(1, isModeled);
                }
            }

            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                count = Integer.parseInt(rs.getString("count"));
            }
            rs.close();

        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }


        return count;
    }

    @Override
    public Integer countConceptBy(String[] Pattern, Long[] category, Long[] refset, boolean isModeled) {
        ConnectionBD connect = new ConnectionBD();
        CallableStatement call;
        int count = 0;

        try (Connection connection = connect.getConnection();) {

            Array ArrayPattern = connection.createArrayOf("text", Pattern);
            call = connection.prepareCall("{call semantikos.count_concept_by_pattern_and_categories_and_refsets(?,?,?,?)}");
            Array ArrayCategories = connection.createArrayOf("integer", category);
            Array ArrayRefSets = connection.createArrayOf("integer", refset);

            call.setArray(1, ArrayCategories);
            call.setArray(2, ArrayRefSets);
            call.setArray(3, ArrayPattern);
            call.setBoolean(4, isModeled);
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                count = Integer.parseInt(rs.getString("count"));
            }
            rs.close();

        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return count;
    }

    @Override
    public int countConceptBy(String Pattern, Long[] category, boolean isModeled) {
        ConnectionBD connect = new ConnectionBD();
        int count = 0;


        String COUNT_WITH_CATEGORIES = "{call semantikos.count_concept_by_conceptid_categories(?,?,?)}";
        String COUNT_WITHOUT_CATEGORIES = "{call semantikos.count_concept_by_concept_id(?,?)}";
        String COUNT_CALL = (category.length > 0) ? COUNT_WITH_CATEGORIES : COUNT_WITHOUT_CATEGORIES;

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(COUNT_CALL)) {

            call.setString(1, Pattern);
            call.setBoolean(2, isModeled);
            if (category.length > 0) {
                call.setArray(3, connect.getConnection().createArrayOf("integer", category));
            }

            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                count = Integer.parseInt(rs.getString("count"));
            }

            rs.close();

        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }
        return count;
    }

    @Override
    public int countModeledConceptBy(Long categoryId) {
        return this.countConceptBy((String[]) null, new Long[]{categoryId}, true);
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
        ConnectionBD connect = new ConnectionBD();

        String sql = "{call semantikos.get_concept_by_id(?)}";
        ConceptSMTK conceptSMTK;
        try (Connection connection = connect.getConnection();
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
    public ConceptSMTK getConceptBy(Category category, long id) {
        return null;
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
     *
     * @return La lista de conceptos contenidos en el ResultSet.
     *
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
        boolean heritable = false;
        String conceptId;

        id = Long.valueOf(resultSet.getString("id"));
        idCategory = Long.valueOf(resultSet.getString("id_category"));
        objectCategory = categoryDAO.getCategoryById(idCategory);
        check = resultSet.getBoolean("is_to_be_reviewed");
        consult = resultSet.getBoolean("is_to_be_consultated");
        modeled = resultSet.getBoolean("is_modeled");
        completelyDefined = resultSet.getBoolean("is_fully_defined");
        published = resultSet.getBoolean("is_published");
        try {
            heritable = resultSet.getBoolean("is_inherited");
        } catch ( Exception ignored ) { }
        conceptId = resultSet.getString("conceptid");
        String observation = resultSet.getString("observation");
        long idTagSMTK = resultSet.getLong("id_tag_smtk");


        /* Se recupera su Tag Semántikos */
        TagSMTK tagSMTKByID = tagSMTKDAO.findTagSMTKByID(idTagSMTK);
        ConceptSMTK conceptSMTK = new ConceptSMTK(id, conceptId, objectCategory, check, consult, modeled, completelyDefined, heritable, published, observation, tagSMTKByID);

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
                String errorMsg = "El concepto no fue creado por una razon desconocida. Alertar al area de desarrollo sobre esto";
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
                String errorMsg = "El concepto no fue creado por una razón desconocida. Alertar al area de desarrollo sobre esto";
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
            logger.info("Información de concepto (CONCEPT_ID=" + conceptSMTK.getConceptID() + ") actualizada exitosamente.");
        } else {
            String errorMsg = "Información de concepto (CONCEPT_ID=" + conceptSMTK.getConceptID() + ") no fue actualizada.";
            logger.error(errorMsg);
            throw new EJBException(errorMsg);
        }
    }

    @Override
    public List<ConceptSMTK> getConceptBy(RefSet refSet) {

        List<ConceptSMTK> concepts = new ArrayList<ConceptSMTK>();

        ConnectionBD connect = new ConnectionBD();


        CallableStatement call;

        try (Connection connection = connect.getConnection();) {

            call = connection.prepareCall("{call semantikos.get_concept_by_refset(?)}");
            call.setLong(1, refSet.getId());
            call.execute();

            ResultSet rs = call.getResultSet();
            concepts = new ArrayList<>();
            while (rs.next()) {
                concepts.add(createConceptSMTKFromResultSet(rs));
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return concepts;
    }

    @Override
    public List<ConceptSMTK> findModeledConceptsBy(RefSet refSet, int page, int pageSize) {
        List<ConceptSMTK> concepts = new ArrayList<ConceptSMTK>();
        ConnectionBD connect = new ConnectionBD();
        CallableStatement call;

        try (Connection connection = connect.getConnection();) {
            call = connection.prepareCall("{call semantikos.find_concepts_by_refset_paginated(?,?,?,?)}");
            call.setLong(1, refSet.getId());
            call.setInt(2, page);
            call.setInt(3, pageSize);
            call.setBoolean(4, Boolean.TRUE);
            call.execute();

            ResultSet rs = call.getResultSet();
            concepts = new ArrayList<>();
            while (rs.next()) {
                concepts.add(createConceptSMTKFromResultSet(rs));
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return concepts;
    }

    @Override
    public Integer countModeledConceptsBy(RefSet refSet) {
        ConnectionBD connect = new ConnectionBD();
        CallableStatement call;
        int count = 0;

        try (Connection connection = connect.getConnection();) {
            call = connection.prepareCall("{call semantikos.count_concepts_by_refset(?,?)}");
            call.setLong(1, refSet.getId());
            call.setBoolean(2, Boolean.TRUE);
            call.execute();
            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                count = Integer.parseInt(rs.getString("count"));
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    @Override
    public List<ConceptSMTK> getConceptDraft() {

        List<ConceptSMTK> concepts = new ArrayList<ConceptSMTK>();
        ConnectionBD connect = new ConnectionBD();


        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.get_concept_draft()}")) {

            call.execute();

            ResultSet rs = call.getResultSet();
            concepts = new ArrayList<>();
            while (rs.next()) {
                concepts.add(createConceptSMTKFromResultSet(rs));
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return concepts;

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
        return getConceptByID(81239); // Desarrollo & Testing
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
            specialConceptCategory = categoryDAO.getCategoryByName("Concepto Especial");
        } catch (IllegalArgumentException iae) {
            String errorMsg = "No se encontró la categoría Especial!";
            logger.error(errorMsg, iae);
            throw new EJBException(errorMsg, iae);
        }

        /* Luego se recuperan los conceptos de la categoría y se busca por el que tenga el FSN adecuado */
        List<ConceptSMTK> specialConcepts = this.getConceptBy(specialConceptCategory);
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
             CallableStatement call= connection.prepareCall("{call semantikos.get_related_concept(?)}")) {

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
             CallableStatement call= connection.prepareCall("{call semantikos.get_concepts_id()}")) {

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
}
