
package cl.minsal.semantikos.ws.shared;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DescripcionNoValida complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DescripcionNoValida">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="razonNoValido" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="validez" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="descripcionesSugeridas" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="descripcionSugerida" type="{http://service.ws.semantikos.minsal.cl/}DescripcionPerfectMatch" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="cantidadRegistros" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DescripcionNoValida", propOrder = {
    "razonNoValido",
    "validez",
    "descripcionesSugeridas",
    "cantidadRegistros"
})
public class DescripcionNoValida {

    protected String razonNoValido;
    protected Boolean validez;
    protected DescripcionNoValida.DescripcionesSugeridas descripcionesSugeridas;
    protected Integer cantidadRegistros;

    /**
     * Gets the value of the razonNoValido property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRazonNoValido() {
        return razonNoValido;
    }

    /**
     * Sets the value of the razonNoValido property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRazonNoValido(String value) {
        this.razonNoValido = value;
    }

    /**
     * Gets the value of the validez property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isValidez() {
        return validez;
    }

    /**
     * Sets the value of the validez property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setValidez(Boolean value) {
        this.validez = value;
    }

    /**
     * Gets the value of the descripcionesSugeridas property.
     * 
     * @return
     *     possible object is
     *     {@link DescripcionNoValida.DescripcionesSugeridas }
     *     
     */
    public DescripcionNoValida.DescripcionesSugeridas getDescripcionesSugeridas() {
        return descripcionesSugeridas;
    }

    /**
     * Sets the value of the descripcionesSugeridas property.
     * 
     * @param value
     *     allowed object is
     *     {@link DescripcionNoValida.DescripcionesSugeridas }
     *     
     */
    public void setDescripcionesSugeridas(DescripcionNoValida.DescripcionesSugeridas value) {
        this.descripcionesSugeridas = value;
    }

    /**
     * Gets the value of the cantidadRegistros property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCantidadRegistros() {
        return cantidadRegistros;
    }

    /**
     * Sets the value of the cantidadRegistros property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCantidadRegistros(Integer value) {
        this.cantidadRegistros = value;
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
     *         &lt;element name="descripcionSugerida" type="{http://service.ws.semantikos.minsal.cl/}DescripcionPerfectMatch" maxOccurs="unbounded" minOccurs="0"/>
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
        "descripcionSugerida"
    })
    public static class DescripcionesSugeridas {

        protected List<DescripcionPerfectMatch> descripcionSugerida;

        /**
         * Gets the value of the descripcionSugerida property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the descripcionSugerida property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDescripcionSugerida().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link DescripcionPerfectMatch }
         * 
         * 
         */
        public List<DescripcionPerfectMatch> getDescripcionSugerida() {
            if (descripcionSugerida == null) {
                descripcionSugerida = new ArrayList<DescripcionPerfectMatch>();
            }
            return this.descripcionSugerida;
        }

    }

}
