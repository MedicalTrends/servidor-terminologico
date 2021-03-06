package cl.minsal.semantikos.model;

import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang.ArrayUtils.EMPTY_LONG_ARRAY;
import static org.apache.commons.lang.ArrayUtils.EMPTY_LONG_OBJECT_ARRAY;

/**
 * @author Andrés Farías on 8/29/16.
 */
public abstract class PersistentEntity implements IPersistentEntity, Serializable {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PersistentEntity.class);

    /** Constante para indicar un valor por defecto que indique NO persistencia */
    public static final long NON_PERSISTED_ID = DAO.NON_PERSISTED_ID;

    /** El identificador único de la entidad, inicialmente fijado en <code>NON_PERSISTED_ID</code>. */
    private long id = NON_PERSISTED_ID;

    public PersistentEntity() {
        this(NON_PERSISTED_ID);
    }

    /**
     * Este método es responsable de crear una lista de ID's de BDD a partir de una lista de entidades persistibles.
     *
     * @param entities Las entidades persistibles.
     * @return Una lista de ID's asociadas 1-1 con las entidades persistibles.
     */
    public static List<Long> getIdList(Collection<? extends PersistentEntity> entities) {

        logger.debug("PersistentEntity.getIdList(" + entities + ")");

        /* Si no hay entidades que procesar, se retorna una lista vacía */
        if (entities == null || entities.isEmpty()){
            return Collections.emptyList();
        }

        List<Long> res = new ArrayList<>();
        for (PersistentEntity entity : entities) {
            if (entity != null) {
                res.add(entity.getId());
            }
        }

        return res;
    }

    public static Long[] getIdArray(@NotNull Collection<? extends PersistentEntity> entities) {
        if(entities==null) {
            return null;
        }
        return getIdList(entities).toArray(new Long[entities.size()]);
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Este método es responsable de retornar el Identificador único de la Entidad en la base de datos.
     *
     * @return El Identificador único en la base de datos o el Identificador Nulo (<code>NON_PERSISTED_ID</code>).
     */
    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public boolean isPersistent() {
        return getId() != NON_PERSISTED_ID;
    }

    public PersistentEntity(long id) {
        this.id = id;
    }
}
