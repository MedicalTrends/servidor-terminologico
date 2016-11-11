package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.relationships.RelationshipAttribute;
import cl.minsal.semantikos.ws.response.RelationshipAttributeResponse;

/**
 * Created by Development on 2016-10-14.
 *
 */
public class RelationshipAttributeMapper {

    public static RelationshipAttributeResponse map(RelationshipAttribute relationshipAttribute) {
        if ( relationshipAttribute != null ) {
            RelationshipAttributeResponse res = new RelationshipAttributeResponse();
            res.setTarget(TargetMapper.map(relationshipAttribute.getTarget()));
            res.setRelationshipAttributeDefinition(RelationshipAttributeDefinitionMapper.map(relationshipAttribute.getRelationAttributeDefinition()));
            res.setRelationship(RelationshipMapper.map(relationshipAttribute.getRelationship()));
            return res;
        }

        return null;
    }

}
