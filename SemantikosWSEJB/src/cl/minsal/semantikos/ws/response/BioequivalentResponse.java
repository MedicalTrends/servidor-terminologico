package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Development on 2016-12-30.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "bioequivalente", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "Bioequivalente", namespace = "http://service.ws.semantikos.minsal.cl/")
public class BioequivalentResponse implements Serializable {

    @XmlElement(name="productoComercial")
    private ConceptLightResponse productoComercial;

    public ConceptLightResponse getProductoComercial() {
        return productoComercial;
    }

    public void setProductoComercial(ConceptLightResponse productoComercial) {
        this.productoComercial = productoComercial;
    }
}
