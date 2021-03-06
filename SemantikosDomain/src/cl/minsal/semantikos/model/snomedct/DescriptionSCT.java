package cl.minsal.semantikos.model.snomedct;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.snapshots.AuditActionType;

import java.io.Serializable;
import java.sql.Timestamp;

import static com.sun.org.apache.xml.internal.utils.LocaleUtility.EMPTY_STRING;

/**
 * Esta clase representa una descripcion Snomed-CT.
 *
 * @author Andres Farias, Diego Soto
 * @version 1.0
 * @created 28-09-2016
 */
public class DescriptionSCT extends PersistentEntity implements SnomedCTComponent, Serializable {

    private DescriptionSCTType descriptionType;
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

    /**
     * Identifies the concept enumeration value that represents the case significance of this description version.
     * For example, the term may be completely case sensitive, case insensitive or initial letter case insensitive.
     * This field will be set to a child of 900000000000447004 | Case significance | within the metadata hierarchy.
     */
    private String term;

    private boolean favourite;

    public static final long CASE_SENSITIVE = 900000000000017005l;

    public static final long CASE_INSENSITIVE = 900000000000448009l;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DescriptionSCT that = (DescriptionSCT) o;

        /* Si ambas están persistidas y no tienen el mismo ID, entonces son distintas */
        if (this.isPersistent() && that.isPersistent() && this.getId() != that.getId()) return false;

        /* Si alguna de ellas no está persistida, comparamos 1. tipo de descripción, 2. término */
        if (!this.getDescriptionType().equals(that.getDescriptionType())) return false;

        return this.getTerm().equals(that.getTerm()) && this.getConceptId() == that.getConceptId();

    }

    @Override
    public int hashCode() {
        int result = descriptionType.hashCode();
        result = 31 * result + effectiveTime.hashCode();
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + (int) (moduleId ^ (moduleId >>> 32));
        result = 31 * result + (int) (conceptId ^ (conceptId >>> 32));
        result = 31 * result + languageCode.hashCode();
        result = 31 * result + (int) (caseSignificanceId ^ (caseSignificanceId >>> 32));
        result = 31 * result + term.hashCode();
        result = 31 * result + (favourite ? 1 : 0);
        return result;
    }

    public static final DescriptionSCT DUMMY_DESCRIPTION_SCT = new DescriptionSCT(-1L, DescriptionSCTType.ACCEPTABLE, null, false, 0L, 0L, EMPTY_STRING, EMPTY_STRING, 0L);

    /**
     * Este es el constructor completo para la clase descriptionSCT
     */
    public DescriptionSCT(long id, DescriptionSCTType type, Timestamp effectiveTime, boolean active, long moduleId, long conceptId, String languageCode, String term, long caseSignificanceId) {
        super(id);
        this.descriptionType = type;
        this.effectiveTime = effectiveTime;
        this.active = active;
        this.moduleId = moduleId;
        this.conceptId = conceptId;
        this.languageCode = languageCode;
        this.term = term;
        this.caseSignificanceId = caseSignificanceId;
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

    public DescriptionSCTType getDescriptionType() {
        return descriptionType;
    }

    @Override
    public String toString() {
        return term;
    }

    public String getRepresentation() {

        return term;
    }

    @Override
    public AuditActionType evaluateChange(SnomedCTComponent snomedCTComponent) {

        DescriptionSCT that = (DescriptionSCT) snomedCTComponent;

        if(this.equals(that))
            return AuditActionType.SNOMED_CT_UNMODIFYING;

        if(this.isActive() && !that.isActive())
            return AuditActionType.SNOMED_CT_INVALIDATION;

        if(!this.isActive() && that.isActive())
            return AuditActionType.SNOMED_CT_RESTORYING;

        return AuditActionType.SNOMED_CT_UNDEFINED;
    }
}
