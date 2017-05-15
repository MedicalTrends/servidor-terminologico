package cl.minsal.semantikos.modelws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Esta clase representa una petición de servicio que recibe como argumento una categoría.
 *
 * @author Alonso Cornejo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "peticionCrosmapSetMembers", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionCrosmapSetMembers", namespace = "http://service.ws.semantikos.minsal.cl/")
public class CrossmapSetMembersRequest extends Request implements Serializable {

    @XmlElement(required = true, name = "nombreAbreviadoCrossmapSet")
    private String crossmapSetAbbreviatedName;

    public String getCrossmapSetAbbreviatedName() {
        return crossmapSetAbbreviatedName;
    }

    public void setCrossmapSetAbbreviatedName(String crossmapSetAbbreviatedName) {
        this.crossmapSetAbbreviatedName = crossmapSetAbbreviatedName;
    }
}
