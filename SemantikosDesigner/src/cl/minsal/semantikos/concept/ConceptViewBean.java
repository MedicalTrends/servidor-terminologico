package cl.minsal.semantikos.concept;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.audit.ConceptAuditAction;
import cl.minsal.semantikos.model.basictypes.BasicTypeDefinition;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.crossmaps.DirectCrossmap;
import cl.minsal.semantikos.model.crossmaps.IndirectCrossmap;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.DescriptionTypeFactory;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.relationships.BasicTypeType;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;
import cl.minsal.semantikos.model.relationships.SnomedCTRelationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BluePrints Developer on 14-07-2016.
 */

@ManagedBean(name = "conceptView")
@SessionScoped
public class ConceptViewBean implements Serializable {

    static private final Logger logger = LoggerFactory.getLogger(ConceptBean.class);

    //@EJB
    ConceptManager conceptManager = (ConceptManager) ServiceLocator.getInstance().getService(ConceptManager.class);

    //@EJB
    RelationshipManager relationshipManager = (RelationshipManager) ServiceLocator.getInstance().getService(RelationshipManager.class);

    //@EJB
    RefSetManager refSetManager = (RefSetManager) ServiceLocator.getInstance().getService(RefSetManager.class);

    //@EJB
    CrossmapsManager crossmapsManager = (CrossmapsManager) ServiceLocator.getInstance().getService(CrossmapsManager.class);

    //@EJB
    AuditManager auditManager = (AuditManager) ServiceLocator.getInstance().getService(AuditManager.class);

    @ManagedProperty(value = "#{conceptExport}")
    private ConceptExportMBean conceptBeanExport;

    ConceptSMTK selectedConcept;

    private List<ConceptAuditAction> auditAction;

    String conceptID;

    private boolean showMore = false;

    List<IndirectCrossmap> indirectCrossmaps = new ArrayList<>();

    public List<ConceptAuditAction> getAuditAction() {
        return auditAction;
    }

    public void setAuditAction(List<ConceptAuditAction> auditAction) {
        this.auditAction = auditAction;
    }

    public String getConceptID() {
        return conceptID;
    }

    public void setConceptID(String conceptID) {
        try {
            showMore = false;
            Long.parseLong(conceptID);
            this.conceptID = conceptID;
            selectedConcept = conceptManager.getConceptByCONCEPT_ID(conceptID);
            selectedConcept.setRelationships(relationshipManager.getRelationshipsBySourceConcept(selectedConcept));
            auditAction = auditManager.getConceptAuditActions(selectedConcept);
            conceptBeanExport.setConceptSMTK(selectedConcept);
            conceptBeanExport.loadConcept();
        }
        catch (NumberFormatException e) {
            logger.warn(e.getMessage());
        }
    }

    //Inicializacion del Bean
    @PostConstruct
    protected void initialize() {
    }

    public RelationshipManager getRelationshipManager() {
        return relationshipManager;
    }

    public void setRelationshipManager(RelationshipManager relationshipManager) {
        this.relationshipManager = relationshipManager;
    }

    public ConceptSMTK getSelectedConcept() {
        return selectedConcept;
    }

    public void setSelectedConcept(ConceptSMTK selectedConcept) {

        this.selectedConcept = selectedConcept;

    }

    public ConceptManager getConceptManager() {
        return conceptManager;
    }

    public void setConceptManager(ConceptManager conceptManager) {
        this.conceptManager = conceptManager;
    }

    public List<Description> getOtherDescriptions() {

        if(selectedConcept == null) {
            return null;
        }

        List<Description> otherDescriptions = new ArrayList<Description>();

        for (Description description : selectedConcept.getDescriptions()) {
            if(DescriptionTypeFactory.getInstance().getDescriptionTypesButFSNandFavorite().contains(description.getDescriptionType())) {
                otherDescriptions.add(description);
            }
        }

        return otherDescriptions;
    }

    public List<Relationship> getSnomedCTRelationships() {

        if(selectedConcept == null) {
            return null;
        }

        if(!selectedConcept.isRelationshipsLoaded()) {
            selectedConcept.setRelationships(relationshipManager.getRelationshipsBySourceConcept(selectedConcept));
        }

        List<Relationship> snomedCTRelationships = new ArrayList<Relationship>();

        for (SnomedCTRelationship relationship : selectedConcept.getRelationshipsSnomedCT()) {
            snomedCTRelationships.add(relationship);
        }

        return snomedCTRelationships;
    }

    public List<Relationship> getSMTKRelationships() {

        if(selectedConcept == null) {
            return null;
        }

        if(!selectedConcept.isRelationshipsLoaded()) {
            selectedConcept.setRelationships(relationshipManager.getRelationshipsBySourceConcept(selectedConcept));
        }

        List<Relationship> smtkRelationships = new ArrayList<Relationship>();

        for (Relationship relationship : selectedConcept.getRelationships()) {
            if(!relationship.getRelationshipDefinition().getTargetDefinition().isSnomedCTType() &&
                    !relationship.getRelationshipDefinition().getTargetDefinition().isCrossMapType() //&&
                    /*!relationship.getRelationshipDefinition().getTargetDefinition().isGMDNType()*/ ) {
                smtkRelationships.add(relationship);
            }
        }

        return smtkRelationships;
    }

    public List<Relationship> getDirectCrossmapsRelationships() {

        if(selectedConcept == null) {
            return null;
        }

        if(!selectedConcept.isRelationshipsLoaded()) {
            selectedConcept.setRelationships(relationshipManager.getRelationshipsBySourceConcept(selectedConcept));
        }

        List<Relationship> smtkRelationships = new ArrayList<Relationship>();

        for (Relationship relationship : selectedConcept.getRelationships()) {
            if(relationship instanceof DirectCrossmap) {
                smtkRelationships.add(relationship);
            }
        }

        return smtkRelationships;
    }

    public List<Relationship> getIndirectCrossmapsRelationships() {

        if(selectedConcept == null) {
            return null;
        }

        if(!selectedConcept.isRelationshipsLoaded()) {
            selectedConcept.setRelationships(relationshipManager.getRelationshipsBySourceConcept(selectedConcept));
        }

        if(indirectCrossmaps.isEmpty()) {
            try {
                indirectCrossmaps = crossmapsManager.getIndirectCrossmaps(selectedConcept);
            } catch (Exception e) {
                e.printStackTrace();
            }
            selectedConcept.getRelationships().addAll(indirectCrossmaps);
        }

        List<Relationship> smtkRelationships = new ArrayList<Relationship>();

        for (Relationship relationship : selectedConcept.getRelationships()) {
            if(relationship instanceof IndirectCrossmap) {
                smtkRelationships.add(relationship);
            }
        }

        return smtkRelationships;
    }

    public List<RefSet> getConceptRefSetList() {
        if(selectedConcept == null) {
            return null;
        }
        return refSetManager.getRefsetsBy(selectedConcept);
    }

    public List<Relationship> getGMDNRelationships() {

        if(selectedConcept == null) {
            return null;
        }

        if(!selectedConcept.isRelationshipsLoaded()) {
            selectedConcept.setRelationships(relationshipManager.getRelationshipsBySourceConcept(selectedConcept));
        }

        List<Relationship> gmdnRelationships = new ArrayList<Relationship>();

        for (Relationship relationship : selectedConcept.getRelationships()) {
            /*
            if(relationship.getRelationshipDefinition().getTargetDefinition().isGMDNType()) {
                gmdnRelationships.add(relationship);
            }
            */
        }

        return gmdnRelationships;
    }

    public String getDateCreationFormat(Timestamp timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(timestamp);
    }

    public boolean isShowMore() {
        return showMore;
    }

    public void setShowMore(boolean showMore) {
        this.showMore = showMore;
    }

    public ConceptExportMBean getConceptBeanExport() {
        return conceptBeanExport;
    }

    public void setConceptBeanExport(ConceptExportMBean conceptBeanExport) {
        this.conceptBeanExport = conceptBeanExport;
    }

    public String formatSMTKRelationship(Relationship relationship) {

        String term = "";

        if(relationship.getRelationshipDefinition().getTargetDefinition().isSMTKType()) {
            ConceptSMTK conceptSMTK = (ConceptSMTK) relationship.getTarget();
            term = conceptSMTK.getDescriptionFavorite().getTerm() + " ";
        }
        if(relationship.getRelationshipDefinition().getTargetDefinition().isHelperTable()) {
            HelperTableRow helperTableRow = (HelperTableRow) relationship.getTarget();
            term = helperTableRow.getDescription() + " ";
        }

        return term;
    }

    public String formatSMTKRelationshipAttributes(Relationship relationship) {

        String term = "";

        if(relationship.getRelationshipDefinition().getTargetDefinition().isSMTKType()) {
            if (relationship.getRelationshipDefinition().isSubstance()) {
                ConceptSMTK conceptSMTK = (ConceptSMTK) relationship.getTarget();
                //term = conceptSMTK.getDescriptionFavorite().getTerm() + " ";

                for (RelationshipAttribute relationshipAttribute : relationship.getRelationshipAttributes()) {

                    if(relationshipAttribute.getRelationAttributeDefinition().isOrderAttribute()) {
                        continue;
                    }

                    if(relationshipAttribute.getRelationAttributeDefinition().isCantidadPPAttribute()) {
                        term = term + "/";
                    }

                    if(relationshipAttribute.getRelationAttributeDefinition().isUnidadPotenciaAttribute() ||
                            relationshipAttribute.getRelationAttributeDefinition().isUnidadPPAttribute() ||
                            relationshipAttribute.getRelationAttributeDefinition().isUnidadAttribute() ||
                            relationshipAttribute.getRelationAttributeDefinition().isUnidadPackMultiAttribute() ||
                            relationshipAttribute.getRelationAttributeDefinition().isUnidadVolumenTotalAttribute() ||
                            relationshipAttribute.getRelationAttributeDefinition().isUnidadVolumenAttribute()) {
                        HelperTableRow helperTableRow = (HelperTableRow) relationshipAttribute.getTarget();
                        term = term + helperTableRow.getCellByColumnName("descripcion abreviada").getStringValue() + " ";
                    }
                    else {
                        if(relationshipAttribute.getRelationAttributeDefinition().isCantidadPPAttribute() &&
                                Float.parseFloat(relationshipAttribute.getTarget().toString()) == 1) {
                            continue;
                        }

                        BasicTypeValue basicTypeValue = (BasicTypeValue) relationshipAttribute.getTarget();
                        BasicTypeDefinition basicTypeDefinition = (BasicTypeDefinition) relationshipAttribute.getRelationAttributeDefinition().getTargetDefinition();

                        if (basicTypeDefinition.getType().equals(BasicTypeType.FLOAT_TYPE)) {
                            DecimalFormat df = new DecimalFormat("###,###.##");
                            term = term + df.format(Float.parseFloat(basicTypeValue.getValue().toString())) + " ";
                        }
                    }
                }
            }
        }
        if(relationship.getRelationshipDefinition().getTargetDefinition().isHelperTable()) {
            HelperTableRow helperTableRow = (HelperTableRow) relationship.getTarget();
            //term = helperTableRow.getDescription() + " ";

            for (RelationshipAttribute relationshipAttribute : relationship.getRelationshipAttributes()) {

                if(relationshipAttribute.getRelationAttributeDefinition().isOrderAttribute()) {
                    continue;
                }

                term = " " + term + relationshipAttribute.getTarget().toString() + " ";
            }
        }

        return term;

    }


}
