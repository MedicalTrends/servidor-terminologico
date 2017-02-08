
package cl.minsal.semantikos.ws.shared;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RespuestaCategorias complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RespuestaCategorias">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="categorias" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://service.ws.semantikos.minsal.cl/}categoria" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "RespuestaCategorias", propOrder = {
    "categorias"
})
public class RespuestaCategorias {

    protected RespuestaCategorias.Categorias categorias;

    /**
     * Gets the value of the categorias property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaCategorias.Categorias }
     *     
     */
    public RespuestaCategorias.Categorias getCategorias() {
        return categorias;
    }

    /**
     * Sets the value of the categorias property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaCategorias.Categorias }
     *     
     */
    public void setCategorias(RespuestaCategorias.Categorias value) {
        this.categorias = value;
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
     *         &lt;element ref="{http://service.ws.semantikos.minsal.cl/}categoria" maxOccurs="unbounded" minOccurs="0"/>
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
        "categoria"
    })
    public static class Categorias {

        @XmlElement(namespace = "")
        protected List<Categoria> categoria;

        /**
         * Gets the value of the categoria property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the categoria property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCategoria().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Categoria }
         * 
         * 
         */
        public List<Categoria> getCategoria() {
            if (categoria == null) {
                categoria = new ArrayList<Categoria>();
            }
            return this.categoria;
        }

    }

}
