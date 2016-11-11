package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.basictypes.Interval;
import cl.minsal.semantikos.ws.response.IntervalResponse;

/**
 * Created by Development on 2016-10-14.
 *
 */
public class IntervalMapper {

    public static IntervalResponse map(Interval interval) {
        if ( interval != null ) {
            IntervalResponse res = new IntervalResponse();
            if ( interval.getLowerBoundary() != null ) {
                res.setLowerBoundary(String.valueOf(interval.getLowerBoundary()));
            }
            if ( interval.getUpperBoundary() != null ) {
                res.setUpperBoundary(String.valueOf(interval.getUpperBoundary()));
            }
            return res;
        }
        return null;
    }

}
