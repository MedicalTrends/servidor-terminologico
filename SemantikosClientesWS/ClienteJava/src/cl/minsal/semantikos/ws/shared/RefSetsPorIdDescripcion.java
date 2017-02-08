
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for refSetsPorIdDescripcion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="refSetsPorIdDescripcion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}peticionRefSetsPorIdDescripcion"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "refSetsPorIdDescripcion", propOrder = {
    "peticionRefSetsPorIdDescripcion"
})
public class RefSetsPorIdDescripcion {

    @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/", required = true)
    protected PeticionRefSetsPorIdDescripcion peticionRefSetsPorIdDescripcion;

    /**
     * Gets the value of the peticionRefSetsPorIdDescripcion property.
     * 
     * @return
     *     possible object is
     *     {@link PeticionRefSetsPorIdDescripcion }
     *     
     */
    public PeticionRefSetsPorIdDescripcion getPeticionRefSetsPorIdDescripcion() {
        return peticionRefSetsPorIdDescripcion;
    }

    /**
     * Sets the value of the peticionRefSetsPorIdDescripcion property.
     * 
     * @param value
     *     allowed object is
     *     {@link PeticionRefSetsPorIdDescripcion }
     *     
     */
    public void setPeticionRefSetsPorIdDescripcion(PeticionRefSetsPorIdDescripcion value) {
        this.peticionRefSetsPorIdDescripcion = value;
    }

}
