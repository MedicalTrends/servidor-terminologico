
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for crossMapsIndirectosPorIDDescripcionResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="crossMapsIndirectosPorIDDescripcionResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="indirectCrossmaps" type="{http://service.ws.semantikos.minsal.cl/}IndirectCrossmapsSearch" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "crossMapsIndirectosPorIDDescripcionResponse", propOrder = {
    "indirectCrossmaps"
})
public class CrossMapsIndirectosPorIDDescripcionResponse {

    protected IndirectCrossmapsSearch indirectCrossmaps;

    /**
     * Gets the value of the indirectCrossmaps property.
     * 
     * @return
     *     possible object is
     *     {@link IndirectCrossmapsSearch }
     *     
     */
    public IndirectCrossmapsSearch getIndirectCrossmaps() {
        return indirectCrossmaps;
    }

    /**
     * Sets the value of the indirectCrossmaps property.
     * 
     * @param value
     *     allowed object is
     *     {@link IndirectCrossmapsSearch }
     *     
     */
    public void setIndirectCrossmaps(IndirectCrossmapsSearch value) {
        this.indirectCrossmaps = value;
    }

}
