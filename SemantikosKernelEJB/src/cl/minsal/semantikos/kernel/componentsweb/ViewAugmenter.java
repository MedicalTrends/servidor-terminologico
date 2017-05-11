package cl.minsal.semantikos.kernel.componentsweb;

import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.model_web.ConceptSMTKWeb;
import cl.minsal.semantikos.model_web.RelationshipDefinitionWeb;

import javax.ejb.Local;
import java.util.Map;

/**
 * @author Andrés Farías on 10/5/16.
 */
@Local
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

    public void augmentRelationships(Category category, ConceptSMTKWeb concept, Map<Long, Relationship> relationships);
}
