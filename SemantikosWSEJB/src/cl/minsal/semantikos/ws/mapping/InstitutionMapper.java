package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.Institution;
import cl.minsal.semantikos.ws.response.InstitutionResponse;

/**
 * Created by Development on 2016-10-13.
 *
 */
public class InstitutionMapper {

    public static InstitutionResponse map(Institution institution) {
        if ( institution != null ) {
            InstitutionResponse res = new InstitutionResponse();
            res.setName(institution.getName());
            return res;
        } else {
            return null;
        }
    }

}
