
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Descripcion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Descripcion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="termino" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valida" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="esSensibleAMayusculas" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="nombreAutgenerado" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="publicado" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="modelado" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="validaHasta" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="creado" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="usos" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="usuarioCreador" type="{http://service.ws.semantikos.minsal.cl/}Usuario" minOccurs="0"/>
 *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}concepto" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Descripcion", propOrder = {
    "id",
    "tipo",
    "termino",
    "valida",
    "esSensibleAMayusculas",
    "nombreAutgenerado",
    "publicado",
    "modelado",
    "validaHasta",
    "creado",
    "usos",
    "usuarioCreador",
    "concepto"
})
public class Descripcion {

    protected String id;
    protected String tipo;
    protected String termino;
    protected Boolean valida;
    protected Boolean esSensibleAMayusculas;
    protected Boolean nombreAutgenerado;
    protected Boolean publicado;
    protected Boolean modelado;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar validaHasta;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar creado;
    protected Long usos;
    protected Usuario usuarioCreador;
    @XmlElement(namespace = "")
    protected Concepto concepto;

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
     * Gets the value of the termino property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTermino() {
        return termino;
    }

    /**
     * Sets the value of the termino property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTermino(String value) {
        this.termino = value;
    }

    /**
     * Gets the value of the valida property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isValida() {
        return valida;
    }

    /**
     * Sets the value of the valida property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setValida(Boolean value) {
        this.valida = value;
    }

    /**
     * Gets the value of the esSensibleAMayusculas property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEsSensibleAMayusculas() {
        return esSensibleAMayusculas;
    }

    /**
     * Sets the value of the esSensibleAMayusculas property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEsSensibleAMayusculas(Boolean value) {
        this.esSensibleAMayusculas = value;
    }

    /**
     * Gets the value of the nombreAutgenerado property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isNombreAutgenerado() {
        return nombreAutgenerado;
    }

    /**
     * Sets the value of the nombreAutgenerado property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setNombreAutgenerado(Boolean value) {
        this.nombreAutgenerado = value;
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
     * Gets the value of the validaHasta property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getValidaHasta() {
        return validaHasta;
    }

    /**
     * Sets the value of the validaHasta property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setValidaHasta(XMLGregorianCalendar value) {
        this.validaHasta = value;
    }

    /**
     * Gets the value of the creado property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreado() {
        return creado;
    }

    /**
     * Sets the value of the creado property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreado(XMLGregorianCalendar value) {
        this.creado = value;
    }

    /**
     * Gets the value of the usos property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getUsos() {
        return usos;
    }

    /**
     * Sets the value of the usos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setUsos(Long value) {
        this.usos = value;
    }

    /**
     * Gets the value of the usuarioCreador property.
     * 
     * @return
     *     possible object is
     *     {@link Usuario }
     *     
     */
    public Usuario getUsuarioCreador() {
        return usuarioCreador;
    }

    /**
     * Sets the value of the usuarioCreador property.
     * 
     * @param value
     *     allowed object is
     *     {@link Usuario }
     *     
     */
    public void setUsuarioCreador(Usuario value) {
        this.usuarioCreador = value;
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

}
