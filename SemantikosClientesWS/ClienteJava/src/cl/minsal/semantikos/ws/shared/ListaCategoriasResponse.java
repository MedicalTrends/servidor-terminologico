
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for listaCategoriasResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listaCategoriasResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}respuestaCategorias" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listaCategoriasResponse", propOrder = {
    "respuestaCategorias"
})
public class ListaCategoriasResponse {

    @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/")
    protected RespuestaCategorias respuestaCategorias;

    /**
     * Gets the value of the respuestaCategorias property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaCategorias }
     *     
     */
    public RespuestaCategorias getRespuestaCategorias() {
        return respuestaCategorias;
    }

    /**
     * Sets the value of the respuestaCategorias property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaCategorias }
     *     
     */
    public void setRespuestaCategorias(RespuestaCategorias value) {
        this.respuestaCategorias = value;
    }

}
