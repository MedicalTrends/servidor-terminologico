package cl.minsal.semantikos.beans.description;

import cl.minsal.semantikos.kernel.components.RelationshipManager;
import cl.minsal.semantikos.model.*;
import cl.minsal.semantikos.model.helpertables.HelperTableRecord;
import cl.minsal.semantikos.model.relationships.*;
import org.primefaces.event.ReorderEvent;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by des01c7 on 02-12-16.
 */
@ManagedBean(name = "autogenerateBeans")
@ViewScoped
public class AutogenerateBeans {

     @EJB
     private RelationshipManager relationshipManager;

    public String autogenerate(List<String> autoGenerateList) {
        String autogenerateString = "";
        for (int i = 0; i < autoGenerateList.size(); i++) {
            if (i == 0) {
                autogenerateString = autoGenerateList.get(i);
            } else {
                autogenerateString = autogenerateString + " + " + autoGenerateList.get(i);
            }
        }
        return autogenerateString;
    }

    public void autogenerateRelationshipWithAttributes(RelationshipDefinition relationshipDefinition, Relationship relationship, ConceptSMTKWeb concept, List<String> autoGenerateList, AutogenerateMC autogenerateMC) {
        if (!concept.isPersistent()) {
            if (relationshipDefinition.getId() == 45) {
                ConceptSMTK conceptRelationship =((ConceptSMTK) relationship.getTarget());
                autoGenerateList.add(conceptRelationship.getDescriptionFavorite().getTerm());
                concept.getDescriptionFavorite().setTerm(autogenerate(autoGenerateList));
                concept.getDescriptionFSN().setTerm(concept.getDescriptionFavorite().getTerm());
                if(conceptRelationship.getDescriptionFSN().isCaseSensitive()){
                    concept.getDescriptionFSN().setCaseSensitive(conceptRelationship.getDescriptionFSN().isCaseSensitive());
                }
            }
            if (relationshipDefinition.getId() == 47) {
                ConceptSMTK conceptRelationship =((ConceptSMTK) relationship.getTarget());
                autogenerateMC.addSustancia(relationship);
                concept.getDescriptionFavorite().setTerm(autogenerateMC.toString());
                concept.getDescriptionFSN().setTerm(concept.getDescriptionFavorite().getTerm());
                if(conceptRelationship.getDescriptionFavorite().isCaseSensitive()){
                    concept.getDescriptionFSN().setCaseSensitive(conceptRelationship.getDescriptionFavorite().isCaseSensitive());
                    concept.getDescriptionFavorite().setCaseSensitive(conceptRelationship.getDescriptionFavorite().isCaseSensitive());
                }
            }
            if (relationshipDefinition.getId() == 58) {
                autogenerateMC.addFFA(relationship);
                concept.getDescriptionFavorite().setTerm(autogenerateMC.toString());
                concept.getDescriptionFSN().setTerm(concept.getDescriptionFavorite().getTerm());
            }
        }
    }
    public void autogenerateRemoveRelationshipWithAttributes(RelationshipDefinition relationshipDefinition, Relationship relationship, ConceptSMTKWeb concept, List<String> autoGenerateList, AutogenerateMC autogenerateMC) {
        if (!concept.isPersistent()) {
            if (relationshipDefinition.getId() == 45) {
                ConceptSMTK conceptRelationship =((ConceptSMTK) relationship.getTarget());
                autoGenerateList.remove(conceptRelationship.getDescriptionFavorite().getTerm());
                concept.getDescriptionFavorite().setTerm(autogenerate(autoGenerateList));
                concept.getDescriptionFSN().setTerm(concept.getDescriptionFavorite().getTerm());
                if(conceptRelationship.getDescriptionFSN().isCaseSensitive()){
                    concept.getDescriptionFSN().setCaseSensitive(false);
                }
            }
            if (relationshipDefinition.getId() == 47) {
                ConceptSMTK conceptRelationship =((ConceptSMTK) relationship.getTarget());
                autogenerateMC.getSustancias().remove(autogenerateMC.generateNameSustancia(relationship));
                concept.getDescriptionFavorite().setTerm(autogenerateMC.toString());
                concept.getDescriptionFSN().setTerm(concept.getDescriptionFavorite().getTerm());
                if(conceptRelationship.getDescriptionFavorite().isCaseSensitive()){
                    concept.getDescriptionFSN().setCaseSensitive(false);
                    concept.getDescriptionFavorite().setCaseSensitive(false);
                }
            }
        }
    }

    public void autogenerateRelationship(RelationshipDefinition relationshipDefinition, Relationship relationship, Target target, ConceptSMTKWeb concept, AutogenerateMC autogenerateMC, AutogenerateMCCE autogenerateMCCE, AutogeneratePCCE autogeneratePCCE) {
        if (!concept.isPersistent()) {
            if (relationshipDefinition.getId() == 48) {
                autogenerateMCCE.setMC(((ConceptSMTK) relationship.getTarget()).getDescriptionFavorite().getTerm());
                concept.getDescriptionFavorite().setTerm(autogenerateMCCE.toString());
                concept.getDescriptionFSN().setTerm(concept.getDescriptionFavorite().getTerm());
            }
            if (relationshipDefinition.getId() == 92) {
                autogenerateMCCE.setCantidad(relationship.getTarget().toString());
                concept.getDescriptionFavorite().setTerm(autogenerateMCCE.toString());
                concept.getDescriptionFSN().setTerm(concept.getDescriptionFavorite().getTerm());
            }
            if (relationshipDefinition.getId() == 93) {
                autogenerateMCCE.setVolumen(target.toString());
                concept.getDescriptionFavorite().setTerm(autogenerateMCCE.toString());
                concept.getDescriptionFSN().setTerm(concept.getDescriptionFavorite().getTerm());
            }
            if (relationshipDefinition.getId() == 77) {
                autogenerateMCCE.setPack(target.toString());
                concept.getDescriptionFavorite().setTerm(autogenerateMCCE.toString());
                concept.getDescriptionFSN().setTerm(concept.getDescriptionFavorite().getTerm());
            }
            if (relationshipDefinition.getId() == 69) {
                autogenerateMC.setVolumen(relationship);
                concept.getDescriptionFavorite().setTerm(autogenerateMC.toString());
                concept.getDescriptionFSN().setTerm(concept.getDescriptionFavorite().getTerm());
            }
            if (relationshipDefinition.getId() == 52) {
                ConceptSMTK c = (ConceptSMTK) relationship.getTarget();
                c.setRelationships(relationshipManager.getRelationshipsBySourceConcept(c));
                autogeneratePCCE.autogeratePCCE((ConceptSMTK) relationship.getTarget());
                concept.getDescriptionFavorite().setTerm(autogeneratePCCE.toString());
                concept.getDescriptionFSN().setTerm(concept.getDescriptionFavorite().getTerm());
            }
            if (relationshipDefinition.getId() == 51) {
                autogeneratePCCE.setPc(((ConceptSMTK) relationship.getTarget()).getDescriptionFavorite().getTerm());
                concept.getDescriptionFavorite().setTerm(autogeneratePCCE.toString());
                concept.getDescriptionFSN().setTerm(concept.getDescriptionFavorite().getTerm());
            }
        }
    }

    public void autogenerateOrder(ConceptSMTKWeb concept,List<String> autoGenerateList, RelationshipDefinition relationshipDefinitionRowEdit, AutogenerateMC autogenerateMC, ReorderEvent event){
        if (!concept.isPersistent()) {

            if (relationshipDefinitionRowEdit.getId() == 45) {
                autoGenerateList = newOrderList(autoGenerateList, event);
                concept.getDescriptionFavorite().setTerm(autogenerate(autoGenerateList));
                concept.getDescriptionFSN().setTerm(concept.getDescriptionFavorite().getTerm());
            }
            if (relationshipDefinitionRowEdit.getId() == 47) {
                autogenerateMC.setSustancias(newOrderList(autogenerateMC.getSustancias(), event));
                concept.getDescriptionFavorite().setTerm(autogenerateMC.toString());
                concept.getDescriptionFSN().setTerm(concept.getDescriptionFavorite().getTerm());
            }
            if (relationshipDefinitionRowEdit.getId() == 58) {
                autogenerateMC.setFfa(newOrderList(autogenerateMC.getFfa(), event));
                concept.getDescriptionFavorite().setTerm(autogenerateMC.toString());
                concept.getDescriptionFSN().setTerm(concept.getDescriptionFavorite().getTerm());
            }
        }
    }

    public void autogenerateAttributeDefinition(RelationshipAttributeDefinition relationshipAttributeDefinition, Target target, RelationshipAttribute attribute, ConceptSMTKWeb concept,AutogenerateMC autogenerateMC, AutogenerateMCCE autogenerateMCCE) {
        if (!concept.isPersistent()) {
            if (relationshipAttributeDefinition.getId() == 16) {
                autogenerateMCCE.setPackUnidad(((HelperTableRecord) target).getValueColumn("description"));
                concept.getDescriptionFavorite().setTerm(autogenerateMCCE.toString());
                concept.getDescriptionFSN().setTerm(concept.getDescriptionFavorite().getTerm());
            }
            if (relationshipAttributeDefinition.getId() == 17) {
                autogenerateMCCE.setVolumenUnidad(((HelperTableRecord) target).getValueColumn("description"));
                concept.getDescriptionFavorite().setTerm(autogenerateMCCE.toString());
                concept.getDescriptionFSN().setTerm(concept.getDescriptionFavorite().getTerm());
            }
            if (relationshipAttributeDefinition.getId() == 12) {
                autogenerateMC.setUnidadVolumen(attribute);
                concept.getDescriptionFavorite().setTerm(autogenerateMC.toString());
                concept.getDescriptionFSN().setTerm(concept.getDescriptionFavorite().getTerm());
            }
            if (relationshipAttributeDefinition.getId() == 15) {
                autogenerateMCCE.setUnidadMedidaCantidad(((HelperTableRecord) target).getValueColumn("description"));
                concept.getDescriptionFavorite().setTerm(autogenerateMCCE.toString());
                concept.getDescriptionFSN().setTerm(concept.getDescriptionFavorite().getTerm());
            }
        }
    }

    public List<String> newOrderList(List<String> list, ReorderEvent event) {
        List<String> autoNuevoOrden = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i != event.getFromIndex()) {
                if (i == event.getToIndex()) {
                    if (event.getFromIndex() < event.getToIndex()) {
                        autoNuevoOrden.add(list.get(i));
                        autoNuevoOrden.add(list.get(event.getFromIndex()));
                    } else {

                        autoNuevoOrden.add(list.get(event.getFromIndex()));
                        autoNuevoOrden.add(list.get(i));
                    }
                } else {
                    autoNuevoOrden.add(list.get(i));
                }
            }
        }
        return autoNuevoOrden;
    }
}
