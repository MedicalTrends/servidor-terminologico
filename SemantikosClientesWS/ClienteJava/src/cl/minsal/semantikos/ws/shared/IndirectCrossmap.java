
package cl.minsal.semantikos.ws.shared;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IndirectCrossmap complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IndirectCrossmap">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mapGroup" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="mapPriority" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="mapRule" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mapAdvice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mapTarget" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="correlation" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="idCrossmapCategory" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IndirectCrossmap", propOrder = {
    "mapGroup",
    "mapPriority",
    "mapRule",
    "mapAdvice",
    "mapTarget",
    "correlation",
    "idCrossmapCategory",
    "state"
})
public class IndirectCrossmap {

    protected int mapGroup;
    protected int mapPriority;
    protected String mapRule;
    protected String mapAdvice;
    protected String mapTarget;
    protected long correlation;
    protected long idCrossmapCategory;
    protected boolean state;

    /**
     * Gets the value of the mapGroup property.
     * 
     */
    public int getMapGroup() {
        return mapGroup;
    }

    /**
     * Sets the value of the mapGroup property.
     * 
     */
    public void setMapGroup(int value) {
        this.mapGroup = value;
    }

    /**
     * Gets the value of the mapPriority property.
     * 
     */
    public int getMapPriority() {
        return mapPriority;
    }

    /**
     * Sets the value of the mapPriority property.
     * 
     */
    public void setMapPriority(int value) {
        this.mapPriority = value;
    }

    /**
     * Gets the value of the mapRule property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMapRule() {
        return mapRule;
    }

    /**
     * Sets the value of the mapRule property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMapRule(String value) {
        this.mapRule = value;
    }

    /**
     * Gets the value of the mapAdvice property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMapAdvice() {
        return mapAdvice;
    }

    /**
     * Sets the value of the mapAdvice property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMapAdvice(String value) {
        this.mapAdvice = value;
    }

    /**
     * Gets the value of the mapTarget property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMapTarget() {
        return mapTarget;
    }

    /**
     * Sets the value of the mapTarget property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMapTarget(String value) {
        this.mapTarget = value;
    }

    /**
     * Gets the value of the correlation property.
     * 
     */
    public long getCorrelation() {
        return correlation;
    }

    /**
     * Sets the value of the correlation property.
     * 
     */
    public void setCorrelation(long value) {
        this.correlation = value;
    }

    /**
     * Gets the value of the idCrossmapCategory property.
     * 
     */
    public long getIdCrossmapCategory() {
        return idCrossmapCategory;
    }

    /**
     * Sets the value of the idCrossmapCategory property.
     * 
     */
    public void setIdCrossmapCategory(long value) {
        this.idCrossmapCategory = value;
    }

    /**
     * Gets the value of the state property.
     * 
     */
    public boolean isState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     */
    public void setState(boolean value) {
        this.state = value;
    }

}
