package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.businessrules.ConceptSearchBR;
import cl.minsal.semantikos.kernel.daos.QueryDAO;
import cl.minsal.semantikos.kernel.factories.QueryFactory;
import cl.minsal.semantikos.model.*;

import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.NoValidDescription;
import cl.minsal.semantikos.model.descriptions.PendingTerm;
import cl.minsal.semantikos.model.queries.*;
import cl.minsal.semantikos.model.relationships.*;

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
    private RelationshipManager relationshipManager;

    @EJB
    private ConceptSearchBR conceptSearchBR;

    @Override
    public GeneralQuery getDefaultGeneralQuery(Category category) {
        GeneralQuery query = QueryFactory.getInstance().findQueryByCategory(category);
        query.resetQuery();
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

        List<ConceptSMTK> conceptSMTKs = (List<ConceptSMTK>) (Object) queryDAO.executeQuery(query);

        /*
        if(conceptSMTKs.isEmpty()) {
            query.setTruncateMatch(true);
            conceptSMTKs = (List<ConceptSMTK>) (Object) queryDAO.executeQuery(query);
        }
        */

        boolean showRelatedConcepts = query.isShowRelatedConcepts();//getShowableRelatedConceptsValue(category);
        List<RelationshipDefinition> sourceSecondOrderShowableAttributes = query.getSourceSecondOrderShowableAttributes();//getSourceSecondOrderShowableAttributesByCategory(category);

        for (ConceptSMTK conceptSMTK : conceptSMTKs) {

            if(!query.getColumns().isEmpty()) {

                //conceptSMTK.setRelationships(relationshipManager.getRelationshipsBySourceConcept(conceptSMTK));

                conceptSMTK.setRelationships(queryDAO.getRelationshipsByColumns(conceptSMTK, query));

                //query.getColumns().get(0).

                // Adding second order columns, if this apply

                List<Relationship> secondOrderRelationships = new ArrayList<>();

                for (RelationshipDefinition secondOrderAttributes : query.getSecondOrderDefinitions()) {

                    for (RelationshipDefinition relationshipDefinition : sourceSecondOrderShowableAttributes) {

                        for (Relationship firstOrderRelationship : conceptSMTK.getRelationshipsByRelationDefinition(relationshipDefinition)) {

                            ConceptSMTK targetConcept = (ConceptSMTK)firstOrderRelationship.getTarget();

                            //for (Relationship secondOrderRelationship : relationshipManager.getRelationshipsBySourceConcept(targetConcept)) {
                            for (Relationship secondOrderRelationship : queryDAO.getRelationshipsBySecondOrderColumns(targetConcept, query)) {

                                if(secondOrderAttributes.equals(secondOrderRelationship.getRelationshipDefinition())) {

                                    secondOrderRelationships.add(secondOrderRelationship);

                                }
                            }
                        }
                    }
                }

                conceptSMTK.getRelationships().addAll(secondOrderRelationships);
                // Adding related concepts to relationships, if this apply
                if(showRelatedConcepts) {
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
    public List<Description> executeQuery(DescriptionQuery query) throws Exception {

        List<Description> descriptions = (List<Description>) (Object) queryDAO.executeQuery(query);

        /*
        if(descriptions.isEmpty()) {
            query.setTruncateMatch(true);
            descriptions = (List<Description>) (Object) queryDAO.executeQuery(query);
        }
        */

        for (Description description : descriptions) {

            Relationship otherThanFullyDefinitional = null;

            for (Relationship relationship : relationshipManager.getRelationshipsBySourceConceptAndTargetType(description.getConceptSMTK(), TargetType.SnomedCT) ) {

                if(relationship.getRelationshipDefinition().getTargetDefinition().isSnomedCTType()){

                    if(otherThanFullyDefinitional == null) {
                        otherThanFullyDefinitional = relationship;
                    }

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

        /*
        if(noValidDescriptions.isEmpty()) {
            query.setTruncateMatch(true);
            noValidDescriptions = (List<NoValidDescription>) (Object) queryDAO.executeQuery(query);
        }
        */

        return noValidDescriptions;
    }

    @Override
    public List<PendingTerm> executeQuery(PendingQuery query) {

        List<PendingTerm> pendingTerms = (List<PendingTerm>) (Object) queryDAO.executeQuery(query);

        /*
        if(pendingTerms.isEmpty()) {
            query.setTruncateMatch(true);
            pendingTerms = (List<PendingTerm>) (Object) queryDAO.executeQuery(query);
        }
        */

        return pendingTerms;
    }

    @Override
    public List<ConceptSMTK> executeQuery(BrowserQuery query) {

        //query.setQuery(conceptSearchBR.standardizationPattern(query.getQuery()));

        List<ConceptSMTK> concepts = (List<ConceptSMTK>) (Object) queryDAO.executeQuery(query);

        /*
        if(concepts.isEmpty()) {
            query.setTruncateMatch(true);
            concepts = (List<ConceptSMTK>) (Object) queryDAO.executeQuery(query);
        }
        */

        return concepts;
    }

    @Override
    public int countQueryResults(Query query) {
        int quantity = (int)queryDAO.countByQuery(query);
        query.setTruncateMatch(false);
        return quantity;
    }

    @Override
    public List<QueryColumn> getShowableAttributesByCategory(Category category) {
        return queryDAO.getShowableAttributesByCategory(category);
    }

    @Override
    public List<RelationshipDefinition> getSearchableAttributesByCategory(Category category) {
        return queryDAO.getSearchableAttributesByCategory(category);
    }

}
