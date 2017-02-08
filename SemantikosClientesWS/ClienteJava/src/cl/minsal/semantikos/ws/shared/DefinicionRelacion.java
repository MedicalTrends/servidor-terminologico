
package cl.minsal.semantikos.ws.shared;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DefinicionRelacion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DefinicionRelacion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}multiplicidad" minOccurs="0"/>
 *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}definicionObjetivo" minOccurs="0"/>
 *         &lt;element name="definicionRelacionExcluida" type="{http://service.ws.semantikos.minsal.cl/}DefinicionRelacion" minOccurs="0"/>
 *         &lt;element name="definicionesAtributoRelacion" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="definicionAtributoRelacion" type="{http://service.ws.semantikos.minsal.cl/}DefinicionRelacionAtributo" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "DefinicionRelacion", propOrder = {
    "name",
    "description",
    "multiplicidad",
    "definicionObjetivo",
    "definicionRelacionExcluida",
    "definicionesAtributoRelacion"
})
public class DefinicionRelacion {

    protected String name;
    protected String description;
    @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/")
    protected Multiplicidad multiplicidad;
    @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/")
    protected DefinicionObjetivo definicionObjetivo;
    protected DefinicionRelacion definicionRelacionExcluida;
    protected DefinicionRelacion.DefinicionesAtributoRelacion definicionesAtributoRelacion;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the multiplicidad property.
     * 
     * @return
     *     possible object is
     *     {@link Multiplicidad }
     *     
     */
    public Multiplicidad getMultiplicidad() {
        return multiplicidad;
    }

    /**
     * Sets the value of the multiplicidad property.
     * 
     * @param value
     *     allowed object is
     *     {@link Multiplicidad }
     *     
     */
    public void setMultiplicidad(Multiplicidad value) {
        this.multiplicidad = value;
    }

    /**
     * Gets the value of the definicionObjetivo property.
     * 
     * @return
     *     possible object is
     *     {@link DefinicionObjetivo }
     *     
     */
    public DefinicionObjetivo getDefinicionObjetivo() {
        return definicionObjetivo;
    }

    /**
     * Sets the value of the definicionObjetivo property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefinicionObjetivo }
     *     
     */
    public void setDefinicionObjetivo(DefinicionObjetivo value) {
        this.definicionObjetivo = value;
    }

    /**
     * Gets the value of the definicionRelacionExcluida property.
     * 
     * @return
     *     possible object is
     *     {@link DefinicionRelacion }
     *     
     */
    public DefinicionRelacion getDefinicionRelacionExcluida() {
        return definicionRelacionExcluida;
    }

    /**
     * Sets the value of the definicionRelacionExcluida property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefinicionRelacion }
     *     
     */
    public void setDefinicionRelacionExcluida(DefinicionRelacion value) {
        this.definicionRelacionExcluida = value;
    }

    /**
     * Gets the value of the definicionesAtributoRelacion property.
     * 
     * @return
     *     possible object is
     *     {@link DefinicionRelacion.DefinicionesAtributoRelacion }
     *     
     */
    public DefinicionRelacion.DefinicionesAtributoRelacion getDefinicionesAtributoRelacion() {
        return definicionesAtributoRelacion;
    }

    /**
     * Sets the value of the definicionesAtributoRelacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefinicionRelacion.DefinicionesAtributoRelacion }
     *     
     */
    public void setDefinicionesAtributoRelacion(DefinicionRelacion.DefinicionesAtributoRelacion value) {
        this.definicionesAtributoRelacion = value;
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
     *         &lt;element name="definicionAtributoRelacion" type="{http://service.ws.semantikos.minsal.cl/}DefinicionRelacionAtributo" maxOccurs="unbounded" minOccurs="0"/>
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
        "definicionAtributoRelacion"
    })
    public static class DefinicionesAtributoRelacion {

        protected List<DefinicionRelacionAtributo> definicionAtributoRelacion;

        /**
         * Gets the value of the definicionAtributoRelacion property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the definicionAtributoRelacion property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDefinicionAtributoRelacion().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link DefinicionRelacionAtributo }
         * 
         * 
         */
        public List<DefinicionRelacionAtributo> getDefinicionAtributoRelacion() {
            if (definicionAtributoRelacion == null) {
                definicionAtributoRelacion = new ArrayList<DefinicionRelacionAtributo>();
            }
            return this.definicionAtributoRelacion;
        }

    }

}
