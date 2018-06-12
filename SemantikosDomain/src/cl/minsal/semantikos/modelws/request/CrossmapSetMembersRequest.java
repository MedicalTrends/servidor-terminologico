package cl.minsal.semantikos.modelws.request;

import cl.minsal.semantikos.modelws.fault.IllegalInputFault;
import cl.minsal.semantikos.modelws.fault.NotFoundFault;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Esta clase representa una petición de servicio que recibe como argumento una categoría.
 *
 * @author Alonso Cornejo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "peticionCrosmapSetMembers", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "PeticionCrosmapSetMembers", namespace = "http://service.ws.semantikos.minsal.cl/")
public class CrossmapSetMembersRequest extends Request implements Serializable {

    @XmlElement(required = true, name = "nombreAbreviadoCrossmapSet")
    private String crossmapSetAbbreviatedName;

    @XmlElement(required = true, defaultValue = "0", name = "numeroPagina")
    private int pageNumber;

    @XmlElement(required = true, defaultValue = "30", name = "tamanoPagina")
    private int pageSize;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getCrossmapSetAbbreviatedName() {
        return crossmapSetAbbreviatedName;
    }

    public void setCrossmapSetAbbreviatedName(String crossmapSetAbbreviatedName) {
        this.crossmapSetAbbreviatedName = crossmapSetAbbreviatedName;
    }

    public void validate() throws IllegalInputFault {
        super.validate();
        if(crossmapSetAbbreviatedName == null || crossmapSetAbbreviatedName.trim().isEmpty()) {
            throw new IllegalInputFault("Debe ingresar el nombre abreviado del CrossmapSet");
        }

        if(pageSize > 100) {
            throw new IllegalInputFault("Tamaño de página excede máximo permitido de 100 registros");
        }
    };
}
