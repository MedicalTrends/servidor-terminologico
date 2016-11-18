package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.relationships.BasicTypeType;
import cl.minsal.semantikos.ws.response.BasicTypeTypeResponse;

/**
 * Created by Development on 2016-10-14.
 *
 */
public class BasicTypeTypeMapper {

    public static BasicTypeTypeResponse map(BasicTypeType basicTypeType) {
        if ( basicTypeType != null ) {
            BasicTypeTypeResponse res = new BasicTypeTypeResponse();
            res.setTypeName(basicTypeType.getTypeName());
            return res;
        }
        return null;
    }

}
