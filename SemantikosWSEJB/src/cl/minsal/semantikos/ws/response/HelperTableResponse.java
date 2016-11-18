package cl.minsal.semantikos.ws.response;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Development on 2016-10-19.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "tablaAuxiliar")
public class HelperTableResponse implements Serializable {

    @XmlElement(name="nombre")
    private String name;
    @XmlElement(name="descripcion")
    private String description;
    @XmlElement(name="nombreTabla")
    private String tablaName;
    @XmlElementWrapper(name = "columnaTablaAuxiliar")
    @XmlElement(name="columna")
    private List<HelperTableColumnResponse> columns;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTablaName() {
        return tablaName;
    }

    public void setTablaName(String tablaName) {
        this.tablaName = tablaName;
    }

    public List<HelperTableColumnResponse> getColumns() {
        return columns;
    }

    public void setColumns(List<HelperTableColumnResponse> columns) {
        this.columns = columns;
    }
}
