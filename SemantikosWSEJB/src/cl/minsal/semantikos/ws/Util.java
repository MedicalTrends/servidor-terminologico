package cl.minsal.semantikos.ws;

import cl.minsal.semantikos.model.PersistentEntity;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Development on 2016-11-24.
 *
 */
public class Util {

    public static List<Long> getIdList(@NotNull Collection<? extends PersistentEntity> entities) {
        List<Long> res = new ArrayList<>(entities.size());

        for ( PersistentEntity entity : entities ) {
            res.add(entity.getId());
        }

        return res;
    }

    public static Long[] getIdArray(@NotNull Collection<? extends PersistentEntity> entities) {
        return getIdList(entities).toArray(new Long[0]);
    }

    public static Date toDate(Timestamp timestamp) {
        if ( timestamp != null ) {
            return new Date(timestamp.getTime());
        } else {
            return null;
        }
    }
}
