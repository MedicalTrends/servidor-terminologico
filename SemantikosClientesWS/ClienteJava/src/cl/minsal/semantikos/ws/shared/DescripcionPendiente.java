
package cl.minsal.semantikos.ws.shared;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DescripcionPendiente complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DescripcionPendiente">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="terminoPreferido" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreCategoria" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pendienteCodificacion" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="validez" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="descripciones" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="descripcion" type="{http://service.ws.semantikos.minsal.cl/}DescripcionSimplificada" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "DescripcionPendiente", propOrder = {
    "terminoPreferido",
    "nombreCategoria",
    "pendienteCodificacion",
    "validez",
    "descripciones"
})
public class DescripcionPendiente {

    protected String terminoPreferido;
    protected String nombreCategoria;
    protected Boolean pendienteCodificacion;
    protected Boolean validez;
    protected DescripcionPendiente.Descripciones descripciones;

    /**
     * Gets the value of the terminoPreferido property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTerminoPreferido() {
        return terminoPreferido;
    }

    /**
     * Sets the value of the terminoPreferido property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTerminoPreferido(String value) {
        this.terminoPreferido = value;
    }

    /**
     * Gets the value of the nombreCategoria property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreCategoria() {
        return nombreCategoria;
    }

    /**
     * Sets the value of the nombreCategoria property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreCategoria(String value) {
        this.nombreCategoria = value;
    }

    /**
     * Gets the value of the pendienteCodificacion property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPendienteCodificacion() {
        return pendienteCodificacion;
    }

    /**
     * Sets the value of the pendienteCodificacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPendienteCodificacion(Boolean value) {
        this.pendienteCodificacion = value;
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
     * Gets the value of the descripciones property.
     * 
     * @return
     *     possible object is
     *     {@link DescripcionPendiente.Descripciones }
     *     
     */
    public DescripcionPendiente.Descripciones getDescripciones() {
        return descripciones;
    }

    /**
     * Sets the value of the descripciones property.
     * 
     * @param value
     *     allowed object is
     *     {@link DescripcionPendiente.Descripciones }
     *     
     */
    public void setDescripciones(DescripcionPendiente.Descripciones value) {
        this.descripciones = value;
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
     *         &lt;element name="descripcion" type="{http://service.ws.semantikos.minsal.cl/}DescripcionSimplificada" maxOccurs="unbounded" minOccurs="0"/>
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
        "descripcion"
    })
    public static class Descripciones {

        protected List<DescripcionSimplificada> descripcion;

        /**
         * Gets the value of the descripcion property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the descripcion property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDescripcion().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link DescripcionSimplificada }
         * 
         * 
         */
        public List<DescripcionSimplificada> getDescripcion() {
            if (descripcion == null) {
                descripcion = new ArrayList<DescripcionSimplificada>();
            }
            return this.descripcion;
        }

    }

}
