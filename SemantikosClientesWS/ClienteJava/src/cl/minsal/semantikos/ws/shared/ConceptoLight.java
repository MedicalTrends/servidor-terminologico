
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ConceptoLight complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ConceptoLight">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="conceptID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idDescripcionPreferida" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcionPreferida" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="categoria" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="esValido" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConceptoLight", propOrder = {
    "conceptID",
    "idDescripcionPreferida",
    "descripcionPreferida",
    "categoria",
    "esValido"
})
public class ConceptoLight {

    protected String conceptID;
    protected String idDescripcionPreferida;
    protected String descripcionPreferida;
    protected String categoria;
    protected Boolean esValido;

    /**
     * Gets the value of the conceptID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConceptID() {
        return conceptID;
    }

    /**
     * Sets the value of the conceptID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConceptID(String value) {
        this.conceptID = value;
    }

    /**
     * Gets the value of the idDescripcionPreferida property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDescripcionPreferida() {
        return idDescripcionPreferida;
    }

    /**
     * Sets the value of the idDescripcionPreferida property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDescripcionPreferida(String value) {
        this.idDescripcionPreferida = value;
    }

    /**
     * Gets the value of the descripcionPreferida property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionPreferida() {
        return descripcionPreferida;
    }

    /**
     * Sets the value of the descripcionPreferida property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionPreferida(String value) {
        this.descripcionPreferida = value;
    }

    /**
     * Gets the value of the categoria property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Sets the value of the categoria property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategoria(String value) {
        this.categoria = value;
    }

    /**
     * Gets the value of the esValido property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEsValido() {
        return esValido;
    }

    /**
     * Sets the value of the esValido property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEsValido(Boolean value) {
        this.esValido = value;
    }

}
