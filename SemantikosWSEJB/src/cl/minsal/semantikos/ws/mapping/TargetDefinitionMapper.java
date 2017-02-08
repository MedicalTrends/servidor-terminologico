package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.relationships.TargetDefinition;
import cl.minsal.semantikos.ws.response.TargetDefinitionResponse;

/**
 * Created by Development on 2016-10-14.
 *
 */
public class TargetDefinitionMapper {

    public static TargetDefinitionResponse map(TargetDefinition targetDefinition) {
        if ( targetDefinition != null ) {
            TargetDefinitionResponse res = new TargetDefinitionResponse();
            res.setBasicType(targetDefinition.isBasicType());
            res.setSMTKType(targetDefinition.isSMTKType());
            res.setHelperTable(targetDefinition.isHelperTable());
            res.setSnomedCTType(targetDefinition.isSnomedCTType());
            res.setCrossMapType(targetDefinition.isCrossMapType());
            return res;
        }

        return null;
    }

}
