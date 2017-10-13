package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.singletons.CategorySingleton;
import cl.minsal.semantikos.kernel.singletons.DescriptionTypeSingleton;
import cl.minsal.semantikos.kernel.singletons.UserSingleton;
import cl.minsal.semantikos.kernel.util.StringUtils;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.categories.CategoryFactory;
import cl.minsal.semantikos.model.descriptions.*;
import cl.minsal.semantikos.model.dtos.ConceptDTO;
import cl.minsal.semantikos.model.dtos.DescriptionDTO;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.model.users.UserFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.CLOB;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.*;
import java.util.*;
import java.util.concurrent.Future;

import static cl.minsal.semantikos.model.DAO.NON_PERSISTED_ID;
import static java.lang.System.currentTimeMillis;
import static java.sql.ResultSet.CLOSE_CURSORS_AT_COMMIT;
import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.TYPE_FORWARD_ONLY;
import static java.sql.Types.TIMESTAMP;

/**
 * @author Andres Farias.
 */
@Stateless
public class WSDAOImpl implements WSDAO {

    /** El logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(WSDAOImpl.class);

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    private ObjectMapper mapper = new ObjectMapper();

    @EJB
    private CategorySingleton categorySingleton;

    @EJB
    private DescriptionTypeSingleton descriptionTypeSingleton;

    @EJB
    private UserSingleton userSingleton;

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Description> searchDescriptionsPerfectMatch(String term, Long[] categories, Long[] refsets, int page, int pageSize) throws IOException {

        /* Se registra el tiempo de inicio */
        //long init = currentTimeMillis();

        List<Description> descriptions = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_ws.search_descriptions_perfect_match_json(?,?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql,TYPE_FORWARD_ONLY, CONCUR_READ_ONLY/*, CLOSE_CURSORS_AT_COMMIT*/)) {

            connection.setAutoCommit(true);
            connection.setReadOnly(true);

            //call.registerOutParameter (1, OracleTypes.LONGVARCHAR);
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

            call.setFetchSize(500);

            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                descriptions.add(createDescriptionFromResultSet(rs));
                //descriptions.add(mapper.readValue(StringUtils.cleanJSON(rs.getString("json_object")), Description.class));
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar descripciones de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        }

        //float time = (float) (currentTimeMillis() - init);

        //logger.info("ws-req-001: {}s", String.format("%.2f", time));

        //logger.info("searchDescriptionsByTerm(" + term + ", " + categories + ", " + refsets + "): " + descriptions);
        //logger.info("searchDescriptionsByTerm(" + term + ", " + categories + ", " + refsets + "): {}s", String.format("%.2f", (currentTimeMillis() - init)/1000.0));
        return descriptions;
    }


    public Description createDescriptionFromResultSet(ResultSet resultSet) throws SQLException, IOException {

        String jsonObject = resultSet.getString("json_object");

        DescriptionDTO descriptionDTO = mapper.readValue(StringUtils.cleanJSON(jsonObject), DescriptionDTO.class);

        long id = descriptionDTO.getId();
        String descriptionID = descriptionDTO.getDescriptionID();
        long idDescriptionType = descriptionDTO.getIdDescriptionType();
        String term = descriptionDTO.getTerm();
        boolean isCaseSensitive = descriptionDTO.isCaseSensitive();
        boolean isAutoGenerated = descriptionDTO.isAutogeneratedName();
        boolean isPublished = descriptionDTO.isPublished();
        boolean isModeled = descriptionDTO.isModeled();
        Timestamp validityUntil = descriptionDTO.getValidityUntil();
        Timestamp creationDate = descriptionDTO.getCreationDate();
        long uses = descriptionDTO.getUses();

        long idUser = descriptionDTO.getIdUser();

        //User user = UserFactory.getInstance().findUserById(idUser);//authDAO.getUserById();
        User user = userSingleton.findUserById(idUser);

        ConceptDTO conceptDTO = descriptionDTO.getConceptDTO();

        ConceptSMTK conceptSMTK = createConceptFromDTO(conceptDTO);

        //DescriptionType descriptionType = DescriptionTypeFactory.getInstance().getDescriptionTypeByID(idDescriptionType);
        DescriptionType descriptionType = descriptionTypeSingleton.getDescriptionTypeByID(idDescriptionType);

        Description description = new Description(id, conceptSMTK, descriptionID, descriptionType, term, uses,
                isCaseSensitive, isAutoGenerated, isPublished,
                validityUntil, creationDate, user, isModeled);

        return description;
    }

    public ConceptSMTK createConceptFromDTO(ConceptDTO conceptDTO) {
        boolean heritable = false;

        long id = conceptDTO.getId();
        /* Se recupera la categoría como objeto de negocio */
        long idCategory = conceptDTO.getIdCategory();
        //objectCategory = categoryDAO.getCategoryById(idCategory);
        //Category objectCategory = CategoryFactory.getInstance().findCategoryById(idCategory);
        Category objectCategory = categorySingleton.findCategoryById(idCategory);

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
        conceptSMTK.setDescriptions(createDescriptionsFromDTO(conceptDTO.getDescriptionsDTO(), conceptSMTK));

        /* Se recuperan sus Etiquetas, solo si posee */
        /*
        if(resultSet.getLong("id_concept") != 0) {
            conceptSMTK.setTags(tagDAO.getTagsByConcept(conceptSMTK));
        }
        */
        conceptSMTK.setTags(conceptDTO.getTags());

        return conceptSMTK;
    }

    public List<Description> createDescriptionsFromDTO(List<DescriptionDTO> descriptionsDTO, ConceptSMTK conceptSMTK) {

        List<Description> descriptions = new ArrayList<>();

        for (DescriptionDTO descriptionDTO : descriptionsDTO) {

            long id = descriptionDTO.getId();
            String descriptionID = descriptionDTO.getDescriptionID();
            long idDescriptionType = descriptionDTO.getIdDescriptionType();
            String term = descriptionDTO.getTerm();
            boolean isCaseSensitive = descriptionDTO.isCaseSensitive();
            boolean isAutoGenerated = descriptionDTO.isAutogeneratedName();
            boolean isPublished = descriptionDTO.isPublished();
            boolean isModeled = descriptionDTO.isModeled();
            Timestamp validityUntil = descriptionDTO.getValidityUntil();
            Timestamp creationDate = descriptionDTO.getCreationDate();
            long uses = descriptionDTO.getUses();

            long idUser = descriptionDTO.getIdUser();

            //User user = UserFactory.getInstance().findUserById(idUser);
            User user = userSingleton.findUserById(idUser);

            //DescriptionType descriptionType = DescriptionTypeFactory.getInstance().getDescriptionTypeByID(idDescriptionType);
            DescriptionType descriptionType = descriptionTypeSingleton.getDescriptionTypeByID(idDescriptionType);

            Description description = new Description(id, conceptSMTK, descriptionID, descriptionType, term, uses,
                    isCaseSensitive, isAutoGenerated, isPublished,
                    validityUntil, creationDate, user, isModeled);

            descriptions.add(description);
        }

        return descriptions;
    }

}
