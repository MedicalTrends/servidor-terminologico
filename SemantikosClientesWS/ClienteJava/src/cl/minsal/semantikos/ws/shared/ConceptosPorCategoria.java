
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for conceptosPorCategoria complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="conceptosPorCategoria">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="peticionConceptosPorCategoria" type="{http://service.ws.semantikos.minsal.cl/}PeticionPorCategoria"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "conceptosPorCategoria", propOrder = {
    "peticionConceptosPorCategoria"
})
public class ConceptosPorCategoria {

    @XmlElement(required = true)
    protected PeticionPorCategoria peticionConceptosPorCategoria;

    /**
     * Gets the value of the peticionConceptosPorCategoria property.
     * 
     * @return
     *     possible object is
     *     {@link PeticionPorCategoria }
     *     
     */
    public PeticionPorCategoria getPeticionConceptosPorCategoria() {
        return peticionConceptosPorCategoria;
    }

    /**
     * Sets the value of the peticionConceptosPorCategoria property.
     * 
     * @param value
     *     allowed object is
     *     {@link PeticionPorCategoria }
     *     
     */
    public void setPeticionConceptosPorCategoria(PeticionPorCategoria value) {
        this.peticionConceptosPorCategoria = value;
    }

}
