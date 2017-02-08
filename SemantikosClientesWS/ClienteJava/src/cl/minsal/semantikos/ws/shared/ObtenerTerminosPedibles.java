
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for obtenerTerminosPedibles complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="obtenerTerminosPedibles">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="peticionObtenerTerminosPedibles" type="{http://service.ws.semantikos.minsal.cl/}PeticionConceptosPedibles"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obtenerTerminosPedibles", propOrder = {
    "peticionObtenerTerminosPedibles"
})
public class ObtenerTerminosPedibles {

    @XmlElement(required = true)
    protected PeticionConceptosPedibles peticionObtenerTerminosPedibles;

    /**
     * Gets the value of the peticionObtenerTerminosPedibles property.
     * 
     * @return
     *     possible object is
     *     {@link PeticionConceptosPedibles }
     *     
     */
    public PeticionConceptosPedibles getPeticionObtenerTerminosPedibles() {
        return peticionObtenerTerminosPedibles;
    }

    /**
     * Sets the value of the peticionObtenerTerminosPedibles property.
     * 
     * @param value
     *     allowed object is
     *     {@link PeticionConceptosPedibles }
     *     
     */
    public void setPeticionObtenerTerminosPedibles(PeticionConceptosPedibles value) {
        this.peticionObtenerTerminosPedibles = value;
    }

}
