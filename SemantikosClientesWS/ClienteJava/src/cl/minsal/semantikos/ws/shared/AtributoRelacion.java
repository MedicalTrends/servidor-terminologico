
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AtributoRelacion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AtributoRelacion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}objetivo" minOccurs="0"/>
 *         &lt;element name="definicionAtributoRelacion" type="{http://service.ws.semantikos.minsal.cl/}DefinicionRelacionAtributo" minOccurs="0"/>
 *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}relacion" minOccurs="0"/>
 *         &lt;element name="tipo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="valor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AtributoRelacion", propOrder = {
    "objetivo",
    "definicionAtributoRelacion",
    "relacion",
    "tipo",
    "nombre",
    "valor"
})
public class AtributoRelacion {

    @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/")
    protected Objetivo objetivo;
    protected DefinicionRelacionAtributo definicionAtributoRelacion;
    @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/")
    protected Relacion relacion;
    protected String tipo;
    protected String nombre;
    protected String valor;

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
     * Gets the value of the definicionAtributoRelacion property.
     * 
     * @return
     *     possible object is
     *     {@link DefinicionRelacionAtributo }
     *     
     */
    public DefinicionRelacionAtributo getDefinicionAtributoRelacion() {
        return definicionAtributoRelacion;
    }

    /**
     * Sets the value of the definicionAtributoRelacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefinicionRelacionAtributo }
     *     
     */
    public void setDefinicionAtributoRelacion(DefinicionRelacionAtributo value) {
        this.definicionAtributoRelacion = value;
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

}
