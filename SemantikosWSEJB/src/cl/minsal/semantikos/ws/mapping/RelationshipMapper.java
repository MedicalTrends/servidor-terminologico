package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;
import cl.minsal.semantikos.model.relationships.TargetDefinition;
import cl.minsal.semantikos.ws.modelws.response.RelationshipAttributeResponse;
import cl.minsal.semantikos.ws.modelws.response.RelationshipResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Development on 2016-10-14.
 *
 */
public class RelationshipMapper {

    public static RelationshipResponse map(Relationship relationship, TargetDefinition targetDefinition) {
        if ( relationship != null ) {
            RelationshipResponse res = new RelationshipResponse();

            res.setTarget(TargetMapper.map(relationship.getTarget(), targetDefinition));

            if ( !(relationship.getTarget() instanceof ConceptSMTK) ) {
                res.setRelationshipDefinition(RelationshipDefinitionMapper.map(relationship.getRelationshipDefinition()));
            }

            if ( relationship.getRelationshipAttributes() != null ) {
                List<RelationshipAttributeResponse> relationshipAttributeResponses = new ArrayList<>(relationship.getRelationshipAttributes().size());
                for ( RelationshipAttribute ra : relationship.getRelationshipAttributes() ) {
                    relationshipAttributeResponses.add(RelationshipAttributeMapper.map(ra));
                }
                res.setRelationshipAttribute(relationshipAttributeResponses);
            }

            return res;
        }
        return null;
    }

}
