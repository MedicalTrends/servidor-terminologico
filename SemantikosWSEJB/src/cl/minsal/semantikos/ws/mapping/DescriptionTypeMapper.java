package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.descriptions.DescriptionType;
import cl.minsal.semantikos.modelws.response.DescriptionTypeResponse;

/**
 * Created by Development on 2016-10-13.
 *
 */
public class DescriptionTypeMapper {

    public static DescriptionTypeResponse map(DescriptionType descriptionType) {
        if ( descriptionType != null ) {
            DescriptionTypeResponse res = new DescriptionTypeResponse();
            res.setName(descriptionType.getName());
            res.setDescription(descriptionType.getDescription());
            return res;
        } else {
            return null;
        }
    }

}
