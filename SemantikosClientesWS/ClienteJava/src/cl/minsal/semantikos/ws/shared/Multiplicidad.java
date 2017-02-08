
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Multiplicidad complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Multiplicidad">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="limiteInferior" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="limiteSuperior" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Multiplicidad", propOrder = {
    "limiteInferior",
    "limiteSuperior"
})
public class Multiplicidad {

    protected Integer limiteInferior;
    protected Integer limiteSuperior;

    /**
     * Gets the value of the limiteInferior property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLimiteInferior() {
        return limiteInferior;
    }

    /**
     * Sets the value of the limiteInferior property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLimiteInferior(Integer value) {
        this.limiteInferior = value;
    }

    /**
     * Gets the value of the limiteSuperior property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLimiteSuperior() {
        return limiteSuperior;
    }

    /**
     * Sets the value of the limiteSuperior property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLimiteSuperior(Integer value) {
        this.limiteSuperior = value;
    }

}
