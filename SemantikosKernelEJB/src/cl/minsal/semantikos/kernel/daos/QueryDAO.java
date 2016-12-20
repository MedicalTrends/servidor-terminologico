package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.browser.GeneralQuery;
import cl.minsal.semantikos.model.browser.DescriptionQuery;
import cl.minsal.semantikos.model.browser.NoValidQuery;
import cl.minsal.semantikos.model.browser.PendingQuery;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by BluePrints Developer on 22-09-2016.
 */

@Local
public interface QueryDAO {

    List<ConceptSMTK> executeQuery(GeneralQuery query);

    List<Description> executeQuery(DescriptionQuery query);

    List<NoValidDescription> executeQuery(NoValidQuery query);

    List<PendingTerm> executeQuery(PendingQuery query);

    long countByQuery(GeneralQuery query);

    long countByQuery(DescriptionQuery query);

    long countByQuery(NoValidQuery query);

    long countByQuery(PendingQuery query);

    List<RelationshipDefinition> getSearchableAttributesByCategory(Category category);

    List<RelationshipDefinition> getShowableAttributesByCategory(Category category);

    List<RelationshipDefinition> getSecondOrderShowableAttributesByCategory(Category category);

    boolean getCustomFilteringValue(Category category);

    boolean getMultipleFilteringValue(Category category, RelationshipDefinition relationshipDefinition);

    boolean getShowableRelatedConceptsValue(Category category);

    boolean getShowableValue(Category category);


}
