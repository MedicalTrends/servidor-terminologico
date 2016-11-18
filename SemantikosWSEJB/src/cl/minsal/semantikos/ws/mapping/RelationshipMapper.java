package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;
import cl.minsal.semantikos.ws.response.RelationshipAttributeResponse;
import cl.minsal.semantikos.ws.response.RelationshipResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Development on 2016-10-14.
 *
 */
public class RelationshipMapper {

    public static RelationshipResponse map(Relationship relationship) {
        if ( relationship != null ) {
            RelationshipResponse res = new RelationshipResponse();
            res.setRelationshipDefinition(RelationshipDefinitionMapper.map(relationship.getRelationshipDefinition()));
            res.setTarget(TargetMapper.map(relationship.getTarget()));
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
