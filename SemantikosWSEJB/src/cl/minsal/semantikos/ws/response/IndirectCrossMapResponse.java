package cl.minsal.semantikos.ws.response;

import cl.minsal.semantikos.model.crossmaps.CrossmapSet;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;
import cl.minsal.semantikos.model.crossmaps.IndirectCrossmap;

import javax.xml.bind.annotation.*;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;

/**
 * @author Andrés Farías on 12/15/16.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "indirectCrossmap", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "IndirectCrossmap", namespace = "http://service.ws.semantikos.minsal.cl/")
public class IndirectCrossMapResponse {

    @XmlElement(name = "idSnomedCT")
    private long idSnomedCT;

    @XmlElement(name = "mapGroup")
    private int mapGroup;

    @XmlElement(name = "mapPriority")
    private int mapPriority;

    @XmlElement(name = "mapRule")
    private String mapRule;

    @XmlElement(name = "mapAdvice")
    private String mapAdvice;

    @XmlElement(name = "mapTarget")
    private String mapTarget;

    @XmlElement(name = "correlation")
    private long correlation;

    @XmlElement(name = "idCrossmapCategory")
    private long idCrossmapCategory;

    @XmlElement(name = "cod1CrossmapSetMembers")
    private String cod1CrossmapSetMembers;

    @XmlElement(name = "description")
    private String description;

    @XmlElement(name = "state")
    private boolean state;

    public IndirectCrossMapResponse() {
    }

    public IndirectCrossMapResponse(IndirectCrossmap indirectCrossmap) {
        this();

        this.mapGroup = indirectCrossmap.getMapGroup();
        this.mapPriority = indirectCrossmap.getMapPriority();
        this.mapRule = indirectCrossmap.getMapRule();
        this.idSnomedCT = indirectCrossmap.getIdSnomedCT();
        this.mapAdvice = indirectCrossmap.getMapAdvice();
        this.mapTarget = indirectCrossmap.getMapTarget();
        this.correlation = indirectCrossmap.getCorrelation();
        this.idCrossmapCategory = indirectCrossmap.getIdCrossmapCategory();
        this.state = indirectCrossmap.isState();

        CrossmapSetMember crossmapSetMember = (CrossmapSetMember)indirectCrossmap.getTarget();

        this.cod1CrossmapSetMembers = crossmapSetMember.getCode()!=null?crossmapSetMember.getCode():EMPTY_STRING;
        this.description = crossmapSetMember.getGloss();
    }

    public long getIdSnomedCT() {
        return idSnomedCT;
    }

    public void setIdSnomedCT(long idSnomedCT) {
        this.idSnomedCT = idSnomedCT;
    }


    public String getCod1CrossmapSetMembers() {
        return cod1CrossmapSetMembers;
    }

    public void setCod1CrossmapSetMembers(String cod1CrossmapSetMembers) {
        this.cod1CrossmapSetMembers = cod1CrossmapSetMembers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMapGroup() {
        return mapGroup;
    }

    public void setMapGroup(int mapGroup) {
        this.mapGroup = mapGroup;
    }

    public int getMapPriority() {
        return mapPriority;
    }

    public void setMapPriority(int mapPriority) {
        this.mapPriority = mapPriority;
    }

    public String getMapRule() {
        return mapRule;
    }

    public void setMapRule(String mapRule) {
        this.mapRule = mapRule;
    }

    public String getMapAdvice() {
        return mapAdvice;
    }

    public void setMapAdvice(String mapAdvice) {
        this.mapAdvice = mapAdvice;
    }

    public String getMapTarget() {
        return mapTarget;
    }

    public void setMapTarget(String mapTarget) {
        this.mapTarget = mapTarget;
    }

    public long getCorrelation() {
        return correlation;
    }

    public void setCorrelation(long correlation) {
        this.correlation = correlation;
    }

    public long getIdCrossmapCategory() {
        return idCrossmapCategory;
    }

    public void setIdCrossmapCategory(long idCrossmapCategory) {
        this.idCrossmapCategory = idCrossmapCategory;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
