
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
 * <p>Java class for Concepto complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Concepto">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="aSerRevisado" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="aSerConsultado" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="modelado" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="completamenteDefinido" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="publicado" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="valido" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="validoHasta" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="observacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}categoria" minOccurs="0"/>
 *         &lt;element name="refSets" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://service.ws.semantikos.minsal.cl/}refSet" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="descripciones" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://service.ws.semantikos.minsal.cl/}descripcion" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="atributos" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://service.ws.semantikos.minsal.cl/}atributo" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="relaciones" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://service.ws.semantikos.minsal.cl/}relacion" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="relacionesSnomedCT" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://service.ws.semantikos.minsal.cl/}relacionSnomedCT" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="crossmapsIndirectos" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="crossmapIndirecto" type="{http://service.ws.semantikos.minsal.cl/}IndirectCrossmap" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="crossmapsDirectos" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="crossmapDirecto" type="{http://service.ws.semantikos.minsal.cl/}CrossmapSetMember" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "Concepto", propOrder = {
    "id",
    "aSerRevisado",
    "aSerConsultado",
    "modelado",
    "completamenteDefinido",
    "publicado",
    "valido",
    "validoHasta",
    "observacion",
    "categoria",
    "refSets",
    "descripciones",
    "atributos",
    "relaciones",
    "relacionesSnomedCT",
    "crossmapsIndirectos",
    "crossmapsDirectos"
})
public class Concepto {

    protected String id;
    protected Boolean aSerRevisado;
    protected Boolean aSerConsultado;
    protected Boolean modelado;
    protected Boolean completamenteDefinido;
    protected Boolean publicado;
    protected Boolean valido;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar validoHasta;
    protected String observacion;

    @XmlElement(namespace = "")
    protected Categoria categoria;
    protected Concepto.RefSets refSets;
    protected Concepto.Descripciones descripciones;
    protected Concepto.Atributos atributos;
    protected Concepto.Relaciones relaciones;
    protected Concepto.RelacionesSnomedCT relacionesSnomedCT;
    protected Concepto.CrossmapsIndirectos crossmapsIndirectos;
    protected Concepto.CrossmapsDirectos crossmapsDirectos;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the aSerRevisado property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isASerRevisado() {
        return aSerRevisado;
    }

    /**
     * Sets the value of the aSerRevisado property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setASerRevisado(Boolean value) {
        this.aSerRevisado = value;
    }

    /**
     * Gets the value of the aSerConsultado property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isASerConsultado() {
        return aSerConsultado;
    }

    /**
     * Sets the value of the aSerConsultado property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setASerConsultado(Boolean value) {
        this.aSerConsultado = value;
    }

    /**
     * Gets the value of the modelado property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isModelado() {
        return modelado;
    }

    /**
     * Sets the value of the modelado property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setModelado(Boolean value) {
        this.modelado = value;
    }

    /**
     * Gets the value of the completamenteDefinido property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCompletamenteDefinido() {
        return completamenteDefinido;
    }

    /**
     * Sets the value of the completamenteDefinido property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCompletamenteDefinido(Boolean value) {
        this.completamenteDefinido = value;
    }

    /**
     * Gets the value of the publicado property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPublicado() {
        return publicado;
    }

    /**
     * Sets the value of the publicado property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPublicado(Boolean value) {
        this.publicado = value;
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
     * Gets the value of the observacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObservacion() {
        return observacion;
    }

    /**
     * Sets the value of the observacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObservacion(String value) {
        this.observacion = value;
    }

    /**
     * Gets the value of the categoria property.
     * 
     * @return
     *     possible object is
     *     {@link Categoria }
     *     
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * Sets the value of the categoria property.
     * 
     * @param value
     *     allowed object is
     *     {@link Categoria }
     *     
     */
    public void setCategoria(Categoria value) {
        this.categoria = value;
    }

    /**
     * Gets the value of the refSets property.
     * 
     * @return
     *     possible object is
     *     {@link Concepto.RefSets }
     *     
     */
    public Concepto.RefSets getRefSets() {
        return refSets;
    }

    /**
     * Sets the value of the refSets property.
     * 
     * @param value
     *     allowed object is
     *     {@link Concepto.RefSets }
     *     
     */
    public void setRefSets(Concepto.RefSets value) {
        this.refSets = value;
    }

    /**
     * Gets the value of the descripciones property.
     * 
     * @return
     *     possible object is
     *     {@link Concepto.Descripciones }
     *     
     */
    public Concepto.Descripciones getDescripciones() {
        return descripciones;
    }

    /**
     * Sets the value of the descripciones property.
     * 
     * @param value
     *     allowed object is
     *     {@link Concepto.Descripciones }
     *     
     */
    public void setDescripciones(Concepto.Descripciones value) {
        this.descripciones = value;
    }

    /**
     * Gets the value of the atributos property.
     * 
     * @return
     *     possible object is
     *     {@link Concepto.Atributos }
     *     
     */
    public Concepto.Atributos getAtributos() {
        return atributos;
    }

    /**
     * Sets the value of the atributos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Concepto.Atributos }
     *     
     */
    public void setAtributos(Concepto.Atributos value) {
        this.atributos = value;
    }

    /**
     * Gets the value of the relaciones property.
     * 
     * @return
     *     possible object is
     *     {@link Concepto.Relaciones }
     *     
     */
    public Concepto.Relaciones getRelaciones() {
        return relaciones;
    }

    /**
     * Sets the value of the relaciones property.
     * 
     * @param value
     *     allowed object is
     *     {@link Concepto.Relaciones }
     *     
     */
    public void setRelaciones(Concepto.Relaciones value) {
        this.relaciones = value;
    }

    /**
     * Gets the value of the relacionesSnomedCT property.
     * 
     * @return
     *     possible object is
     *     {@link Concepto.RelacionesSnomedCT }
     *     
     */
    public Concepto.RelacionesSnomedCT getRelacionesSnomedCT() {
        return relacionesSnomedCT;
    }

    /**
     * Sets the value of the relacionesSnomedCT property.
     * 
     * @param value
     *     allowed object is
     *     {@link Concepto.RelacionesSnomedCT }
     *     
     */
    public void setRelacionesSnomedCT(Concepto.RelacionesSnomedCT value) {
        this.relacionesSnomedCT = value;
    }

    /**
     * Gets the value of the crossmapsIndirectos property.
     * 
     * @return
     *     possible object is
     *     {@link Concepto.CrossmapsIndirectos }
     *     
     */
    public Concepto.CrossmapsIndirectos getCrossmapsIndirectos() {
        return crossmapsIndirectos;
    }

    /**
     * Sets the value of the crossmapsIndirectos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Concepto.CrossmapsIndirectos }
     *     
     */
    public void setCrossmapsIndirectos(Concepto.CrossmapsIndirectos value) {
        this.crossmapsIndirectos = value;
    }

    /**
     * Gets the value of the crossmapsDirectos property.
     * 
     * @return
     *     possible object is
     *     {@link Concepto.CrossmapsDirectos }
     *     
     */
    public Concepto.CrossmapsDirectos getCrossmapsDirectos() {
        return crossmapsDirectos;
    }

    /**
     * Sets the value of the crossmapsDirectos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Concepto.CrossmapsDirectos }
     *     
     */
    public void setCrossmapsDirectos(Concepto.CrossmapsDirectos value) {
        this.crossmapsDirectos = value;
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
     *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}atributo" maxOccurs="unbounded" minOccurs="0"/>
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

        @XmlElement(namespace = "")
        protected List<Atributo> atributo;

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
         * {@link Atributo }
         * 
         * 
         */
        public List<Atributo> getAtributo() {
            if (atributo == null) {
                atributo = new ArrayList<Atributo>();
            }
            return this.atributo;
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
     *         &lt;element name="crossmapDirecto" type="{http://service.ws.semantikos.minsal.cl/}CrossmapSetMember" maxOccurs="unbounded" minOccurs="0"/>
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
        "crossmapDirecto"
    })
    public static class CrossmapsDirectos {

        protected List<CrossmapSetMember> crossmapDirecto;

        /**
         * Gets the value of the crossmapDirecto property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the crossmapDirecto property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCrossmapDirecto().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CrossmapSetMember }
         * 
         * 
         */
        public List<CrossmapSetMember> getCrossmapDirecto() {
            if (crossmapDirecto == null) {
                crossmapDirecto = new ArrayList<CrossmapSetMember>();
            }
            return this.crossmapDirecto;
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
     *         &lt;element name="crossmapIndirecto" type="{http://service.ws.semantikos.minsal.cl/}IndirectCrossmap" maxOccurs="unbounded" minOccurs="0"/>
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
        "crossmapIndirecto"
    })
    public static class CrossmapsIndirectos {

        protected List<IndirectCrossmap> crossmapIndirecto;

        /**
         * Gets the value of the crossmapIndirecto property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the crossmapIndirecto property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCrossmapIndirecto().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link IndirectCrossmap }
         * 
         * 
         */
        public List<IndirectCrossmap> getCrossmapIndirecto() {
            if (crossmapIndirecto == null) {
                crossmapIndirecto = new ArrayList<IndirectCrossmap>();
            }
            return this.crossmapIndirecto;
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
     *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}descripcion" maxOccurs="unbounded" minOccurs="0"/>
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

        @XmlElement(namespace = "")
        protected List<Descripcion> descripcion;

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
         * {@link Descripcion }
         * 
         * 
         */
        public List<Descripcion> getDescripcion() {
            if (descripcion == null) {
                descripcion = new ArrayList<Descripcion>();
            }
            return this.descripcion;
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
     *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}refSet" maxOccurs="unbounded" minOccurs="0"/>
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
        "refSet"
    })
    public static class RefSets {

        @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/")
        protected List<RefSet> refSet;

        /**
         * Gets the value of the refSet property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the refSet property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getRefSet().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link RefSet }
         * 
         * 
         */
        public List<RefSet> getRefSet() {
            if (refSet == null) {
                refSet = new ArrayList<RefSet>();
            }
            return this.refSet;
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
     *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}relacion" maxOccurs="unbounded" minOccurs="0"/>
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
        "relacion"
    })
    public static class Relaciones {

        @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/")
        protected List<Relacion> relacion;

        /**
         * Gets the value of the relacion property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the relacion property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getRelacion().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Relacion }
         * 
         * 
         */
        public List<Relacion> getRelacion() {
            if (relacion == null) {
                relacion = new ArrayList<Relacion>();
            }
            return this.relacion;
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
     *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}relacionSnomedCT" maxOccurs="unbounded" minOccurs="0"/>
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
        "relacionSnomedCT"
    })
    public static class RelacionesSnomedCT {

        @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/")
        protected List<RelacionSnomedCT> relacionSnomedCT;

        /**
         * Gets the value of the relacionSnomedCT property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the relacionSnomedCT property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getRelacionSnomedCT().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link RelacionSnomedCT }
         * 
         * 
         */
        public List<RelacionSnomedCT> getRelacionSnomedCT() {
            if (relacionSnomedCT == null) {
                relacionSnomedCT = new ArrayList<RelacionSnomedCT>();
            }
            return this.relacionSnomedCT;
        }

    }

}
