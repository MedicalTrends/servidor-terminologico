package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.businessrules.ConceptSearchBR;
import cl.minsal.semantikos.kernel.businessrules.DescriptionSearchBR;
import cl.minsal.semantikos.kernel.daos.QueryDAO;
import cl.minsal.semantikos.kernel.factories.QueryFactory;
import cl.minsal.semantikos.model.*;

import cl.minsal.semantikos.model.categories.Category;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.NoValidDescription;
import cl.minsal.semantikos.model.descriptions.PendingTerm;
import cl.minsal.semantikos.model.queries.*;
import cl.minsal.semantikos.model.relationships.*;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.EMPTY_LIST;

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

    @EJB
    private DescriptionSearchBR descriptionSearchBR;

    @Resource
    private SessionContext ctx;

    @Override
    public GeneralQuery getDefaultGeneralQuery(Category category) {
        GeneralQuery query = QueryFactory.getInstance().findQueryByCategory(category);
        query.resetQuery();
        return query;
    }

    @Override
    public DescriptionQuery getDefaultDescriptionQuery() {

        DescriptionQuery query = new DescriptionQuery();
        query.resetQuery();
        return query;
    }

    @Override
    public NoValidQuery getDefaultNoValidQuery() {

        NoValidQuery noValidQuery = new NoValidQuery();
        noValidQuery.resetQuery();
        return noValidQuery;
    }

    @Override
    public PendingQuery getDefaultPendingQuery() {
        PendingQuery pendingQuery = new PendingQuery();
        pendingQuery.resetQuery();
        return pendingQuery;
    }

    @Override
    public BrowserQuery getDefaultBrowserQuery() {
        BrowserQuery browserQuery = new BrowserQuery();
        browserQuery.resetQuery();
        return browserQuery;
    }

    @Override
    public SnomedQuery getDefaultSnomedQuery() {
        SnomedQuery snomedQuery = new SnomedQuery();
        snomedQuery.resetQuery();
        return snomedQuery;
    }

    @Override
    public List<ConceptSMTK> executeQuery(GeneralQuery query) {

        /*
        if(query.getQuery().trim().isEmpty()) {
            return EMPTY_LIST;
        }
        */

        query.setQuery(descriptionSearchBR.escapeSpecialCharacters(query.getQuery()));

        List<ConceptSMTK> conceptSMTKs = (List<ConceptSMTK>) (Object) queryDAO.executeQuery(query);

        if(conceptSMTKs.isEmpty()) {
            query.setQuery(descriptionSearchBR.removeStopWords(query.getQuery()));
            conceptSMTKs = (List<ConceptSMTK>) (Object) queryDAO.executeQuery(query);
        }

        boolean showRelatedConcepts = query.isShowRelatedConcepts();//getShowableRelatedConceptsValue(category);
        List<RelationshipDefinition> sourceSecondOrderShowableAttributes = query.getSourceSecondOrderShowableAttributes();//getSourceSecondOrderShowableAttributesByCategory(category);

        for (ConceptSMTK conceptSMTK : conceptSMTKs) {

            if(!query.getColumns().isEmpty()) {

                conceptSMTK.setRelationships(queryDAO.getRelationshipsByColumns(conceptSMTK, query));
                //conceptSMTK.setRelationships(relationshipManager.getRelationshipsBySourceConcept(conceptSMTK));

                // Adding second order columns, if this apply

                List<Relationship> secondOrderRelationships = new ArrayList<>();

                for (RelationshipDefinition secondOrderAttributes : query.getSecondOrderDefinitions()) {

                    for (RelationshipDefinition relationshipDefinition : sourceSecondOrderShowableAttributes) {

                        for (Relationship firstOrderRelationship : conceptSMTK.getRelationshipsByRelationDefinition(relationshipDefinition)) {

                            ConceptSMTK targetConcept = (ConceptSMTK)firstOrderRelationship.getTarget();

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

        /*
        if(query.getQuery().trim().isEmpty()) {
            return EMPTY_LIST;
        }
        */

        query.setQuery(descriptionSearchBR.escapeSpecialCharacters(query.getQuery()));

        List<Description> descriptions = (List<Description>) (Object) queryDAO.executeQuery(query);

        if(descriptions.isEmpty()) {
            query.setQuery(descriptionSearchBR.removeStopWords(query.getQuery()));
            descriptions = (List<Description>) (Object) queryDAO.executeQuery(query);
        }

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
            if(!description.getConceptSMTK().isRelationshipsLoaded()) {
                description.getConceptSMTK().setRelationships(Arrays.asList(otherThanFullyDefinitional));
            }

        }

        return descriptions;
        //return queryDAO.executeQuery(query);

    }

    @Override
    public List<NoValidDescription> executeQuery(NoValidQuery query) {

        /*
        if(query.getQuery().trim().isEmpty()) {
            return EMPTY_LIST;
        }
        */

        query.setQuery(descriptionSearchBR.escapeSpecialCharacters(query.getQuery()));

        List<NoValidDescription> noValidDescriptions = (List<NoValidDescription>) (Object) queryDAO.executeQuery(query);

        if(noValidDescriptions.isEmpty()) {
            query.setQuery(descriptionSearchBR.removeStopWords(query.getQuery()));
            noValidDescriptions = (List<NoValidDescription>) (Object) queryDAO.executeQuery(query);
        }

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

        /*
        if(query.getQuery().trim().isEmpty()) {
            return EMPTY_LIST;
        }
        */

        query.setQuery(descriptionSearchBR.escapeSpecialCharacters(query.getQuery()));

        List<PendingTerm> pendingTerms = (List<PendingTerm>) (Object) queryDAO.executeQuery(query);

        if(pendingTerms.isEmpty()) {
            query.setQuery(descriptionSearchBR.removeStopWords(query.getQuery()));
            pendingTerms = (List<PendingTerm>) (Object) queryDAO.executeQuery(query);
        }

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

        //Principal principal = ctx.getCallerPrincipal();

        if(query.getQuery().trim().isEmpty()) {
            return EMPTY_LIST;
        }

        query.setQuery(descriptionSearchBR.escapeSpecialCharacters(query.getQuery()));

        List<ConceptSMTK> concepts = (List<ConceptSMTK>) (Object) queryDAO.executeQuery(query);

        if(concepts.isEmpty()) {
            query.setQuery(descriptionSearchBR.removeStopWords(query.getQuery()));
            concepts = (List<ConceptSMTK>) (Object) queryDAO.executeQuery(query);
        }

        /*
        if(concepts.isEmpty()) {
            query.setTruncateMatch(true);
            concepts = (List<ConceptSMTK>) (Object) queryDAO.executeQuery(query);
        }
        */

        return concepts;
    }

    @Override
    public List<ConceptSCT> executeQuery(SnomedQuery query) {

        //query.setQuery(conceptSearchBR.standardizationPattern(query.getQuery()));

        //Principal principal = ctx.getCallerPrincipal();

        if(query.getQuery().trim().isEmpty()) {
            return EMPTY_LIST;
        }

        List<ConceptSCT> concepts = (List<ConceptSCT>) (Object) queryDAO.executeQuery(query);

        if(concepts.isEmpty()) {
            query.setQuery(descriptionSearchBR.removeStopWords(query.getQuery()));
            concepts = (List<ConceptSCT>) (Object) queryDAO.executeQuery(query);
        }

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

        if(query instanceof BrowserQuery || query instanceof SnomedQuery) {
            if(query.getQuery().trim().isEmpty()) {
                return 0;
            }
        }

        query.setQuery(descriptionSearchBR.escapeSpecialCharacters(query.getQuery()));
        int quantity = (int) queryDAO.countByQuery(query);

        if(quantity == 0) {
            query.setQuery(descriptionSearchBR.removeStopWords(query.getQuery()));
            quantity = (int) queryDAO.countByQuery(query);
        }

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
