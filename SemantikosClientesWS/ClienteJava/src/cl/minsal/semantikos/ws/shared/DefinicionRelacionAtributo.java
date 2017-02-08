
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DefinicionRelacionAtributo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DefinicionRelacionAtributo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}multiplicidad" minOccurs="0"/>
 *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}definicionObjetivo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DefinicionRelacionAtributo", propOrder = {
    "name",
    "multiplicidad",
    "definicionObjetivo"
})
public class DefinicionRelacionAtributo {

    protected String name;
    @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/")
    protected Multiplicidad multiplicidad;
    @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/")
    protected DefinicionObjetivo definicionObjetivo;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the multiplicidad property.
     * 
     * @return
     *     possible object is
     *     {@link Multiplicidad }
     *     
     */
    public Multiplicidad getMultiplicidad() {
        return multiplicidad;
    }

    /**
     * Sets the value of the multiplicidad property.
     * 
     * @param value
     *     allowed object is
     *     {@link Multiplicidad }
     *     
     */
    public void setMultiplicidad(Multiplicidad value) {
        this.multiplicidad = value;
    }

    /**
     * Gets the value of the definicionObjetivo property.
     * 
     * @return
     *     possible object is
     *     {@link DefinicionObjetivo }
     *     
     */
    public DefinicionObjetivo getDefinicionObjetivo() {
        return definicionObjetivo;
    }

    /**
     * Sets the value of the definicionObjetivo property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefinicionObjetivo }
     *     
     */
    public void setDefinicionObjetivo(DefinicionObjetivo value) {
        this.definicionObjetivo = value;
    }

}
