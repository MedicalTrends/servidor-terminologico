package cl.minsal.semantikos.ws.response;

import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;

import javax.xml.bind.annotation.*;

/**
 * @author Andrés Farías on 12/15/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "indirectCrossmap", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "IndirectCrossmap", namespace = "http://service.ws.semantikos.minsal.cl/")
public class CrossmapSetMemberResponse {

    /** ID de negocio */
    @XmlElement(name = "idCrossmapSetMember")
    private long idCrossmapSetMember;

    /** Terminología a la que pertenece */
    @XmlElement(name = "crossmapSet")
    private CrossmapSetResponse crossmapSet;

    @XmlElement(name = "code")
    private String code;

    @XmlElement(name = "gloss")
    private String gloss;

    public CrossmapSetMemberResponse() {
    }

    public CrossmapSetMemberResponse(CrossmapSetMember crossmapSetMember) {
        this();

        this.idCrossmapSetMember = crossmapSetMember.getIdCrossmapSetMember();
        this.crossmapSet = new CrossmapSetResponse(crossmapSetMember.getCrossmapSet());
        this.code = crossmapSetMember.getCode1();
        this.gloss = crossmapSetMember.getGloss();
    }

    public long getIdCrossmapSetMember() {
        return idCrossmapSetMember;
    }

    public void setIdCrossmapSetMember(long idCrossmapSetMember) {
        this.idCrossmapSetMember = idCrossmapSetMember;
    }

    public CrossmapSetResponse getCrossmapSet() {
        return crossmapSet;
    }

    public void setCrossmapSet(CrossmapSetResponse crossmapSet) {
        this.crossmapSet = crossmapSet;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGloss() {
        return gloss;
    }

    public void setGloss(String gloss) {
        this.gloss = gloss;
    }
}
