package cl.minsal.semantikos.ws.mapping;

import cl.minsal.semantikos.model.tags.Tag;
import cl.minsal.semantikos.modelws.response.TagResponse;

/**
 * Created by Development on 2016-10-11.
 *
 */
public class TagMapper {

    public static TagResponse map(Tag tag) {
        if ( tag != null ) {
            TagResponse res = new TagResponse();
            res.setName(tag.getName());
            return res;
        } else {
            return null;
        }
    }

}
