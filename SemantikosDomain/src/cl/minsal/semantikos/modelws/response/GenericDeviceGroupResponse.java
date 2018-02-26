package cl.minsal.semantikos.modelws.response;

import cl.minsal.semantikos.model.crossmaps.*;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetType;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;

/**
 * Created by des01c7 on 20-11-17.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "genericDeviceGroupResponse", namespace = "http://service.ws.semantikos.minsal.cl/")
@XmlType(name = "GenericDeviceGroupResponse", namespace = "http://service.ws.semantikos.minsal.cl/")
public class GenericDeviceGroupResponse extends CrossmapSetRecordResponse implements Serializable {

    /** Terminología a la que pertenece */
    private CrossmapSet crossmapSet;

    private long code;

    private String termName;

    private String termDefinition;

    private String termStatus;

    private Timestamp createdDate;

    public CrossmapSet getCrossmapSet() {
        return crossmapSet;
    }

    public void setCrossmapSet(CrossmapSet crossmapSet) {
        this.crossmapSet = crossmapSet;
    }

    private Timestamp modifiedDate;

    private Timestamp obsoletedDate;

    public GenericDeviceGroupResponse() {
    }

    private List<ConceptSCT> conceptSCTs = new ArrayList<>();

    public GenericDeviceGroupResponse(CrossmapSetRecord crossmapSetMember) {
        if(crossmapSetMember instanceof GenericDeviceGroup) {

            this.code = (((GenericDeviceGroup) crossmapSetMember).getCode());
            //this.crossmapSet = new CrossmapSetResponse(crossmapSetMember.getCrossmapSet());
            this.termName = ((GenericDeviceGroup) crossmapSetMember).getTermName();
            this.termDefinition = ((GenericDeviceGroup) crossmapSetMember).getTermDefinition();

            this.createdDate = ((GenericDeviceGroup) crossmapSetMember).getCreatedDate();

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

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
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

}
