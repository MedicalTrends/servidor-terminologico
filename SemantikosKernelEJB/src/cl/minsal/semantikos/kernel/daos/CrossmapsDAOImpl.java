package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.kernel.util.ConnectionBD;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.MultiplicityFactory;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.model.crossmaps.*;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.currentTimeMillis;

/**
 * @author Andrés Farías on 8/19/16.
 */
@Stateless
public class CrossmapsDAOImpl implements CrossmapsDAO {

    private static final Logger logger = LoggerFactory.getLogger(ConceptDAOImpl.class);

    @EJB
    private ConceptDAO conceptDAO;

    @EJB
    private RelationshipDefinitionDAO relationshipDAO;

    @EJB
    private CrossmapFactory crossmapFactory;

    @Override
    public DirectCrossmap create(DirectCrossmap directCrossmap, User user) {

        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.create_direct_crossmap(?,?,?,?)}")) {

            /*
                1. source concept
                2. external term id
                3. validity until.
                4. user id
            */
            call.setLong(1, directCrossmap.getSourceConcept().getId());
            call.setLong(2, directCrossmap.getTarget().getId());
            call.setTimestamp(3, new Timestamp(currentTimeMillis()));
            call.setLong(4, user.getIdUser());
            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                directCrossmap.setId(rs.getLong(1));
            }
            rs.close();
        } catch (SQLException e) {
            String s = "Error al crear un Crossmap en la base de datos";
            logger.error(s);
            throw new EJBException(s, e);
        }

        return directCrossmap;
    }

    @Override
    public DirectCrossmap getDirectCrossmapById(long idCrossmap) {
        ConnectionBD connect = new ConnectionBD();
        DirectCrossmap directCrossmapFromResultSet;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.get_direct_crossmap(?)}")) {

            call.setLong(1, idCrossmap);
            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                directCrossmapFromResultSet = createDirectCrossmapFromResultSet(rs);
            } else {
                throw new EJBException("Error al intentar obtener un crossmap directo de ID= " + idCrossmap);
            }
            rs.close();
        } catch (SQLException e) {
            String s = "Error al crear un Crossmap en la base de datos";
            logger.error(s);
            throw new EJBException(s, e);
        }

        return directCrossmapFromResultSet;
    }

    /**
     * Este método es responsable de crear un objeto <code>DirectCrossmap</code> a partir de un ResultSet.
     *
     * @param rs El ResultSet a partir del cual se crea el crossmap.
     *
     * @return Un Crossmap Directo creado a partir del result set.
     */
    private DirectCrossmap createDirectCrossmapFromResultSet(ResultSet rs) throws SQLException {
        // id bigint, id_concept bigint, id_crossmapset bigint, id_user bigint, id_validity_until timestamp
        long id = rs.getLong("id");
        long idConcept = rs.getLong("id_concept");
        long idCrossmapSet = rs.getLong("id_crossmapsetmember");
        long idRelationshipDefinition = rs.getLong("id_relationshipdefinition");
        long idUser = rs.getLong("id_user");
        Timestamp validityUntil = rs.getTimestamp("validity_until");

        ConceptSMTK conceptSMTK = conceptDAO.getConceptByID(idConcept);
        CrossmapSetMember crossmapSetMember = this.getCrossmapSetMemberById(idCrossmapSet);
        RelationshipDefinition relationshipDefinition = relationshipDAO.getRelationshipDefinitionByID(idRelationshipDefinition);
        return new DirectCrossmap(id, conceptSMTK, crossmapSetMember, relationshipDefinition, validityUntil);
    }

    @Override
    public DirectCrossmap bindConceptSMTKToCrossmapSetMember(ConceptSMTK conceptSMTK, CrossmapSetMember crossmapSetMember) {
        return null;
    }

    @Override
    public CrossmapSetMember getCrossmapSetMemberById(long idCrossmapSetMember) {

        ConnectionBD connect = new ConnectionBD();
        CrossmapSet crossmapSetFromResultSet;

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.get_crossmapsetmember_by_id(?)}")) {

            call.setLong(1, idCrossmapSetMember);
            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                return crossmapFactory.createCrossmapSetMemberFromResultSet(rs);
            }

            rs.close();
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        throw new IllegalArgumentException("No existe un crossmapSetMember con ID=" + idCrossmapSetMember);
    }

    @Override
    public List<CrossmapSetMember> getRelatedCrossMapSetMembers(ConceptSCT conceptSCT) {

        List<CrossmapSetMember> crossmapSetMembers = new ArrayList<>();
        ConnectionBD connect = new ConnectionBD();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.get_related_crossmapset_member(?)}")) {

            call.setLong(1, conceptSCT.getId());
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                CrossmapSetMember crossmapSetMember = crossmapFactory.createCrossmapSetMemberFromResultSet(rs);
                crossmapSetMembers.add(crossmapSetMember);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return crossmapSetMembers;
    }
    
    @Override
    public CrossmapSet getCrossmapSetByID(long id) {

        ConnectionBD connect = new ConnectionBD();
        CrossmapSet crossmapSetFromResultSet;

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.get_crossmapset_by_id(?)}")) {

            call.setLong(1, id);
            call.execute();

            ResultSet rs = call.getResultSet();
            if (rs.next()) {
                crossmapSetFromResultSet = createCrossmapSetFromResultSet(rs);
            } else {
                throw new EJBException("Error al intentar obtener un crossmap directo de ID= " + id);
            }
            rs.close();
        } catch (SQLException e) {
            String s = "Error al crear un Crossmap en la base de datos";
            logger.error(s);
            throw new EJBException(s, e);
        }

        return crossmapSetFromResultSet;
    }

    @Override
    public List<CrossmapSetMember> findCrossmapSetMemberBy(CrossmapSet crossmapSet, String pattern) {
        List<CrossmapSetMember> crossmapSetMembers = new ArrayList<CrossmapSetMember>();
        ConnectionBD connect = new ConnectionBD();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.find_crossmapsetmember_by_pattern_and_crossmapset(?,?)}")) {

            call.setLong(1, crossmapSet.getId());
            call.setString(2, pattern);
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                CrossmapSetMember crossmapSetMember = createCrossmapSetMemberFromResultSet(rs, crossmapSet);
                crossmapSetMembers.add(crossmapSetMember);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return crossmapSetMembers;
    }

    @Override
    public List<CrossmapSetMember> findCrossmapSetMemberByCod1(CrossmapSet crossmapSet, String cod) {
        List<CrossmapSetMember> crossmapSetMembers = new ArrayList<CrossmapSetMember>();
        ConnectionBD connect = new ConnectionBD();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.find_crossmapsetmember_by_cod1_and_crossmapset(?,?)}")) {

            call.setLong(1, crossmapSet.getId());
            call.setString(2, cod);
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                CrossmapSetMember crossmapSetMember = createCrossmapSetMemberFromResultSet(rs, crossmapSet);
                crossmapSetMembers.add(crossmapSetMember);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return crossmapSetMembers;
    }

    @Override
    public List<CrossmapSet> getCrossmapSets() {
        ConnectionBD connect = new ConnectionBD();
        List<CrossmapSet> crossmapSets = new ArrayList<>();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.get_crossmapsets()}")) {

            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                crossmapSets.add(getCrossmapSetByID(rs.getLong(1)));
            }
            rs.close();
        } catch (SQLException e) {
            String s = "Error al recuperar los crossmaps";
            logger.error(s);
            throw new EJBException(s, e);
        }

        return crossmapSets;
    }

    @Override
    public List<IndirectCrossmap> getCrossmapsBySCT(long idConceptSCT, ConceptSMTK sourceConcept) {

        List<IndirectCrossmap> indirectCrossmaps = new ArrayList<>();

        ConnectionBD connect = new ConnectionBD();
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.get_crossmap_by_sct(?)}")) {

            call.setLong(1, idConceptSCT);
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {

                IndirectCrossmap indirectCrossmap = createIndirectCrossMapFromResultSet(rs, sourceConcept);
                indirectCrossmaps.add(indirectCrossmap);
            }
            rs.close();
        } catch (SQLException e) {
            String s = "Error al recuperar los crossmaps";
            logger.error(s);
            throw new EJBException(s, e);
        }

        return indirectCrossmaps;
    }

    @Override
    public List<CrossmapSetMember> getCrossmapSetMemberByAbbreviatedName(String crossmapSetAbbreviatedName) {
        List<CrossmapSetMember> crossmapSetMembers = new ArrayList<CrossmapSetMember>();

        CrossmapSet crossmapSet = getCrossmapSetByAbbreviatedName(crossmapSetAbbreviatedName);
        ConnectionBD connect = new ConnectionBD();

        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.get_crossmapsetmember_by_cms_abbreviated_name(?)}")) {

            call.setString(1, crossmapSetAbbreviatedName);
            call.execute();

            ResultSet rs = call.getResultSet();
            while (rs.next()) {
                CrossmapSetMember crossmapSetMember = createCrossmapSetMemberFromResultSet(rs, crossmapSet);
                crossmapSetMembers.add(crossmapSetMember);
            }
            rs.close();
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }

        return crossmapSetMembers;
    }

    private CrossmapSet getCrossmapSetByAbbreviatedName(String crossmapSetAbbreviatedName) {

        ConnectionBD connect = new ConnectionBD();

        ResultSet rs;
        try (Connection connection = connect.getConnection();
             CallableStatement call = connection.prepareCall("{call semantikos.get_crossmapset_by_cms_abbreviated_name(?)}")) {

            call.setString(1, crossmapSetAbbreviatedName);
            call.execute();

            rs = call.getResultSet();
            if (rs.next()) {
                CrossmapSet crossmapSetFromResultSet = createCrossmapSetFromResultSet(rs);
                rs.close();
                return crossmapSetFromResultSet;
            } else {
                rs.close();
                throw new IllegalArgumentException("No existe un Crossmap Set de nombre abreviado " + crossmapSetAbbreviatedName);
            }
        } catch (SQLException e) {
            logger.error("Se produjo un error al acceder a la BDD.", e);
            throw new EJBException(e);
        }
    }

    private IndirectCrossmap createIndirectCrossMapFromResultSet(ResultSet rs, ConceptSMTK sourceConcept) throws SQLException {

        long id = rs.getLong("id");
        long idCrossMapSetMember = rs.getLong("id_cross_map_set_member");
        CrossmapSetMember crossmapSetMemberById = this.getCrossmapSetMemberById(idCrossMapSetMember);
        RelationshipDefinition relationshipDefinition = new RelationshipDefinition("Indirect Crossmap", "Un crossmap Indirecto", MultiplicityFactory.ONE_TO_ONE, crossmapSetMemberById.getCrossmapSet());

        int mapGroup = rs.getInt("map_group");
        int mapPriority = rs.getInt("map_priority");
        String mapRule = rs.getString("map_rule");
        String mapAdvice = rs.getString("map_advice");
        String mapTarget = rs.getString("map_target");

        long idCorrelation = rs.getLong("id_correlation");
        long idCrossmapCategory = rs.getLong("id_crossmap_category");
        boolean state = rs.getBoolean("state");

        IndirectCrossmap indirectCrossmap = new IndirectCrossmap(id, sourceConcept, crossmapSetMemberById, relationshipDefinition, null);
        indirectCrossmap.setCorrelation(idCorrelation);
        indirectCrossmap.setIdCrossmapCategory(idCrossmapCategory);
        indirectCrossmap.setMapGroup(mapGroup);
        indirectCrossmap.setMapPriority(mapPriority);
        indirectCrossmap.setMapRule(mapRule);
        indirectCrossmap.setMapAdvice(mapAdvice);
        indirectCrossmap.setMapTarget(mapTarget);
        indirectCrossmap.setState(state);

        return indirectCrossmap;
    }


    /**
     * Este método es responsable de crear un objeto <code>CrossmapSetMember</code> a partir de un ResultSet.
     *
     * @param rs El ResultSet a partir del cual se crea el crossmap.
     *
     * @return Un Crossmap Directo creado a partir del result set.
     */
    private CrossmapSetMember createCrossmapSetMemberFromResultSet(ResultSet rs, CrossmapSet crossmapSet) throws SQLException {
        // id bigint, id_concept bigint, id_crossmapset bigint, id_user bigint, id_validity_until timestamp
        long id = rs.getLong("id");
        String code = rs.getString("code");
        String gloss = rs.getString("gloss");

        return new CrossmapSetMember(id, id, crossmapSet, code, gloss);
    }

    /**
     * Este método es responsable de crear un objeto <code>CrossmapSetMember</code> a partir de un ResultSet.
     *
     * @param rs El ResultSet a partir del cual se crea el crossmap.
     *
     * @return Un Crossmap Directo creado a partir del result set.
     */
    private CrossmapSet createCrossmapSetFromResultSet(ResultSet rs) throws SQLException {
        // id bigint, id_concept bigint, id_crossmapset bigint, id_user bigint, id_validity_until timestamp
        long id = rs.getLong("id");
        String nameAbbreviated = rs.getString("name_abbreviated");
        String name = rs.getString("name");
        int version = rs.getInt("version");
        boolean state = rs.getBoolean("state");

        return new CrossmapSet(id, nameAbbreviated, name, version, state);
    }
}
