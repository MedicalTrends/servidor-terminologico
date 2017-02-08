
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for crossMapsIndirectosPorIDDescripcion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="crossMapsIndirectosPorIDDescripcion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DescripcionID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "crossMapsIndirectosPorIDDescripcion", propOrder = {
    "descripcionID"
})
public class CrossMapsIndirectosPorIDDescripcion {

    @XmlElement(name = "DescripcionID", required = true)
    protected String descripcionID;

    /**
     * Gets the value of the descripcionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionID() {
        return descripcionID;
    }

    /**
     * Sets the value of the descripcionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionID(String value) {
        this.descripcionID = value;
    }

}
