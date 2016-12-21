package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-10-14.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "intervalo", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "Intervalo", namespace = "http://service.ws.semantikos.minsal.cl/")
public class IntervalResponse implements Serializable {

    @XmlElement(name="limiteInferior")
    private String lowerBoundary;
    @XmlElement(name="limiteSuperior")
    private String upperBoundary;

    public String getLowerBoundary() {
        return lowerBoundary;
    }

    public void setLowerBoundary(String lowerBoundary) {
        this.lowerBoundary = lowerBoundary;
    }

    public String getUpperBoundary() {
        return upperBoundary;
    }

    public void setUpperBoundary(String upperBoundary) {
        this.upperBoundary = upperBoundary;
    }
}
