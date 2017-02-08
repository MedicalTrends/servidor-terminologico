package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.QueryDAO;
import cl.minsal.semantikos.kernel.daos.RelationshipDAO;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.browser.*;
import cl.minsal.semantikos.model.relationships.*;
import sun.security.krb5.internal.crypto.Des;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by BluePrints Developer on 21-09-2016.
 */
@Stateless
public class QueryManagerImpl implements QueryManager {


    @EJB
    QueryDAO queryDAO;

    @EJB
    private ConceptManager conceptManager;

    @EJB
    private CategoryManager categoryManager;

    @EJB
    private RelationshipDAO relationshipDAO;

    @Override
    public GeneralQuery getDefaultGeneralQuery(Category category) {

        GeneralQuery query = new GeneralQuery();

        List<Category> categories = new ArrayList<Category>();
        categories.add(category);
        query.setCategories(categories);

        List<QueryFilter> filters = new ArrayList<QueryFilter>();
        query.setFilters(filters);

        // Stablishing custom filtering value
        query.setCustomFilterable(getCustomFilteringValue(category));

        // Adding dynamic columns
        for (RelationshipDefinition relationshipDefinition : getShowableAttributesByCategory(category)) {
            query.getColumns().add(new QueryColumn(relationshipDefinition.getName(), new Sort(null, false), relationshipDefinition));

        }

        // Adding second order columns, if this apply
        for (RelationshipDefinition relationshipDefinition : category.getRelationshipDefinitions() ) {
            if(relationshipDefinition.getTargetDefinition().isSMTKType()){
                Category categoryDestination = (Category) relationshipDefinition.getTargetDefinition();
                for (RelationshipDefinition relationshipDefinitionDestination : getSecondOrderShowableAttributesByCategory(categoryDestination)) {
                    QueryColumn secondOrderColumn = new QueryColumn(relationshipDefinitionDestination.getName(), new Sort(null, false), relationshipDefinitionDestination);
                    if(relationshipDefinitionDestination.isU_asist() && category.getNameAbbreviated().equals("MCCE")) {
                        continue;
                    }
                    if(relationshipDefinitionDestination.isCondicionDeVenta() && category.getNameAbbreviated().equals("PC")) {
                        continue;
                    }
                    else {
                        query.getColumns().add(secondOrderColumn);
                        secondOrderColumn.setSecondOrder(true);
                    }
                }
            }
        }

        // Adding related concepts category to columns, if this apply
        if(getShowableRelatedConceptsValue(category)){
            for (Category relatedCategory : categoryManager.getRelatedCategories(category)) {
                if(getShowableValue(relatedCategory)) {
                    RelationshipDefinition rd = new RelationshipDefinition(relatedCategory.getId(), relatedCategory.getName(), relatedCategory.getName(), relatedCategory, MultiplicityFactory.ONE_TO_ONE);
                    query.getColumns().add(new QueryColumn(rd.getName(), new Sort(null, false), rd));
                }
            }
        }

        // Adding dynamic filters
        for (RelationshipDefinition relationshipDefinition : getSearchableAttributesByCategory(category)) {
            QueryFilter queryFilter = new QueryFilter(relationshipDefinition);
            queryFilter.setMultiple(getMultipleFilteringValue(category, relationshipDefinition));
            query.getFilters().add(queryFilter);
        }

        // Adding second order filters, if this apply
        for (RelationshipDefinition relationshipDefinition : category.getRelationshipDefinitions() ) {
            if(relationshipDefinition.getTargetDefinition().isSMTKType()){
                Category categoryDestination = (Category) relationshipDefinition.getTargetDefinition();
                for (RelationshipDefinition relationshipDefinitionDestination : getSecondOrderSearchableAttributesByCategory(categoryDestination)) {
                    QueryFilter secondOrderQueryFilter = new QueryFilter(relationshipDefinitionDestination);
                    secondOrderQueryFilter.setMultiple(getMultipleFilteringValue(categoryDestination, relationshipDefinition));
                    secondOrderQueryFilter.setSecondOrder(true);
                    query.getFilters().add(secondOrderQueryFilter);
                }
            }
        }

        // Adding second derivate filters, if this apply
        for (RelationshipAttributeDefinition relationshipAttributeDefinition : getSecondDerivateSearchableAttributesByCategory(category)) {
            QueryFilterAttribute queryFilter = new QueryFilterAttribute(relationshipAttributeDefinition);
            query.getAttributeFilters().add(queryFilter);
        }

        return query;
    }

    @Override
    public DescriptionQuery getDefaultDescriptionQuery() {

        DescriptionQuery query = new DescriptionQuery();

        return query;
    }

    @Override
    public NoValidQuery getDefaultNoValidQuery() {

        NoValidQuery noValidQuery = new NoValidQuery();

        return noValidQuery;
    }

    @Override
    public PendingQuery getDefaultPendingQuery() {
        PendingQuery pendingQuery = new PendingQuery();

        return pendingQuery;
    }

    @Override
    public BrowserQuery getDefaultBrowserQuery() {
        return new BrowserQuery();
    }

    @Override
    public List<ConceptSMTK> executeQuery(GeneralQuery query) {

        //return conceptQueryDAO.callQuery(query);
        List<ConceptSMTK> conceptSMTKs = (List<ConceptSMTK>) (Object) queryDAO.executeQuery(query);

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
    public List<Description> executeQuery(DescriptionQuery query) {

        List<Description> descriptions = (List<Description>) (Object) queryDAO.executeQuery(query);

        for (Description description : descriptions) {

            Relationship otherThanFullyDefinitional = null;

            for (Relationship relationship : conceptManager.getRelationships(description.getConceptSMTK()) ) {

                if(relationship.getRelationshipDefinition().getTargetDefinition().isSnomedCTType()){

                    if(otherThanFullyDefinitional == null)
                        otherThanFullyDefinitional = relationship;

                    // Si existe una relación ES_UN_MAPEO, se agrega esta relación y se detiene la búsqueda
                    SnomedCTRelationship fullyDefinitional = (SnomedCTRelationship) relationship;

                    if(fullyDefinitional.isES_UN_MAPEO()) {
                        description.getConceptSMTK().setRelationships(Arrays.asList(relationship));
                        break;
                    }

                }

            }

            // Si no se encontró una relación ES_UN_MAPEO, se agrega la primera relación a SNOMED_CT encontrada
            if(!description.getConceptSMTK().isRelationshipsLoaded()){
                description.getConceptSMTK().setRelationships(Arrays.asList(otherThanFullyDefinitional));
            }

        }

        return descriptions;
        //return queryDAO.executeQuery(query);

    }

    @Override
    public List<NoValidDescription> executeQuery(NoValidQuery query) {

        List<NoValidDescription> noValidDescriptions = (List<NoValidDescription>) (Object) queryDAO.executeQuery(query);

        return noValidDescriptions;
    }

    @Override
    public List<PendingTerm> executeQuery(PendingQuery query) {

        List<PendingTerm> pendingTerms = (List<PendingTerm>) (Object) queryDAO.executeQuery(query);

        return pendingTerms;
    }

    @Override
    public List<ConceptSMTK> executeQuery(BrowserQuery query) {
        query.setQuery(conceptManager.standardizationPattern(query.getQuery()));
        return (List<ConceptSMTK>) (Object) queryDAO.executeQuery(query);
    }

    @Override
    public int countQueryResults(GeneralQuery query) {
        return (int)queryDAO.countByQuery(query);
    }

    @Override
    public int countQueryResults(DescriptionQuery query) {
        return (int)queryDAO.countByQuery(query);
    }

    @Override
    public int countQueryResults(NoValidQuery query) {
        return (int)queryDAO.countByQuery(query);
    }

    @Override
    public int countQueryResults(PendingQuery query) {
        return (int)queryDAO.countByQuery(query);
    }

    @Override
    public int countQueryResults(BrowserQuery query) {
        return (int)queryDAO.countByQuery(query);
    }

    @Override
    public List<RelationshipDefinition> getShowableAttributesByCategory(Category category) {
        return queryDAO.getShowableAttributesByCategory(category);
    }

    public List<RelationshipDefinition> getSecondOrderShowableAttributesByCategory(Category category){
        return queryDAO.getSecondOrderShowableAttributesByCategory(category);
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
        return queryDAO.getSearchableAttributesByCategory(category);
    }

    public List<RelationshipDefinition> getSecondOrderSearchableAttributesByCategory(Category category){
        return queryDAO.getSecondOrderSearchableAttributesByCategory(category);
    }

    public List<RelationshipAttributeDefinition> getSecondDerivateSearchableAttributesByCategory(Category category) {
        return queryDAO.getSecondDerivateSearchableAttributesByCategory(category);
    }

    private boolean getCustomFilteringValue(Category category) {
        return queryDAO.getCustomFilteringValue(category);
    }

    private boolean getMultipleFilteringValue(Category category, RelationshipDefinition relationshipDefinition) {
        return queryDAO.getMultipleFilteringValue(category, relationshipDefinition);
    }

    private boolean getShowableRelatedConceptsValue(Category category){
        return queryDAO.getShowableRelatedConceptsValue(category);
    }

    private boolean getShowableValue(Category category){
        return queryDAO.getShowableValue(category);
    }
}
