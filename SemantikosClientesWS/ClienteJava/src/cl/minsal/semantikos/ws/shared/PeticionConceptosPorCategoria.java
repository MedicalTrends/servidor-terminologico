
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PeticionConceptosPorCategoria complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PeticionConceptosPorCategoria">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nombreCategoria" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numeroPagina" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="tamanoPagina" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PeticionConceptosPorCategoria", propOrder = {
    "nombreCategoria",
    "numeroPagina",
    "tamanoPagina"
})
public class PeticionConceptosPorCategoria {

    @XmlElement(required = true)
    protected String nombreCategoria;
    @XmlElement(defaultValue = "0")
    protected Integer numeroPagina;
    @XmlElement(defaultValue = "10")
    protected Integer tamanoPagina;

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
     * Gets the value of the numeroPagina property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumeroPagina() {
        return numeroPagina;
    }

    /**
     * Sets the value of the numeroPagina property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumeroPagina(Integer value) {
        this.numeroPagina = value;
    }

    /**
     * Gets the value of the tamanoPagina property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTamanoPagina() {
        return tamanoPagina;
    }

    /**
     * Sets the value of the tamanoPagina property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTamanoPagina(Integer value) {
        this.tamanoPagina = value;
    }

}
