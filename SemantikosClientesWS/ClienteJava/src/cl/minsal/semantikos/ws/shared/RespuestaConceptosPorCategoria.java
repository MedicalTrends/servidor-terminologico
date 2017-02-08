
package cl.minsal.semantikos.ws.shared;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RespuestaConceptosPorCategoria complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RespuestaConceptosPorCategoria">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="conceptos" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://service.ws.semantikos.minsal.cl/}concepto" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="cantidadRegistros" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RespuestaConceptosPorCategoria", propOrder = {
    "conceptos",
    "cantidadRegistros"
})
public class RespuestaConceptosPorCategoria {

    protected RespuestaConceptosPorCategoria.Conceptos conceptos;
    protected int cantidadRegistros;

    /**
     * Gets the value of the conceptos property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaConceptosPorCategoria.Conceptos }
     *     
     */
    public RespuestaConceptosPorCategoria.Conceptos getConceptos() {
        return conceptos;
    }

    /**
     * Sets the value of the conceptos property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaConceptosPorCategoria.Conceptos }
     *     
     */
    public void setConceptos(RespuestaConceptosPorCategoria.Conceptos value) {
        this.conceptos = value;
    }

    /**
     * Gets the value of the cantidadRegistros property.
     * 
     */
    public int getCantidadRegistros() {
        return cantidadRegistros;
    }

    /**
     * Sets the value of the cantidadRegistros property.
     * 
     */
    public void setCantidadRegistros(int value) {
        this.cantidadRegistros = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}concepto" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "concepto"
    })
    public static class Conceptos {

        @XmlElement(namespace = "")
        protected List<Concepto> concepto;

        /**
         * Gets the value of the concepto property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the concepto property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getConcepto().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Concepto }
         * 
         * 
         */
        public List<Concepto> getConcepto() {
            if (concepto == null) {
                concepto = new ArrayList<Concepto>();
            }
            return this.concepto;
        }

    }

}
