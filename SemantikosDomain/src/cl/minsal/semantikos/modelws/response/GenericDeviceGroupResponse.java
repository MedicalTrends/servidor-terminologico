package cl.minsal.semantikos.modelws.response;

import cl.minsal.semantikos.model.crossmaps.*;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by des01c7 on 20-11-17.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "genericDeviceGroup", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "GenericDeviceGroup", namespace = "http://service.ws.semantikos.minsal.cl/")
public class GenericDeviceGroupResponse extends CrossmapSetRecordResponse implements Serializable {

    @XmlElement(name = "NombreCortoCrossmapSet")
    private String shortNameCrossmapSet;

    @XmlElement(name = "VersionCrossmapSet")
    private int versionCrossmapSet;

    @XmlElement(name = "code")
    private long code;

    @XmlElement(name = "termName")
    private String termName;

    @XmlElement(name = "termDefinition")
    private String termDefinition;

    @XmlElement(name = "termStatus")
    private String termStatus;

    @XmlElement(name = "createdDate")
    private String createdDate;

    private Timestamp modifiedDate;

    private Timestamp obsoletedDate;

    public GenericDeviceGroupResponse() {
    }

    private List<ConceptSCT> conceptSCTs = new ArrayList<>();

    public GenericDeviceGroupResponse(CrossmapSetMember crossmapSetMember) {
        if(crossmapSetMember instanceof GenericDeviceGroup) {

            this.code = (((GenericDeviceGroup) crossmapSetMember).getCode());
            //this.crossmapSet = new CrossmapSetResponse(crossmapSetMember.getCrossmapSet());
            this.termName = ((GenericDeviceGroup) crossmapSetMember).getTermName();
            this.termDefinition = ((GenericDeviceGroup) crossmapSetMember).getTermDefinition();

            this.createdDate = new SimpleDateFormat("dd/MM/yyyy").format(((GenericDeviceGroup) crossmapSetMember).getCreatedDate());

            this.shortNameCrossmapSet = ((GenericDeviceGroup) crossmapSetMember).getCrossmapSet().getAbbreviatedName();
            this.versionCrossmapSet = ((GenericDeviceGroup) crossmapSetMember).getCrossmapSet().getVersion();

        }
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public String getTermDefinition() {
        return termDefinition;
    }

    public void setTermDefinition(String termDefinition) {
        this.termDefinition = termDefinition;
    }

    public String getTermStatus() {
        return termStatus;
    }

    public void setTermStatus(String termStatus) {
        this.termStatus = termStatus;
    }

    public List<ConceptSCT> getConceptSCTs() {
        return conceptSCTs;
    }

    public void setConceptSCTs(List<ConceptSCT> conceptSCTs) {
        this.conceptSCTs = conceptSCTs;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Timestamp getObsoletedDate() {
        return obsoletedDate;
    }

    public void setObsoletedDate(Timestamp obsoletedDate) {
        this.obsoletedDate = obsoletedDate;
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode() + new Long(code).hashCode();
    }

    @Override
    public String toString() {
        return getTermName();
    }

    public String getShortNameCrossmapSet() {
        return shortNameCrossmapSet;
    }

    public void setShortNameCrossmapSet(String shortNameCrossmapSet) {
        this.shortNameCrossmapSet = shortNameCrossmapSet;
    }

    public int getVersionCrossmapSet() {
        return versionCrossmapSet;
    }

    public void setVersionCrossmapSet(int versionCrossmapSet) {
        this.versionCrossmapSet = versionCrossmapSet;
    }

}
