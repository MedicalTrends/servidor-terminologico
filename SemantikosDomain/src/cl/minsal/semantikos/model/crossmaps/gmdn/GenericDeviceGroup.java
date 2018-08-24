package cl.minsal.semantikos.model.crossmaps.gmdn;

import cl.minsal.semantikos.model.crossmaps.CrossmapSet;
import cl.minsal.semantikos.model.crossmaps.CrossmapSetMember;
import cl.minsal.semantikos.model.crossmaps.cie10.Disease;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetType;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by des01c7 on 20-11-17.
 */
public class GenericDeviceGroup extends CrossmapSetMember implements Serializable {


    private long code;

    private String termName;

    private String termDefinition;

    private String termStatus;

    private Timestamp createdDate;

    private Timestamp modifiedDate;

    private Timestamp obsoletedDate;

    private List<CollectiveTerm> collectiveTerms;

    public GenericDeviceGroup(CrossmapSet crossmapSet, long id, long code, String termName, String termDefinition, String termStatus, Timestamp createdDate, Timestamp modifiedDate, Timestamp obsoletedDate) {
        super(id, crossmapSet);
        this.code = code;
        this.termName = termName;
        this.termDefinition = termDefinition;
        this.termStatus = termStatus;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.obsoletedDate = obsoletedDate;
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

    public List<CollectiveTerm> getCollectiveTerms() {
        return collectiveTerms;
    }

    public void setCollectiveTerms(List<CollectiveTerm> collectiveTerms) {
        this.collectiveTerms = collectiveTerms;
    }

    @Override
    public TargetType getTargetType() {
        return TargetType.CrossMap;
    }

    @Override
    public String getRepresentation() {
        return this.code + this.termName;
    }

    @Override
    public Target copy() {
        return new GenericDeviceGroup(getCrossmapSet(), this.getId(), this.getCode(), this.getTermName(), this.getTermDefinition(), this.getTermStatus(), this.getCreatedDate(), this.getModifiedDate(), this.getObsoletedDate());
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode() + new Long(code).hashCode();
    }

    @Override
    public String toString() {
        return getTermName();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof GenericDeviceGroup
                ? String.valueOf(getId()).equals(String.valueOf(((GenericDeviceGroup) other).getId()))
                : other == this;
    }
}