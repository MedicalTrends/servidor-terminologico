package cl.minsal.semantikos.modelws.response;

import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;
import cl.minsal.semantikos.model.crossmaps.GenericDeviceGroup;

import javax.xml.bind.annotation.*;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;

/**
 * @author Andrés Farías on 12/15/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "crossmapSetMember", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "CrossmapSetMember", namespace = "http://service.ws.semantikos.minsal.cl/")
public class CrossmapSetMemberResponse extends CrossmapSetRecordResponse {

    @XmlElement(name = "NombreCortoCrossmapSet")
    private String shortNameCrossmapSet;

    @XmlElement(name = "VersionCrossmapSet")
    private int versionCrossmapSet;

    /** ID de negocio */
    @XmlElement(name = "idCrossmapSetMember")
    private long idCrossmapSetMember;

    /** Terminología a la que pertenece */
    /*
    @XmlElement(name = "crossmapSet")
    private CrossmapSetResponse crossmapSet;
    */

    @XmlElement(name = "cod1CrossmapSetMember")
    private String code1;

    @XmlElement(name = "descripcionCrossmapSetMember")
    private String gloss;


    public CrossmapSetMemberResponse() {
    }

    public CrossmapSetMemberResponse(CrossmapSetMember crossmapSetMember) {
        this();

        if(crossmapSetMember instanceof CrossmapSetMember) {

            this.idCrossmapSetMember = ((CrossmapSetMember) crossmapSetMember).getIdCrossmapSetMember();
            //this.crossmapSet = new CrossmapSetResponse(crossmapSetMember.getCrossmapSet());
            this.code1 = ((CrossmapSetMember) crossmapSetMember).getCode()!=null?((CrossmapSetMember) crossmapSetMember).getCode():EMPTY_STRING;
            this.gloss = ((CrossmapSetMember) crossmapSetMember).getGloss();

            this.shortNameCrossmapSet = ((CrossmapSetMember) crossmapSetMember).getCrossmapSet().getAbbreviatedName();
            this.versionCrossmapSet = ((CrossmapSetMember) crossmapSetMember).getCrossmapSet().getVersion();
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
