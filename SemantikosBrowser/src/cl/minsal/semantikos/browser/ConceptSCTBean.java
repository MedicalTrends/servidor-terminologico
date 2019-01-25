package cl.minsal.semantikos.browser;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.components.GuestPreferences;
import cl.minsal.semantikos.kernel.components.SnomedCTManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.snomedct.DescriptionSCT;
import cl.minsal.semantikos.model.snomedct.DescriptionSCTType;
import cl.minsal.semantikos.model.snomedct.RelationshipSCT;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BluePrints Developer on 14-07-2016.
 */

@ManagedBean(name = "conceptSCT")
@SessionScoped
public class ConceptSCTBean {

    static private final Logger logger = LoggerFactory.getLogger(ConceptSCTBean.class);

    //@EJB
    SnomedCTManager snomedCTManager = (SnomedCTManager) ServiceLocator.getInstance().getService(SnomedCTManager.class);

    ConceptSCT selectedConcept;

    long conceptID;

    @ManagedProperty(value = "#{guestPreferences}")
    GuestPreferences guestPreferences;

    @ManagedProperty(value = "#{browserBean}")
    BrowserBean browserBean;

    //Inicializacion del Bean
    @PostConstruct
    protected void initialize() {
        guestPreferences.setTheme("teal");

    }

    public ConceptSCT getSelectedConcept() {
        return selectedConcept;
    }

    public void setSelectedConcept(ConceptSCT selectedConcept) {

        this.selectedConcept = selectedConcept;

    }

    public List<DescriptionSCT> getOtherDescriptions() {

        if(selectedConcept == null) {
            return null;
        }

        List<DescriptionSCT> otherDescriptions = new ArrayList<DescriptionSCT>();

        for (DescriptionSCT description : selectedConcept.getDescriptions()) {
            if(description.getDescriptionType().equals(DescriptionSCTType.SYNONYM) ||
                    description.getDescriptionType().equals(DescriptionSCTType.ACCEPTABLE))
                otherDescriptions.add(description);
        }

        return otherDescriptions;
    }

    public List<ConceptSCT> getRelationshipDefinitions() {

        List<ConceptSCT> relationshipDefinitions = new ArrayList<>();

        if(selectedConcept == null) {
            return  relationshipDefinitions;
        }

        for (RelationshipSCT relationshipDefinition : selectedConcept.getRelationships()) {
            if(!relationshipDefinitions.contains(relationshipDefinition.getTypeConcept())) {
                relationshipDefinitions.add(relationshipDefinition.getTypeConcept());
            }
        }

        return relationshipDefinitions;
    }
    
    public List<RelationshipSCT> getRelationshipsByRelationshipDefinition(ConceptSCT concept) {
        
        List<RelationshipSCT> relationships = new ArrayList<>();

        if(concept == null) {
            return relationships;
        }

        for (RelationshipSCT relationshipSCT : selectedConcept.getRelationships()) {
            if(relationshipSCT.isActive() && relationshipSCT.getTypeConcept().equals(concept)) {
                relationships.add(relationshipSCT);
            }
        }

        return relationships;
    }

    public GuestPreferences getGuestPreferences() {
        return guestPreferences;
    }

    public void setGuestPreferences(GuestPreferences guestPreferences) {
        this.guestPreferences = guestPreferences;
    }

    public long getConceptID() {
        return conceptID;
    }

    public void setConceptID(long conceptID) {
        this.conceptID = conceptID;
        selectedConcept = snomedCTManager.getConceptByID(conceptID);
        selectedConcept.setRelationships(snomedCTManager.getRelationshipsFrom(selectedConcept));
        if(!browserBean.isSnomedCT()) {
            browserBean.setSnomedCT(true);
        }
        updateConceptTree(browserBean.getRoot());
    }

    public void initConceptTree() {
        browserBean.setRoot(new DefaultTreeNode(new Object(), null));
        selectedConcept.getDescriptionFSN().setLanguageCode("");
        DefaultTreeNode node = new DefaultTreeNode(selectedConcept, browserBean.getRoot());
    }

    public TreeNode updateConceptTree(TreeNode treeNode) {

        treeNode.setExpanded(true);

        if(browserBean.getRoot().getChildren().isEmpty()){
            initConceptTree();
            return treeNode;
        }

        if(treeNode.getChildren().isEmpty()) {

            String relationship = browserBean.getRelationship(treeNode.getData(), selectedConcept);

            if (!relationship.isEmpty()) {
                selectedConcept.getDescriptionFSN().setLanguageCode(relationship);
                new DefaultTreeNode(selectedConcept, treeNode);
                return treeNode;
            } else {
                initConceptTree();
                return browserBean.getRoot();
            }
        }

        for (TreeNode node : treeNode.getChildren()) {
            if(node.getData().equals(selectedConcept)) {
                node.getChildren().clear();
                return node;
            }
            else {
                return updateConceptTree(node);
            }
        }

        initConceptTree();
        return browserBean.getRoot();

    }

    public BrowserBean getBrowserBean() {
        return browserBean;
    }

    public void setBrowserBean(BrowserBean browserBean) {
        this.browserBean = browserBean;
    }

}
