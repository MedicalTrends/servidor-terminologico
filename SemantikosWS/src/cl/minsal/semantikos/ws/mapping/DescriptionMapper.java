package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.Description;
import cl.minsal.semantikos.ws.response.DescriptionResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Development on 2016-10-13.
 *
 */
public class DescriptionMapper {

    public static DescriptionResponse map(Description description) {
        if ( description != null ) {
            DescriptionResponse res = new DescriptionResponse();
            res.setDescriptionID(description.getDescriptionId());
            res.setTerm(description.getTerm());
            res.setValid(description.isValid());
            res.setCaseSensitive(description.isCaseSensitive());
            if ( description.getDescriptionType() != null ) {
                res.setType(description.getDescriptionType().getName());
            }
            return res;
        } else {
            return null;
        }
    }

}
