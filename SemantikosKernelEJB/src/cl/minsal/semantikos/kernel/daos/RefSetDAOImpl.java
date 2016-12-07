package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.Description;
import cl.minsal.semantikos.model.Institution;
import cl.minsal.semantikos.model.RefSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrés Farías on 9/20/16.
 */
@Stateless
public class RefSetDAOImpl implements RefSetDAO {

    private static final Logger logger = LoggerFactory.getLogger(RefSetDAO.class);

    @EJB
    private ConceptDAO conceptDAO;

    @Override
    public void persist(RefSet refSet) {

        ConnectionBD connect = new ConnectionBD();
        String CREATE_REFSET = "{call semantikos.create_refset(?,?,?,?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(CREATE_REFSET)) {

            call.setString(1, refSet.getName());
            call.setLong(2, refSet.getInstitution().getId());
            call.setTimestamp(3, refSet.getCreationDate());
            call.setTimestamp(4, refSet.getValidityUntil());
            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                refSet.setId(rs.getLong(1));
            }
            rs.close();

        } catch (SQLException e) {
            logger.error("Error al crear el RefSet:" + refSet, e);
        }
    }

    @Override
    public void update(RefSet refSet) {
        ConnectionBD connect = new ConnectionBD();
        String UPDATE_REFSET = "{call semantikos.update_refset(?,?,?,?,?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(UPDATE_REFSET)) {

            call.setLong(1, refSet.getId());
            call.setString(2, refSet.getName());
            call.setLong(3, refSet.getInstitution().getId());
            call.setTimestamp(4, refSet.getCreationDate());
            call.setTimestamp(5, refSet.getValidityUntil());
            call.execute();
        } catch (SQLException e) {
            logger.error("Error al crear el RefSet:" + refSet, e);
        }
    }

    @Override
    public void bind(ConceptSMTK conceptSMTK, RefSet refSet) {
        ConnectionBD connect = new ConnectionBD();
        String UPDATE_REFSET = "{call semantikos.bind_concept_to_refset(?,?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(UPDATE_REFSET)) {

            call.setLong(1, refSet.getId());
            call.setLong(2, conceptSMTK.getId());
            call.execute();
        } catch (SQLException e) {
            logger.error("Error al asociar el RefSet:" + refSet + " al concepto " + conceptSMTK, e);
        }
    }

    @Override
    public void unbind(ConceptSMTK conceptSMTK, RefSet refSet) {
        ConnectionBD connect = new ConnectionBD();
        String UPDATE_REFSET = "{call semantikos.unbind_concept_from_refset(?,?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(UPDATE_REFSET)) {

            call.setLong(1, refSet.getId());
            call.setLong(2, conceptSMTK.getId());
            call.execute();
        } catch (SQLException e) {
            logger.error("Error al des-asociar el RefSet:" + refSet + " al concepto " + conceptSMTK, e);
        }
    }

    @Override
    public List<RefSet> getReftsets() {

        //TODO: terminar esto

        List<RefSet> refSets= new ArrayList<>();

        ConnectionBD connect = new ConnectionBD();
        String ALL_REFSETS = "{call semantikos.get_all_refsets()}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(ALL_REFSETS)) {

            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                refSets.add(createRefsetFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al al obtener los RefSets ", e);
        }

        return refSets;
    }

    @Override
    public List<RefSet> getRefsetsBy(ConceptSMTK conceptSMTK) {
        List<RefSet> refSets= new ArrayList<>();

        ConnectionBD connect = new ConnectionBD();
        String ALL_REFSETS_BY_CONCEPT = "{call semantikos.get_refsets_by_concept(?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(ALL_REFSETS_BY_CONCEPT)) {
            call.setLong(1,conceptSMTK.getId());
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                refSets.add(createRefsetFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al al obtener los RefSets ", e);
        }

        return refSets;
    }
    @Override
    public List<RefSet> findRefsetsByName(String pattern) {
        List<RefSet> refSets = new ArrayList<>();

        ConnectionBD connect = new ConnectionBD();
        String ALL_REFSETS = "{call semantikos.find_refsets_by_name(?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(ALL_REFSETS)) {
            call.setString(1, pattern);
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                refSets.add(createRefsetFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al buscar los RefSets ", e);
        }

        return refSets;
    }

    @Override
    public List<RefSet> findByConcept(ConceptSMTK conceptSMTK) {
        List<RefSet> refSets = new ArrayList<>();
        ConnectionBD connect = new ConnectionBD();
        String ALL_REFSETS = "{call semantikos.find_refsets_by_concept(?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(ALL_REFSETS)) {
            call.setLong(1, conceptSMTK.getId());
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                refSets.add(createRefsetFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al buscar los RefSets ", e);
        }

        return refSets;
    }

    @Override
    public RefSet getRefsetBy(long id) {
        RefSet refSet=null;
        ConnectionBD connect = new ConnectionBD();
        String GET_REFSET_BY_ID = "{call semantikos.get_refset_by_id(?)}";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(GET_REFSET_BY_ID)) {
            call.setLong(1,id);
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                refSet=createRefsetFromResultSet(rs);
            }
        } catch (SQLException e) {
            logger.error("Error al al obtener los RefSets ", e);
        }

        return refSet;
    }

    private RefSet createRefsetFromResultSet(ResultSet rs) throws SQLException {

        long id= rs.getLong("id");
        String name = rs.getString("name");
        Timestamp timestamp= rs.getTimestamp("creation_date");
        Timestamp validity= rs.getTimestamp("validity_until");

        Institution institution= new Institution();
        institution.setId(1);
        institution.setName("MINSAL");

        RefSet refSet= new RefSet(name,institution,timestamp);
        refSet.setId(id);
        refSet.setValidityUntil(validity);
        refSet.setConcepts(conceptDAO.getConceptBy(refSet)); // FIXME: Pueden haber muchos conceptos por RefSet, mucha memoria
        return refSet;
    }
}
