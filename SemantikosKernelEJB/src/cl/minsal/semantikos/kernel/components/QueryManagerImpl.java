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
import java.util.Map;

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
    public List<ConceptDTO> executeQuery(GeneralQuery query) {

        //return conceptQueryDAO.callQuery(query);
        List<ConceptDTO> conceptSMTKs = (List<ConceptDTO>) (Object) queryDAO.executeQuery(query);

        for (ConceptDTO conceptSMTK : conceptSMTKs) {

            if(!query.getColumns().isEmpty()) {

                //conceptSMTK.setRelationships(relationshipManager.getRelationshipsBySourceConcept(conceptSMTK));
                conceptSMTK.setRelationshipsDTO(queryDAO.getRelationshipsDTOByConcept(conceptSMTK));

                // Adding second order columns, if this apply
                List<Relationship> secondOrderRelationships = new ArrayList<>();

                for (RelationshipDefinition secondOrderAttributes : query.getSecondOrderDefinitions()) {

                    for (RelationshipDefinition relationshipDefinition : query.getSourceSecondOrderShowableAttributes()) {

                        for (RelationshipDTO firstOrderRelationship : conceptSMTK.getRelationshipsDTOByName(relationshipDefinition.getName())) {

                            TargetDTO targetConcept = firstOrderRelationship.getTarget();

                            for (Relationship secondOrderRelationship : relationshipDAO.getRelationshipsBySourceConcept(targetConcept.getId())) {

                                if(secondOrderAttributes.equals(secondOrderRelationship.getRelationshipDefinition())) {
                                    //secondOrderRelationships.add(secondOrderRelationship);
                                    conceptSMTK.getRelationshipsDTO().add(new RelationshipDTO(secondOrderRelationship));
                                }
                            }
                        }
                    }
                }

                //conceptSMTK.getRelationships().addAll(secondOrderRelationships);
                // Adding related concepts to relationships, if this apply
                if(query.isShowRelatedConcepts()) {
                    for (ConceptSMTK relatedConcept : conceptManager.getRelatedConcepts(conceptSMTK)) {
                        RelationshipDefinition rd = new RelationshipDefinition(relatedConcept.getCategory().getId(), relatedConcept.getCategory().getName(), relatedConcept.getCategory().getName(), relatedConcept.getCategory(), MultiplicityFactory.ONE_TO_ONE);
                        Relationship r = new Relationship(conceptSMTK, relatedConcept, rd, new ArrayList<RelationshipAttribute>(), null);
                        //conceptSMTK.addRelationship(new Relationship(conceptSMTK, relatedConcept, rd, new ArrayList<RelationshipAttribute>(), null));
                        conceptSMTK.getRelationshipsDTO().add(new RelationshipDTO(r));
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
    public int countQueryResults(Query query) {
        int quantity = (int)queryDAO.countByQuery(query);
        query.setTruncateMatch(false);
        return quantity;
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
