package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.queries.*;
import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.relationships.RelationshipAttributeDefinition;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by BluePrints Developer on 22-09-2016.
 */

@Local
public interface QueryDAO {


    List<Object> executeQuery(IQuery query);

    long countByQuery(IQuery query);

    List<RelationshipDefinition> getSearchableAttributesByCategory(Category category);

    List<RelationshipDefinition> getSecondOrderSearchableAttributesByCategory(Category category);

    List<RelationshipAttributeDefinition> getSecondDerivateSearchableAttributesByCategory(Category category);

    List<RelationshipDefinition> getShowableAttributesByCategory(Category category);

    List<RelationshipDefinition> getSecondOrderShowableAttributesByCategory(Category category);

    List<RelationshipDTO> getRelationshipsDTOByConcept(ConceptDTO concept);

    boolean getCustomFilteringValue(Category category);

    boolean getMultipleFilteringValue(Category category, RelationshipDefinition relationshipDefinition);

    boolean getShowableRelatedConceptsValue(Category category);

    boolean getShowableValue(Category category);

    int getCompositeValue(Category category, RelationshipDefinition relationshipDefinition);


}
