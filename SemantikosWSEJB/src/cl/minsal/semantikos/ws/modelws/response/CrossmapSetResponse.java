package cl.minsal.semantikos.ws.modelws.response;

import cl.minsal.semantikos.model.crossmaps.CrossmapSet;

import javax.xml.bind.annotation.*;

/**
 * @author Andrés Farías on 12/16/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "crossmapSet", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "CrossmapSet", namespace = "http://service.ws.semantikos.minsal.cl/")
public class CrossmapSetResponse {

    /** Nombre corto CrossmapSet */

    @XmlElement(name = "nombreCortoCrossmapSet")
    private String abbreviatedName;

    @XmlElement(name = "nombreCrossmapSet")
    private String name;

    /** Año de la versión */
    @XmlElement(name = "version")
    private int version;

    public CrossmapSetResponse() {
    }

    public CrossmapSetResponse(CrossmapSet crossmapSet) {
        this.abbreviatedName = crossmapSet.getAbbreviatedName();
        this.name = crossmapSet.getName();
        this.version = crossmapSet.getVersion();
    }

    public String getAbbreviatedName() {
        return abbreviatedName;
    }

    public void setAbbreviatedName(String abbreviatedName) {
        this.abbreviatedName = abbreviatedName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
