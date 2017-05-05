package cl.minsal.semantikos.ws.response;

import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;

import javax.xml.bind.annotation.*;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;

/**
 * @author Andrés Farías on 12/15/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "crossmapSetMember", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "CrossmapSetMember", namespace = "http://service.ws.semantikos.minsal.cl/")
public class CrossmapSetMemberResponse {

    @XmlElement(name = "NombreCortoCrossmapSet")
    private String shortNameCrossmapSet;


    /** ID de negocio */
    @XmlElement(name = "idCrossmapSetMember")
    private long idCrossmapSetMember;

    /** Terminología a la que pertenece */
    /*
    @XmlElement(name = "crossmapSet")
    private CrossmapSetResponse crossmapSet;
    */

    @XmlElement(name = "code1")
    private String code1;

    @XmlElement(name = "gloss")
    private String gloss;

    public CrossmapSetMemberResponse() {
    }

    public CrossmapSetMemberResponse(CrossmapSetMember crossmapSetMember) {
        this();

        this.idCrossmapSetMember = crossmapSetMember.getIdCrossmapSetMember();
        //this.crossmapSet = new CrossmapSetResponse(crossmapSetMember.getCrossmapSet());
        this.code1 = crossmapSetMember.getCode()!=null?crossmapSetMember.getCode():EMPTY_STRING;
        this.gloss = crossmapSetMember.getGloss();

        this.shortNameCrossmapSet = crossmapSetMember.getCrossmapSet().getAbbreviatedName();
    }

    public long getIdCrossmapSetMember() {
        return idCrossmapSetMember;
    }

    public void setIdCrossmapSetMember(long idCrossmapSetMember) {
        this.idCrossmapSetMember = idCrossmapSetMember;
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
