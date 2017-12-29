package cl.minsal.semantikos.modelws.request;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-11-22.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "peticionConceptIDPorGS1", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionConceptIDPorGS1", namespace = "http://service.ws.semantikos.minsal.cl/")
public class ConceptIDByGS1Request extends Request implements Serializable {

    @XmlElement(required = true, name = "GS1")
    private int GS1;

    public int getGS1() {
        return GS1;
    }

    public void setGS1(int GS1) {
        this.GS1 = GS1;
    }
}
