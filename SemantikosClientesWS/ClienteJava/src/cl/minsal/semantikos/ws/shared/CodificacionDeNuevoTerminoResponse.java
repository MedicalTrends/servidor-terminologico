
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for codificacionDeNuevoTerminoResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="codificacionDeNuevoTerminoResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}respuestaCodificacionDeNuevoTermino" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "codificacionDeNuevoTerminoResponse", propOrder = {
    "respuestaCodificacionDeNuevoTermino"
})
public class CodificacionDeNuevoTerminoResponse {

    @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/")
    protected RespuestaCodificacionDeNuevoTermino respuestaCodificacionDeNuevoTermino;

    /**
     * Gets the value of the respuestaCodificacionDeNuevoTermino property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaCodificacionDeNuevoTermino }
     *     
     */
    public RespuestaCodificacionDeNuevoTermino getRespuestaCodificacionDeNuevoTermino() {
        return respuestaCodificacionDeNuevoTermino;
    }

    /**
     * Sets the value of the respuestaCodificacionDeNuevoTermino property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaCodificacionDeNuevoTermino }
     *     
     */
    public void setRespuestaCodificacionDeNuevoTermino(RespuestaCodificacionDeNuevoTermino value) {
        this.respuestaCodificacionDeNuevoTermino = value;
    }

}
