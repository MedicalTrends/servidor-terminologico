package cl.minsal.semantikos.model.crossmaps;

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
public class GenericDeviceGroup extends CrossmapSetRecord implements Serializable, ICrossmapSetRecord {

    /** Terminología a la que pertenece */
    private CrossmapSet crossmapSet;

    private long code;

    private String termName;

    private String termDefinition;

    private String termStatus;

    private Timestamp createdDate;

    private Timestamp modifiedDate;

    private Timestamp obsoletedDate;

    /**
     * Esta lista representa el mapping GMDN-SnomedCT
     * TODO: Si es necesario, redefinir cuando Minsal envie los datafiles
     */
    private List<ConceptSCT> conceptSCTs = new ArrayList<>();

    public GenericDeviceGroup(CrossmapSet crossmapSet, long id, long code, String termName, String termDefinition, String termStatus, Timestamp createdDate, Timestamp modifiedDate, Timestamp obsoletedDate) {
        super(id);
        this.crossmapSet = crossmapSet;
        this.code = code;
        this.termName = termName;
        this.termDefinition = termDefinition;
        this.termStatus = termStatus;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.obsoletedDate = obsoletedDate;
        this.conceptSCTs = conceptSCTs;
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
    public TargetType getTargetType() {
        return TargetType.CrossMap;
    }

    @Override
    public String getRepresentation() {
        return this.code + this.termName;
    }

    @Override
    public Target copy() {
        return new GenericDeviceGroup(this.crossmapSet, this.getId(), this.getCode(), this.getTermName(), this.getTermDefinition(), this.getTermStatus(), this.getCreatedDate(), this.getModifiedDate(), this.getObsoletedDate());
    }
}
