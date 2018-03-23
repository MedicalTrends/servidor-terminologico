package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.crossmaps.*;
import cl.minsal.semantikos.model.crossmaps.cie10.Disease;
import cl.minsal.semantikos.model.relationships.MultiplicityFactory;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.SnomedCTRelationship;
import cl.minsal.semantikos.model.users.User;
import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.currentTimeMillis;

/**
 * @author Andrés Farías on 8/19/16.
 */
@Stateless
public class CIE10DAOImpl implements CIE10DAO {

    private static final Logger logger = LoggerFactory.getLogger(ConceptDAOImpl.class);

    @EJB
    private ConceptDAO conceptDAO;

    @EJB
    private RelationshipDefinitionDAO relationshipDAO;

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    @Override
    public Disease getDiseaseById(long id) {

        String sql = "begin ? := stk.stk_pck_cie10.get_disease_by_id(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, id);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            if (rs.next()) {
                return createDiseaseFromResultSet(rs);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        throw new IllegalArgumentException("No existe un disease con ID=" + id);
    }

    @Override
    public List<Disease> findDiseasesByPattern(String pattern) {

        List<Disease> diseases = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_cie10.find_diseases_by_pattern(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setString(2, pattern);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                diseases.add(createDiseaseFromResultSet(rs));
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return diseases;
    }

    @Override
    public List<Disease> getDiseases() {

        List<Disease> diseases = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_cie10.get_diseases; end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                diseases.add(createDiseaseFromResultSet(rs));
            }
            rs.close();
        } catch (SQLException e) {
            String s = "Error al recuperar los crossmaps";
            logger.error(s);
            throw new EJBException(s, e);
        }

        return diseases;
    }

    @Override
    public List<Disease> getDiseasesPaginated(int page, int pageSize) {

        List<Disease> diseases = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_cie10.get_diseases_paginated(?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setInt(2, page);
            call.setInt(3, pageSize);

            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                diseases.add(createDiseaseFromResultSet(rs));
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return diseases;
    }


    /**
     * Este método es responsable de crear un objeto <code>CrossmapSetMember</code> a partir de un ResultSet.
     *
     * @param rs El ResultSet a partir del cual se crea el crossmap.
     *
     * @return Un Crossmap Directo creado a partir del result set.
     */
    public Disease createDiseaseFromResultSet(ResultSet rs) throws SQLException {
        // id bigint, id_concept bigint, id_crossmapset bigint, id_user bigint, id_validity_until timestamp
        long id = rs.getLong("id");
        String code = rs.getString("code");
        String gloss = rs.getString("gloss");
        CrossmapSet crossmapSet = CrossmapSetFactory.getInstance().findCrossmapSetsById(rs.getLong("id_cross_map_set"));

        return new Disease(id, id, crossmapSet, code, gloss);
    }

}
