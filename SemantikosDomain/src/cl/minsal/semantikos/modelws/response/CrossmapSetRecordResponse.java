package cl.minsal.semantikos.modelws.response;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.relationships.Target;

import javax.xml.bind.annotation.*;

/**
 * @author Andrés Farías on 11/3/16.
 */
@XmlSeeAlso({CrossmapSetMemberResponse.class, GenericDeviceGroupResponse.class})
public abstract class CrossmapSetRecordResponse {

    public CrossmapSetRecordResponse() {
    }

}
