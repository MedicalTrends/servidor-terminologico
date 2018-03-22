package cl.minsal.semantikos.model.dtos;

import cl.minsal.semantikos.model.crossmaps.CrossmapSet;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;
import cl.minsal.semantikos.model.crossmaps.gmdn.CollectiveTerm;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetType;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by des01c7 on 20-11-17.
 */
public class GenericDeviceGroupDTO extends CrossmapSetMemberDTO implements Serializable {

    private long code;

    private String termName;

    private String termDefinition;

    private String termStatus;

    private Timestamp createdDate;

    private Timestamp modifiedDate;

    private Timestamp obsoletedDate;

    //private List<CollectiveTerm> collectiveTerms;

    public GenericDeviceGroupDTO() {
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