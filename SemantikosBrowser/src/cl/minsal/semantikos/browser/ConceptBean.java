package cl.minsal.semantikos.browser;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.*;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.crossmaps.DirectCrossmap;
import cl.minsal.semantikos.model.crossmaps.IndirectCrossmap;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.descriptions.DescriptionTypeFactory;
import cl.minsal.semantikos.model.refsets.RefSet;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.SnomedCTRelationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BluePrints Developer on 14-07-2016.
 */

@ManagedBean(name = "concept")
@ViewScoped
public class ConceptBean implements Serializable {

    static private final Logger logger = LoggerFactory.getLogger(ConceptBean.class);

    //@EJB
    ConceptManager conceptManager = (ConceptManager) ServiceLocator.getInstance().getService(ConceptManager.class);

    //@EJB
    RelationshipManager relationshipManager = (RelationshipManager) ServiceLocator.getInstance().getService(RelationshipManager.class);

    //@EJB
    RefSetManager refSetManager = (RefSetManager) ServiceLocator.getInstance().getService(RefSetManager.class);

    //@EJB
    CrossmapsManager crossmapsManager = (CrossmapsManager) ServiceLocator.getInstance().getService(CrossmapsManager.class);

    ConceptSMTK selectedConcept;

    String conceptID;

    List<IndirectCrossmap> indirectCrossmaps = new ArrayList<>();

    public String getConceptID() {
        return conceptID;
    }

    public void setConceptID(String conceptID) {
        this.conceptID = conceptID;
        selectedConcept = conceptManager.getConceptByCONCEPT_ID(conceptID);
        selectedConcept.setRelationships(relationshipManager.getRelationshipsBySourceConcept(selectedConcept));
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
            if(DescriptionTypeFactory.getInstance().getDescriptionTypesButFSNandFavorite().contains(description.getDescriptionType()))
                otherDescriptions.add(description);
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
            indirectCrossmaps = crossmapsManager.getIndirectCrossmaps(selectedConcept);
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

}