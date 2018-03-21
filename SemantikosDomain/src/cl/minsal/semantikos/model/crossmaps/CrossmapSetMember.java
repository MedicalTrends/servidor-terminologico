package cl.minsal.semantikos.model.crossmaps;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetType;

import java.io.Serializable;

/**
 * @author Andrés Farías on 11/3/16.
 */
public abstract class CrossmapSetMember extends PersistentEntity implements Target {

    public CrossmapSetMember() {
    }

    public CrossmapSetMember(long id) {
        super(id);
    }

}
