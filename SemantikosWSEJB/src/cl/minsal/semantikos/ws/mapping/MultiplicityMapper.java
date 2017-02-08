package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.Multiplicity;
import cl.minsal.semantikos.ws.response.MultiplicityResponse;

/**
 * Created by Development on 2016-10-14.
 *
 */
public class MultiplicityMapper {

    public static MultiplicityResponse map(Multiplicity multiplicity) {
        if ( multiplicity != null ) {
            MultiplicityResponse res = new MultiplicityResponse();
            res.setLowerBoundary(multiplicity.getLowerBoundary());
            res.setUpperBoundary(multiplicity.getUpperBoundary());
            return res;
        }

        return null;
    }

}
