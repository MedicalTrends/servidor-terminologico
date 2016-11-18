package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Development on 2016-10-19.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "columnaTablaAuxiliar")
public class HelperTableColumnResponse implements Serializable {

    @XmlElement(name="nombreColumna")
    private String columnName;
    @XmlElement(name="esLlavePrimaria")
    private Boolean isPK;
    @XmlElement(name="esBuscable")
    private Boolean isSearchable;
    @XmlElement(name="esMostrable")
    private Boolean isShowable;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Boolean getPK() {
        return isPK;
    }

    public void setPK(Boolean PK) {
        isPK = PK;
    }

    public Boolean getSearchable() {
        return isSearchable;
    }

    public void setSearchable(Boolean serachable) {
        isSearchable = serachable;
    }

    public Boolean getShowable() {
        return isShowable;
    }

    public void setShowable(Boolean showable) {
        isShowable = showable;
    }
}
