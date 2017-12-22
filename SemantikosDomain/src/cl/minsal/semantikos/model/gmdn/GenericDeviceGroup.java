package cl.minsal.semantikos.model.gmdn;

import cl.minsal.semantikos.model.snomedct.ConceptSCT;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by des01c7 on 20-11-17.
 */
public class GenericDeviceGroup implements Serializable {

    private long code;

    private String termName;

    private String termDefinition;

    private String termStatus;

    private char termTypeIdentifier;

    private String productSpecifier;

    private List<DeviceCategory> deviceCategories = new ArrayList<>();

    private List<CollectiveTerm> collectiveTerms = new ArrayList<>();

    /**
     * Esta lista representa el mapping GMDN-SnomedCT
     * TODO: Si es necesario, redefinir cuando Minsal envie los datafiles
     */
    private List<ConceptSCT> conceptSCTs = new ArrayList<>();


    public GenericDeviceGroup(long code, String termName, String termDefinition, String termStatus, char termTypeIdentifier, String productSpecifier) {
        this.code = code;
        this.termName = termName;
        this.termDefinition = termDefinition;
        this.termStatus = termStatus;
        this.termTypeIdentifier = termTypeIdentifier;
        this.productSpecifier = productSpecifier;
    }

    public GenericDeviceGroup(long code, String termName, String termDefinition, String termStatus, char termTypeIdentifier, String productSpecifier, List<DeviceCategory> deviceCategories, List<CollectiveTerm> collectiveTerms) {
        this.code = code;
        this.termName = termName;
        this.termDefinition = termDefinition;
        this.termStatus = termStatus;
        this.termTypeIdentifier = termTypeIdentifier;
        this.productSpecifier = productSpecifier;
        this.deviceCategories = deviceCategories;
        this.collectiveTerms = collectiveTerms;
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

    public char getTermTypeIdentifier() {
        return termTypeIdentifier;
    }

    public void setTermTypeIdentifier(char termTypeIdentifier) {
        this.termTypeIdentifier = termTypeIdentifier;
    }

    public String getProductSpecifier() {
        return productSpecifier;
    }

    public void setProductSpecifier(String productSpecifier) {
        this.productSpecifier = productSpecifier;
    }

    public List<DeviceCategory> getDeviceCategories() {
        return deviceCategories;
    }

    public void setDeviceCategories(List<DeviceCategory> deviceCategories) {
        this.deviceCategories = deviceCategories;
    }

    public List<CollectiveTerm> getCollectiveTerms() {
        return collectiveTerms;
    }

    public void setCollectiveTerms(List<CollectiveTerm> collectiveTerms) {
        this.collectiveTerms = collectiveTerms;
    }

    public List<ConceptSCT> getConceptSCTs() {
        return conceptSCTs;
    }

    public void setConceptSCTs(List<ConceptSCT> conceptSCTs) {
        this.conceptSCTs = conceptSCTs;
    }
}