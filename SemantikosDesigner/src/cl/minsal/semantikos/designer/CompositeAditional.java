package cl.minsal.semantikos.designer;

import cl.minsal.semantikos.clients.ServiceLocator;
import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.helpertables.HelperTableRow;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.RelationshipAttribute;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.modelweb.ConceptSMTKWeb;
import cl.minsal.semantikos.modelweb.RelationshipWeb;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by des01c7 on 14-10-16.
 */
@ManagedBean(name = "compositeAditionalBean")
@ViewScoped
public class CompositeAditional {
    //TODO: refactorizar
    //@EJB
    private ConceptManager conceptManager = (ConceptManager) ServiceLocator.getInstance().getService(ConceptManager.class);

    private Map<Long, List<Relationship>> relationships;

    @PostConstruct
    public void init() {
        relationships = new HashMap<>();
    }


    public Target getAditionalInfo(ConceptSMTK conceptSMTK, long relationshipDefinition, long relationshipAttributeDefinition) {

        Relationship relationship = getAditionalInfo(conceptSMTK, relationshipDefinition);
        for (RelationshipAttribute relationshipAttribute : relationship.getRelationshipAttributes()) {
            if (relationshipAttribute.getRelationAttributeDefinition().getId() == relationshipAttributeDefinition) {
                return relationshipAttribute.getTarget();
            }
        }
        return null;
    }

    public Relationship getAditionalInfo(ConceptSMTK conceptSMTK, long relationshipDefinition) {
        conceptSMTK.setRelationships(getRelationships(conceptSMTK));
        ConceptSMTKWeb conceptSMTKWeb = new ConceptSMTKWeb(conceptSMTK);
        for (RelationshipWeb relationshipWeb : conceptSMTKWeb.getValidPersistedRelationshipsWeb()) {
            if (relationshipWeb.getRelationshipDefinition().getId() == relationshipDefinition) {
                return relationshipWeb;
            }
        }
        return null;
    }

    public String getCantidadMC(ConceptSMTK conceptSMTK) {
        conceptSMTK.setRelationships(getRelationships(conceptSMTK));
        ConceptSMTKWeb conceptSMTKWeb = new ConceptSMTKWeb(conceptSMTK);

        for (RelationshipWeb relationshipWeb : conceptSMTKWeb.getValidPersistedRelationshipsWeb()) {
            if (relationshipWeb.getRelationshipDefinition().getId() == 69) {//TODO: reparar este numero magico

                return relationshipWeb.getTarget().toString();
            }
        }

        return null;
    }

    public String getUnidadCantidadMC(ConceptSMTK conceptSMTK) {

        conceptSMTK.setRelationships(getRelationships(conceptSMTK));
        ConceptSMTKWeb conceptSMTKWeb = new ConceptSMTKWeb(conceptSMTK);
        for (RelationshipWeb relationshipWeb : conceptSMTKWeb.getValidPersistedRelationshipsWeb()) {
            if (relationshipWeb.getRelationshipDefinition().getId() == 69) { //TODO: reparar este numero magico
                for (RelationshipAttribute relationshipAttribute : relationshipWeb.getRelationshipAttributes()) {
                    if (relationshipAttribute.getRelationAttributeDefinition().getId() == 12) { //TODO: reparar este numero magico
                        HelperTableRow helperRecord = (HelperTableRow) relationshipAttribute.getTarget();
                        return helperRecord.getDescription();
                    }

                }

            }
        }

        return null;
    }

    public String getCantidadMCToPCCE(ConceptSMTK conceptSMTK) {
        conceptSMTK.setRelationships(getRelationships(conceptSMTK));
        ConceptSMTKWeb conceptSMTKWeb = new ConceptSMTKWeb(conceptSMTK);

        for (RelationshipWeb relationshipWeb : conceptSMTKWeb.getValidPersistedRelationshipsWeb()) {
            if (relationshipWeb.getRelationshipDefinition().getId() == 48) {


                return getCantidadMC((ConceptSMTK) relationshipWeb.getTarget());
            }
        }
        return null;

    }

    public String getUnidadCantidadMCToPCCE(ConceptSMTK conceptSMTK) {
        conceptSMTK.setRelationships(getRelationships(conceptSMTK));
        ConceptSMTKWeb conceptSMTKWeb = new ConceptSMTKWeb(conceptSMTK);

        for (RelationshipWeb relationshipWeb : conceptSMTKWeb.getValidPersistedRelationshipsWeb()) {
            if (relationshipWeb.getRelationshipDefinition().getId() == 48) {
                return getUnidadCantidadMC((ConceptSMTK) relationshipWeb.getTarget());
            }
        }
        return null;
    }

    public List<Relationship> getRelationships(ConceptSMTK conceptSMTK) {

        if (!relationships.containsKey(conceptSMTK.getId())) {
            List<Relationship> relationshipsList = null;
            try {
                relationshipsList = conceptManager.loadRelationships(conceptSMTK);
            } catch (Exception e) {
                e.printStackTrace();
            }
            relationships.put(conceptSMTK.getId(), relationshipsList);
            return relationshipsList;
        } else {
            return relationships.get(conceptSMTK.getId());
        }

    }


}
