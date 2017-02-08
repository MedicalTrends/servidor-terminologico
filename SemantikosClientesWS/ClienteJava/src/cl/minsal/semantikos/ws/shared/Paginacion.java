
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Paginacion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Paginacion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="totalRegistros" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="paginaActual" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="registrosPorPagina" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="mostrandoDesde" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="mostrandoHasta" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Paginacion", propOrder = {
    "totalRegistros",
    "paginaActual",
    "registrosPorPagina",
    "mostrandoDesde",
    "mostrandoHasta"
})
public class Paginacion {

    protected Integer totalRegistros;
    protected Integer paginaActual;
    protected Integer registrosPorPagina;
    protected Integer mostrandoDesde;
    protected Integer mostrandoHasta;

    /**
     * Gets the value of the totalRegistros property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTotalRegistros() {
        return totalRegistros;
    }

    /**
     * Sets the value of the totalRegistros property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTotalRegistros(Integer value) {
        this.totalRegistros = value;
    }

    /**
     * Gets the value of the paginaActual property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPaginaActual() {
        return paginaActual;
    }

    /**
     * Sets the value of the paginaActual property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPaginaActual(Integer value) {
        this.paginaActual = value;
    }

    /**
     * Gets the value of the registrosPorPagina property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRegistrosPorPagina() {
        return registrosPorPagina;
    }

    /**
     * Sets the value of the registrosPorPagina property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRegistrosPorPagina(Integer value) {
        this.registrosPorPagina = value;
    }

    /**
     * Gets the value of the mostrandoDesde property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMostrandoDesde() {
        return mostrandoDesde;
    }

    /**
     * Sets the value of the mostrandoDesde property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMostrandoDesde(Integer value) {
        this.mostrandoDesde = value;
    }

    /**
     * Gets the value of the mostrandoHasta property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMostrandoHasta() {
        return mostrandoHasta;
    }

    /**
     * Sets the value of the mostrandoHasta property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMostrandoHasta(Integer value) {
        this.mostrandoHasta = value;
    }

}
