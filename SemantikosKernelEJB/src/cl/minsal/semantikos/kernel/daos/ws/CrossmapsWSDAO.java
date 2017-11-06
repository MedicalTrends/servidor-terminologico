package cl.minsal.semantikos.kernel.daos.ws;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.crossmaps.CrossmapSet;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;
import cl.minsal.semantikos.model.crossmaps.DirectCrossmap;
import cl.minsal.semantikos.model.crossmaps.IndirectCrossmap;
import cl.minsal.semantikos.model.dtos.CrossmapSetMemberDTO;
import cl.minsal.semantikos.model.relationships.SnomedCTRelationship;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Andrés Farías
 */
@Local
public interface CrossmapsWSDAO {

    public CrossmapSetMember createCrossmapSetMemberFromDTO(CrossmapSetMemberDTO crossmapSetMemberDTO, CrossmapSet crossmapSet);
}
