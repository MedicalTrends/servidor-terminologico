
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getCrossmapSets complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getCrossmapSets">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idInstitucion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getCrossmapSets", propOrder = {
    "idInstitucion"
})
public class GetCrossmapSets {

    @XmlElement(required = true)
    protected String idInstitucion;

    /**
     * Gets the value of the idInstitucion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdInstitucion() {
        return idInstitucion;
    }

    /**
     * Sets the value of the idInstitucion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdInstitucion(String value) {
        this.idInstitucion = value;
    }

}
