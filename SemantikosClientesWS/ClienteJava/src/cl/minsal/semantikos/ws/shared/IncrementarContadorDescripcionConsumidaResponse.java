
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for incrementarContadorDescripcionConsumidaResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="incrementarContadorDescripcionConsumidaResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="descripcionCounter" type="{http://service.ws.semantikos.minsal.cl/}Descripcion" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "incrementarContadorDescripcionConsumidaResponse", propOrder = {
    "descripcionCounter"
})
public class IncrementarContadorDescripcionConsumidaResponse {

    protected Descripcion descripcionCounter;

    /**
     * Gets the value of the descripcionCounter property.
     * 
     * @return
     *     possible object is
     *     {@link Descripcion }
     *     
     */
    public Descripcion getDescripcionCounter() {
        return descripcionCounter;
    }

    /**
     * Sets the value of the descripcionCounter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Descripcion }
     *     
     */
    public void setDescripcionCounter(Descripcion value) {
        this.descripcionCounter = value;
    }

}
