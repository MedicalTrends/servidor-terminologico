package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.basictypes.BasicTypeDefinition;
import cl.minsal.semantikos.ws.response.BasicTypeDefinitionResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Development on 2016-10-14.
 *
 */
public class BasicTypeDefinitionMapper {

    public static BasicTypeDefinitionResponse map(BasicTypeDefinition basicTypeDefinition) {
        if ( basicTypeDefinition != null ) {
            BasicTypeDefinitionResponse res = new BasicTypeDefinitionResponse();
            res.setName(basicTypeDefinition.getName());
            res.setDescription(basicTypeDefinition.getDescription());
            res.setInterval(IntervalMapper.map(basicTypeDefinition.getInterval()));
            res.setType(BasicTypeTypeMapper.map(basicTypeDefinition.getType()));
            if ( basicTypeDefinition.getDomain() != null ) {
                List<String> domains = new ArrayList<>(basicTypeDefinition.getDomain().size());
                for ( Object domain : basicTypeDefinition.getDomain() ) {
                    if ( domain != null ) {
                        domains.add(String.valueOf(domain));
                    }
                }
                res.setDomain(domains);
            }
            return res;
        }
        return null;
    }

}
