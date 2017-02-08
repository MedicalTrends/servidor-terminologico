
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PeticionConceptosPorRefSet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PeticionConceptosPorRefSet">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nombreRefSet" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "PeticionConceptosPorRefSet", propOrder = {
    "nombreRefSet",
    "idEstablecimiento"
})
public class PeticionConceptosPorRefSet {

    @XmlElement(required = true)
    protected String nombreRefSet;
    @XmlElement(required = true)
    protected String idEstablecimiento;

    /**
     * Gets the value of the nombreRefSet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreRefSet() {
        return nombreRefSet;
    }

    /**
     * Sets the value of the nombreRefSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreRefSet(String value) {
        this.nombreRefSet = value;
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
