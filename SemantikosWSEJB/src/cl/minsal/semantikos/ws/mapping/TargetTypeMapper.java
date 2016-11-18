package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.basictypes.BasicTypeDefinition;
import cl.minsal.semantikos.model.relationships.TargetDefinition;
import cl.minsal.semantikos.model.relationships.TargetType;
import cl.minsal.semantikos.ws.response.BasicTypeDefinitionResponse;
import cl.minsal.semantikos.ws.response.TargetDefinitionResponse;
import cl.minsal.semantikos.ws.response.TargetTypeResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Development on 2016-10-14.
 *
 */
public class TargetTypeMapper {

    public static TargetTypeResponse map(TargetType targetType) {
        if ( targetType != null ) {
            TargetTypeResponse res = new TargetTypeResponse();

            res.setType(targetType.toString());
            res.setName(targetType.name());
            res.setDescription(targetType.getDescription());

            if ( targetType.getTargetDefinitions() != null ) {
                List<TargetDefinitionResponse> targetDefinitions = new ArrayList<>(targetType.getTargetDefinitions().size());
                for (TargetDefinition targetDefinition : targetType.getTargetDefinitions() ) {
                    targetDefinitions.add(TargetDefinitionMapper.map(targetDefinition));
                }
                res.setTargetDefinitions(targetDefinitions);
            }

            if ( targetType.getBasicTypeDefinitions() != null ) {
                List<BasicTypeDefinitionResponse> basicTypeDefinitions = new ArrayList<>(targetType.getTargetDefinitions().size());
                for (BasicTypeDefinition basicTypeDefinition : targetType.getBasicTypeDefinitions()) {
                    basicTypeDefinitions.add(BasicTypeDefinitionMapper.map(basicTypeDefinition));
                }
                res.setBasicTypeDefinitions(basicTypeDefinitions);
            }

            return res;
        }
        return null;
    }

}
