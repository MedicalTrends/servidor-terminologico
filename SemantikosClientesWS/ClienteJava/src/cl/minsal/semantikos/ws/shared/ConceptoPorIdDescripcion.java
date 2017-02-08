
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for conceptoPorIdDescripcion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="conceptoPorIdDescripcion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idDescripcion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "conceptoPorIdDescripcion", propOrder = {
    "idDescripcion"
})
public class ConceptoPorIdDescripcion {

    @XmlElement(required = true)
    protected String idDescripcion;

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

}
