
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for crossmapSetMembersDeCrossmapSetResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="crossmapSetMembersDeCrossmapSetResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="crossmapSetMember" type="{http://service.ws.semantikos.minsal.cl/}CrossmapSetMembersResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "crossmapSetMembersDeCrossmapSetResponse", propOrder = {
    "crossmapSetMember"
})
public class CrossmapSetMembersDeCrossmapSetResponse {

    protected CrossmapSetMembersResponse crossmapSetMember;

    /**
     * Gets the value of the crossmapSetMember property.
     * 
     * @return
     *     possible object is
     *     {@link CrossmapSetMembersResponse }
     *     
     */
    public CrossmapSetMembersResponse getCrossmapSetMember() {
        return crossmapSetMember;
    }

    /**
     * Sets the value of the crossmapSetMember property.
     * 
     * @param value
     *     allowed object is
     *     {@link CrossmapSetMembersResponse }
     *     
     */
    public void setCrossmapSetMember(CrossmapSetMembersResponse value) {
        this.crossmapSetMember = value;
    }

}
