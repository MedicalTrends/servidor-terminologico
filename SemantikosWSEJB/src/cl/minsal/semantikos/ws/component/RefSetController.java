package cl.minsal.semantikos.ws.component;

import cl.minsal.semantikos.kernel.components.RefSetManager;
import cl.minsal.semantikos.model.RefSet;
import cl.minsal.semantikos.ws.fault.NotFoundFault;
import cl.minsal.semantikos.ws.mapping.RefSetMapper;
import cl.minsal.semantikos.ws.response.RefSetResponse;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Development on 2016-11-18.
 *
 */
@Stateless
public class RefSetController {

    @EJB
    private RefSetManager refSetManager;

    public List<RefSetResponse> refSetList(Boolean includeAllInstitutions) throws NotFoundFault {
        List<RefSetResponse> res = new ArrayList<>();
        List<RefSet> refSets = this.refSetManager.getAllRefSets();
        if ( refSets != null ) {
            for ( RefSet refSet : refSets ) {
                res.add(this.getResponse(refSet));
            }
        }
        // TODO includeAllInstitutions
        return res;
    }


    public RefSetResponse getResponse(RefSet refSet) throws NotFoundFault {
        if ( refSet == null ) {
            throw new NotFoundFault("RefSet no encontrado");
        }
        return RefSetMapper.map(refSet);
    }

}
