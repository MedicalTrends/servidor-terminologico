package cl.minsal.semantikos.kernel.daos;

import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.browser.ConceptQuery;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by BluePrints Developer on 22-09-2016.
 */

@Local
public interface ConceptQueryDAO {

    //public List<ConceptSMTK> callQuery(ConceptQuery query);

    List<ConceptSMTK> executeQuery(ConceptQuery query);

    List<RelationshipDefinition> getSearchableAttributesByCategory(Category category);

    List<RelationshipDefinition> getShowableAttributesByCategory(Category category);

    boolean getCustomFilteringValue(Category category);

}
