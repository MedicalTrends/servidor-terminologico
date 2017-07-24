package cl.minsal.semantikos.kernel.daos.mappers;

import cl.minsal.semantikos.kernel.daos.ConceptDAO;
import cl.minsal.semantikos.kernel.daos.CrossmapsDAO;
import cl.minsal.semantikos.kernel.daos.TagSMTKDAO;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.crossmaps.CrossmapSet;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;
import cl.minsal.semantikos.model.crossmaps.DirectCrossmap;
import cl.minsal.semantikos.model.crossmaps.IndirectCrossmap;
import cl.minsal.semantikos.model.relationships.MultiplicityFactory;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.tags.TagSMTK;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by root on 28-06-17.
 */
@Singleton
public class CrossmapMapper {

    @EJB
    CrossmapsDAO crossmapsDAO;

    @EJB
    ConceptDAO conceptDAO;

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
        CrossmapSetMember crossmapSetMember = crossmapsDAO.getCrossmapSetMemberById(idCrossmapSet);
        RelationshipDefinition relationshipDefinition = conceptSMTK.getCategory().findRelationshipDefinitionsById(idRelationshipDefinition).get(0);

        return new DirectCrossmap(id, conceptSMTK, crossmapSetMember, relationshipDefinition, validityUntil);
    }

    public IndirectCrossmap createIndirectCrossMapFromResultSet(ResultSet rs, ConceptSMTK sourceConcept) throws SQLException {

        long id = rs.getLong("id");
        long idCrossMapSetMember = rs.getLong("id_cross_map_set_member");
        CrossmapSetMember crossmapSetMemberById = crossmapsDAO.getCrossmapSetMemberById(idCrossMapSetMember);
        RelationshipDefinition relationshipDefinition = new RelationshipDefinition("Indirect Crossmap", "Un crossmap Indirecto", MultiplicityFactory.ONE_TO_ONE, crossmapSetMemberById.getCrossmapSet());

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
    public CrossmapSetMember createCrossmapSetMemberFromResultSet(ResultSet rs, CrossmapSet crossmapSet) throws SQLException {
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
