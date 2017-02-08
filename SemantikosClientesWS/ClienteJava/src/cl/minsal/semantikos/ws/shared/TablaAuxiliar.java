
package cl.minsal.semantikos.ws.shared;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TablaAuxiliar complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TablaAuxiliar">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreTabla" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="columnaTablaAuxiliar" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="columna" type="{http://service.ws.semantikos.minsal.cl/}ColumnaTablaAuxiliar" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "TablaAuxiliar", propOrder = {
    "nombre",
    "descripcion",
    "nombreTabla",
    "columnaTablaAuxiliar"
})
public class TablaAuxiliar {

    protected String nombre;
    protected String descripcion;
    protected String nombreTabla;
    protected TablaAuxiliar.ColumnaTablaAuxiliar columnaTablaAuxiliar;

    /**
     * Gets the value of the nombre property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the value of the nombre property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Gets the value of the descripcion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Sets the value of the descripcion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcion(String value) {
        this.descripcion = value;
    }

    /**
     * Gets the value of the nombreTabla property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreTabla() {
        return nombreTabla;
    }

    /**
     * Sets the value of the nombreTabla property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreTabla(String value) {
        this.nombreTabla = value;
    }

    /**
     * Gets the value of the columnaTablaAuxiliar property.
     * 
     * @return
     *     possible object is
     *     {@link TablaAuxiliar.ColumnaTablaAuxiliar }
     *     
     */
    public TablaAuxiliar.ColumnaTablaAuxiliar getColumnaTablaAuxiliar() {
        return columnaTablaAuxiliar;
    }

    /**
     * Sets the value of the columnaTablaAuxiliar property.
     * 
     * @param value
     *     allowed object is
     *     {@link TablaAuxiliar.ColumnaTablaAuxiliar }
     *     
     */
    public void setColumnaTablaAuxiliar(TablaAuxiliar.ColumnaTablaAuxiliar value) {
        this.columnaTablaAuxiliar = value;
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
     *         &lt;element name="columna" type="{http://service.ws.semantikos.minsal.cl/}ColumnaTablaAuxiliar" maxOccurs="unbounded" minOccurs="0"/>
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
        "columna"
    })
    public static class ColumnaTablaAuxiliar {

        protected List<cl.minsal.semantikos.ws.shared.ColumnaTablaAuxiliar> columna;

        /**
         * Gets the value of the columna property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the columna property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getColumna().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link cl.minsal.semantikos.ws.shared.ColumnaTablaAuxiliar }
         * 
         * 
         */
        public List<cl.minsal.semantikos.ws.shared.ColumnaTablaAuxiliar> getColumna() {
            if (columna == null) {
                columna = new ArrayList<cl.minsal.semantikos.ws.shared.ColumnaTablaAuxiliar>();
            }
            return this.columna;
        }

    }

}
