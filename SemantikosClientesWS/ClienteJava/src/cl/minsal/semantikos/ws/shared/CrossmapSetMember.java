
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CrossmapSetMember complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CrossmapSetMember">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idCrossmapSetMember" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}crossmapSet" minOccurs="0"/>
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gloss" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CrossmapSetMember", propOrder = {
    "idCrossmapSetMember",
    "crossmapSet",
    "code",
    "gloss"
})
public class CrossmapSetMember {

    protected long idCrossmapSetMember;
    @XmlElement(namespace = "http://service.ws.semantikos.minsal.cl/")
    protected CrossmapSet crossmapSet;
    protected String code;
    protected String gloss;

    /**
     * Gets the value of the idCrossmapSetMember property.
     * 
     */
    public long getIdCrossmapSetMember() {
        return idCrossmapSetMember;
    }

    /**
     * Sets the value of the idCrossmapSetMember property.
     * 
     */
    public void setIdCrossmapSetMember(long value) {
        this.idCrossmapSetMember = value;
    }

    /**
     * Gets the value of the crossmapSet property.
     * 
     * @return
     *     possible object is
     *     {@link CrossmapSet }
     *     
     */
    public CrossmapSet getCrossmapSet() {
        return crossmapSet;
    }

    /**
     * Sets the value of the crossmapSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link CrossmapSet }
     *     
     */
    public void setCrossmapSet(CrossmapSet value) {
        this.crossmapSet = value;
    }

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Gets the value of the gloss property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGloss() {
        return gloss;
    }

    /**
     * Sets the value of the gloss property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGloss(String value) {
        this.gloss = value;
    }

}
