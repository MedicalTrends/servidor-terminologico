
package cl.minsal.semantikos.ws.shared;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Relacion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Relacion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}objetivo" minOccurs="0"/>
 *         &lt;element name="definicion" type="{http://service.ws.semantikos.minsal.cl/}DefinicionRelacion" minOccurs="0"/>
 *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}concepto" minOccurs="0"/>
 *         &lt;element name="atributos" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="atributo" type="{http://service.ws.semantikos.minsal.cl/}AtributoRelacion" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "Relacion", propOrder = {
    "objetivo",
    "definicion",
    "concepto",
    "atributos"
})
public class Relacion {

    @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/")
    protected Objetivo objetivo;
    protected DefinicionRelacion definicion;
    @XmlElement(namespace = "")
    protected Concepto concepto;
    protected Relacion.Atributos atributos;

    /**
     * Gets the value of the objetivo property.
     * 
     * @return
     *     possible object is
     *     {@link Objetivo }
     *     
     */
    public Objetivo getObjetivo() {
        return objetivo;
    }

    /**
     * Sets the value of the objetivo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Objetivo }
     *     
     */
    public void setObjetivo(Objetivo value) {
        this.objetivo = value;
    }

    /**
     * Gets the value of the definicion property.
     * 
     * @return
     *     possible object is
     *     {@link DefinicionRelacion }
     *     
     */
    public DefinicionRelacion getDefinicion() {
        return definicion;
    }

    /**
     * Sets the value of the definicion property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefinicionRelacion }
     *     
     */
    public void setDefinicion(DefinicionRelacion value) {
        this.definicion = value;
    }

    /**
     * Gets the value of the concepto property.
     * 
     * @return
     *     possible object is
     *     {@link Concepto }
     *     
     */
    public Concepto getConcepto() {
        return concepto;
    }

    /**
     * Sets the value of the concepto property.
     * 
     * @param value
     *     allowed object is
     *     {@link Concepto }
     *     
     */
    public void setConcepto(Concepto value) {
        this.concepto = value;
    }

    /**
     * Gets the value of the atributos property.
     * 
     * @return
     *     possible object is
     *     {@link Relacion.Atributos }
     *     
     */
    public Relacion.Atributos getAtributos() {
        return atributos;
    }

    /**
     * Sets the value of the atributos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Relacion.Atributos }
     *     
     */
    public void setAtributos(Relacion.Atributos value) {
        this.atributos = value;
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
     *         &lt;element name="atributo" type="{http://service.ws.semantikos.minsal.cl/}AtributoRelacion" maxOccurs="unbounded" minOccurs="0"/>
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
        "atributo"
    })
    public static class Atributos {

        protected List<AtributoRelacion> atributo;

        /**
         * Gets the value of the atributo property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the atributo property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAtributo().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link AtributoRelacion }
         * 
         * 
         */
        public List<AtributoRelacion> getAtributo() {
            if (atributo == null) {
                atributo = new ArrayList<AtributoRelacion>();
            }
            return this.atributo;
        }

    }

}
