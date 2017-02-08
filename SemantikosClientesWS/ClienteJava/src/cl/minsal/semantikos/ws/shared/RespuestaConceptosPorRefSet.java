
package cl.minsal.semantikos.ws.shared;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RespuestaConceptosPorRefSet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RespuestaConceptosPorRefSet">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}refSet" minOccurs="0"/>
 *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}paginacion" minOccurs="0"/>
 *         &lt;element name="conceptos" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="concepto" type="{http://service.ws.semantikos.minsal.cl/}ConceptoLight" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RespuestaConceptosPorRefSet", propOrder = {
    "refSet",
    "paginacion",
    "conceptos"
})
public class RespuestaConceptosPorRefSet {

    @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/")
    protected RefSet refSet;
    @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/")
    protected Paginacion paginacion;
    protected RespuestaConceptosPorRefSet.Conceptos conceptos;

    /**
     * Gets the value of the refSet property.
     * 
     * @return
     *     possible object is
     *     {@link RefSet }
     *     
     */
    public RefSet getRefSet() {
        return refSet;
    }

    /**
     * Sets the value of the refSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link RefSet }
     *     
     */
    public void setRefSet(RefSet value) {
        this.refSet = value;
    }

    /**
     * Gets the value of the paginacion property.
     * 
     * @return
     *     possible object is
     *     {@link Paginacion }
     *     
     */
    public Paginacion getPaginacion() {
        return paginacion;
    }

    /**
     * Sets the value of the paginacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Paginacion }
     *     
     */
    public void setPaginacion(Paginacion value) {
        this.paginacion = value;
    }

    /**
     * Gets the value of the conceptos property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaConceptosPorRefSet.Conceptos }
     *     
     */
    public RespuestaConceptosPorRefSet.Conceptos getConceptos() {
        return conceptos;
    }

    /**
     * Sets the value of the conceptos property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaConceptosPorRefSet.Conceptos }
     *     
     */
    public void setConceptos(RespuestaConceptosPorRefSet.Conceptos value) {
        this.conceptos = value;
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
     *         &lt;element name="concepto" type="{http://service.ws.semantikos.minsal.cl/}ConceptoLight" maxOccurs="unbounded" minOccurs="0"/>
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

        protected List<ConceptoLight> concepto;

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
         * {@link ConceptoLight }
         * 
         * 
         */
        public List<ConceptoLight> getConcepto() {
            if (concepto == null) {
                concepto = new ArrayList<ConceptoLight>();
            }
            return this.concepto;
        }

    }

}
