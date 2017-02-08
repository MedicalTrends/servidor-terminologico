
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DefinicionObjetivo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DefinicionObjetivo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="esTipoBasico" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="esTipoSMTK" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="esTablaAuxiliar" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="esTipoSnomedCT" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="esTipoCrossMap" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DefinicionObjetivo", propOrder = {
    "esTipoBasico",
    "esTipoSMTK",
    "esTablaAuxiliar",
    "esTipoSnomedCT",
    "esTipoCrossMap"
})
public class DefinicionObjetivo {

    protected Boolean esTipoBasico;
    protected Boolean esTipoSMTK;
    protected Boolean esTablaAuxiliar;
    protected Boolean esTipoSnomedCT;
    protected Boolean esTipoCrossMap;

    /**
     * Gets the value of the esTipoBasico property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEsTipoBasico() {
        return esTipoBasico;
    }

    /**
     * Sets the value of the esTipoBasico property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEsTipoBasico(Boolean value) {
        this.esTipoBasico = value;
    }

    /**
     * Gets the value of the esTipoSMTK property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEsTipoSMTK() {
        return esTipoSMTK;
    }

    /**
     * Sets the value of the esTipoSMTK property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEsTipoSMTK(Boolean value) {
        this.esTipoSMTK = value;
    }

    /**
     * Gets the value of the esTablaAuxiliar property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEsTablaAuxiliar() {
        return esTablaAuxiliar;
    }

    /**
     * Sets the value of the esTablaAuxiliar property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEsTablaAuxiliar(Boolean value) {
        this.esTablaAuxiliar = value;
    }

    /**
     * Gets the value of the esTipoSnomedCT property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEsTipoSnomedCT() {
        return esTipoSnomedCT;
    }

    /**
     * Sets the value of the esTipoSnomedCT property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEsTipoSnomedCT(Boolean value) {
        this.esTipoSnomedCT = value;
    }

    /**
     * Gets the value of the esTipoCrossMap property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEsTipoCrossMap() {
        return esTipoCrossMap;
    }

    /**
     * Sets the value of the esTipoCrossMap property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEsTipoCrossMap(Boolean value) {
        this.esTipoCrossMap = value;
    }

}
