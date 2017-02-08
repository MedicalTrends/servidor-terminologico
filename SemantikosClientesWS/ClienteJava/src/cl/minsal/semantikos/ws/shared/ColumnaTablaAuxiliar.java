
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ColumnaTablaAuxiliar complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ColumnaTablaAuxiliar">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nombreColumna" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="esLlavePrimaria" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="esBuscable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="esMostrable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ColumnaTablaAuxiliar", propOrder = {
    "nombreColumna",
    "esLlavePrimaria",
    "esBuscable",
    "esMostrable"
})
public class ColumnaTablaAuxiliar {

    protected String nombreColumna;
    protected Boolean esLlavePrimaria;
    protected Boolean esBuscable;
    protected Boolean esMostrable;

    /**
     * Gets the value of the nombreColumna property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreColumna() {
        return nombreColumna;
    }

    /**
     * Sets the value of the nombreColumna property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreColumna(String value) {
        this.nombreColumna = value;
    }

    /**
     * Gets the value of the esLlavePrimaria property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEsLlavePrimaria() {
        return esLlavePrimaria;
    }

    /**
     * Sets the value of the esLlavePrimaria property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEsLlavePrimaria(Boolean value) {
        this.esLlavePrimaria = value;
    }

    /**
     * Gets the value of the esBuscable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEsBuscable() {
        return esBuscable;
    }

    /**
     * Sets the value of the esBuscable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEsBuscable(Boolean value) {
        this.esBuscable = value;
    }

    /**
     * Gets the value of the esMostrable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEsMostrable() {
        return esMostrable;
    }

    /**
     * Sets the value of the esMostrable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEsMostrable(Boolean value) {
        this.esMostrable = value;
    }

}
