package cl.minsal.semantikos.modelws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-11-22.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "peticionContarDescripcionConsumida", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionContarDescripcionConsumida", namespace = "http://service.ws.semantikos.minsal.cl/")
public class DescriptionHitRequest extends Request implements Serializable {

    @XmlElement(required = true, name = "descriptionID")
    private String descriptionID;

    public String getDescriptionID() {
        return descriptionID;
    }

    public void setDescriptionID(String descriptionID) {
        this.descriptionID = descriptionID;
    }

}
