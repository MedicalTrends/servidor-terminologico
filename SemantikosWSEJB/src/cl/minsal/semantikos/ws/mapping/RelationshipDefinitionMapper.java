package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.relationships.RelationshipAttributeDefinition;
import cl.minsal.semantikos.model.relationships.RelationshipDefinition;
import cl.minsal.semantikos.ws.response.RelationshipAttributeDefinitionResponse;
import cl.minsal.semantikos.ws.response.RelationshipDefinitionResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Development on 2016-10-14.
 *
 */
public class RelationshipDefinitionMapper {

    public static RelationshipDefinitionResponse map(RelationshipDefinition relationshipDefinition) {
        if ( relationshipDefinition != null ) {
            RelationshipDefinitionResponse res = new RelationshipDefinitionResponse();
            res.setName(relationshipDefinition.getName());
            res.setDescription(relationshipDefinition.getDescription());
            res.setMultiplicity(MultiplicityMapper.map(relationshipDefinition.getMultiplicity()));
            res.setTargetDefinition(TargetDefinitionMapper.map(relationshipDefinition.getTargetDefinition()));
            if ( relationshipDefinition.getRelationshipAttributeDefinitions() != null ) {
                List<RelationshipAttributeDefinitionResponse> relationshipAttributeDefinitionResponses = new ArrayList<>(relationshipDefinition.getRelationshipAttributeDefinitions().size());
                for (RelationshipAttributeDefinition relationshipAttributeDefinition : relationshipDefinition.getRelationshipAttributeDefinitions() ) {
                    relationshipAttributeDefinitionResponses.add(RelationshipAttributeDefinitionMapper.map(relationshipAttributeDefinition));
                }
                res.setRelationshipAttributeDefinitionResponses(relationshipAttributeDefinitionResponses);
            }
            return res;
        }
        return null;
    }

}
