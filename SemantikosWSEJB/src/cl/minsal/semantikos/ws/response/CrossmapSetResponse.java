package cl.minsal.semantikos.ws.response;

import cl.minsal.semantikos.model.crossmaps.CrossmapSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Andrés Farías on 12/16/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "crossmapSet", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "CrossmapSet", namespace = "http://service.ws.semantikos.minsal.cl/")
public class CrossmapSetResponse {

    private String abbreviatedName;

    private String name;

    /** Año de la versión */
    private int version;

    public CrossmapSetResponse() {
    }

    public CrossmapSetResponse(CrossmapSet crossmapSet) {
        this.abbreviatedName = crossmapSet.getAbbreviatedName();
        this.name = crossmapSet.getName();
        this.version = crossmapSet.getVersion();
    }
}
