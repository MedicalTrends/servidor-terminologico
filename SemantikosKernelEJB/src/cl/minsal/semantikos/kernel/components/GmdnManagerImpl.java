package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.businessrules.CrossMapCreationBR;
import cl.minsal.semantikos.kernel.businessrules.CrossMapRemovalBR;
import cl.minsal.semantikos.kernel.daos.CrossmapsDAO;
import cl.minsal.semantikos.kernel.daos.GmdnDAO;
import cl.minsal.semantikos.kernel.factories.CrossmapFactory;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.crossmaps.*;
import cl.minsal.semantikos.model.gmdn.CollectiveTerm;
import cl.minsal.semantikos.model.gmdn.DeviceType;
import cl.minsal.semantikos.model.gmdn.GenericDeviceGroup;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.SnomedCTRelationship;
import cl.minsal.semantikos.model.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrés Farías on 8/30/16.
 */
@Stateless
@LocalBean
public class GmdnManagerImpl implements GmdnManager {

    /** El logger de la clase */
    private static final Logger logger = LoggerFactory.getLogger(GmdnManagerImpl.class);

    @EJB
    private GmdnDAO gmdnDAO;


    @Override
    public List<DeviceType> findDeviceTypesByPattern(String pattern) {
        return gmdnDAO.findDeviceTypeByPattern(pattern);
    }

    @Override
    public DeviceType getDeviceTypeById(long id) {
        return gmdnDAO.getDeviceTypeById(id);
    }

    @Override
    public List<CollectiveTerm> getParentLines(GenericDeviceGroup genericDeviceGroup) {

        List<CollectiveTerm> collectiveTerms = gmdnDAO.getCollectiveTermsByGenericDeviceGroup(genericDeviceGroup);

        return gmdnDAO.getParentLines(collectiveTerms);
    }
}
