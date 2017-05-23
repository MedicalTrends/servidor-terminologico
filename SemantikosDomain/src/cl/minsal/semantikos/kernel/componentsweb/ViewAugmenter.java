package cl.minsal.semantikos.kernel.componentsweb;

import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.modelweb.ConceptSMTKWeb;
import cl.minsal.semantikos.modelweb.RelationshipDefinitionWeb;

import java.util.Map;

/**
 * @author Andrés Farías on 10/5/16.
 */
public interface ViewAugmenter {

    /**
     * Blablabla
     *
     * @param category
     * @param relationshipDefinition bla bla
     * @return blabla
     */
    public RelationshipDefinitionWeb augmentRelationshipDefinition(Category category, RelationshipDefinition relationshipDefinition);

    public ConceptSMTKWeb augmentConcept(Category category, ConceptSMTKWeb concept);

    public Map<Long, Relationship> augmentRelationships(Category category, Map<Long, Relationship> relationships);
}
