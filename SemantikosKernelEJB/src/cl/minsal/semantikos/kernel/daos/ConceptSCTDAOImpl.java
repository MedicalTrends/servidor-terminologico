package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.users.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static cl.minsal.semantikos.util.StringUtils.underScoreToCamelCaseJSON;

/**
 * @author Andrés Farías
 */
@Stateless
public class ConceptSCTDAOImpl implements ConceptSCTDAO {

    private static final Logger logger = LoggerFactory.getLogger(ConceptSCTDAOImpl.class);

    @EJB
    private SnomedCTDAO snomedCTDAO;

    @Override
    public ConceptSCT getConceptCSTByID(long idConceptCST) {

        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.get_concept_sct_by_id(?)}";
        ConceptSCT conceptSCT;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, idConceptCST);
            call.execute();

            ResultSet rs = call.getResultSet();

            if (rs.next()) {
                String resultJSON = rs.getString(1);
                ObjectMapper mapper = new ObjectMapper();
                conceptSCT = mapper.readValue(underScoreToCamelCaseJSON(resultJSON), ConceptSCT.class);

            } else {
                String errorMsg = "La relacion no fue creada. Esta es una situación imposible. Contactar a Desarrollo";
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
            rs.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        } catch (IOException e) {
            String errorMsg = "Error al parsear la respuesta de get_concept_sct_by_id(" + idConceptCST + ")";
            logger.error(errorMsg);
            throw new EJBException(errorMsg, e);
        }

        return conceptSCT;
    }

    @Override
    public void persist(ConceptSCT conceptSCT, User user) {

        ConnectionBD connect = new ConnectionBD();
        long id;
        String sql = "{call semantikos.create_concept_sct(?,?,?,?,?,?,?,?,?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, conceptSCT.getId());
            call.setTimestamp(2, conceptSCT.getEffectiveTime());
            call.setBoolean(3, conceptSCT.isActive());

            call.execute();

            ResultSet rs = call.getResultSet();

            if (rs.next()) {
            /* Se recupera el ID del concepto persistido */
                id = rs.getLong(1);
                //conceptSMTK.setId(id);
            } else {
                String errorMsg = "El concepto no fue creado por una razon desconocida. Alertar al area de desarrollo sobre esto";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            rs.close();
        } catch (SQLException e) {
            String errorMsg = "El concepto ";// + conceptSMTK.toString() + " no fue persistido.";
            logger.error(errorMsg, e);
            throw new EJBException(errorMsg, e);
        }
    }
}
