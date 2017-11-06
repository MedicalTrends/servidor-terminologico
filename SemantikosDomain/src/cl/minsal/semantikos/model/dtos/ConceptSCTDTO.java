package cl.minsal.semantikos.model.dtos;

import cl.minsal.semantikos.model.PersistentEntity;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.relationships.TargetType;
import cl.minsal.semantikos.model.snapshots.AuditActionType;
import cl.minsal.semantikos.model.snomedct.DescriptionSCT;
import cl.minsal.semantikos.model.snomedct.RelationshipSCT;
import cl.minsal.semantikos.model.snomedct.SnomedCTComponent;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static cl.minsal.semantikos.model.snomedct.DescriptionSCTType.FSN;
import static cl.minsal.semantikos.model.snomedct.DescriptionSCTType.SYNONYM;
import static java.util.Collections.emptyList;


/**
 * Esta clase representa un concepto Snomed-CT.
 *
 * @author Andres Farias
 * @version 1.0
 * @created 17-ago-2016 12:52:05
 */
public class ConceptSCTDTO implements TargetDTO, Serializable {

    /** Identificador único (oficial) de Snomed CT para este concepto. */
    private long id;

    /** Descripciones del Concepto */
    private List<DescriptionSCT> descriptions;

    private List<RelationshipSCT> relationships;

    /**
     * Definition: Specifies the inclusive date at which the component version's state became the then current valid
     * state of the component
     */
    private Timestamp effectiveTime;

    /**
     * <p></p>Si el concepto Snomed CT está vigente
     *
     * <p>Specifies whether the concept 's state was active or inactive from the nominal release date specified by the
     * effectiveTime</p>
     */
    private boolean isActive;

    /** <p>Identifies the concept version's module. Set to a descendant of |Module| within the metadata hierarchy.</p> */
    private long moduleId;

    /**
     * <p>Specifies if the concept version is primitive or fully defined. Set to a child of | Definition status | in
     * the metadata hierarchy.</p>
     */
    private long definitionStatusId;

    public ConceptSCTDTO() {
    }

    public void setId(long idSnomedCT) {
        this.id = idSnomedCT;
    }

    public Timestamp getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Timestamp effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public long getModuleId() {
        return moduleId;
    }

    public void setModuleId(long moduleId) {
        this.moduleId = moduleId;
    }

    public long getId() {
        return id;
    }

    public long getDefinitionStatusId() {
        return definitionStatusId;
    }

    public void setDefinitionStatusId(long definitionStatusId) {
        this.definitionStatusId = definitionStatusId;
    }

    public void setDescriptions(List<DescriptionSCT> descriptions) {
        this.descriptions = descriptions;
    }

    public List<DescriptionSCT> getDescriptions() {
        return descriptions;
    }

}