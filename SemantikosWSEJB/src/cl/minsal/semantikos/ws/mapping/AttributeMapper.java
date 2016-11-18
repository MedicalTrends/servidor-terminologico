package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.ws.response.AttributeResponse;

/**
 * Created by Development on 2016-10-20.
 *
 */
public class AttributeMapper {

    public static AttributeResponse map(Relationship relationship) {
        if ( relationship != null ) {
            AttributeResponse res = new AttributeResponse();
            if ( relationship.getRelationshipDefinition() != null ) {
                res.setName(relationship.getRelationshipDefinition().getName());
            }
            if ( relationship.getTarget() != null
                    && relationship.getTarget() instanceof BasicTypeValue ) {
                BasicTypeValue btv = (BasicTypeValue) relationship.getTarget();
                if ( btv.getValue() != null ) {
                    res.setValue(String.valueOf(btv.getValue()));
                }
                if (  btv.getTargetType() != null ) {
                    res.setType(btv.getTargetType().toString());
                }
            }
            return res;
        }
        return null;
    }

}
