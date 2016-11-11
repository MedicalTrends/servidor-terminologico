package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.Category;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.Description;
import cl.minsal.semantikos.model.RefSet;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.ws.response.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Development on 2016-10-13.
 *
 */
public class ConceptMapper {

    public static ConceptResponse map(ConceptSMTK conceptSMTK) {
        if ( conceptSMTK != null ) {
            ConceptResponse res = new ConceptResponse();
            res.setPublished(conceptSMTK.isPublished());
            res.setModeled(conceptSMTK.isModeled());
            res.setConceptId(conceptSMTK.getConceptID());
            res.setFullyDefined(conceptSMTK.isFullyDefined());
            res.setObservation(conceptSMTK.getObservation());
            res.setToBeConsulted(conceptSMTK.isToBeConsulted());
            res.setToBeReviewed(conceptSMTK.isToBeReviewed());
            res.setValidUntil(MappingUtil.toDate(conceptSMTK.getValidUntil()));
            return res;
        } else {
            return null;
        }
    }

    public static ConceptResponse appendDescriptions(ConceptResponse conceptResponse, ConceptSMTK conceptSMTK) {
        if ( conceptResponse != null
                && conceptSMTK != null
                && conceptSMTK.getDescriptions() != null ) {
            List<DescriptionResponse> descriptions = new ArrayList<>(conceptSMTK.getDescriptions().size());
            for ( Description description : conceptSMTK.getDescriptions() ) {
                descriptions.add(DescriptionMapper.map(description));
            }
            Collections.sort(descriptions);
            conceptResponse.setDescriptions(descriptions);
        }
        return conceptResponse;
    }

    public static ConceptResponse appendAttributes(ConceptResponse conceptResponse, ConceptSMTK conceptSMTK) {
        if ( conceptResponse != null
                && conceptSMTK != null  ) {
            List<Relationship> attributes = conceptSMTK.getRelationshipsBasicType();
            if ( attributes != null ) {
                List<AttributeResponse> attributeResponses = new ArrayList<>(attributes.size());
                for ( Relationship relationship : attributes ) {
                    attributeResponses.add(AttributeMapper.map(relationship));
                }
                conceptResponse.setAttributes(attributeResponses);
            }
        }
        return conceptResponse;
    }

    public static ConceptResponse appendRelationships(ConceptResponse conceptResponse, ConceptSMTK conceptSMTK) {
        if ( conceptResponse != null
                && conceptSMTK != null  ) {
            List<Relationship> relationships = conceptSMTK.getRelationshipsNonBasicType();
            if ( relationships != null ) {
                List<RelationshipResponse> relationshipResponses = new ArrayList<>(relationships.size());
                for ( Relationship relationship : relationships ) {
                    relationshipResponses.add(RelationshipMapper.map(relationship));
                }
                conceptResponse.setRelationships(relationshipResponses);
            }
        }
        return conceptResponse;
    }

    public static ConceptResponse appendCategory(ConceptResponse conceptResponse, ConceptSMTK conceptSMTK) {
        if ( conceptResponse != null
                && conceptSMTK != null ) {
            conceptResponse.setCategory(CategoryMapper.map(conceptSMTK.getCategory()));
        }
        return conceptResponse;
    }

    public static ConceptResponse appendRefSets(ConceptResponse conceptResponse, ConceptSMTK conceptSMTK) {
        if ( conceptResponse != null
                && conceptSMTK != null ) {
            List<RefSet> refSets = conceptSMTK.getRefsets();
            if ( refSets != null ) {
                List<RefSetResponse> res = new ArrayList<>(refSets.size());
                for ( RefSet refSet : refSets ) {
                    res.add(RefSetMapper.map(refSet));
                }
                conceptResponse.setRefsets(res);
            }
        }
        return conceptResponse;
    }

}
