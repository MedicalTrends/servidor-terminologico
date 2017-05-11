package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.relationships.Target;

/**
 * Created by des01c7 on 04-01-17.
 */
public class ExtendedRelationshipAttributeDefinitionInfo extends ExtendedRelationshipDefinitionInfo {

    public static final ExtendedRelationshipAttributeDefinitionInfo DEFAULT_CONFIGURATION = new ExtendedRelationshipAttributeDefinitionInfo(PersistentEntity.NON_PERSISTED_ID, 0);

    public ExtendedRelationshipAttributeDefinitionInfo(long idComposite, int order) {
        super(idComposite,  order);
    }

    public ExtendedRelationshipAttributeDefinitionInfo(long idComposite, int order, Target defaultValue) {
        super(idComposite, order, defaultValue);
    }

}
