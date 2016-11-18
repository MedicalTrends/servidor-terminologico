package cl.minsal.semantikos.ws.mapping;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Development on 2016-10-13.
 *
 */
public class MappingUtil {

    public static Date toDate(Timestamp timestamp) {
        if ( timestamp != null ) {
            return new Date(timestamp.getTime());
        } else {
            return null;
        }
    }

}
