
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for buscarTermino complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="buscarTermino">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="peticionBuscarTermino" type="{http://service.ws.semantikos.minsal.cl/}PeticionBuscarTerminoSimple"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "buscarTermino", propOrder = {
    "peticionBuscarTermino"
})
public class BuscarTermino {

    @XmlElement(required = true)
    protected PeticionBuscarTerminoSimple peticionBuscarTermino;

    /**
     * Gets the value of the peticionBuscarTermino property.
     * 
     * @return
     *     possible object is
     *     {@link PeticionBuscarTerminoSimple }
     *     
     */
    public PeticionBuscarTerminoSimple getPeticionBuscarTermino() {
        return peticionBuscarTermino;
    }

    /**
     * Sets the value of the peticionBuscarTermino property.
     * 
     * @param value
     *     allowed object is
     *     {@link PeticionBuscarTerminoSimple }
     *     
     */
    public void setPeticionBuscarTermino(PeticionBuscarTerminoSimple value) {
        this.peticionBuscarTermino = value;
    }

}
