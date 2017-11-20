package cl.minsal.semantikos.kernel.daos.ws;

import cl.minsal.semantikos.kernel.daos.TagDAO;
import cl.minsal.semantikos.kernel.util.StringUtils;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.descriptions.*;
import cl.minsal.semantikos.model.dtos.ConceptDTO;
import cl.minsal.semantikos.model.dtos.DescriptionDTO;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.model.users.UserFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import static java.lang.System.currentTimeMillis;

/**
 * @author Andres Farias.
 */
@Stateless
public class DescriptionWSDAOImpl implements DescriptionWSDAO {

    /** El logger para esta clase */
    private static final Logger logger = LoggerFactory.getLogger(DescriptionWSDAOImpl.class);

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    ObjectMapper mapper = new ObjectMapper();

    @EJB
    TagDAO tagDAO;

    @EJB
    ConceptWSDAO conceptDAO;

    @Override
    //@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Description> searchDescriptionsPerfectMatch(String term, Long[] categories, Long[] refsets, int page, int pageSize) {

        /* Se registra el tiempo de inicio */
        //long init = currentTimeMillis();

        List<Description> descriptions = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_ws.search_descriptions_perfect_match_json(?,?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            //connection.setAutoCommit(true);
            //connection.setReadOnly(true);

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

            //call.setFetchSize(500);

            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                descriptions.add(createDescriptionFromResultSet(rs));
                //descriptions.add(mapper.readValue(StringUtils.cleanJSON(rs.getString("json_object")), Description.class));
            }

            rs.close();

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar descripciones de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        } catch (IOException e) {
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

    @Override
    public List<Description> searchDescriptionsTruncateMatch(String term, Long[] categories, Long[] refsets, int page, int pageSize) {

        List<Description> descriptions = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_ws.search_descriptions_truncate_match_json(?,?,?,?,?); end;";

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
                descriptions.add(createDescriptionFromResultSet(rs));
            }

        } catch (SQLException e) {
            String errorMsg = "Error al recuperar descripciones de la BDD.";
            logger.error(errorMsg, e);
            throw new EJBException(e);
        } catch (IOException e) {
            String errorMsg = "Error al recuperar descripciones de la BDD.";
            logger.error(errorMsg, e);
        }

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

        User user = UserFactory.getInstance().findUserById(idUser);//authDAO.getUserById();

        ConceptDTO conceptDTO = descriptionDTO.getConceptDTO();

        ConceptSMTK conceptSMTK = conceptDAO.createConceptFromDTO(conceptDTO);

        DescriptionType descriptionType = DescriptionTypeFactory.getInstance().getDescriptionTypeByID(idDescriptionType);

        Description description = new Description(id, conceptSMTK, descriptionID, descriptionType, term, uses,
                isCaseSensitive, isAutoGenerated, isPublished,
                validityUntil, creationDate, user, isModeled);

        return description;
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

            User user = UserFactory.getInstance().findUserById(idUser);

            DescriptionType descriptionType = DescriptionTypeFactory.getInstance().getDescriptionTypeByID(idDescriptionType);

            Description description = new Description(id, conceptSMTK, descriptionID, descriptionType, term, uses,
                    isCaseSensitive, isAutoGenerated, isPublished,
                    validityUntil, creationDate, user, isModeled);

            descriptions.add(description);
        }

        return descriptions;
    }

}
