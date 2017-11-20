package cl.minsal.semantikos.kernel.daos.ws;

import cl.minsal.semantikos.kernel.daos.ConceptDAO;
import cl.minsal.semantikos.kernel.daos.ConceptDAOImpl;
import cl.minsal.semantikos.kernel.daos.CrossmapsDAO;
import cl.minsal.semantikos.kernel.daos.RelationshipDefinitionDAO;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.crossmaps.*;
import cl.minsal.semantikos.model.dtos.CrossmapSetMemberDTO;
import cl.minsal.semantikos.model.relationships.MultiplicityFactory;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model.relationships.SnomedCTRelationship;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
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
public class CrossmapsWSDAOImpl implements CrossmapsWSDAO {

    private static final Logger logger = LoggerFactory.getLogger(ConceptDAOImpl.class);


    /**
     * Este método es responsable de crear un objeto <code>CrossmapSetMember</code> a partir de un ResultSet.
     *
     * @param crossmapSetMemberDTO El ResultSet a partir del cual se crea el crossmap.
     *
     * @return Un Crossmap Directo creado a partir del result set.
     */
    public CrossmapSetMember createCrossmapSetMemberFromDTO(CrossmapSetMemberDTO crossmapSetMemberDTO) {
        // id bigint, id_concept bigint, id_crossmapset bigint, id_user bigint, id_validity_until timestamp
        long id = crossmapSetMemberDTO.getId();
        String code = crossmapSetMemberDTO.getCode();
        String gloss = crossmapSetMemberDTO.getGloss();

        CrossmapSet crossmapSet = CrossmapSetFactory.getInstance().findCrossmapSetsById(crossmapSetMemberDTO.getCrossmapSetId());

        return new CrossmapSetMember(id, id, crossmapSet, code, gloss);
    }


}
