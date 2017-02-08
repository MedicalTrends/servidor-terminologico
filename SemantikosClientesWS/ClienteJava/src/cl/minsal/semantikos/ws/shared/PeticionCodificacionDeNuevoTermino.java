
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PeticionCodificacionDeNuevoTermino complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PeticionCodificacionDeNuevoTermino">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="establecimiento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idConcepto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="termino" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tipoDescripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="esSensibleAMayusculas" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="observacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="profesional" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="profesion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="especialidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sub-especialidad" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="categoria" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PeticionCodificacionDeNuevoTermino", propOrder = {
    "establecimiento",
    "idConcepto",
    "termino",
    "tipoDescripcion",
    "esSensibleAMayusculas",
    "email",
    "observacion",
    "profesional",
    "profesion",
    "especialidad",
    "subEspecialidad",
    "categoria"
})
public class PeticionCodificacionDeNuevoTermino {

    @XmlElement(required = true)
    protected String establecimiento;
    @XmlElement(required = true)
    protected String idConcepto;
    @XmlElement(required = true)
    protected String termino;
    @XmlElement(defaultValue = "Preferida")
    protected String tipoDescripcion;
    @XmlElement(defaultValue = "false")
    protected Boolean esSensibleAMayusculas;
    protected String email;
    protected String observacion;
    protected String profesional;
    protected String profesion;
    protected String especialidad;
    @XmlElement(name = "sub-especialidad", required = true)
    protected String subEspecialidad;

    @XmlElement(required = true)
    protected String categoria;

    /**
     * Gets the value of the establecimiento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstablecimiento() {
        return establecimiento;
    }

    /**
     * Sets the value of the establecimiento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstablecimiento(String value) {
        this.establecimiento = value;
    }

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
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
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
     * Gets the value of the profesional property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProfesional() {
        return profesional;
    }

    /**
     * Sets the value of the profesional property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfesional(String value) {
        this.profesional = value;
    }

    /**
     * Gets the value of the profesion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProfesion() {
        return profesion;
    }

    /**
     * Sets the value of the profesion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfesion(String value) {
        this.profesion = value;
    }

    /**
     * Gets the value of the especialidad property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEspecialidad() {
        return especialidad;
    }

    /**
     * Sets the value of the especialidad property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEspecialidad(String value) {
        this.especialidad = value;
    }

    /**
     * Gets the value of the subEspecialidad property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubEspecialidad() {
        return subEspecialidad;
    }

    /**
     * Sets the value of the subEspecialidad property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubEspecialidad(String value) {
        this.subEspecialidad = value;
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

}
