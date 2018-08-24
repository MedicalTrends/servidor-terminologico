package cl.minsal.semantikos.kernel.componentsweb;

import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.modelweb.ConceptSMTKWeb;
import cl.minsal.semantikos.modelweb.RelationshipDefinitionWeb;

import javax.ejb.Local;
import javax.ejb.Remote;
import java.util.List;
import java.util.Map;

/**
 * @author Andrés Farías on 10/5/16.
 */
@Remote
public interface ViewAugmenter {

    /**
     * Blablabla
     *
     * @param category
     * @param relationshipDefinition bla bla
     * @return blabla
     */
    public RelationshipDefinitionWeb augmentRelationshipDefinition(Category category, RelationshipDefinition relationshipDefinition);

    public ConceptSMTKWeb augmentConcept(ConceptSMTKWeb concept, List<RelationshipDefinitionWeb> relationshipDefinitionsWeb);

    public Map<Long, Relationship> augmentRelationships(Category category, Map<Long, Relationship> relationships);
}
