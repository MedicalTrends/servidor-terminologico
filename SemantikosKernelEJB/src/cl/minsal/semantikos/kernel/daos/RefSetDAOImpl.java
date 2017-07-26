package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.users.Institution;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.ArrayUtils.EMPTY_LONG_ARRAY;
import static org.apache.commons.lang.ArrayUtils.EMPTY_LONG_OBJECT_ARRAY;

/**
 * @author Andrés Farías on 9/20/16.
 */
@Stateless
public class RefSetDAOImpl implements RefSetDAO {

    private static final Logger logger = LoggerFactory.getLogger(RefSetDAO.class);

    @EJB
    private ConceptDAO conceptDAO;

    @EJB
    private InstitutionDAO institutionDAO;

    @Override
    public void persist(RefSet refSet) {

        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_refset.create_refset(?,?,?,?); end;";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.NUMERIC);
            call.setString(2, refSet.getName());
            call.setLong(3, refSet.getInstitution().getId());
            call.setTimestamp(4, refSet.getCreationDate());
            call.setTimestamp(5, refSet.getValidityUntil());
            call.execute();

            //ResultSet rs = (ResultSet) call.getObject(1);

            if (call.getLong(1) > 0) {
                refSet.setId(call.getLong(1));
            }
            //rs.close();

        } catch (SQLException e) {
            logger.error("Error al crear el RefSet:" + refSet, e);
        }
    }

    @Override
    public void update(RefSet refSet) {
        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_refset.update_refset(?,?,?,?,?); end;";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.NUMERIC);
            call.setLong(2, refSet.getId());
            call.setString(3, refSet.getName());
            call.setLong(4, refSet.getInstitution().getId());
            call.setTimestamp(5, refSet.getCreationDate());
            if(refSet.getValidityUntil()==null){
                call.setNull(6, Types.TIMESTAMP);
            }else {
                call.setTimestamp(6, refSet.getValidityUntil());
            }
            call.execute();
        } catch (SQLException e) {
            logger.error("Error al crear el RefSet:" + refSet, e);
        }
    }

    @Override
    public void bind(ConceptSMTK conceptSMTK, RefSet refSet) {
        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_refset.bind_concept_to_refset(?,?); end;";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.NUMERIC);
            call.setLong(2, refSet.getId());
            call.setLong(3, conceptSMTK.getId());
            call.execute();
        } catch (SQLException e) {
            logger.error("Error al asociar el RefSet:" + refSet + " al concepto " + conceptSMTK, e);
        }
    }

    @Override
    public void unbind(ConceptSMTK conceptSMTK, RefSet refSet) {
        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_refset.unbind_concept_from_refset(?,?); end;";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.NUMERIC);
            call.setLong(2, refSet.getId());
            call.setLong(3, conceptSMTK.getId());
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

        String sql = "begin ? := stk.stk_pck_refset.get_all_refsets; end;";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                refSets.add(createRefsetFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al al obtener los RefSets ", e);
        }

        return refSets;
    }

    @Override
    public List<RefSet> getValidRefsets() {

        //TODO: terminar esto

        List<RefSet> refSets= new ArrayList<>();

        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_refset.get_valid_refsets; end;";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

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

        String sql = "begin ? := stk.stk_pck_refset.get_refsets_by_concept(?); end;";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2,conceptSMTK.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                refSets.add(createRefsetFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al al obtener los RefSets ", e);
        }

        return refSets;
    }

    @Override
    public List<RefSet> getRefsetBy(Institution institution) {
        List<RefSet> refSetsByInstitution= new ArrayList<>();
        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_refset.get_refset_by_institution(?); end;";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2,institution.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                refSetsByInstitution.add(createRefsetFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al al obtener los RefSets ", e);
        }
        return refSetsByInstitution;
    }

    @Override
    public RefSet getRefsetBy(long id) {
        RefSet refSet=null;
        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_refset.get_refset_by_id(?); end;";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2,id);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                refSet=createRefsetFromResultSet(rs);
            }
        } catch (SQLException e) {
            logger.error("Error al al obtener los RefSets ", e);
        }

        return refSet;
    }

    @Override
    public List<RefSet> getRefsetsBy(List<Long> categories, String pattern) {
        List<RefSet> refSets= new ArrayList<>();

        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_refset.get_refsets_by_categories_and_pattern(?,?); end;";

        try (Connection connection = connect.getConnection();

             CallableStatement call = connection.prepareCall(sql)) {
            Array categoryIds = connection.createArrayOf("long", categories.toArray(new Long[categories.size()]));

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setArray(2,categoryIds);
            call.setString(3,pattern);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                refSets.add(createRefsetFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al al obtener los RefSets ", e);
        }

        return refSets;
    }

    private RefSet createRefsetFromResultSet(ResultSet rs) throws SQLException {

        long id= rs.getLong("id");
        String name = rs.getString("name_refset");
        Timestamp timestamp= rs.getTimestamp("creation_date");
        Timestamp validity= rs.getTimestamp("validity_until");

        Institution institution= institutionDAO.getInstitutionBy(rs.getLong("id_institution"));

        RefSet refSet= new RefSet(name,institution,timestamp);
        refSet.setId(id);
        refSet.setValidityUntil(validity);
        refSet.setConcepts(conceptDAO.findConcepts(EMPTY_LONG_OBJECT_ARRAY, new Long[]{refSet.getId()}, null));
        return refSet;
    }

    @Override
    public List<RefSet> findRefsetsByName(String pattern) {
        List<RefSet> refSets = new ArrayList<>();

        ConnectionBD connect = new ConnectionBD();

        String sql = "begin ? := stk.stk_pck_refset.find_refsets_by_name(?); end;";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, pattern);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

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

        String sql = "begin ? := stk.stk_pck_refset.find_refsets_by_concept(?); end;";

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, conceptSMTK.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                refSets.add(createRefsetFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al buscar los RefSets ", e);
        }

        return refSets;
    }
}
