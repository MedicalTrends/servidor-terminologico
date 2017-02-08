
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for listaRefSet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listaRefSet">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="incluyeEstablecimientos" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="idStablishment" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listaRefSet", propOrder = {
    "incluyeEstablecimientos",
    "idStablishment"
})
public class ListaRefSet {

    @XmlElement(defaultValue = "true")
    protected Boolean incluyeEstablecimientos;
    @XmlElement(required = true)
    protected String idStablishment;

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

}
