package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;
import cl.minsal.semantikos.modelws.response.RelationshipAttributeResponse;

/**
 * Created by Development on 2016-10-14.
 *
 */
public class RelationshipAttributeMapper {

    public static RelationshipAttributeResponse map(RelationshipAttribute relationshipAttribute) {
        if ( relationshipAttribute != null ) {
            RelationshipAttributeResponse res = new RelationshipAttributeResponse();

            if ( relationshipAttribute.getTarget() != null
                    && relationshipAttribute.getTarget() instanceof BasicTypeValue) {
//                res.setType(String.valueOf(relationshipAttribute.getTarget().getTargetType()));
                res.setValue(String.valueOf(((BasicTypeValue) relationshipAttribute.getTarget()).getValue()));
                if ( relationshipAttribute.getRelationAttributeDefinition() != null ) {
                    res.setName(relationshipAttribute.getRelationAttributeDefinition().getName());
                }
            } else {
                res.setTarget(TargetMapper.map(relationshipAttribute.getTarget(), relationshipAttribute.getRelationAttributeDefinition().getTargetDefinition()));
                res.setRelationshipAttributeDefinition(RelationshipAttributeDefinitionMapper.map(relationshipAttribute.getRelationAttributeDefinition()));
                res.setRelationship(RelationshipMapper.map(relationshipAttribute.getRelationship(), relationshipAttribute.getRelationAttributeDefinition().getTargetDefinition()));
            }

            return res;
        }

        return null;
    }

}
