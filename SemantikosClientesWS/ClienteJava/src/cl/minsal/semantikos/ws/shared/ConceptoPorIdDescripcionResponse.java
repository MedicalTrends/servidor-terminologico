
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for conceptoPorIdDescripcionResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="conceptoPorIdDescripcionResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}concepto" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "conceptoPorIdDescripcionResponse", propOrder = {
    "concepto"
})
public class ConceptoPorIdDescripcionResponse {

    @XmlElement(namespace = "")
    protected Concepto concepto;

    /**
     * Gets the value of the concepto property.
     * 
     * @return
     *     possible object is
     *     {@link Concepto }
     *     
     */
    public Concepto getConcepto() {
        return concepto;
    }

    /**
     * Sets the value of the concepto property.
     * 
     * @param value
     *     allowed object is
     *     {@link Concepto }
     *     
     */
    public void setConcepto(Concepto value) {
        this.concepto = value;
    }

}
