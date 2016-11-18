package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.relationships.RelationshipAttributeDefinition;
import cl.minsal.semantikos.ws.response.RelationshipAttributeDefinitionResponse;

/**
 * Created by Development on 2016-10-14.
 *
 */
public class RelationshipAttributeDefinitionMapper {

    public static RelationshipAttributeDefinitionResponse map(RelationshipAttributeDefinition relationshipAttributeDefinition) {
        if ( relationshipAttributeDefinition != null ) {
            RelationshipAttributeDefinitionResponse res = new RelationshipAttributeDefinitionResponse();
            res.setName(relationshipAttributeDefinition.getName());
            res.setMultiplicity(MultiplicityMapper.map(relationshipAttributeDefinition.getMultiplicity()));
            res.setTargetDefinition(TargetDefinitionMapper.map(relationshipAttributeDefinition.getTargetDefinition()));
            return res;
        }

        return null;
    }

}
