
package cl.minsal.semantikos.ws.shared;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for RefSet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RefSet">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valido" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="validoHasta" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="creadoEn" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="institucion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="conceptos" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://service.ws.semantikos.minsal.cl/}concepto" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "RefSet", propOrder = {
    "nombre",
    "valido",
    "validoHasta",
    "creadoEn",
    "institucion",
    "conceptos"
})
public class RefSet {

    protected String nombre;
    protected Boolean valido;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar validoHasta;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar creadoEn;
    protected String institucion;
    protected RefSet.Conceptos conceptos;

    /**
     * Gets the value of the nombre property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the value of the nombre property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Gets the value of the valido property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isValido() {
        return valido;
    }

    /**
     * Sets the value of the valido property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setValido(Boolean value) {
        this.valido = value;
    }

    /**
     * Gets the value of the validoHasta property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getValidoHasta() {
        return validoHasta;
    }

    /**
     * Sets the value of the validoHasta property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setValidoHasta(XMLGregorianCalendar value) {
        this.validoHasta = value;
    }

    /**
     * Gets the value of the creadoEn property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreadoEn() {
        return creadoEn;
    }

    /**
     * Sets the value of the creadoEn property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreadoEn(XMLGregorianCalendar value) {
        this.creadoEn = value;
    }

    /**
     * Gets the value of the institucion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstitucion() {
        return institucion;
    }

    /**
     * Sets the value of the institucion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstitucion(String value) {
        this.institucion = value;
    }

    /**
     * Gets the value of the conceptos property.
     * 
     * @return
     *     possible object is
     *     {@link RefSet.Conceptos }
     *     
     */
    public RefSet.Conceptos getConceptos() {
        return conceptos;
    }

    /**
     * Sets the value of the conceptos property.
     * 
     * @param value
     *     allowed object is
     *     {@link RefSet.Conceptos }
     *     
     */
    public void setConceptos(RefSet.Conceptos value) {
        this.conceptos = value;
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
     *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}concepto" maxOccurs="unbounded" minOccurs="0"/>
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
        "concepto"
    })
    public static class Conceptos {

        @XmlElement(namespace = "")
        protected List<Concepto> concepto;

        /**
         * Gets the value of the concepto property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the concepto property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getConcepto().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Concepto }
         * 
         * 
         */
        public List<Concepto> getConcepto() {
            if (concepto == null) {
                concepto = new ArrayList<Concepto>();
            }
            return this.concepto;
        }

    }

}
