
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for buscarTruncatePerfectResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="buscarTruncatePerfectResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="respuestaConceptos" type="{http://service.ws.semantikos.minsal.cl/}RespuestaConceptosPorCategoria" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "buscarTruncatePerfectResponse", propOrder = {
    "respuestaConceptos"
})
public class BuscarTruncatePerfectResponse {

    protected RespuestaConceptosPorCategoria respuestaConceptos;

    /**
     * Gets the value of the respuestaConceptos property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaConceptosPorCategoria }
     *     
     */
    public RespuestaConceptosPorCategoria getRespuestaConceptos() {
        return respuestaConceptos;
    }

    /**
     * Sets the value of the respuestaConceptos property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaConceptosPorCategoria }
     *     
     */
    public void setRespuestaConceptos(RespuestaConceptosPorCategoria value) {
        this.respuestaConceptos = value;
    }

}
