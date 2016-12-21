package cl.minsal.semantikos.model;

import cl.minsal.semantikos.kernel.daos.DAO;
import cl.minsal.semantikos.model.crossmaps.CrossMapType;

/**
 * @author Andrés Farías on 8/29/16.
 */
public abstract class PersistentEntity implements IPersistentEntity {

    /** Constante para indicar un valor por defecto que indique NO persistencia */
    public static final long NON_PERSISTED_ID = DAO.NON_PERSISTED_ID;

    /** El identificador único de la entidad, inicialmente fijado en <code>NON_PERSISTED_ID</code>. */
    private long id = NON_PERSISTED_ID;

    public PersistentEntity() {
        this(NON_PERSISTED_ID);
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
        return this.getId() != NON_PERSISTED_ID;
    }

    public PersistentEntity(long id) {
        this.id = id;
    }
}
