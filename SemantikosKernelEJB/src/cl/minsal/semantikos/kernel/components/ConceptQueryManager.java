package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.browser.ConceptQuery;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by BluePrints Developer on 21-09-2016.
 */
@Local
public interface ConceptQueryManager {

    public ConceptQuery getDefaultQueryByCategory(Category category);

    public List<ConceptSMTK> executeQuery(ConceptQuery query);

    public int countConceptQuery(ConceptQuery query);

    public List<RelationshipDefinition> getSearchableAttributesByCategory(Category category);

    public List<RelationshipDefinition> getShowableAttributesByCategory(Category category);

}
