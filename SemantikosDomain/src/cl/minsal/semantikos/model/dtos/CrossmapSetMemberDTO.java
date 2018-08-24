package cl.minsal.semantikos.model.dtos;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.crossmaps.CrossmapSet;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetType;

import java.io.Serializable;

/**
 * @author Andrés Farías on 11/3/16.
 */
public class CrossmapSetMemberDTO implements TargetDTO, Serializable {

    /** ID de negocio */
    private long id;

    /** Terminología a la que pertenece */
    private long crossmapSetId;

    public CrossmapSetMemberDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCrossmapSetId() {
        return crossmapSetId;
    }

    public void setCrossmapSetId(long crossmapSetId) {
        this.crossmapSetId = crossmapSetId;
    }

}
