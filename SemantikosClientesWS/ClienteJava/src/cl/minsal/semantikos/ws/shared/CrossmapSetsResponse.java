
package cl.minsal.semantikos.ws.shared;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CrossmapSetsResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CrossmapSetsResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cantidadRegistros" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="crossmapSets" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://service.ws.semantikos.minsal.cl/}crossmapSet" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "CrossmapSetsResponse", propOrder = {
    "cantidadRegistros",
    "crossmapSets"
})
public class CrossmapSetsResponse {

    protected int cantidadRegistros;
    protected CrossmapSetsResponse.CrossmapSets crossmapSets;

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
     * Gets the value of the crossmapSets property.
     * 
     * @return
     *     possible object is
     *     {@link CrossmapSetsResponse.CrossmapSets }
     *     
     */
    public CrossmapSetsResponse.CrossmapSets getCrossmapSets() {
        return crossmapSets;
    }

    /**
     * Sets the value of the crossmapSets property.
     * 
     * @param value
     *     allowed object is
     *     {@link CrossmapSetsResponse.CrossmapSets }
     *     
     */
    public void setCrossmapSets(CrossmapSetsResponse.CrossmapSets value) {
        this.crossmapSets = value;
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
     *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}crossmapSet" maxOccurs="unbounded" minOccurs="0"/>
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
        "crossmapSet"
    })
    public static class CrossmapSets {

        @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/")
        protected List<CrossmapSet> crossmapSet;

        /**
         * Gets the value of the crossmapSet property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the crossmapSet property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCrossmapSet().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CrossmapSet }
         * 
         * 
         */
        public List<CrossmapSet> getCrossmapSet() {
            if (crossmapSet == null) {
                crossmapSet = new ArrayList<CrossmapSet>();
            }
            return this.crossmapSet;
        }

    }

}
