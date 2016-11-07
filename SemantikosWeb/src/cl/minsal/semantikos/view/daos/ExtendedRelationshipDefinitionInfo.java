package cl.minsal.semantikos.view.daos;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.relationships.Target;

/**
 * @author Andrés Farías on 10/6/16.
 */
public class ExtendedRelationshipDefinitionInfo {

    public static final ExtendedRelationshipDefinitionInfo DEFAULT_CONFIGURATION = new ExtendedRelationshipDefinitionInfo(PersistentEntity.NON_PERSISTED_ID, 0, null);

    /** Identificador del composite */
    private final long idComposite;

    /** El orden de la relación */
    private final int order;

    /** El valor por defecto de la relación */
    private final Target defaultValue;

    public ExtendedRelationshipDefinitionInfo(long idComposite, int order, Target defaultValue) {
        this.idComposite = idComposite;
        this.order = order;
        this.defaultValue = defaultValue;
    }

    public long getIdComposite() {
        return idComposite;
    }

    public int getOrder() {
        return order;
    }

    public Target getDefaultValue() {
        return defaultValue;
    }
}
