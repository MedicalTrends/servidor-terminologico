
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for listaRefSetResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listaRefSetResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="refsetResponse" type="{http://service.ws.semantikos.minsal.cl/}RespuestaRefSets" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listaRefSetResponse", propOrder = {
    "refsetResponse"
})
public class ListaRefSetResponse {

    protected RespuestaRefSets refsetResponse;

    /**
     * Gets the value of the refsetResponse property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaRefSets }
     *     
     */
    public RespuestaRefSets getRefsetResponse() {
        return refsetResponse;
    }

    /**
     * Sets the value of the refsetResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaRefSets }
     *     
     */
    public void setRefsetResponse(RespuestaRefSets value) {
        this.refsetResponse = value;
    }

}
