package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.ConceptQueryDAO;
import cl.minsal.semantikos.kernel.daos.RelationshipDAO;
import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.MultiplicityFactory;
import cl.minsal.semantikos.model.browser.ConceptQuery;
import cl.minsal.semantikos.model.browser.ConceptQueryColumn;
import cl.minsal.semantikos.model.browser.ConceptQueryFilter;
import cl.minsal.semantikos.model.browser.Sort;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BluePrints Developer on 21-09-2016.
 */
@Stateless
public class ConceptQueryManagerImpl implements ConceptQueryManager{


    @EJB
    ConceptQueryDAO conceptQueryDAO;

    @EJB
    private ConceptManager conceptManager;

    @EJB
    private CategoryManager categoryManager;

    @EJB
    private RelationshipDAO relationshipDAO;

    @Override
    public ConceptQuery getDefaultQueryByCategory(Category category) {

        ConceptQuery query = new ConceptQuery();

        List<Category> categories = new ArrayList<Category>();
        categories.add(category);
        query.setCategories(categories);

        List<ConceptQueryFilter> filters = new ArrayList<ConceptQueryFilter>();
        query.setFilters(filters);

        // Stablishing custom filtering value
        query.setCustomFilterable(getCustomFilteringValue(category));

        // Adding dynamic columns
        for (RelationshipDefinition relationshipDefinition : getShowableAttributesByCategory(category)) {
            query.getColumns().add(new ConceptQueryColumn(relationshipDefinition.getName(), new Sort(null, false), relationshipDefinition));

        }

        // Adding second order columns, if this apply
        for (RelationshipDefinition relationshipDefinition : category.getRelationshipDefinitions() ) {
            if(relationshipDefinition.getTargetDefinition().isSMTKType()){
                Category categoryDestination = (Category) relationshipDefinition.getTargetDefinition();
                for (RelationshipDefinition relationshipDefinitionDestination : getSecondOrderShowableAttributesByCategory(categoryDestination)) {
                    ConceptQueryColumn secondOrderColumn = new ConceptQueryColumn(relationshipDefinitionDestination.getName(), new Sort(null, false), relationshipDefinitionDestination);
                    query.getColumns().add(secondOrderColumn);
                    secondOrderColumn.setSecondOrder(true);
                }
            }
        }

        // Adding related concepts category to columns, if this apply
        if(getShowableRelatedConceptsValue(category)){
            for (Category relatedCategory : categoryManager.getRelatedCategories(category)) {
                if(getShowableValue(relatedCategory)) {
                    RelationshipDefinition rd = new RelationshipDefinition(relatedCategory.getId(), relatedCategory.getName(), relatedCategory.getName(), relatedCategory, MultiplicityFactory.ONE_TO_ONE);
                    query.getColumns().add(new ConceptQueryColumn(rd.getName(), new Sort(null, false), rd));
                }
            }
        }

        // Adding dynamic filters
        for (RelationshipDefinition relationshipDefinition : getSearchableAttributesByCategory(category)) {
            ConceptQueryFilter conceptQueryFilter = new ConceptQueryFilter(relationshipDefinition);
            conceptQueryFilter.setMultiple(getMultipleFilteringValue(category, relationshipDefinition));
            query.getFilters().add(conceptQueryFilter);
        }

        return query;
    }

    @Override
    public List<ConceptSMTK> executeQuery(ConceptQuery query) {

        //return conceptQueryDAO.callQuery(query);
        List<ConceptSMTK> conceptSMTKs = conceptQueryDAO.executeQuery(query);

        for (ConceptSMTK conceptSMTK : conceptSMTKs) {
            if(!query.getColumns().isEmpty()) {
                conceptSMTK.setRelationships(conceptManager.loadRelationships(conceptSMTK));
                Category category = query.getCategories().get(0);
                List<RelationshipDefinition> secondOrderAttributes = query.getSecondOrderDefinitions();
                // Adding second order columns, if this apply
                List<Relationship> secondOrderRelationships = new ArrayList<>();
                for (RelationshipDefinition relationshipDefinition : getSourceSecondOrderShowableAttributesByCategory(category)) {
                    for (Relationship firstOrderRelationship : conceptSMTK.getRelationshipsByRelationDefinition(relationshipDefinition)) {
                        ConceptSMTK targetConcept = (ConceptSMTK)firstOrderRelationship.getTarget();
                        for (Relationship secondOrderRelationship : relationshipDAO.getRelationshipsBySourceConcept(targetConcept.getId())) {
                            if(secondOrderAttributes.contains(secondOrderRelationship.getRelationshipDefinition()))
                                secondOrderRelationships.add(secondOrderRelationship);
                        }
                    }
                }
                conceptSMTK.getRelationships().addAll(secondOrderRelationships);
                // Adding related concepts to relationships, if this apply
                if(getShowableRelatedConceptsValue(category)){
                    for (ConceptSMTK relatedConcept : conceptManager.getRelatedConcepts(conceptSMTK)) {
                        RelationshipDefinition rd = new RelationshipDefinition(relatedConcept.getCategory().getId(), relatedConcept.getCategory().getName(), relatedConcept.getCategory().getName(), relatedConcept.getCategory(), MultiplicityFactory.ONE_TO_ONE);
                        conceptSMTK.addRelationship(new Relationship(conceptSMTK, relatedConcept, rd, new ArrayList<RelationshipAttribute>(), null));
                    }
                }
            }
        }

        return conceptSMTKs;

    }

    @Override
    public int countConceptQuery(ConceptQuery query) {
        return (int)conceptQueryDAO.countConceptByQuery(query);
    }

    @Override
    public List<RelationshipDefinition> getShowableAttributesByCategory(Category category) {
        return conceptQueryDAO.getShowableAttributesByCategory(category);
    }

    public List<RelationshipDefinition> getSecondOrderShowableAttributesByCategory(Category category){
        return conceptQueryDAO.getSecondOrderShowableAttributesByCategory(category);
    }

    public List<RelationshipDefinition> getSourceSecondOrderShowableAttributesByCategory(Category category){
        List<RelationshipDefinition> someRelationshipDefinitions = new ArrayList<>();
        for (RelationshipDefinition relationshipDefinition : category.getRelationshipDefinitions() ) {
            if(relationshipDefinition.getTargetDefinition().isSMTKType()){
                Category categoryDestination = (Category) relationshipDefinition.getTargetDefinition();
                if(!getSecondOrderShowableAttributesByCategory(categoryDestination).isEmpty())
                    someRelationshipDefinitions.add(relationshipDefinition);
            }
        }
        return someRelationshipDefinitions;
    }

    @Override
    public List<RelationshipDefinition> getSearchableAttributesByCategory(Category category) {
        return conceptQueryDAO.getSearchableAttributesByCategory(category);
    }

    private boolean getCustomFilteringValue(Category category){
        return conceptQueryDAO.getCustomFilteringValue(category);
    }

    private boolean getMultipleFilteringValue(Category category, RelationshipDefinition relationshipDefinition){
        return conceptQueryDAO.getMultipleFilteringValue(category, relationshipDefinition);
    }

    private boolean getShowableRelatedConceptsValue(Category category){
        return conceptQueryDAO.getShowableRelatedConceptsValue(category);
    }

    private boolean getShowableValue(Category category){
        return conceptQueryDAO.getShowableValue(category);
    }
}
