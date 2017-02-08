
package cl.minsal.semantikos.ws.shared;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CrossmapSetMembersResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CrossmapSetMembersResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="crossmapSetMembers" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://service.ws.semantikos.minsal.cl/}crossmapSetMember" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "CrossmapSetMembersResponse", propOrder = {
    "crossmapSetMembers"
})
public class CrossmapSetMembersResponse {

    protected CrossmapSetMembersResponse.CrossmapSetMembers crossmapSetMembers;

    /**
     * Gets the value of the crossmapSetMembers property.
     * 
     * @return
     *     possible object is
     *     {@link CrossmapSetMembersResponse.CrossmapSetMembers }
     *     
     */
    public CrossmapSetMembersResponse.CrossmapSetMembers getCrossmapSetMembers() {
        return crossmapSetMembers;
    }

    /**
     * Sets the value of the crossmapSetMembers property.
     * 
     * @param value
     *     allowed object is
     *     {@link CrossmapSetMembersResponse.CrossmapSetMembers }
     *     
     */
    public void setCrossmapSetMembers(CrossmapSetMembersResponse.CrossmapSetMembers value) {
        this.crossmapSetMembers = value;
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
     *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}crossmapSetMember" maxOccurs="unbounded" minOccurs="0"/>
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
        "crossmapSetMember"
    })
    public static class CrossmapSetMembers {

        @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/")
        protected List<CrossmapSetMember> crossmapSetMember;

        /**
         * Gets the value of the crossmapSetMember property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the crossmapSetMember property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCrossmapSetMember().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CrossmapSetMember }
         * 
         * 
         */
        public List<CrossmapSetMember> getCrossmapSetMember() {
            if (crossmapSetMember == null) {
                crossmapSetMember = new ArrayList<CrossmapSetMember>();
            }
            return this.crossmapSetMember;
        }

    }

}
