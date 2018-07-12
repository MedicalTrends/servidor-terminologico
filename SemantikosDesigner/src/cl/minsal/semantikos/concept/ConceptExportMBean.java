package cl.minsal.semantikos.concept;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.AuditManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.crossmaps.DirectCrossmap;
import cl.minsal.semantikos.model.crossmaps.IndirectCrossmap;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.audit.ConceptAuditAction;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.SnomedCTRelationship;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.modelweb.ConceptSMTKWeb;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UINamingContainer;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static cl.minsal.semantikos.model.audit.AuditActionType.CONCEPT_CREATION;
import static cl.minsal.semantikos.model.audit.AuditActionType.CONCEPT_PUBLICATION;
import static cl.minsal.semantikos.model.audit.AuditActionType.REFSET_BINDING;
import static java.util.Collections.EMPTY_LIST;

/**
 * @author Francisco Mendez
 */
@ManagedBean(name = "conceptExport")
@ViewScoped
public class ConceptExportMBean extends UINamingContainer {

    private String init;

    public String getInit() {
        return init;
    }

    private List<ConceptBasic> conceptBasics;

    private ConceptSMTK conceptSMTK;

    //80614

    //@EJB
    AuditManager auditManager = (AuditManager) ServiceLocator.getInstance().getService(AuditManager.class);

    private List<Relationship> crossMapsRelationships;

    private List<RefSet> refSets;

    private List<ConceptAuditAction> auditAction;

    public List<ConceptAuditAction> getAuditAction() {
        return auditAction;
    }

    public void setAuditAction(List<ConceptAuditAction> auditAction) {
        this.auditAction = auditAction;
    }

    @PostConstruct
    protected void initialize() {
    }

    public void loadConcept() {

            conceptBasics = new ArrayList<ConceptBasic>();

            conceptBasics.add(new ConceptBasic("Fecha Informe", getReportDate()));
            conceptBasics.add(new ConceptBasic("ConceptID", conceptSMTK.getConceptID()));
            conceptBasics.add(new ConceptBasic("Categoría", conceptSMTK.getCategory().toString()));
            conceptBasics.add(new ConceptBasic("Estado", conceptSMTK.isModeled() ? "Modelado" : "Borrador"));
            conceptBasics.add(new ConceptBasic("Fecha Creación", getCreationDate(auditAction)));
            conceptBasics.add(new ConceptBasic("Fecha Publicación", getPublicationDate(auditAction)));
            conceptBasics.add(new ConceptBasic("FSN", conceptSMTK.getDescriptionFSN().toString()));
            conceptBasics.add(new ConceptBasic("Preferida", conceptSMTK.getDescriptionFavorite().toString()));
            conceptBasics.add(new ConceptBasic("Tipo Creación", conceptSMTK.isFullyDefined() ? "Completamente Definido" : "Primitivo"));
            conceptBasics.add(new ConceptBasic("Observación", conceptSMTK.getObservation()));

            crossMapsRelationships = new ArrayList<Relationship>();

    }

    public String getPublicationDate(List<ConceptAuditAction> conceptAuditActions) {
        for (ConceptAuditAction conceptAuditAction : conceptAuditActions) {
            if( conceptAuditAction.getAuditActionType().equals(CONCEPT_PUBLICATION)) {
                return conceptAuditAction.getActionDateFormat();
            }
        }
        return "";
    }
    public String getCreationDate(List<ConceptAuditAction> conceptAuditActions) {
        for (ConceptAuditAction conceptAuditAction : conceptAuditActions) {
            if( conceptAuditAction.getAuditActionType().equals(CONCEPT_CREATION)) {
                return conceptAuditAction.getActionDateFormat();
            }
        }
        return "";
    }
    public String getRefsetBindingDate(List<ConceptAuditAction> conceptAuditActions) {
        for (ConceptAuditAction conceptAuditAction : conceptAuditActions) {
            if( conceptAuditAction.getAuditActionType().equals(REFSET_BINDING)) {
                return conceptAuditAction.getActionDateFormat();
            }
        }
        return "";
    }

    public String getReportDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Timestamp(System.currentTimeMillis()));
    }

    public List<ConceptBasic> getConceptBasics() {
        return conceptBasics;
    }

    public void setConceptBasics(List<ConceptBasic> conceptBasics) {
        this.conceptBasics = conceptBasics;
    }

    public ConceptSMTK getConceptSMTK() {
        return conceptSMTK;
    }

    public void setConceptSMTK(ConceptSMTK conceptSMTK) {

        this.conceptSMTK = conceptSMTK;
        auditAction = auditManager.getConceptAuditActions(conceptSMTK, true);
    }

    /**
     * Este método es responsable de actuar como una fábrica de objetos <code>SnomedRelationship</code>.
     *
     * @return Una lista de los DTO de relaciones Snomed CT.
     */
    public List<SnomedCTRelationshipDTO> getSnomedCTRelationships() {

        if(conceptSMTK == null) {
            return EMPTY_LIST;
        }

        List<SnomedCTRelationshipDTO> snomedCTRelationships = new ArrayList<SnomedCTRelationshipDTO>();

        for (SnomedCTRelationship relationship : conceptSMTK.getRelationshipsSnomedCT()) {
            snomedCTRelationships.add(new SnomedCTRelationshipDTO(relationship));
        }

        return snomedCTRelationships;
    }

    public List<Relationship> getSMTKRelationships() {

        if(conceptSMTK == null) {
            return EMPTY_LIST;
        }

        List<Relationship> smtkRelationships = new ArrayList<Relationship>();

        for (Relationship relationship : conceptSMTK.getRelationships()) {
            if(!relationship.getRelationshipDefinition().getTargetDefinition().isSnomedCTType() &&
                !relationship.getRelationshipDefinition().getTargetDefinition().isCrossMapType()) {
                smtkRelationships.add(relationship);
            }
        }

        return smtkRelationships;
    }

    public List<Relationship> getDirectCrossmapsRelationships() {

        if(conceptSMTK == null) {
            return EMPTY_LIST;
        }

        List<Relationship> smtkRelationships = new ArrayList<Relationship>();

        for (Relationship relationship : conceptSMTK.getRelationships()) {
            if(relationship instanceof DirectCrossmap) {
                smtkRelationships.add(relationship);
            }
        }

        return smtkRelationships;
    }

    public List<Relationship> getIndirectCrossmapsRelationships() {

        if(conceptSMTK == null) {
            return EMPTY_LIST;
        }

        List<Relationship> smtkRelationships = new ArrayList<Relationship>();

        for (Relationship relationship : conceptSMTK.getRelationships()) {
            if(relationship instanceof IndirectCrossmap) {
                smtkRelationships.add(relationship);
            }
        }

        return smtkRelationships;
    }

    public List<Relationship> getCrossMapsRelationships() {
        return crossMapsRelationships;
    }

    public List<RefSet> getRefSets() {
        return refSets;
    }
}

class SnomedCTRelationshipDTO {

    private SnomedCTRelationship relationship;

    public SnomedCTRelationshipDTO(SnomedCTRelationship relationship) {
        this.relationship = relationship;
    }

    public ConceptSCT getSnomedCT() {
        return relationship.getTarget();
    }

    public String getRelationType(){
        return relationship.getRelationshipDefinition().getName();
    }
}
