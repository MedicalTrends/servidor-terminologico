
package cl.minsal.semantikos.ws.shared;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PeticionBuscarTermino complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PeticionBuscarTermino">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="termino" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nombreCategoria" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="nombreRefSet" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="numeroPagina" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="tamanoPagina" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PeticionBuscarTermino", propOrder = {
    "termino",
    "nombreCategoria",
    "nombreRefSet",
    "numeroPagina",
    "tamanoPagina"
})
public class PeticionBuscarTermino {

    @XmlElement(required = true)
    protected String termino;
    protected List<String> nombreCategoria;
    protected List<String> nombreRefSet;
    @XmlElement(defaultValue = "0")
    protected Integer numeroPagina;
    @XmlElement(defaultValue = "10")
    protected Integer tamanoPagina;

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
     * Gets the value of the nombreCategoria property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nombreCategoria property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNombreCategoria().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getNombreCategoria() {
        if (nombreCategoria == null) {
            nombreCategoria = new ArrayList<String>();
        }
        return this.nombreCategoria;
    }

    /**
     * Gets the value of the nombreRefSet property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nombreRefSet property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNombreRefSet().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getNombreRefSet() {
        if (nombreRefSet == null) {
            nombreRefSet = new ArrayList<String>();
        }
        return this.nombreRefSet;
    }

    /**
     * Gets the value of the numeroPagina property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumeroPagina() {
        return numeroPagina;
    }

    /**
     * Sets the value of the numeroPagina property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumeroPagina(Integer value) {
        this.numeroPagina = value;
    }

    /**
     * Gets the value of the tamanoPagina property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTamanoPagina() {
        return tamanoPagina;
    }

    /**
     * Sets the value of the tamanoPagina property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTamanoPagina(Integer value) {
        this.tamanoPagina = value;
    }

}
