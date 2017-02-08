
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for buscarTerminoResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="buscarTerminoResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="respuestaBuscarTermino" type="{http://service.ws.semantikos.minsal.cl/}RespuestaBuscarTerminoGenerica" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "buscarTerminoResponse", propOrder = {
    "respuestaBuscarTermino"
})
public class BuscarTerminoResponse {

    protected RespuestaBuscarTerminoGenerica respuestaBuscarTermino;

    /**
     * Gets the value of the respuestaBuscarTermino property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaBuscarTerminoGenerica }
     *     
     */
    public RespuestaBuscarTerminoGenerica getRespuestaBuscarTermino() {
        return respuestaBuscarTermino;
    }

    /**
     * Sets the value of the respuestaBuscarTermino property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaBuscarTerminoGenerica }
     *     
     */
    public void setRespuestaBuscarTermino(RespuestaBuscarTerminoGenerica value) {
        this.respuestaBuscarTermino = value;
    }

}
