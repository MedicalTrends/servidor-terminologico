
package cl.minsal.semantikos.ws.shared;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RespuestaRefSets complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RespuestaRefSets">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="refsets" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="refset" type="{http://service.ws.semantikos.minsal.cl/}RefSet" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "RespuestaRefSets", propOrder = {
    "refsets"
})
public class RespuestaRefSets {

    protected RespuestaRefSets.Refsets refsets;

    /**
     * Gets the value of the refsets property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaRefSets.Refsets }
     *     
     */
    public RespuestaRefSets.Refsets getRefsets() {
        return refsets;
    }

    /**
     * Sets the value of the refsets property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaRefSets.Refsets }
     *     
     */
    public void setRefsets(RespuestaRefSets.Refsets value) {
        this.refsets = value;
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
     *         &lt;element name="refset" type="{http://service.ws.semantikos.minsal.cl/}RefSet" maxOccurs="unbounded" minOccurs="0"/>
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
        "refset"
    })
    public static class Refsets {

        protected List<RefSet> refset;

        /**
         * Gets the value of the refset property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the refset property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getRefset().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link RefSet }
         * 
         * 
         */
        public List<RefSet> getRefset() {
            if (refset == null) {
                refset = new ArrayList<RefSet>();
            }
            return this.refset;
        }

    }

}
