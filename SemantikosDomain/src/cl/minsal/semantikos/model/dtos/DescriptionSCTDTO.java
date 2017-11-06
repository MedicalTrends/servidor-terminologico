package cl.minsal.semantikos.model.dtos;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.snapshots.AuditActionType;
import cl.minsal.semantikos.model.snomedct.DescriptionSCTType;
import cl.minsal.semantikos.model.snomedct.SnomedCTComponent;

import java.io.Serializable;
import java.sql.Timestamp;


public class DescriptionSCTDTO implements Serializable {


    private long id;

    private long typeId;

    /**
     * Definition: Specifies the inclusive date at which the component version's state became the then current valid
     * state of the component
     */

    private Timestamp effectiveTime;

    /**
     * <p></p>Si la descripción Snomed CT está vigente
     *
     * <p>Specifies whether the description's state was active or inactive from the nominal release date specified by
     * the
     * effectiveTime</p>
     */
    private boolean active;

    /**
     * <p>Identifies the description version's module. Set to a descendant of |Module| within the metadata
     * hierarchy.</p>
     */
    private long moduleId;


    /**
     * Identifies the concept to which this description belongs. Set to an Identifier of a concept in the 138875005 |
     * SNOMED CT Concept | hierarchy
     * within the Concept file. Note that versions of descriptions and concepts don't belong to each other. Which
     * version of any given description
     * is combined with which version of its owning concept depends on the point in time at which they are accessed.
     */
    private long conceptId;

    /**
     * Specifies the language of the description text using the two character ISO -639-1 code. Note that this specifies
     * a language level only,
     * not a dialect or country code.
     */
    private String languageCode;

    /**
     * The description version's text value, represented in UTF-8 encoding.
     */
    private long caseSignificanceId;

    private long acceptabilityId;

    public DescriptionSCTDTO() {
    }

    /**
     * Identifies the concept enumeration value that represents the case significance of this description version.
     * For example, the term may be completely case sensitive, case insensitive or initial letter case insensitive.
     * This field will be set to a child of 900000000000447004 | Case significance | within the metadata hierarchy.
     */
    private String term;

    private boolean favourite;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public Timestamp getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Timestamp effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getModuleId() {
        return moduleId;
    }

    public void setModuleId(long moduleId) {
        this.moduleId = moduleId;
    }

    public long getConceptId() {
        return conceptId;
    }

    public void setConceptId(long conceptId) {
        this.conceptId = conceptId;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public long getCaseSignificanceId() {
        return caseSignificanceId;
    }

    public void setCaseSignificanceId(long caseSignificanceId) {
        this.caseSignificanceId = caseSignificanceId;
    }

    public long getAcceptabilityId() {
        return acceptabilityId;
    }

    public void setAcceptabilityId(long acceptabilityId) {
        this.acceptabilityId = acceptabilityId;
    }

}
