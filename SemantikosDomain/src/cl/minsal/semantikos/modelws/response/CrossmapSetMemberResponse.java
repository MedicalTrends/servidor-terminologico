package cl.minsal.semantikos.modelws.response;

import javax.xml.bind.annotation.*;

/**
 * @author Andrés Farías on 11/3/16.
 */
@XmlSeeAlso({DiseaseResponse.class, GenericDeviceGroupResponse.class})
public class CrossmapSetMemberResponse {

    /** ID de negocio */
    @XmlElement(name = "idCrossmapSetMember")
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CrossmapSetMemberResponse() {
    }

}
