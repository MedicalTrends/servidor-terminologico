
package cl.minsal.semantikos.ws.shared;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RespuestaBuscarTerminoGenerica complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RespuestaBuscarTerminoGenerica">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="descripcionPerfectMatch" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="descripcionesPerfectMatch" type="{http://service.ws.semantikos.minsal.cl/}DescripcionAC" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="descripcionesNoValidas" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://service.ws.semantikos.minsal.cl/}descripcionNoValida" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="descripcionesPendientes" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://service.ws.semantikos.minsal.cl/}descripcionPendiente" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RespuestaBuscarTerminoGenerica", propOrder = {
    "descripcionPerfectMatch",
    "descripcionesNoValidas",
    "descripcionesPendientes"
})
public class RespuestaBuscarTerminoGenerica {

    protected RespuestaBuscarTerminoGenerica.DescripcionPerfectMatch descripcionPerfectMatch;
    protected RespuestaBuscarTerminoGenerica.DescripcionesNoValidas descripcionesNoValidas;
    protected RespuestaBuscarTerminoGenerica.DescripcionesPendientes descripcionesPendientes;

    /**
     * Gets the value of the descripcionPerfectMatch property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaBuscarTerminoGenerica.DescripcionPerfectMatch }
     *     
     */
    public RespuestaBuscarTerminoGenerica.DescripcionPerfectMatch getDescripcionPerfectMatch() {
        return descripcionPerfectMatch;
    }

    /**
     * Sets the value of the descripcionPerfectMatch property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaBuscarTerminoGenerica.DescripcionPerfectMatch }
     *     
     */
    public void setDescripcionPerfectMatch(RespuestaBuscarTerminoGenerica.DescripcionPerfectMatch value) {
        this.descripcionPerfectMatch = value;
    }

    /**
     * Gets the value of the descripcionesNoValidas property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaBuscarTerminoGenerica.DescripcionesNoValidas }
     *     
     */
    public RespuestaBuscarTerminoGenerica.DescripcionesNoValidas getDescripcionesNoValidas() {
        return descripcionesNoValidas;
    }

    /**
     * Sets the value of the descripcionesNoValidas property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaBuscarTerminoGenerica.DescripcionesNoValidas }
     *     
     */
    public void setDescripcionesNoValidas(RespuestaBuscarTerminoGenerica.DescripcionesNoValidas value) {
        this.descripcionesNoValidas = value;
    }

    /**
     * Gets the value of the descripcionesPendientes property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaBuscarTerminoGenerica.DescripcionesPendientes }
     *     
     */
    public RespuestaBuscarTerminoGenerica.DescripcionesPendientes getDescripcionesPendientes() {
        return descripcionesPendientes;
    }

    /**
     * Sets the value of the descripcionesPendientes property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaBuscarTerminoGenerica.DescripcionesPendientes }
     *     
     */
    public void setDescripcionesPendientes(RespuestaBuscarTerminoGenerica.DescripcionesPendientes value) {
        this.descripcionesPendientes = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}descripcionNoValida" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "descripcionNoValida"
    })
    public static class DescripcionesNoValidas {

        @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/")
        protected List<DescripcionNoValida> descripcionNoValida;

        /**
         * Gets the value of the descripcionNoValida property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the descripcionNoValida property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDescripcionNoValida().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link DescripcionNoValida }
         * 
         * 
         */
        public List<DescripcionNoValida> getDescripcionNoValida() {
            if (descripcionNoValida == null) {
                descripcionNoValida = new ArrayList<DescripcionNoValida>();
            }
            return this.descripcionNoValida;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}descripcionPendiente" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "descripcionPendiente"
    })
    public static class DescripcionesPendientes {

        @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/")
        protected List<DescripcionPendiente> descripcionPendiente;

        /**
         * Gets the value of the descripcionPendiente property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the descripcionPendiente property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDescripcionPendiente().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link DescripcionPendiente }
         * 
         * 
         */
        public List<DescripcionPendiente> getDescripcionPendiente() {
            if (descripcionPendiente == null) {
                descripcionPendiente = new ArrayList<DescripcionPendiente>();
            }
            return this.descripcionPendiente;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="descripcionesPerfectMatch" type="{http://service.ws.semantikos.minsal.cl/}DescripcionAC" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "descripcionesPerfectMatch"
    })
    public static class DescripcionPerfectMatch {

        protected List<DescripcionAC> descripcionesPerfectMatch;

        /**
         * Gets the value of the descripcionesPerfectMatch property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the descripcionesPerfectMatch property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDescripcionesPerfectMatch().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link DescripcionAC }
         * 
         * 
         */
        public List<DescripcionAC> getDescripcionesPerfectMatch() {
            if (descripcionesPerfectMatch == null) {
                descripcionesPerfectMatch = new ArrayList<DescripcionAC>();
            }
            return this.descripcionesPerfectMatch;
        }

    }

}
