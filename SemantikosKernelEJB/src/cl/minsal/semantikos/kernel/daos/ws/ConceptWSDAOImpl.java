package cl.minsal.semantikos.kernel.daos.ws;

import cl.minsal.semantikos.kernel.daos.TagDAO;
import cl.minsal.semantikos.kernel.util.StringUtils;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.categories.CategoryFactory;
import cl.minsal.semantikos.model.dtos.ConceptDTO;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.currentTimeMillis;

/**
 * Created by des01c7 on 03-11-17.
 */
@Stateless
public class ConceptWSDAOImpl implements ConceptWSDAO {

    /** El logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(ConceptWSDAOImpl.class);

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    @EJB
    TagDAO tagDAO;

    @EJB
    DescriptionWSDAO descriptionDAO;

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public ConceptSMTK getConceptByID(long id) {
        //ConnectionBD connect = new ConnectionBD();
        /* Se registra el tiempo de inicio */
        long init = currentTimeMillis();

        String sql = "begin ? := stk.stk_pck_ws.get_concept_by_id_json(?); end;";

        ConceptSMTK conceptSMTK;

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.VARCHAR);
            call.setLong(2, id);
            call.execute();

            //ResultSet rs = call.getResultSet();
            String jsonObject = (String) call.getObject(1);

            if (jsonObject != null) {
                ConceptDTO conceptDTO = mapper.readValue(StringUtils.cleanJSON(jsonObject), ConceptDTO.class);

                conceptSMTK = createConceptFromDTO(conceptDTO);
            } else {
                String errorMsg = "No existe un concepto con CONCEPT_ID=" + id;
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }

        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        } catch (IOException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return conceptSMTK;
    }

    @Override
    public List<ConceptSMTK> getConceptsPaginated(Long categoryId, int pageSize, int pageNumber) {
        List<ConceptSMTK> concepts = new ArrayList<>();
        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_ws.find_concept_by_categories_paginated_json(?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setArray(2, connection.unwrap(OracleConnection.class).createARRAY("STK.NUMBER_ARRAY", new Long[]{categoryId}));
            call.setInt(3, pageNumber);
            call.setInt(4, pageSize);
            call.setBoolean(5, true);

            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                ConceptSMTK e = createConceptFromResultSet(rs);
                concepts.add(e);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        } catch (IOException e) {
            String errorMsg = "Error al recuperar descripciones de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        return concepts;
    }

    @Override
    public List<ConceptSMTK> getRelatedConcepts(ConceptSMTK conceptSMTK) {

        List<ConceptSMTK> concepts = new ArrayList<>();

        //ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_ws.get_related_concepts_json(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, conceptSMTK.getId());
            call.execute();

            //ResultSet rs = call.getResultSet();
            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                concepts.add(createConceptFromResultSet(rs));
            }
            rs.close();

        } catch (SQLException e) {
            logger.error("Error al buscar conceptos relacionados", e);
            throw new EJBException(e);
        } catch (IOException e) {
            logger.error("Error al buscar conceptos relacionados", e);
            throw new EJBException(e);
        }

        return concepts;
    }

    public ConceptSMTK createConceptFromResultSet(ResultSet resultSet) throws SQLException, IOException {

        String jsonObject = resultSet.getString("json_object");

        ConceptDTO conceptDTO = mapper.readValue(StringUtils.cleanJSON(jsonObject), ConceptDTO.class);

        ConceptSMTK conceptSMTK = createConceptFromDTO(conceptDTO);

        return conceptSMTK;
    }

    public ConceptSMTK createConceptFromDTO(ConceptDTO conceptDTO) {
        boolean heritable = false;

        long id = conceptDTO.getId();
        /* Se recupera la categoría como objeto de negocio */
        long idCategory = conceptDTO.getIdCategory();
        //objectCategory = categoryDAO.getCategoryById(idCategory);
        Category objectCategory = CategoryFactory.getInstance().findCategoryById(idCategory);

        boolean check = conceptDTO.isToBeReviewed();
        boolean consult = conceptDTO.isToBeConsulted();
        boolean modeled = conceptDTO.isModeled();
        boolean completelyDefined = conceptDTO.isFullyDefined();
        boolean published = conceptDTO.isPublished();
        String conceptId = conceptDTO.getConceptID();
        String observation = conceptDTO.getObservation();
        long idTagSMTK = conceptDTO.getIdTagSMTK();

        /**
         * Try y catch ignored porque no todas las funciones de la BD que recuperan Concepts de la BD traen esta
         * columna.
         * Ej: Usar la funcion semantikos.find_concepts_by_refset_paginated para recueprar conceptos se cae con la
         * excepcion:
         * org.postgresql.util.PSQLException: The column name is_inherited was not found in this ResultSet.
         */
        try {
            heritable = conceptDTO.isInherited();
        } catch (Exception ignored) {
        }

        /* Se recupera su Tag Semántikos */
        //TagSMTK tagSMTKByID = tagSMTKDAO.findTagSMTKByID(idTagSMTK);
        TagSMTK tagSMTKByID = TagSMTKFactory.getInstance().findTagSMTKById(idTagSMTK);

        ConceptSMTK conceptSMTK = new ConceptSMTK(id, conceptId, objectCategory, check, consult, modeled,
                completelyDefined, heritable, published, observation, tagSMTKByID);

        /* Se recuperan las descripciones del concepto */
        conceptSMTK.setDescriptions(descriptionDAO.createDescriptionsFromDTO(conceptDTO.getDescriptionsDTO(), conceptSMTK));

        /* Se recuperan sus Etiquetas, solo si posee */
        /*
        if(resultSet.getLong("id_concept") != 0) {
            conceptSMTK.setTags(tagDAO.getTagsByConcept(conceptSMTK));
        }
        */
        //conceptSMTK.setTags(conceptDTO.getTags());
        if(conceptDTO.isHasTags()) {
            conceptSMTK.setTags(tagDAO.getTagsByConcept(conceptSMTK));
        }

        return conceptSMTK;
    }
}
