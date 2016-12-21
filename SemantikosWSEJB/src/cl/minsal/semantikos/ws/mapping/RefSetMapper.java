package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.RefSet;
import cl.minsal.semantikos.ws.Util;
import cl.minsal.semantikos.ws.response.RefSetResponse;

import java.util.Date;

/**
 * Created by Development on 2016-10-13.
 *
 */
public class RefSetMapper {

    public static RefSetResponse map(RefSet refSet) {
        if ( refSet != null ) {
            RefSetResponse res = new RefSetResponse();
            res.setName(refSet.getName());

            Date validityUntil = Util.toDate(refSet.getValidityUntil());
            if ( validityUntil != null ) {
                res.setValidityUntil(validityUntil);
                res.setValid(validityUntil.after(new Date()));
            } else {
                res.setValid(Boolean.TRUE);
            }

//            res.setCreationDate(Util.toDate(refSet.getCreationDate()));
            if ( refSet.getInstitution() != null ) {
                res.setInstitution(refSet.getInstitution().getName());
            }
            return res;
        } else {
            return null;
        }
    }

}
