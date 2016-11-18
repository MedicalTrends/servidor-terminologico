package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Development on 2016-10-14.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "multiplicidad")
public class MultiplicityResponse implements Serializable {

    @XmlElement(name="limiteInferior")
    private Integer lowerBoundary;
    @XmlElement(name="limiteSuperior")
    private Integer upperBoundary;

    public Integer getLowerBoundary() {
        return lowerBoundary;
    }

    public void setLowerBoundary(Integer lowerBoundary) {
        this.lowerBoundary = lowerBoundary;
    }

    public Integer getUpperBoundary() {
        return upperBoundary;
    }

    public void setUpperBoundary(Integer upperBoundary) {
        this.upperBoundary = upperBoundary;
    }
}
