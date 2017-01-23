package cl.minsal.semantikos.kernel.daos;


import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
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

import static java.lang.System.currentTimeMillis;

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
        return this.findConceptsBy(new Long[]{categoryId}, true, pageSize, pageNumber);
    }

    @Override
    public List<ConceptSMTK> findConceptsBy(Long[] categories, boolean modeled, int pageSize, int pageNumber) {

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
    public List<ConceptSMTK> findConceptsBy(Category category, int pageSize, int pageNumber) {

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
    public List<ConceptSMTK> findConceptsBy(Category category) {

        logger.debug("ConceptDAO.findConceptsBy(" + category.getName() + ")");
        long init = currentTimeMillis();

        List<ConceptSMTK> concepts = new ArrayList<>();

        /* Esta funcion recupera los ID's de los conceptos de una categoría */
        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.find_concept_by_category(?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, category.getId());
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                long conceptId = rs.getLong("id");
                concepts.add(this.getConceptByID(conceptId));
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        logger.debug("ConceptDAO.findConceptsBy(" + category.getName() + "): " + concepts.size() + " conceptos " +
                "recuperados.");
        logger.debug("ConceptDAO.findConceptsBy(" + category.getName() + "): {}ms", currentTimeMillis()-init);
        return concepts;
    }

    @Override
    public List<ConceptSMTK> findConceptsBy(String[] pattern, boolean isModeled, int pageSize, int pageNumber) {
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
    public List<ConceptSMTK> findConceptsBy(String[] pattern, Long[] categories, boolean modeled, int pageSize, int
            pageNumber) {

        List<ConceptSMTK> concepts = new ArrayList<ConceptSMTK>();
        ConnectionBD connect = new ConnectionBD();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.find_concept_by_pattern_and_categories" +
                     "(?,?,?,?,?)}")) {

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
    public List<ConceptSMTK> findConceptsBy(String[] pattern, Long[] categories, Long[] refsets, boolean modeled, int
            pageSize, int pageNumber) {

        long init = currentTimeMillis();

        List<ConceptSMTK> concepts = new ArrayList<ConceptSMTK>();
        ConnectionBD connect = new ConnectionBD();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos" +
                     ".find_concept_by_pattern_and_categories_and_refsets(?,?,?,?,?,?)}")) {

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

        logger.debug("ConceptDAO.findConceptsBy(" + Arrays.toString(pattern) + ", " + Arrays.toString(categories) + ", " + Arrays.toString(refsets) + ", " + modeled + ", " + pageSize + ", " + pageNumber + ") => " + concepts);
        logger.debug("ConceptDAO.findConceptsBy(" + Arrays.toString(pattern) + ", " + Arrays.toString(categories) + ", " + Arrays.toString(refsets) + ", " + modeled + ", " + pageSize + ", " + pageNumber + ") : " + (currentTimeMillis() - init) + "ms");

        return concepts;
    }

    @Override
    public List<ConceptSMTK> findConceptsBy(String PatternOrConceptId, Long[] Category, int pageNumber, int pageSize,
                                            boolean isModeled) {

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
            call = connection.prepareCall("{call semantikos.count_concept_by_pattern_and_categories_and_refsets(?,?," +
                    "?,?)}");
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
    public ConceptSMTK findConceptsBy(Category category, long id) {
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
     * @return El concepto con la información básica contenida en el registro, y se le agregan sus tags. Esto no
     * incluye sus descripciones o relaciones.
     * @throws SQLException Se arroja si hay un problema SQL.
     */
    private ConceptSMTK createConceptSMTKFromResultSet(ResultSet resultSet) throws SQLException {

        long id = Long.valueOf(resultSet.getString("id"));
        long idCategory = Long.valueOf(resultSet.getString("id_category"));
        Category objectCategory;
        boolean check = resultSet.getBoolean("is_to_be_reviewed");
        boolean consult = resultSet.getBoolean("is_to_be_consultated");
        boolean modeled = resultSet.getBoolean("is_modeled");
        boolean completelyDefined = resultSet.getBoolean("is_fully_defined");
        boolean published = resultSet.getBoolean("is_published");
        String conceptId = resultSet.getString("conceptid");
        boolean heritable = resultSet.getBoolean("is_inherited");
        String observation = resultSet.getString("observation");
        long idTagSMTK = resultSet.getLong("id_tag_smtk");

        /* Se recupera la categoría como objeto de negocio */
        objectCategory = categoryDAO.getCategoryById(idCategory);

        /* Se recupera su Tag Semántikos */
        TagSMTK tagSMTKByID = tagSMTKDAO.findTagSMTKByID(idTagSMTK);
        ConceptSMTK conceptSMTK = new ConceptSMTK(id, conceptId, objectCategory, check, consult, modeled,
                completelyDefined, heritable, published, observation, tagSMTKByID);

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
    public List<ConceptSMTK> findConceptsBy(RefSet refSet) {

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
    public List<ConceptSMTK> findConceptsTruncatePerfectBy(String[] arrayPattern, boolean isModeled) {
        List<ConceptSMTK> concepts = new ArrayList<ConceptSMTK>();
        ConnectionBD connect = new ConnectionBD();
        CallableStatement call;

        try (Connection connection = connect.getConnection();) {
            call = connection.prepareCall("{call semantikos.search_perfect_truncate_concept_by_pattern(?,?)}");
            call.setArray(1, connection.createArrayOf("text", arrayPattern));
            call.setBoolean(2, isModeled);
            call.execute();

            ResultSet rs = call.getResultSet();
            concepts = new ArrayList<>();
            while (rs.next()) {
                concepts.add(createConceptSMTKFromResultSet(rs));
            }
            rs.close();

        } catch (SQLException e) {
            logger.error("Error al invocar 'search_perfect_truncate_concept_by_pattern'", e);
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
    public List<ConceptSMTK> findConceptsWithStringBasicType(Category aCategory, RelationshipDefinition
            stringBasicTypeAttribute, String aString) {

        ConnectionBD connect = new ConnectionBD();
        List<ConceptSMTK> conceptSMTKs = new ArrayList<>();

        /**
         * semantikos.find_concepts_by_attribute_boolean(?,?,?)
         * Param 1: Category ID.
         * Param 2: Relationship Attribute ID
         * Param 3: String value.
         */
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.find_concepts_by_attribute_string(?,?," +
                     "?)}")) {

            call.setLong(1, aCategory.getId());
            call.setLong(2, stringBasicTypeAttribute.getId());
            call.setString(3, aString);
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                ConceptSMTK aConcept = createConceptSMTKFromResultSet(rs);
                conceptSMTKs.add(aConcept);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al llamar la consulta find_concepts_by_attribute_string con parámetros: " +
                    aCategory.getId() + " " + stringBasicTypeAttribute.getId() + " " + stringBasicTypeAttribute.getId();
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return conceptSMTKs;
    }

    @Override
    public List<ConceptSMTK> findConceptsWithBooleanBasicType(Category aCategory, RelationshipDefinition
            booleanBasicTypeAttribute, Boolean aBoolean) {

        logger.debug("findConceptsWithBooleanBasicType(" + aCategory + ", " + booleanBasicTypeAttribute + ", " + aBoolean + ")");

        ConnectionBD connect = new ConnectionBD();
        List<ConceptSMTK> conceptSMTKs = new ArrayList<>();

        /**
         * semantikos.find_concepts_by_attribute_boolean(?,?,?)
         * Param 1: Category ID.
         * Param 2: Relationship Attribute ID
         * Param 3: String value.
         */
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.find_concepts_by_attribute_boolean(?,?,?)}")) {

            call.setLong(1, aCategory.getId());
            call.setLong(2, booleanBasicTypeAttribute.getId());
            call.setBoolean(3, aBoolean);
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                ConceptSMTK aConcept = createConceptSMTKFromResultSet(rs);
                conceptSMTKs.add(aConcept);
            }
            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al llamar la consulta 'find_concepts_by_attribute_boolean' con parámetros: " +
                    aCategory.getId() + " " + booleanBasicTypeAttribute.getId() + " " + booleanBasicTypeAttribute.getId() + ": " + e.getMessage();
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return conceptSMTKs;
    }

    @Override
    public List<ConceptSMTK> findConceptsWithStringBasicType(Category aCategory, ArrayList<RefSet> refsets,
                                                             RelationshipDefinition stringBasicTypeAttribute, String
                                                                     aString) {

        List<ConceptSMTK> conceptSMTKs = new ArrayList<>();
        Long[] theRefSetIDs = PersistentEntity.getIdArray(refsets);

        /**
         * semantikos.find_concepts_by_attribute_string_category_refset(?,?,?,?)
         * IN p_id_category bigint,
         * IN p_id_refset bigint[],
         * IN p_id_relationship_definition bigint,
         * IN p_string_value character varying
         *
         * Param 1: Category ID (bigint).
         * Param 2: Refsets (bigint[]).
         * Param 3: Relationship Attribute ID (bigint)
         * Param 4: String value character varying.
         */
        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos" +
                     ".find_concepts_by_attribute_string_category_refset(?,?,?,?)}")) {

            call.setLong(1, aCategory.getId());
            call.setArray(2, connection.createArrayOf("integer", theRefSetIDs));
            call.setLong(3, stringBasicTypeAttribute.getId());
            call.setString(4, aString);

            logger.debug("ConceptDAO.find_concepts_by_attribute_string.find_concepts_by_attribute_string(cat=" +
                    aCategory.getId() + ", refsets=" + refsets + ", attrType=" + stringBasicTypeAttribute.getId() +
                    ", val=" + aString + ")");
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                ConceptSMTK aConcept = createConceptSMTKFromResultSet(rs);
                conceptSMTKs.add(aConcept);
            }
            rs.close();
            logger.debug("ConceptDAO.find_concepts_by_attribute_string.find_concepts_by_attribute_string()=" +
                    conceptSMTKs);

        } catch (SQLException e) {
            String errorMsg = "Error al llamar la consulta find_concepts_by_attribute_string con parámetros: " +
                    aCategory.getId() + " " + stringBasicTypeAttribute.getId() + " " + stringBasicTypeAttribute.getId();
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }

        return conceptSMTKs;
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

        /* Se valida si ya fue recuperado y se recupera de lo guardado en memoria*/
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
        List<ConceptSMTK> specialConcepts = this.findConceptsBy(specialConceptCategory);

        for (ConceptSMTK specialConcept : specialConcepts) {
            /* Se agregan sus descripciones */
            specialConcept.setDescriptions(descriptionDAO.getDescriptionsByConcept(specialConcept));

            /* Se valida que el concepto tenga la descripcion preferida (para evitar una excepcion en caso de que la BDD este corrupta */
            Description descriptionFavorite;
            try{
                descriptionFavorite = specialConcept.getDescriptionFavorite();
            }

            /* Si no tiene se continua con el siguiente concepto */
            catch (BusinessRuleException bre){
                logger.error("El concepto " + specialConcept + " no posee descripción Preferida.");
                continue;
            }

            if (descriptionFavorite.getTerm().equalsIgnoreCase(PENDING_CONCEPT_FSN_DESCRIPTION)) {
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
}
