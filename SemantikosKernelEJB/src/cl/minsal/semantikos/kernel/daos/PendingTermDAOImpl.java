package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.PendingTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrés Farías on 11/22/16.
 */
@Stateless
public class PendingTermDAOImpl implements PendingTermDAO {

    private static final Logger logger = LoggerFactory.getLogger(PendingTermDAOImpl.class);

    @EJB
    private ConceptDAO conceptDAO;

    @EJB
    private CategoryDAO categoryDAO;

    @EJB
    private DescriptionDAO descriptionDAO;

    @Override
    public void persist(PendingTerm pendingTerm) {
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
        String sql = "{call semantikos.create_pending_term(?,?,?,?,?,?,?,?,?,?,?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setString(1, pendingTerm.getTerm());
            call.setTimestamp(2, new Timestamp(pendingTerm.getDate().getTime()));
            call.setBoolean(3, pendingTerm.isSensibility());
            call.setLong(4, pendingTerm.getCategory().getId());
            call.setString(5, pendingTerm.getNameProfessional());
            call.setString(6, pendingTerm.getProfession());
            call.setString(7, pendingTerm.getSpeciality());
            call.setString(8, pendingTerm.getSubSpeciality());
            call.setString(9, pendingTerm.getMail());
            call.setString(10, pendingTerm.getObservation());
            call.setString(11,pendingTerm.getPlaceOrigin());

            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                pendingTerm.setId(rs.getLong(1));
            } else {
                String errorMsg = "El termino pendiente no fue creado. Contacte a Desarrollo";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            rs.close();
        } catch (SQLException e) {
            throw new EJBException(e);
        }
    }

    @Override
    public void bindTerm2Description(PendingTerm pendingTerm, Description description) {

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
        String sql = "{call semantikos.bind_pending_term_to_description(?,?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, pendingTerm.getId());
            call.setLong(2, description.getId());
            call.execute();
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        /* Se valida si no están asociados para asociarles */
        Description pendingTermRelatedDescription = pendingTerm.getRelatedDescription();
        if (pendingTermRelatedDescription == null || !pendingTermRelatedDescription.equals(description)){
            pendingTerm.setRelatedDescription(description);
        }
    }

    @Override
    public List<PendingTerm> getAllPendingTerms() {

        List<PendingTerm> pendingTerms = new ArrayList<>();

        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.get_all_pending_terms(?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, conceptDAO.getPendingConcept().getId());
            call.execute();

            ResultSet resultSet = call.getResultSet();
            while(resultSet.next()){
                pendingTerms.add(createPendingTermFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return pendingTerms;
    }

    @Override
    public PendingTerm getPendingTermById(long id) {

        PendingTerm pendingTerm = null;

        ConnectionBD connect = new ConnectionBD();

        String sql = "{call semantikos.get_pending_term_by_id(?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, id);
            call.execute();

            ResultSet resultSet = call.getResultSet();

            ResultSet rs = call.getResultSet();

            if (rs.next()) {
                pendingTerm = createPendingTermFromResultSet(rs);
            } else {
                String errorMsg = "No existe un término pendiente con id=" + id;
                logger.error(errorMsg);
                throw new IllegalArgumentException(errorMsg);
            }
        } catch (SQLException e) {
            throw new EJBException(e);
        }

        return pendingTerm;
    }

    @Override
    public void updateSearchIndexes(PendingTerm pendingTerm) {

        ConnectionBD connect = new ConnectionBD();
        String sql = "{call semantikos.update_pending_term_search_indexes(?)}";
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.setLong(1, pendingTerm.getId());

            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                if (!rs.getBoolean(1)) {
                    throw new EJBException("Los índices de búsqueda del término con id=" + pendingTerm.getId() + " no fueron actualizados.");
                }
            } else {
                String errorMsg = "Los índices de búsqueda del término con id=" + pendingTerm.getId() + " no fueron actualizados.";
                logger.error(errorMsg);
                throw new EJBException(errorMsg);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Los índices de búsqueda del término con id=" + pendingTerm.getId() + " no fueron actualizados.", e);
            throw new EJBException("Los índices de búsqueda del término con id=" + pendingTerm.getId() + " no fueron actualizados.", e);
        }
    }

    private PendingTerm createPendingTermFromResultSet(ResultSet resultSet) throws SQLException {

        long id = resultSet.getLong("id");
        String term = resultSet.getString("term");
        boolean sensibility = resultSet.getBoolean("sensibility");
        long idCategory = resultSet.getLong("id_category");
        String nameProfessional = resultSet.getString("name_professional");
        String profession = resultSet.getString("profession");
        String specialty = resultSet.getString("specialty");
        String subSpecialty = resultSet.getString("subspecialty");
        String mail = resultSet.getString("mail");
        String observation = resultSet.getString("observation");
        Timestamp submissionDate = resultSet.getTimestamp("submission_date");
        Description description = descriptionDAO.getDescriptionBy(resultSet.getLong("id_description"));
        String placeOrigin = resultSet.getString("place_origin");

        PendingTerm pendingTerm = new PendingTerm(id, term, submissionDate, sensibility, categoryDAO.getCategoryById(idCategory), nameProfessional, profession, specialty, subSpecialty, mail, observation, placeOrigin);
        pendingTerm.setRelatedDescription(description);

        return pendingTerm;
    }
}
