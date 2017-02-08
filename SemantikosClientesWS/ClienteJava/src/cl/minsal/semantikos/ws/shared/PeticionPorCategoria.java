
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PeticionPorCategoria complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PeticionPorCategoria">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nombreCategoria" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idEstablecimiento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PeticionPorCategoria", propOrder = {
    "nombreCategoria",
    "idEstablecimiento"
})
public class PeticionPorCategoria {

    @XmlElement(required = true)
    protected String nombreCategoria;
    @XmlElement(required = true, defaultValue = "1")
    protected String idEstablecimiento;

    /**
     * Gets the value of the nombreCategoria property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreCategoria() {
        return nombreCategoria;
    }

    /**
     * Sets the value of the nombreCategoria property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreCategoria(String value) {
        this.nombreCategoria = value;
    }

    /**
     * Gets the value of the idEstablecimiento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdEstablecimiento() {
        return idEstablecimiento;
    }

    /**
     * Sets the value of the idEstablecimiento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdEstablecimiento(String value) {
        this.idEstablecimiento = value;
    }

}
