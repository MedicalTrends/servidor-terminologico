
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
 * <p>Java class for Objetivo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Objetivo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tipo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="activo" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="idModulo" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="estadoDefinicionId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="valido" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="tiempoEfectivo" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="valor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="conceptoSMTK" type="{http://service.ws.semantikos.minsal.cl/}Concepto" minOccurs="0"/>
 *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}relacion" minOccurs="0"/>
 *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}tablaAuxiliar" minOccurs="0"/>
 *         &lt;element name="fields">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
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
@XmlType(name = "Objetivo", propOrder = {
    "tipo",
    "activo",
    "idModulo",
    "estadoDefinicionId",
    "valido",
    "tiempoEfectivo",
    "valor",
    "conceptoSMTK",
    "relacion",
    "tablaAuxiliar",
    "fields"
})
public class Objetivo {

    protected String tipo;
    protected Boolean activo;
    protected Long idModulo;
    protected Long estadoDefinicionId;
    protected Boolean valido;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar tiempoEfectivo;
    protected String valor;
    protected Concepto conceptoSMTK;
    @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/")
    protected Relacion relacion;
    @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/")
    protected TablaAuxiliar tablaAuxiliar;
    @XmlElement(required = true)
    protected Objetivo.Fields fields;

    /**
     * Gets the value of the tipo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Sets the value of the tipo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipo(String value) {
        this.tipo = value;
    }

    /**
     * Gets the value of the activo property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isActivo() {
        return activo;
    }

    /**
     * Sets the value of the activo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setActivo(Boolean value) {
        this.activo = value;
    }

    /**
     * Gets the value of the idModulo property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdModulo() {
        return idModulo;
    }

    /**
     * Sets the value of the idModulo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdModulo(Long value) {
        this.idModulo = value;
    }

    /**
     * Gets the value of the estadoDefinicionId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getEstadoDefinicionId() {
        return estadoDefinicionId;
    }

    /**
     * Sets the value of the estadoDefinicionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setEstadoDefinicionId(Long value) {
        this.estadoDefinicionId = value;
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
     * Gets the value of the tiempoEfectivo property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTiempoEfectivo() {
        return tiempoEfectivo;
    }

    /**
     * Sets the value of the tiempoEfectivo property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTiempoEfectivo(XMLGregorianCalendar value) {
        this.tiempoEfectivo = value;
    }

    /**
     * Gets the value of the valor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValor() {
        return valor;
    }

    /**
     * Sets the value of the valor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValor(String value) {
        this.valor = value;
    }

    /**
     * Gets the value of the conceptoSMTK property.
     * 
     * @return
     *     possible object is
     *     {@link Concepto }
     *     
     */
    public Concepto getConceptoSMTK() {
        return conceptoSMTK;
    }

    /**
     * Sets the value of the conceptoSMTK property.
     * 
     * @param value
     *     allowed object is
     *     {@link Concepto }
     *     
     */
    public void setConceptoSMTK(Concepto value) {
        this.conceptoSMTK = value;
    }

    /**
     * Gets the value of the relacion property.
     * 
     * @return
     *     possible object is
     *     {@link Relacion }
     *     
     */
    public Relacion getRelacion() {
        return relacion;
    }

    /**
     * Sets the value of the relacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Relacion }
     *     
     */
    public void setRelacion(Relacion value) {
        this.relacion = value;
    }

    /**
     * Gets the value of the tablaAuxiliar property.
     * 
     * @return
     *     possible object is
     *     {@link TablaAuxiliar }
     *     
     */
    public TablaAuxiliar getTablaAuxiliar() {
        return tablaAuxiliar;
    }

    /**
     * Sets the value of the tablaAuxiliar property.
     * 
     * @param value
     *     allowed object is
     *     {@link TablaAuxiliar }
     *     
     */
    public void setTablaAuxiliar(TablaAuxiliar value) {
        this.tablaAuxiliar = value;
    }

    /**
     * Gets the value of the fields property.
     * 
     * @return
     *     possible object is
     *     {@link Objetivo.Fields }
     *     
     */
    public Objetivo.Fields getFields() {
        return fields;
    }

    /**
     * Sets the value of the fields property.
     * 
     * @param value
     *     allowed object is
     *     {@link Objetivo.Fields }
     *     
     */
    public void setFields(Objetivo.Fields value) {
        this.fields = value;
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
     *         &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    @XmlType(name = "", propOrder = {
        "entry"
    })
    public static class Fields {

        protected List<Objetivo.Fields.Entry> entry;

        /**
         * Gets the value of the entry property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the entry property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getEntry().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Objetivo.Fields.Entry }
         * 
         * 
         */
        public List<Objetivo.Fields.Entry> getEntry() {
            if (entry == null) {
                entry = new ArrayList<Objetivo.Fields.Entry>();
            }
            return this.entry;
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
         *         &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
            "key",
            "value"
        })
        public static class Entry {

            protected String key;
            protected String value;

            /**
             * Gets the value of the key property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getKey() {
                return key;
            }

            /**
             * Sets the value of the key property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setKey(String value) {
                this.key = value;
            }

            /**
             * Gets the value of the value property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setValue(String value) {
                this.value = value;
            }

        }

    }

}
