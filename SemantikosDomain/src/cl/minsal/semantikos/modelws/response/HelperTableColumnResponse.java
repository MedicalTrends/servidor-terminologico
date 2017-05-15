package cl.minsal.semantikos.modelws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Development on 2016-10-19.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "columnaTablaAuxiliar", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "ColumnaTablaAuxiliar", namespace = "http://service.ws.semantikos.minsal.cl/")
public class HelperTableColumnResponse implements Serializable {

    @XmlElement(name="nombreColumna")
    private String columnName;
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
