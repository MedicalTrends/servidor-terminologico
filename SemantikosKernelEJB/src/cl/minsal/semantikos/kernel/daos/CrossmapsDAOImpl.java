package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.relationships.MultiplicityFactory;
import cl.minsal.semantikos.model.relationships.SnomedCTRelationship;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.model.crossmaps.*;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
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
public class CrossmapsDAOImpl implements CrossmapsDAO {

    private static final Logger logger = LoggerFactory.getLogger(ConceptDAOImpl.class);

    @EJB
    private ConceptDAO conceptDAO;

    @EJB
    private RelationshipDefinitionDAO relationshipDAO;

    @EJB
    private CIE10DAO cie10DAO;

    @EJB
    private GMDNDAO gmdndao;

    @Resource(lookup = "java:jboss/OracleDS")
    private DataSource dataSource;

    @Override
    public DirectCrossmap create(DirectCrossmap directCrossmap, User user) {

        String sql = "begin ? := stk.stk_pck_crossmap.create_direct_crossmap(?,?,?,?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            /*
                1. source concept
                2. external term id
                3. validity until.
                4. user id
            */
            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, directCrossmap.getSourceConcept().getId());
            call.setLong(3, directCrossmap.getTarget().getId());
            call.setTimestamp(4, new Timestamp(currentTimeMillis()));
            call.setLong(5, user.getId());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

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
    public CrossmapSetMember getCrossmapSetMemberById(CrossmapSet crossmapSet, long idCrossmapSetMember) {

        switch (crossmapSet.getAbbreviatedName()) {
            case CrossmapSet.CIE10:
                return cie10DAO.getDiseaseById(idCrossmapSetMember);
            case CrossmapSet.GMDN:
                return gmdndao.getGenericDeviceGroupById(idCrossmapSetMember);
            default:
                throw new IllegalArgumentException("CrossmapSet"+crossmapSet+" no soportado");
        }

    }

    @Override
    public List<CrossmapSetMember> findCrossmapSetMemberByPattern(CrossmapSet crossmapSet, String pattern) {

        switch (crossmapSet.getAbbreviatedName()) {
            case CrossmapSet.CIE10:
                return (List<CrossmapSetMember>) (Object) cie10DAO.findDiseasesByPattern(pattern);
            case CrossmapSet.GMDN:
                return (List<CrossmapSetMember>) (Object) gmdndao.findGenericDeviceGroupsByPattern(pattern);
            default:
                throw new IllegalArgumentException("CrossmapSet"+crossmapSet+" no soportado");
        }

    }

    @Override
    public CrossmapSet getCrossmapSetByID(long id) {

        CrossmapSet crossmapSetFromResultSet;

        String sql = "begin ? := stk.stk_pck_crossmap.get_crossmapset_by_id(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, id);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

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
    public List<CrossmapSet> getCrossmapSets() {

        List<CrossmapSet> crossmapSets = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_crossmap.get_crossmapsets; end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                crossmapSets.add(createCrossmapSetFromResultSet(rs));
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
    public List<CrossmapSetMember> getCrossmapSetMembers(CrossmapSet crossmapSet) {

        switch (crossmapSet.getAbbreviatedName()) {
            case CrossmapSet.CIE10:
                return (List<CrossmapSetMember>) (Object) cie10DAO.getDiseases();
            case CrossmapSet.GMDN:
                return (List<CrossmapSetMember>) (Object) gmdndao.getGenericDeviceGroups();
            default:
                throw new IllegalArgumentException("CrossmapSet"+crossmapSet+" no soportado");
        }

    }

    @Override
    public List<CrossmapSetMember> getCrossmapSetMembersPaginated(CrossmapSet crossmapSet, int page, int pageSize) {

        switch (crossmapSet.getAbbreviatedName()) {
            case CrossmapSet.CIE10:
                return (List<CrossmapSetMember>) (Object) cie10DAO.getDiseasesPaginated(page, pageSize);
            case CrossmapSet.GMDN:
                return (List<CrossmapSetMember>) (Object) gmdndao.getGenericDeviceGroupsPaginated(page, pageSize);
            default:
                throw new IllegalArgumentException("CrossmapSet: "+crossmapSet+" no soportado");
        }

    }

    @Override
    public List<IndirectCrossmap> getCrossmapsBySCT(SnomedCTRelationship snomedCTRelationship, ConceptSMTK sourceConcept) {

        List<IndirectCrossmap> indirectCrossmaps = new ArrayList<>();

        String sql = "begin ? := stk.stk_pck_crossmap.get_crossmap_by_sct(?); end;";

        try (Connection connection = dataSource.getConnection();
             CallableStatement call = connection.prepareCall(sql)) {

            call.registerOutParameter (1, OracleTypes.CURSOR);
            call.setLong(2, snomedCTRelationship.getTarget().getIdSnomedCT());
            call.execute();

            ResultSet rs = (ResultSet) call.getObject(1);

            while (rs.next()) {
                IndirectCrossmap indirectCrossmap = createIndirectCrossMapFromResultSet(rs, sourceConcept);
                indirectCrossmap.setCreationDate(snomedCTRelationship.getCreationDate());
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

    /**
     * Este método es responsable de crear un objeto <code>DirectCrossmap</code> a partir de un ResultSet.
     *
     * @param rs El ResultSet a partir del cual se crea el crossmap.
     *
     * @return Un Crossmap Directo creado a partir del result set.
     */
    public DirectCrossmap createDirectCrossmapFromResultSet(ResultSet rs) throws SQLException {
        // id bigint, id_concept bigint, id_crossmapset bigint, id_user bigint, id_validity_until timestamp
        long id = rs.getLong("id");
        long idConcept = rs.getLong("id_concept");
        long idCrossmapSet = rs.getLong("id_crossmapsetmember");
        long idRelationshipDefinition = rs.getLong("id_relationshipdefinition");
        long idUser = rs.getLong("id_user");
        Timestamp validityUntil = rs.getTimestamp("validity_until");

        ConceptSMTK conceptSMTK = conceptDAO.getConceptByID(idConcept);
        CrossmapSetMember crossmapSetMember = (CrossmapSetMember) getCrossmapSetMemberById(CrossmapSetFactory.getInstance().findCrossmapSetsById(idCrossmapSet), idCrossmapSet);
        RelationshipDefinition relationshipDefinition = conceptSMTK.getCategory().findRelationshipDefinitionsById(idRelationshipDefinition).get(0);

        return new DirectCrossmap(id, conceptSMTK, crossmapSetMember, relationshipDefinition, validityUntil);
    }

    public IndirectCrossmap createIndirectCrossMapFromResultSet(ResultSet rs, ConceptSMTK sourceConcept) throws SQLException {

        long id = rs.getLong("id");
        long idCrossMapSetMember = rs.getLong("id_cross_map_set_member");

        long idSnomedCT = rs.getLong("id_snomed_ct");
        long idCrossmapSet = rs.getLong("id_cross_map_set");
        int mapGroup = rs.getInt("map_group");
        int mapPriority = rs.getInt("map_priority");
        String mapRule = rs.getString("map_rule");
        String mapAdvice = rs.getString("map_advice");
        String mapTarget = rs.getString("map_target");

        long idCorrelation = rs.getLong("id_correlation");
        long idCrossmapCategory = rs.getLong("id_crossmap_category");
        boolean state = rs.getBoolean("state");

        CrossmapSetMember crossmapSetMemberById = getCrossmapSetMemberById(CrossmapSetFactory.getInstance().findCrossmapSetsById(idCrossmapSet), idCrossMapSetMember);
        RelationshipDefinition relationshipDefinition = new RelationshipDefinition("Indirect Crossmap", "Un crossmap Indirecto", MultiplicityFactory.ONE_TO_ONE, CrossmapSetFactory.getInstance().findCrossmapSetsById(idCrossmapSet));

        IndirectCrossmap indirectCrossmap = new IndirectCrossmap(id, sourceConcept, crossmapSetMemberById, relationshipDefinition, null);
        indirectCrossmap.setIdCrossmapSet(idCrossmapSet);
        indirectCrossmap.setIdSnomedCT(idSnomedCT);
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
    public CrossmapSet createCrossmapSetFromResultSet(ResultSet rs) throws SQLException {
        // id bigint, id_concept bigint, id_crossmapset bigint, id_user bigint, id_validity_until timestamp
        long id = rs.getLong("id");
        String nameAbbreviated = rs.getString("name_abbreviated");
        String name = rs.getString("name");
        int version = Integer.parseInt(rs.getString("version"));
        boolean state = rs.getBoolean("state");

        return new CrossmapSet(id, nameAbbreviated, name, version, state);
    }

}
