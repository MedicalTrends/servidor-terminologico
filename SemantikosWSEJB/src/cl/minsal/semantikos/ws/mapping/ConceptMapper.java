package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.Description;
import cl.minsal.semantikos.model.DescriptionType;
import cl.minsal.semantikos.model.RefSet;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.SnomedCTRelationship;
import cl.minsal.semantikos.ws.response.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Development on 2016-10-13.
 *
 */
public class ConceptMapper {

    public static List<AttributeResponse> getAttributes(ConceptSMTK conceptSMTK) {
        if ( conceptSMTK != null ) {
            List<AttributeResponse> attributeResponses = new ArrayList<>();

            for ( Relationship relationship : conceptSMTK.getRelationships() ) {
                attributeResponses.add(AttributeMapper.map(relationship));
            }

            return attributeResponses;
        }
        return null;
    }

    public static ConceptResponse appendAttributes(ConceptResponse conceptResponse, ConceptSMTK conceptSMTK) {
        if ( conceptResponse != null ) {
            conceptResponse.setAttributes(getAttributes(conceptSMTK));
        }
        return conceptResponse;
    }

    public static List<DescriptionResponse> getDescriptions(ConceptSMTK conceptSMTK) {
        if ( conceptSMTK != null && conceptSMTK.getDescriptions() != null ) {
            List<DescriptionResponse> descriptions = new ArrayList<>(conceptSMTK.getDescriptions().size());
            for ( Description description : conceptSMTK.getDescriptions() ) {
                descriptions.add(DescriptionMapper.map(description));
            }
            Collections.sort(descriptions);
            return descriptions;
        }
        return null;
    }

    private static List<DescriptionResponse> getPreferredDescriptions(ConceptSMTK conceptSMTK) {
        if ( conceptSMTK != null && conceptSMTK.getDescriptions() != null ) {
            List<DescriptionResponse> descriptions = new ArrayList<>(conceptSMTK.getDescriptions().size());
            for ( Description description : conceptSMTK.getDescriptions() ) {
                if ( DescriptionType.PREFERIDA.equals(description.getDescriptionType()) ) {
                    descriptions.add(DescriptionMapper.map(description));
                }
            }
            Collections.sort(descriptions);
            return descriptions;
        }
        return null;
    }

    public static ConceptResponse appendDescriptions(ConceptResponse conceptResponse, ConceptSMTK conceptSMTK) {
        if ( conceptResponse != null ) {
            conceptResponse.setDescriptions(getDescriptions(conceptSMTK));
        }
        return conceptResponse;
    }

    public static ConceptResponse appendPreferredDescriptions(ConceptResponse conceptResponse, ConceptSMTK conceptSMTK) {
        if ( conceptResponse != null ) {
            conceptResponse.setDescriptions(getPreferredDescriptions(conceptSMTK));
        }
        return conceptResponse;
    }

    public static List<RelationshipResponse> getRelationships(ConceptSMTK conceptSMTK) {
        if ( conceptSMTK != null  ) {
            List<Relationship> relationships = conceptSMTK.getRelationshipsNonBasicType();
            if ( relationships != null ) {
                List<RelationshipResponse> relationshipResponses = new ArrayList<>(relationships.size());
                for ( Relationship relationship : relationships ) {
                    relationshipResponses.add(RelationshipMapper.map(relationship, relationship.getRelationshipDefinition().getTargetDefinition()));
                }
                return relationshipResponses;
            }
        }
        return null;
    }

    public static ConceptResponse appendRelationships(ConceptResponse conceptResponse, ConceptSMTK conceptSMTK) {
        if ( conceptResponse != null ) {
            conceptResponse.setRelationships(getRelationships(conceptSMTK));
        }
        return conceptResponse;
    }

    public static List<RefSetResponse> getRefSets(ConceptSMTK conceptSMTK) {
        if ( conceptSMTK != null ) {
            List<RefSet> refSets = conceptSMTK.getRefsets();
            if ( refSets != null ) {
                List<RefSetResponse> res = new ArrayList<>(refSets.size());
                for ( RefSet refSet : refSets ) {
                    res.add(RefSetMapper.map(refSet));
                }
                return res;
            }
        }
        return null;
    }

    public static ConceptResponse appendRefSets(ConceptResponse conceptResponse, ConceptSMTK conceptSMTK) {
        if ( conceptResponse != null ) {
            conceptResponse.setRefsets(getRefSets(conceptSMTK));
        }
        return conceptResponse;
    }

    public static List<SnomedCTRelationshipResponse> getSnomedCTRelationships(ConceptSMTK source) {
        if (source != null) {
            List<SnomedCTRelationship> relationships = source.getRelationshipsSnomedCT();
            if ( relationships != null ) {
                List<SnomedCTRelationshipResponse> res = new ArrayList<>(relationships.size());
                for ( SnomedCTRelationship relationship : relationships ) {
                    res.add(SnomedCTRelationshipMapper.map(relationship));
                }
                return res;
            }
        }
        return null;
    }

    public static ConceptResponse appendSnomedCTRelationships(ConceptResponse conceptResponse, ConceptSMTK source) {
        if ( conceptResponse != null ) {
            conceptResponse.setSnomedCTRelationshipResponses(getSnomedCTRelationships(source));
        }

        return conceptResponse;
    }
}
