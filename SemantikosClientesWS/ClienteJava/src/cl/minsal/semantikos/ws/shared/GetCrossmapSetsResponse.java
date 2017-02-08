
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getCrossmapSetsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getCrossmapSetsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="crossmapSetResponse" type="{http://service.ws.semantikos.minsal.cl/}CrossmapSetsResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getCrossmapSetsResponse", propOrder = {
    "crossmapSetResponse"
})
public class GetCrossmapSetsResponse {

    protected CrossmapSetsResponse crossmapSetResponse;

    /**
     * Gets the value of the crossmapSetResponse property.
     * 
     * @return
     *     possible object is
     *     {@link CrossmapSetsResponse }
     *     
     */
    public CrossmapSetsResponse getCrossmapSetResponse() {
        return crossmapSetResponse;
    }

    /**
     * Sets the value of the crossmapSetResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link CrossmapSetsResponse }
     *     
     */
    public void setCrossmapSetResponse(CrossmapSetsResponse value) {
        this.crossmapSetResponse = value;
    }

}
