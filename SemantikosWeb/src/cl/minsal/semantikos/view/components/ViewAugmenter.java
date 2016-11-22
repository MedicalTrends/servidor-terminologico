package cl.minsal.semantikos.view.components;

import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.ConceptSMTKWeb;
import cl.minsal.semantikos.model.relationships.RelationshipAttributeDefinition;
import cl.minsal.semantikos.model.relationships.RelationshipDefinitionWeb;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;

import javax.ejb.Local;

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
}
