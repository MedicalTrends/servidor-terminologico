package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.RefSet;
import cl.minsal.semantikos.ws.response.RefSetResponse;

/**
 * Created by Development on 2016-10-13.
 *
 */
public class RefSetMapper {

    public static RefSetResponse map(RefSet refSet) {
        if ( refSet != null ) {
            RefSetResponse res = new RefSetResponse();
            res.setName(refSet.getName());
            res.setValidityUntil(MappingUtil.toDate(refSet.getValidityUntil()));
            res.setCreationDate(MappingUtil.toDate(refSet.getCreationDate()));
            if ( refSet.getInstitution() != null ) {
                res.setInstitution(refSet.getInstitution().getName());
            }
            return res;
        } else {
            return null;
        }
    }

}
