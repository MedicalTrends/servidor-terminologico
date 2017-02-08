
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DescripcionPerfectMatch complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DescripcionPerfectMatch">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idConcepto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idDescripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="termino" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoDescripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreCategoria" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="terminoPreferido" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DescripcionPerfectMatch", propOrder = {
    "idConcepto",
    "idDescripcion",
    "termino",
    "tipoDescripcion",
    "nombreCategoria",
    "terminoPreferido"
})
public class DescripcionPerfectMatch {

    protected String idConcepto;
    protected String idDescripcion;
    protected String termino;
    protected String tipoDescripcion;
    protected String nombreCategoria;
    protected String terminoPreferido;

    /**
     * Gets the value of the idConcepto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdConcepto() {
        return idConcepto;
    }

    /**
     * Sets the value of the idConcepto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdConcepto(String value) {
        this.idConcepto = value;
    }

    /**
     * Gets the value of the idDescripcion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDescripcion() {
        return idDescripcion;
    }

    /**
     * Sets the value of the idDescripcion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDescripcion(String value) {
        this.idDescripcion = value;
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
     * Gets the value of the tipoDescripcion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoDescripcion() {
        return tipoDescripcion;
    }

    /**
     * Sets the value of the tipoDescripcion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoDescripcion(String value) {
        this.tipoDescripcion = value;
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

}
