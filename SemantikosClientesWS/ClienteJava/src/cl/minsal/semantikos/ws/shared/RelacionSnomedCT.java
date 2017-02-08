
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RelacionSnomedCT complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RelacionSnomedCT">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tipoRelacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idConceptoSnomedCT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="grupo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RelacionSnomedCT", propOrder = {
    "tipoRelacion",
    "idConceptoSnomedCT",
    "descripcion",
    "grupo"
})
public class RelacionSnomedCT {

    protected String tipoRelacion;
    protected String idConceptoSnomedCT;
    protected String descripcion;
    protected String grupo;

    /**
     * Gets the value of the tipoRelacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoRelacion() {
        return tipoRelacion;
    }

    /**
     * Sets the value of the tipoRelacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoRelacion(String value) {
        this.tipoRelacion = value;
    }

    /**
     * Gets the value of the idConceptoSnomedCT property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdConceptoSnomedCT() {
        return idConceptoSnomedCT;
    }

    /**
     * Sets the value of the idConceptoSnomedCT property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdConceptoSnomedCT(String value) {
        this.idConceptoSnomedCT = value;
    }

    /**
     * Gets the value of the descripcion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Sets the value of the descripcion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcion(String value) {
        this.descripcion = value;
    }

    /**
     * Gets the value of the grupo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrupo() {
        return grupo;
    }

    /**
     * Sets the value of the grupo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrupo(String value) {
        this.grupo = value;
    }

}
