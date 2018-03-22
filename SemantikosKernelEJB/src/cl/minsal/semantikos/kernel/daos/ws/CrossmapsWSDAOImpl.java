package cl.minsal.semantikos.kernel.daos.ws;

import cl.minsal.semantikos.kernel.daos.ConceptDAO;
import cl.minsal.semantikos.kernel.daos.ConceptDAOImpl;
import cl.minsal.semantikos.kernel.daos.CrossmapsDAO;
import cl.minsal.semantikos.kernel.daos.RelationshipDefinitionDAO;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.crossmaps.*;
import cl.minsal.semantikos.model.crossmaps.cie10.Disease;
import cl.minsal.semantikos.model.crossmaps.gmdn.GenericDeviceGroup;
import cl.minsal.semantikos.model.dtos.CrossmapSetMemberDTO;
import cl.minsal.semantikos.model.dtos.DiseaseDTO;
import cl.minsal.semantikos.model.dtos.GenericDeviceGroupDTO;
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

        switch ((int)id) {
            case 1:
                return createDiseaseFromDTO((DiseaseDTO) crossmapSetMemberDTO);
            case 2:
                return createGenericDeviceGroupFromDTO((GenericDeviceGroupDTO) crossmapSetMemberDTO);
            default:
                throw new IllegalArgumentException("CrossmapSet: "+id+" no soportado");
        }
    }


    public CrossmapSetMember createDiseaseFromDTO(DiseaseDTO diseaseDTO) {
        // id bigint, id_concept bigint, id_crossmapset bigint, id_user bigint, id_validity_until timestamp
        long id = diseaseDTO.getId();
        String code = diseaseDTO.getCode();
        String gloss = diseaseDTO.getGloss();

        CrossmapSet crossmapSet = CrossmapSetFactory.getInstance().findCrossmapSetsById(diseaseDTO.getCrossmapSetId());

        return new Disease(id, id, crossmapSet, code, gloss);
    }

    public CrossmapSetMember createGenericDeviceGroupFromDTO(GenericDeviceGroupDTO genericDeviceGroupDTO) {
        // id bigint, id_concept bigint, id_crossmapset bigint, id_user bigint, id_validity_until timestamp
        long code = genericDeviceGroupDTO.getCode();
        String termName  = genericDeviceGroupDTO.getTermName();
        String termDefinition = genericDeviceGroupDTO.getTermDefinition();
        String termStatus = genericDeviceGroupDTO.getTermStatus();
        Timestamp createdDate = genericDeviceGroupDTO.getCreatedDate();
        Timestamp modifiedDate = genericDeviceGroupDTO.getModifiedDate();
        Timestamp obsoletedDate = genericDeviceGroupDTO.getObsoletedDate();
        CrossmapSet crossmapSet = CrossmapSetFactory.getInstance().findCrossmapSetsById(genericDeviceGroupDTO.getCrossmapSetId());

        return new GenericDeviceGroup(crossmapSet, code, code, termName, termDefinition, termStatus, createdDate, modifiedDate, obsoletedDate);
    }


}
