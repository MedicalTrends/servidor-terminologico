
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for descripcionesPreferidasPorRefSetResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="descripcionesPreferidasPorRefSetResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}respuestaConceptosPorRefSet" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "descripcionesPreferidasPorRefSetResponse", propOrder = {
    "respuestaConceptosPorRefSet"
})
public class DescripcionesPreferidasPorRefSetResponse {

    @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/")
    protected RespuestaConceptosPorRefSet respuestaConceptosPorRefSet;

    /**
     * Gets the value of the respuestaConceptosPorRefSet property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaConceptosPorRefSet }
     *     
     */
    public RespuestaConceptosPorRefSet getRespuestaConceptosPorRefSet() {
        return respuestaConceptosPorRefSet;
    }

    /**
     * Sets the value of the respuestaConceptosPorRefSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaConceptosPorRefSet }
     *     
     */
    public void setRespuestaConceptosPorRefSet(RespuestaConceptosPorRefSet value) {
        this.respuestaConceptosPorRefSet = value;
    }

}
