package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.modelws.response.RefSetResponse;

import java.sql.Timestamp;
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

            Timestamp validityUntil1 = refSet.getValidityUntil();
            if (validityUntil1 != null){
                Date validityUntil = new Date(validityUntil1.getTime());
                res.setValidityUntil(validityUntil);
                res.setValid(validityUntil.after(new Date()));
            }

//            res.setCreationDate(Util.toDate(refSet.getCreationDate()));
            if ( refSet.getInstitution() != null ) {
                res.setInstitution(refSet.getInstitution().getName());
            }
            return res;
        } else {
            /* TODO: ARCH! */
            return null;
        }
    }

}
