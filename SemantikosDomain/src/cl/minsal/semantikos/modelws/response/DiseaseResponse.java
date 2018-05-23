package cl.minsal.semantikos.modelws.response;

import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;
import cl.minsal.semantikos.model.crossmaps.cie10.Disease;

import javax.xml.bind.annotation.*;

import java.io.Serializable;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;

/**
 * @author Andrés Farías on 12/15/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "disease", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "Disease", namespace = "http://service.ws.semantikos.minsal.cl/")
public class DiseaseResponse extends CrossmapSetMemberResponse implements Serializable {

    /** ID de negocio */
    @XmlElement(name = "idCrossmapSetMember")
    private long idCrossmapSetMember;

    @XmlElement(name = "NombreCortoCrossmapSet")
    private String shortNameCrossmapSet;

    @XmlElement(name = "VersionCrossmapSet")
    private int versionCrossmapSet;

    /** Terminología a la que pertenece */
    /*
    @XmlElement(name = "crossmapSet")
    private CrossmapSetResponse crossmapSet;
    */

    @XmlElement(name = "cod1CrossmapSetMember")
    private String code1;

    @XmlElement(name = "descripcionCrossmapSetMember")
    private String gloss;


    public DiseaseResponse() {
    }

    public DiseaseResponse(CrossmapSetMember crossmapSetMember) {
        this();

        if(crossmapSetMember instanceof Disease) {

            setIdCrossmapSetMember(crossmapSetMember.getId());
            //this.crossmapSet = new CrossmapSetResponse(crossmapSetMember.getCrossmapSet());
            this.code1 = ((Disease) crossmapSetMember).getCode()!=null?((Disease) crossmapSetMember).getCode():EMPTY_STRING;
            this.gloss = ((Disease) crossmapSetMember).getGloss();

            this.shortNameCrossmapSet = ((Disease) crossmapSetMember).getCrossmapSet().getAbbreviatedName();
            this.versionCrossmapSet = ((Disease) crossmapSetMember).getCrossmapSet().getVersion();
        }
    }

    public long getIdCrossmapSetMember() {
        return idCrossmapSetMember;
    }

    public void setIdCrossmapSetMember(long idCrossmapSetMember) {
        this.idCrossmapSetMember = idCrossmapSetMember;
    }

    public int getVersionCrossmapSet() {
        return versionCrossmapSet;
    }

    public void setVersionCrossmapSet(int versionCrossmapSet) {
        this.versionCrossmapSet = versionCrossmapSet;
    }

    /*
    public CrossmapSetResponse getCrossmapSet() {
        return crossmapSet;
    }

    public void setCrossmapSet(CrossmapSetResponse crossmapSet) {
        this.crossmapSet = crossmapSet;
    }
    */

    public String getCode1() {
        return code1;
    }

    public void setCode(String code1) {
        this.code1 = code1;
    }

    public String getGloss() {
        return gloss;
    }

    public void setGloss(String gloss) {
        this.gloss = gloss;
    }

    public String getShortNameCrossmapSet() {
        return shortNameCrossmapSet;
    }

    public void setShortNameCrossmapSet(String shortNameCrossmapSet) {
        this.shortNameCrossmapSet = shortNameCrossmapSet;
    }

    public void setCode1(String code1) {
        this.code1 = code1;
    }

}
