
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for conceptosPorRefSet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="conceptosPorRefSet">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}peticionConceptosPorRefSet"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "conceptosPorRefSet", propOrder = {
    "peticionConceptosPorRefSet"
})
public class ConceptosPorRefSet {

    @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/", required = true)
    protected PeticionConceptosPorRefSet peticionConceptosPorRefSet;

    /**
     * Gets the value of the peticionConceptosPorRefSet property.
     * 
     * @return
     *     possible object is
     *     {@link PeticionConceptosPorRefSet }
     *     
     */
    public PeticionConceptosPorRefSet getPeticionConceptosPorRefSet() {
        return peticionConceptosPorRefSet;
    }

    /**
     * Sets the value of the peticionConceptosPorRefSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link PeticionConceptosPorRefSet }
     *     
     */
    public void setPeticionConceptosPorRefSet(PeticionConceptosPorRefSet value) {
        this.peticionConceptosPorRefSet = value;
    }

}
