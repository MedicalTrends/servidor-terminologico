
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PeticionRefSetsPorIdDescripcion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PeticionRefSetsPorIdDescripcion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idDescripcion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idStablishment" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="incluyeEstablecimientos" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PeticionRefSetsPorIdDescripcion", propOrder = {
    "idDescripcion",
    "idStablishment",
    "incluyeEstablecimientos"
})
public class PeticionRefSetsPorIdDescripcion {

    @XmlElement(required = true)
    protected String idDescripcion;
    @XmlElement(required = true)
    protected String idStablishment;
    @XmlElement(defaultValue = "true")
    protected Boolean incluyeEstablecimientos;

    /**
     * Gets the value of the idDescripcion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDescripcion() {
        return idDescripcion;
    }

    /**
     * Sets the value of the idDescripcion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDescripcion(String value) {
        this.idDescripcion = value;
    }

    /**
     * Gets the value of the idStablishment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdStablishment() {
        return idStablishment;
    }

    /**
     * Sets the value of the idStablishment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdStablishment(String value) {
        this.idStablishment = value;
    }

    /**
     * Gets the value of the incluyeEstablecimientos property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncluyeEstablecimientos() {
        return incluyeEstablecimientos;
    }

    /**
     * Sets the value of the incluyeEstablecimientos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncluyeEstablecimientos(Boolean value) {
        this.incluyeEstablecimientos = value;
    }

    public Boolean getIncluyeEstablecimientos() {
        return incluyeEstablecimientos;
    }
}
