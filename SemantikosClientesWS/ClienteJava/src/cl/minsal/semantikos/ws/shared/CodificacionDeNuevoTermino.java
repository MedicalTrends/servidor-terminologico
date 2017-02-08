
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * javax.xml.ws.soap.SOAPFaultException: Unmarshalling Error: elemento inesperado (URI:"http://service.ws.semantikos.minsal.cl/", local:"peticionCodificacionDeNuevoTermino"). Los elementos esperados son <{}peticionCodificacionDeNuevoTermino>
 *
 * Solucion:
 *
 * public class CodificacionDeNuevoTermino {
 *
 * @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/", required = true)
 * protected PeticionCodificacionDeNuevoTermino peticionCodificacionDeNuevoTermino;
 *
 * ==>
 *
 * public class CodificacionDeNuevoTermino {
 *
 * @XmlElement(namespace = "", required = true)
 * protected PeticionCodificacionDeNuevoTermino peticionCodificacionDeNuevoTermino;
 *
 *
 * <p>Java class for codificacionDeNuevoTermino complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="codificacionDeNuevoTermino">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}peticionCodificacionDeNuevoTermino"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "codificacionDeNuevoTermino", propOrder = {
    "peticionCodificacionDeNuevoTermino"
})
public class CodificacionDeNuevoTermino {

    @XmlElement(namespace = "", required = true)
    protected PeticionCodificacionDeNuevoTermino peticionCodificacionDeNuevoTermino;

    /**
     * Gets the value of the peticionCodificacionDeNuevoTermino property.
     * 
     * @return
     *     possible object is
     *     {@link PeticionCodificacionDeNuevoTermino }
     *     
     */
    public PeticionCodificacionDeNuevoTermino getPeticionCodificacionDeNuevoTermino() {
        return peticionCodificacionDeNuevoTermino;
    }

    /**
     * Sets the value of the peticionCodificacionDeNuevoTermino property.
     * 
     * @param value
     *     allowed object is
     *     {@link PeticionCodificacionDeNuevoTermino }
     *     
     */
    public void setPeticionCodificacionDeNuevoTermino(PeticionCodificacionDeNuevoTermino value) {
        this.peticionCodificacionDeNuevoTermino = value;
    }

}
