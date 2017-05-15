
package cl.minsal.semantikos.ws.shared;

import cl.minsal.semantikos.modelws.request.DescriptionIDorConceptIDRequest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for crossMapsDirectosPorIDDescripcion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="crossMapsDirectosPorIDDescripcion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DescripcionID" type="{http://service.ws.semantikos.minsal.cl/}descriptionIDorConceptIDRequest"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "crossMapsDirectosPorIDDescripcion", propOrder = {
    "descripcionID"
})
public class CrossMapsDirectosPorIDDescripcion {

    @XmlElement(name = "DescripcionID", required = true)
    protected DescriptionIDorConceptIDRequest descripcionID;

    /**
     * Gets the value of the descripcionID property.
     * 
     * @return
     *     possible object is
     *     {@link DescriptionIDorConceptIDRequest }
     *     
     */
    public DescriptionIDorConceptIDRequest getDescripcionID() {
        return descripcionID;
    }

    /**
     * Sets the value of the descripcionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link DescriptionIDorConceptIDRequest }
     *     
     */
    public void setDescripcionID(DescriptionIDorConceptIDRequest value) {
        this.descripcionID = value;
    }

}
