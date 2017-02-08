
package cl.minsal.semantikos.ws.shared;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IndirectCrossmapsSearch complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IndirectCrossmapsSearch">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="indirectCrossmaps" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://service.ws.semantikos.minsal.cl/}indirectCrossmap" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "IndirectCrossmapsSearch", propOrder = {
    "indirectCrossmaps"
})
public class IndirectCrossmapsSearch {

    protected IndirectCrossmapsSearch.IndirectCrossmaps indirectCrossmaps;

    /**
     * Gets the value of the indirectCrossmaps property.
     * 
     * @return
     *     possible object is
     *     {@link IndirectCrossmapsSearch.IndirectCrossmaps }
     *     
     */
    public IndirectCrossmapsSearch.IndirectCrossmaps getIndirectCrossmaps() {
        return indirectCrossmaps;
    }

    /**
     * Sets the value of the indirectCrossmaps property.
     * 
     * @param value
     *     allowed object is
     *     {@link IndirectCrossmapsSearch.IndirectCrossmaps }
     *     
     */
    public void setIndirectCrossmaps(IndirectCrossmapsSearch.IndirectCrossmaps value) {
        this.indirectCrossmaps = value;
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
     *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}indirectCrossmap" maxOccurs="unbounded" minOccurs="0"/>
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
        "indirectCrossmap"
    })
    public static class IndirectCrossmaps {

        @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/")
        protected List<IndirectCrossmap> indirectCrossmap;

        /**
         * Gets the value of the indirectCrossmap property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the indirectCrossmap property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getIndirectCrossmap().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link IndirectCrossmap }
         * 
         * 
         */
        public List<IndirectCrossmap> getIndirectCrossmap() {
            if (indirectCrossmap == null) {
                indirectCrossmap = new ArrayList<IndirectCrossmap>();
            }
            return this.indirectCrossmap;
        }

    }

}
