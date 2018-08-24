package cl.minsal.semantikos.model.crossmaps;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.relationships.Target;

/**
 * @author Andrés Farías on 11/3/16.
 */
public abstract class CrossmapSetMember extends PersistentEntity implements Target {

    CrossmapSet crossmapSet;

    public CrossmapSetMember() {}

    public CrossmapSetMember(long id) {
        super(id);
    }

    public CrossmapSetMember(long id, CrossmapSet crossmapSet) {
        super(id);
        this.crossmapSet = crossmapSet;
    }

    public CrossmapSetMember(CrossmapSet crossmapSet) {

        this.crossmapSet = crossmapSet;
    }

    public CrossmapSet getCrossmapSet() {

        return crossmapSet;
    }

    public void setCrossmapSet(CrossmapSet crossmapSet) {
        this.crossmapSet = crossmapSet;
    }
}
