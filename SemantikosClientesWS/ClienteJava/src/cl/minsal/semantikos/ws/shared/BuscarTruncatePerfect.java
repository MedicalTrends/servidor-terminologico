
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for buscarTruncatePerfect complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="buscarTruncatePerfect">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}peticionBuscarTermino"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "buscarTruncatePerfect", propOrder = {
    "peticionBuscarTermino"
})
public class BuscarTruncatePerfect {

    private PeticionBuscarTermino peticionBuscarTermino;

    /**
     * Gets the value of the peticionBuscarTermino property.
     * 
     * @return
     *     possible object is
     *     {@link PeticionBuscarTermino }
     *     
     */
    @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/", required = true)
    public PeticionBuscarTermino getPeticionBuscarTermino() {
        return peticionBuscarTermino;
    }

    /**
     * Sets the value of the peticionBuscarTermino property.
     * 
     * @param value
     *     allowed object is
     *     {@link PeticionBuscarTermino }
     *     
     */
    public void setPeticionBuscarTermino(PeticionBuscarTermino value) {
        this.peticionBuscarTermino = value;
    }

}
